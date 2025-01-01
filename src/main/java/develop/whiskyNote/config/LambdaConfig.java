package develop.whiskyNote.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-dev.yml")
@ConfigurationProperties(prefix = "firebase.lambda")
@Getter
@Setter
public class LambdaConfig {
    private String url;
}
