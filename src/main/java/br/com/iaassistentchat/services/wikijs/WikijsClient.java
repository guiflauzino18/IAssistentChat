package br.com.iaassistentchat.services.wikijs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;


@Component
public class WikijsClient {

    private final WebClient webClient;

    public WikijsClient(WikijsConfig config) {

        this.webClient = WebClient.builder()
                .baseUrl(config.getBaseUrl())
                .defaultCookie(config.getCookieName(),config.getToken())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<String> runQuery(String query){
        Map<String, String> body = Map.of("query", query);
        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class);
    }
}