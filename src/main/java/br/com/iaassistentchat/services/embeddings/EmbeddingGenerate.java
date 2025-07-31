package br.com.iaassistentchat.services.embeddings;
import br.com.iaassistentchat.DTO.EmbeddingDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.pgvector.PGvector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmbeddingGenerate {

    @Autowired
    private OpenAiEmbeddingModel embeddingModel;
    @Autowired
    private EmbeddingClient embeddingClient;

    Logger logger = LoggerFactory.getLogger(EmbeddingGenerate.class);
    public Mono<List<EmbeddingDTO>> embeddingsGenerate(List<String> chunks, String source, LocalDateTime lastModified){

        logger.info("Gerando Embeddings...");
        return embeddingRequest(chunks) //Faz request à API que gera os embeddings
                .map(item -> collectEmbeddings(item, chunks, source, lastModified)) //Resultado da request á coletado e gerado um EmbeddingDTO
                .subscribeOn(Schedulers.boundedElastic()); //Para lidar com operações bloqueantes dentro de um fluxo reativo
    }

    private Mono<JsonNode> embeddingRequest(List<String> chunks){
         return Mono.fromCallable(() -> embeddingClient.request(chunks));
    }

    private List<EmbeddingDTO> collectEmbeddings(JsonNode json, List<String> chunks, String source, LocalDateTime lastModified){

        List<EmbeddingDTO> listDTO = new ArrayList<>();


        for (int i = 0; i < json.size(); i++){
            float[] vetor = new float[json.get(i).get("embedding").size()];

            for (int j = 0; j < json.get(i).get("embedding").size(); j++){
                vetor[j] = json.get(i).get("embedding").get(j).floatValue();
            }

            listDTO.add(new EmbeddingDTO(chunks.get(i), vetor, source, lastModified ));
        }

        return listDTO;

    }

}