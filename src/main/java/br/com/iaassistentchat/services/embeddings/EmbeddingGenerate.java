package br.com.iaassistentchat.services.embeddings;
import br.com.iaassistentchat.DTO.EmbeddingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmbeddingGenerate {

    @Autowired
    private OpenAiEmbeddingModel embeddingModel;

    Logger logger = LoggerFactory.getLogger(EmbeddingGenerate.class);

    public Mono<List<EmbeddingDTO>> embeddingsGenerate(List<String> chunks, String source){

        logger.info("Gerando Embeddings...");

        return Flux.fromIterable(paginar(chunks, 2))
                .concatMap(lote ->
                                Mono.fromSupplier(() -> {
                                    EmbeddingResponse response = embeddingModel.embedForResponse(lote);
                                    List<EmbeddingDTO> listDTO = new ArrayList<>();

                                    for (int i = 0; i < lote.size(); i ++){
                                        var texto = chunks.get(i);
                                        var vetor = response.getResults().get(i).getOutput();

                                        listDTO.add(new EmbeddingDTO(texto, vetor, source));
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

        return pages;
    }
}