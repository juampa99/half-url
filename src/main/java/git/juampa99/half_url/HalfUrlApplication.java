package git.juampa99.half_url;

import git.juampa99.half_url.config.KeyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(KeyConfig.class)
public class HalfUrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(HalfUrlApplication.class, args);
    }

}
