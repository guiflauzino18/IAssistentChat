package br.com.iaassistentchat;

import br.com.iaassistentchat.services.embeddings.ContentSplitter;
import br.com.iaassistentchat.services.embeddings.EmbeddingGenerate;
import br.com.iaassistentchat.services.embeddings.EmbeddingPersitence;
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

    private Logger logger = LoggerFactory.getLogger(StartupRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Iniciando...");

        logger.info("Buscando páginas na Wikijs");
        wikijsService.pageList()
                .subscribe(pages -> {
                    pages.forEach(page -> {

                        logger.info("Buscando conteúdo de cada página");
                        wikijsService.pageContent(page.getId()).subscribe(content -> {

                            logger.info("Quebrando conteúdo da página em chunks");
                            var chunk = splitter.split(content.getContent(), 1000, 100);

                            logger.info("Gerando embeddings");
                            embeddingGenerate.embeddingsGenerate(chunk, content.getTitle()).subscribe(items -> {

                                logger.info("Persistindo dados no Banco");
                                persitenceService.save(items);
                            });
                        });
                    });
                });
    }
}
