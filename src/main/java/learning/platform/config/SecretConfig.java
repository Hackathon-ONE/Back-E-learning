package learning.platform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.config")
public class SecretConfig {

    // Variable que se llenará desde app.config.secret
    private String secret;

    // Setter necesario para @ConfigurationProperties
    public void setSecret(String secret) {
        this.secret = secret;
    }

    // Getter para usar el valor en tu app
    public String getSecret() {
        return secret;
    }
}