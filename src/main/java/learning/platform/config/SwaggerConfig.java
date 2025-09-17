package learning.platform.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 🔹 Información de la API
                .info(new Info()
                        .title("Lumina API")
                        .version("1.0")
                        .description("Documentación de la API para la plataforma de aprendizaje electrónico")
                        .termsOfService("http://localhost:8080/terms")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                // 🔹 Requerimiento de seguridad global (JWT)
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                // 🔹 Definición del esquema de seguridad
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Introduce el token JWT con el formato: Bearer {token}")));
    }
}
