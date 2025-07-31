package br.com.iaassistentchat.services.rag;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class ChatClient {

    @Value("${spring.ai.openai.chat.options.model}")
    private String model;
    private String url;
    private String chatPath;
    private String togetherApiKey;

    private Logger logger = LoggerFactory.getLogger(ChatClient.class);
    private RestClient restClient;

    public  ChatClient(
            @Value("${spring.ai.openai.chat.completions-path}") String chatPath,
            @Value("${spring.ai.openai.base-url}") String url,
            @Value("${spring.ai.openai.api-key}") String togetherApiKey

    ){

        this.chatPath = chatPath;
        this.url = url;
        this.togetherApiKey = togetherApiKey;

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(120))
                .build();


        this.restClient = RestClient.builder()
                .requestFactory(new JdkClientHttpRequestFactory(httpClient))
                .baseUrl(url+"/"+chatPath)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public JsonNode chat(String prompt){

        logger.info("Realizando completion");

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                        "role", "user",
                        "content", prompt
                )
                )
        );

        try {

            ResponseEntity<JsonNode> response = restClient.post()
                    .header("Authorization", "Bearer "+togetherApiKey)
                    .body(body)
                    .retrieve()
                    .toEntity(JsonNode.class);

            assert response.getBody() != null;
            return response.getBody().get("choices").get(0).get("message").get("content");

        }catch (Exception e){
            throw  new IllegalArgumentException(e.getMessage());
        }
    }
}
