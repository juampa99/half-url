package git.juampa99.half_url.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keyconfig")
public class KeyConfig {

    private int HEX_LENGTH;
    private int MAX_KEY_LENGTH;

    public int getHex_length() {
        return HEX_LENGTH;
    }

    public void setHex_length(int hex_length) {
        this.HEX_LENGTH = hex_length;
    }

    public int getMAX_KEY_LENGTH() {
        return MAX_KEY_LENGTH;
    }

    public void setMAX_KEY_LENGTH(int MAX_KEY_LENGTH) {
        this.MAX_KEY_LENGTH = MAX_KEY_LENGTH;
    }
}
