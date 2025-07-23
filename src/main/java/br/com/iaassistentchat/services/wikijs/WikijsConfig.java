package br.com.iaassistentchat.services.wikijs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wikijs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class WikijsConfig {

    private String baseUrl;
    private String token;
    private String cookieName;
}
