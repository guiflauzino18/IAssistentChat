package br.com.iaassistentchat.services.rag;

import br.com.iaassistentchat.DTO.EmbeddingDTO;
import br.com.iaassistentchat.DTO.EmbeddingResultDTO;
import br.com.iaassistentchat.services.embeddings.EmbeddingClient;
import br.com.iaassistentchat.services.embeddings.EmbeddingGenerate;
import br.com.iaassistentchat.services.embeddings.EmbeddingSearch;
import com.pgvector.PGvector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    @Autowired
    private EmbeddingGenerate embeddingGenerate;
    @Autowired
    private EmbeddingSearch embeddingSearch;
    @Autowired
    private ChatClient chatClient;

    private Logger logger = LoggerFactory.getLogger(RagService.class);

    public Mono<String> chatResponse(String pergunta) {

        return generateEmbeddingsOfAsk(pergunta)
                .flatMap(this::doSearchOfSimilarity)
                .map(item -> promptGenerate(item, pergunta))
                .map(this::requestToChatAPI);
    }


    private Mono<EmbeddingDTO> generateEmbeddingsOfAsk(String pergunta){
        return embeddingGenerate.embeddingsGenerate(List.of(pergunta), "pergunta", LocalDateTime.now())
                .map(item -> {
                    System.out.println("##########################"+item.size());
                    return item.getFirst();
                });
    }

    private Mono<List<EmbeddingResultDTO>>  doSearchOfSimilarity(EmbeddingDTO embedding){
        return Mono.fromCallable(() -> embeddingSearch.findBySimilarity(new PGvector(embedding.getVetor()), 5));

    }

    private String promptGenerate(List<EmbeddingResultDTO> result, String pergunta){

        String contexto = result.stream()
        .map(EmbeddingResultDTO::text)
        .collect(Collectors.joining("\n"));

        String source = result.stream()
        .map(EmbeddingResultDTO::source)
        .collect(Collectors.joining("\n"));


        logger.info("Gerando Prompt");
        return """
                Você é uma IA treinada para responder à perguntas de usuários sobre processos da empresa.
                Abaixo está a pergunta do usuário e um texto de referencia para voce responder.
                A resposta deve estar relacionada exclusivamente ao contexto abaixo, caso não esteja retorne a resposta dizendo que não foi possível encontrar a resposta.
                
                
                Contexto:
                %s
                
                Pergunta:
                %s
                
                Fonte:
                %s
                """
                .formatted(contexto, pergunta, source);
    }

    private String requestToChatAPI(String prompt) {
        var response = chatClient.chat(prompt);
        System.out.println(prompt);
        return response.asText().replaceAll("(?s)<think>.*</think>", "");
    }
}
