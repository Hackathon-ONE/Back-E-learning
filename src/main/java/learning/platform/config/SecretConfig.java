package learning.platform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "app.config")
public class SecretConfig {
    private String secret;
    //Setter del valor del secreto
    public void setSecret(String secret) {
        this.secret = secret;
    }
    //Getter del valor del proyecto
    public String getSecret() {
        return secret;
    }
}
