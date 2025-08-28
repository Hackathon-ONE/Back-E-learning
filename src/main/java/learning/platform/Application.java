package learning.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		Dotenv dotenv = null;

		try {
			dotenv = Dotenv.configure()
					.directory("platform") // asegúrate que el .env está en esta carpeta
					.ignoreIfMalformed()
					.ignoreIfMissing() // no falla si no existe, se puede manejar
					.load();
		} catch (DotenvException e) {
			System.err.println("❌ Error al cargar .env: " + e.getMessage());
		}

		if (dotenv != null) {
			System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL", ""));
			System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME", ""));
			System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD", ""));
		} else {
			System.err.println("⚠️ Variables de entorno no cargadas. Revisa tu archivo .env");
		}

		SpringApplication.run(Application.class, args);
	}
}