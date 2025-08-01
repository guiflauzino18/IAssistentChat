package br.com.iaassistentchat.services.embeddings;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Map;

@Component
public class EmbeddingClient {

    private final RestClient restClient;
    private final String embeddingModel;
    private final String togetherApiKey;


    public EmbeddingClient(RestClient restClient,
                           @Value("${spring.ai.openai.embedding.options.model}") String embeddingModel,
                           @Value("${spring.ai.openai.api-key}") String togetherApiKey) {

        this.restClient = restClient;
        this.embeddingModel = embeddingModel;
        this.togetherApiKey = togetherApiKey;

    }

    public JsonNode request(List<String> chunks){


        Map<String, Object> body = Map.of(
                "model", embeddingModel,
                "input", chunks
        );

        try {
            ResponseEntity<JsonNode> response = restClient.post()
                    .header("Authorization", "Bearer "+togetherApiKey)
                    .body(body)
                    .retrieve()
                    .toEntity(JsonNode.class);

            assert response.getBody() != null;
            return response.getBody().get("data");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter Embeddings da Together.AI: "+e.getMessage());
        }

    }


}
