package br.com.iaassistentchat.services.embeddings;

import br.com.iaassistentchat.DTO.EmbeddingDTO;
import br.com.iaassistentchat.model.EmbeddingEntity;
import br.com.iaassistentchat.repository.EmbeddingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class EmbeddingPersitence {

    @Autowired
    private EmbeddingRepository repository;
    @Autowired
    private EmbeddingConveter conveter;


    private Logger logger = LoggerFactory.getLogger(EmbeddingPersitence.class);

    public List<EmbeddingEntity> save(List<EmbeddingDTO> embeddings) throws Exception {

        //Converter dto para Entity
        List<EmbeddingEntity> entities = embeddings.stream()
                .map(dto -> conveter.toEntity(dto)
        ).toList();

        //Persiste no banco
        logger.info("Persistindo embeddings no Banco");


        // Se embedding existe no banco pelo mesmo pageId, então a página está sendo atualizada.
        // Com isso os chunks da página no banco é removido e salvo os novos.
        //Isso é necessário para evitar chunks de mesmo texto no banco.
        var entityVerify = embeddings.getFirst();
        if (repository.existByPageId(entityVerify.getPageId())){

            //Deleta embedding pelo pageId
            int deleted = repository.deleteByPageId(entityVerify.getPageId());
            logger.info("%s sendo atualizada. Removendo antigos %d chunks.".formatted(entityVerify.getSource(), deleted));
        }

        for (EmbeddingEntity entity : entities) {

            repository.save(entity);
        }

        logger.info("%s psalvo no banco".formatted(entityVerify));

        return  entities;

    }
}
