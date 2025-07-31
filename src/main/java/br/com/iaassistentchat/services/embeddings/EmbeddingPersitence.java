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

    public List<EmbeddingEntity> save(List<EmbeddingDTO> embeddings){

        //Converter dto para Entity
        List<EmbeddingEntity> entities = embeddings.stream()
                .map(dto -> {
                    var entity = conveter.toEntity(dto);
                    return entity;
                }
        ).toList();

        //Persiste no banco
        logger.info("Persistindo embeddings no Banco");

        for (EmbeddingEntity embeddingEntity : entities) {
            repository.save(embeddingEntity);

        }

        return  entities;

    }
}
