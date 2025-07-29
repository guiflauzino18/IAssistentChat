package br.com.iaassistentchat.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientBuilder {

    @Value("${spring.ai.openai.api-key}")
    private String togetherApiKey;
    @Value("${spring.ai.openai.embedding.embedding-path}")
    private String embeddingPath;
    @Value("${spring.ai.openai.embedding.options.model}")
    private String embeddingModel;
    @Value("${spring.ai.openai.base-url}")
    private String baseURL;

    @Bean
    public RestClient restEmbeddingBuilder(){
        return RestClient.builder()
                .baseUrl(baseURL+"/"+embeddingPath)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
