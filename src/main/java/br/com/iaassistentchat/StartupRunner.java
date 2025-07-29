package br.com.iaassistentchat;

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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Iniciando...");

        logger.info("Buscando páginas na Wikijs");
        wikijsService.pageList()
                .subscribe(pages -> {
                    pages.forEach(page -> {

                        if (pageVersion.updated(page)){

                            logger.info("Buscando conteúdo da página %s".formatted(page.getTitle()));
                            wikijsService.pageContent(page.getId()).subscribe(content -> {


                                logger.info("Quebrando conteúdo da página em chunks");
                                var chunks = splitter.split(content.getContent(), 1000, 100);


                                logger.info("Gerando embeddings");
                                embeddingGenerate.embeddingsGenerate(
                                        chunks, content.getTitle(),
                                        page.getLastModified()).subscribe(items -> {

                                    persitenceService.save(items);
                                });
                            });
                        }

                        logger.info("Página %s não atualizada.".formatted(page.getTitle()));
                    });

                    logger.info("Finalizado geração de embeddings.");
                });
    }
}
