package learning.platform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "app.config")
public class SecretConfig {
    private String pass;
    private String secret;
    private String user;
    private String dpath;
    private String dburl;

    //Setter del valor del secreto


    public void setDburl(String dburl) {
        this.dburl = dburl;
    }

    public void setDpath(String dpath) {
        this.dpath = dpath;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
    //Getter del valor del proyecto


    public String getDburl() {
        return dburl;
    }

    public String getDpath() {
        return dpath;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getSecret() {
        return secret;
    }
}
