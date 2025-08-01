package br.com.iaassistentchat;

import br.com.iaassistentchat.DTO.EmbeddingDTO;
import br.com.iaassistentchat.DTO.EmbeddingResultDTO;
import br.com.iaassistentchat.DTO.PageContentDTO;
import br.com.iaassistentchat.DTO.PageListDTO;
import br.com.iaassistentchat.model.EmbeddingEntity;
import br.com.iaassistentchat.services.embeddings.ContentSplitter;
import br.com.iaassistentchat.services.embeddings.EmbeddingGenerate;
import br.com.iaassistentchat.services.embeddings.EmbeddingPersitence;
import br.com.iaassistentchat.services.rag.ChatClient;
import br.com.iaassistentchat.services.rag.RagService;
import br.com.iaassistentchat.services.wikijs.WikijsPageVersionCompare;
import br.com.iaassistentchat.services.wikijs.WikijsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class StartupRunner implements ApplicationRunner {

    @Autowired
    private WikijsService wikijsService;
    @Autowired
    private ContentSplitter splitter;
    @Autowired
    private EmbeddingGenerate embeddingGenerate;
    @Autowired
    private EmbeddingPersitence persitenceService;
    @Autowired
    private RagService ragService;
    @Autowired
    private WikijsPageVersionCompare pageVersion;

    private Logger logger = LoggerFactory.getLogger(StartupRunner.class);

    EmbeddingDTO embedding = new EmbeddingDTO();
    PageContentDTO pageContentDTO = new PageContentDTO();
    EmbeddingResultDTO embeddingResultDTO = null;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        startEmbedding();

    }

    public void startEmbedding(){
        searchPagesInWikijs()
                .flatMapMany(Flux::fromIterable)                            //Percorre a lista retornada
                .filter(pageListDTO -> pageVersion.updated(pageListDTO))   //Filtra a condição para continuar o fluxo
                .parallel(3) //Processa 3 elementos de uma vez
                .runOn(Schedulers.boundedElastic())
                .flatMap(pageListDTO -> // Inicia o processamento de um item da lista
                        getContentFromPage(pageListDTO.getId()) //Pega conteudo da página
                                .map(this::splitPageContentInChunks) //Gera chunks
                                .flatMap(chunks -> //Gera embeddings
                                        generateEmbeddingsFromChunks(chunks, pageListDTO.getTitle(), pageListDTO.getLastModified(), pageListDTO.getId())
                                ).flatMap(this::saveEmbedding) //Salva os embeddings no banco
                ).sequential()//Retorn para o fluxo
                .subscribe(
                        saved -> logger.info("Saved {}", saved),
                        error -> logger.error("Erro ", error),
                        () -> logger.info("Finalizado")
                );
    }

    private Mono<List<PageListDTO>> searchPagesInWikijs(){
        logger.info("Buscando páginas na Wiki js");
        return wikijsService.pageList();
    }

    private Mono<PageContentDTO> getContentFromPage(int id){
        logger.info("Obtendo conteúdo da página "+id);
        return wikijsService.pageContent(id);
    }

    private List<String> splitPageContentInChunks(PageContentDTO content){
        logger.info("QUebrando conteúdo da página %s em Chunks".formatted(content.getTitle()));
        return splitter.split(content.getContent(), content.getTitle(), 1000, 10);
    }

    private Mono<List<EmbeddingDTO>> generateEmbeddingsFromChunks(List<String> chunks, String title, LocalDateTime lastModified, int pageId){
        logger.info("Gerando embeddings da página %s".formatted(title));
        return embeddingGenerate.embeddingsGenerate(chunks, title, lastModified, pageId);
    }

    private Mono<List<EmbeddingEntity>> saveEmbedding(List<EmbeddingDTO> embeddings){
        logger.info("Salvando embeddings no banco");
        return Mono.fromCallable(() -> persitenceService.save(embeddings));
    }
}
