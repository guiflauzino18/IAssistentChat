package br.com.iaassistentchat.services.embeddings;
import br.com.iaassistentchat.DTO.EmbeddingDTO;
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

        return Flux.fromIterable(paginar(chunks, 2))
                .concatMap(lote ->
                                Mono.fromSupplier(() -> {
                                    if (lote.isEmpty()){
                                        throw new IllegalArgumentException("Lote não pode ser vazio");
                                    }

                                    //EmbeddingResponse response = embeddingModel.embedForResponse(lote);
                                    var response = embeddingClient.request(lote);
                                    List<EmbeddingDTO> listDTO = new ArrayList<>();

                                    for (int i = 0; i < response.size(); i ++){

                                        var texto = chunks.get(i);
                                        float[] vetor = new float[response.get(i).get("embedding").size()];

                                        for (int j =0; j < response.get(i).get("embedding").size(); j++ ){

                                            vetor[j] = response.get(i).get("embedding").get(j).floatValue();
                                        }

                                        listDTO.add(new EmbeddingDTO(texto, vetor, source, lastModified));
                                    }
                                    return listDTO;
                                }).subscribeOn(Schedulers.boundedElastic())

                        )
                .flatMap(Flux::fromIterable)
                .collectList(); //Junta todos os Embeddings em uma única lista final

    }

    //Para não enviar conteúdos das página de uma vez para gerar os embedding, será criado paginas
    // para enviar o conteúdos aos pocuos
    private List<List<String>> paginar(List<String> chunks, int pageLenght){
        List<List<String>> pages = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i += pageLenght){
            List<String> page = chunks.subList(i, Math.min(i + pageLenght, chunks.size()));
            pages.add(page);
        }

        System.out.println("-----------------_"+pages.size());

        return pages;
    }
}