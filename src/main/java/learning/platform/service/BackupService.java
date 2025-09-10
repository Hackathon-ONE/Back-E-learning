package learning.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import learning.platform.config.SecretConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BackupService {

    private final SecretConfig secretConfig;
    private final DataSource dataSource;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String backupDir = "backups/";

    @Value("${backup.tables:users,courses,enrollments,payments,lessons}") // por defecto
    private String tablesCsv;

    private final String LAST_HASH_FILE_NAME = "last_hash.txt";

    public BackupService(SecretConfig secretConfig, DataSource dataSource) {
        this.secretConfig = secretConfig;
        this.dataSource = dataSource;
    }

    /**
     * Cron: cada hora en el minuto 0 (igual que tu ejemplo). Ajusta según necesidad.
     * Ej: "0 0 * * * *" -> cada hora.
     */
    @Scheduled(cron = "${backup.cron:0 0 * * * *}")
    public void runBackupScheduled() {
        try {
            runBackup();
        } catch (Exception e) {
            // loguea el error (puedes usar logger)
            System.err.println("Error al ejecutar backup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void runBackup() throws Exception {
        File dir = new File(backupDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("No se pudo crear directorio de backups: " + backupDir);
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        // intentamos crear .sql con pg_dump, si no, creamos .json por JDBC
        File backupFile = new File(dir, "backup_" + timestamp + ".sql");
        boolean pgDumpSuccess = false;

        // 1) Intentar usar pg_dump si hay ruta configurada o en PATH
        String password = secretConfig.getPass();
        if (tryPgDump(backupFile, password)) {
            pgDumpSuccess = true;
        }

        // 2) Si pg_dump falló, fallback por JDBC a JSON
        if (!pgDumpSuccess) {
            File jsonFile = new File(dir, "backup_" + timestamp + ".json");
            jdbcDumpToJson(jsonFile);
            backupFile = jsonFile;
        }

        // 3) Calcular hash y comparar con el último
        String currentHash;
        try (InputStream is = Files.newInputStream(backupFile.toPath())) {
            currentHash = DigestUtils.sha256Hex(is);
        }

        String lastHash = loadLastHash();
        if (currentHash.equals(lastHash)) {
            // No hubo cambios -> borrar archivo redundante
            if (!backupFile.delete()) {
                System.out.println("No hubo cambios, pero no se pudo borrar: " + backupFile.getAbsolutePath());
            } else {
                System.out.println("⚠️ Sin cambios en la BD, backup no guardado");
            }
        } else {
            saveLastHash(currentHash);
            System.out.println("✅ Backup guardado: " + backupFile.getAbsolutePath());
        }
    }

    private boolean tryPgDump(File backupFile, String dbPassword) {
        List<String> candidates = new ArrayList<>();
        if (secretConfig.getDpath() != null && !secretConfig.getDpath().isBlank()) {
            candidates.add(secretConfig.getDpath());
        }
        candidates.add("pg_dump"); // intentar en PATH

        // extraer database name desde datasourceUrl (jdbc:postgresql://host:port/dbname)
        String dbName = extractDatabaseName(secretConfig.getDburl());

        for (String exe : candidates) {
            try {
                List<String> cmd = new ArrayList<>();
                cmd.add(exe);
                // argumentos básicos; si quieres más opciones, añadir aquí
                cmd.add("-U");
                cmd.add(secretConfig.getUser());
                cmd.add("-d");
                cmd.add(dbName);
                cmd.add("-F");
                cmd.add("p"); // formato plain SQL
                cmd.add("-f");
                cmd.add(backupFile.getAbsolutePath());

                ProcessBuilder pb = new ProcessBuilder(cmd);
                // setea PGPASSWORD si está disponible
                if (dbPassword != null) pb.environment().put("PGPASSWORD", dbPassword);
                pb.redirectErrorStream(true);
                Process p = pb.start();

                // opcional: leer salida para debug
                try (InputStream is = p.getInputStream()) {
                    byte[] buffer = new byte[1024];
                    while (is.available() > 0) { // consume lo que haya
                        int r = is.read(buffer);
                        if (r <= 0) break;
                    }
                } catch (IOException ignored) {}

                int exit = p.waitFor();
                if (exit == 0 && backupFile.exists()) {
                    System.out.println("pg_dump ejecutado correctamente con: " + exe);
                    return true;
                } else {
                    System.out.println("pg_dump falló con: " + exe + " exit=" + exit);
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("pg_dump no disponible o falló en: " + exe + " -> " + e.getMessage());
                // intentar siguiente candidato
            }
        }
        return false;
    }

    private void jdbcDumpToJson(File file) throws Exception {
        String[] tables = Arrays.stream(tablesCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        ObjectNode root = objectMapper.createObjectNode();

        try (Connection conn = dataSource.getConnection()) {
            for (String table : tables) {
                List<Map<String, Object>> rows = new ArrayList<>();
                String sql = "SELECT * FROM " + table;
                try (PreparedStatement ps = conn.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {

                    ResultSetMetaData meta = rs.getMetaData();
                    int cols = meta.getColumnCount();

                    while (rs.next()) {
                        Map<String, Object> row = new LinkedHashMap<>();
                        for (int i = 1; i <= cols; i++) {
                            Object value = rs.getObject(i);
                            String colName = meta.getColumnLabel(i);
                            // Jackson serializa Timestamp/Date automáticamente
                            row.put(colName, value);
                        }
                        rows.add(row);
                    }
                } catch (SQLException sqle) {
                    System.out.println("Tabla no encontrada o error leyendo " + table + ": " + sqle.getMessage());
                    // skip table o lanzar según política
                    continue;
                }
                root.set(table, objectMapper.valueToTree(rows));
            }
        }

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
        System.out.println("Dump JDBC escrito en JSON: " + file.getAbsolutePath());
    }

    private String loadLastHash() throws IOException {
        File f = new File(backupDir, LAST_HASH_FILE_NAME);
        if (!f.exists()) return "";
        return Files.readString(f.toPath());
    }

    private void saveLastHash(String hash) throws IOException {
        File f = new File(backupDir, LAST_HASH_FILE_NAME);
        Files.writeString(f.toPath(), hash);
    }

    private String extractDatabaseName(String jdbcUrl) {
        // jdbc:postgresql://host:port/dbname?params...
        if (jdbcUrl == null) return "";
        int idx = jdbcUrl.lastIndexOf('/');
        if (idx == -1) return "";
        String tail = jdbcUrl.substring(idx + 1);
        int q = tail.indexOf('?');
        if (q >= 0) tail = tail.substring(0, q);
        return tail;
    }
}
