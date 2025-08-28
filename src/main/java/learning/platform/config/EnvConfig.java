package learning.platform.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    private final Dotenv dotenv;

    public EnvConfig() {
        Dotenv tempDotenv = null;
        try {
            // Intenta cargar .env desde la carpeta "platform"
            tempDotenv = Dotenv.configure()
                    .directory("platform") // ruta relativa desde el root del proyecto
                    .ignoreIfMalformed()   // ignora si hay errores de formato
                    .ignoreIfMissing()     // no falla si no encuentra, pero se puede manejar
                    .load();

            if (tempDotenv == null) {
                System.err.println("⚠️ No se encontró el archivo .env. Revisa la ruta o crea el archivo con tus variables.");
            }
        } catch (DotenvException e) {
            System.err.println("❌ Error al cargar .env: " + e.getMessage());
        }

        this.dotenv = tempDotenv;
    }

    public String get(String key) {
        if (dotenv == null) {
            throw new RuntimeException("El archivo .env no está cargado. No se puede obtener la variable: " + key);
        }
        return dotenv.get(key);
    }
}
