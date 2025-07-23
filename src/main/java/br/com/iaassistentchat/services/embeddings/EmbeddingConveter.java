package br.com.iaassistentchat.services.embeddings;

import br.com.iaassistentchat.DTO.EmbeddingDTO;
import br.com.iaassistentchat.model.EmbeddingEntity;
import com.pgvector.PGvector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmbeddingConveter {

    private Logger logger = LoggerFactory.getLogger(EmbeddingConveter.class);

    public EmbeddingEntity toEntity(EmbeddingDTO dto){

        logger.info("Converterndo DTO para Entity");
        var entity = new EmbeddingEntity();
        entity.setText(dto.getTexto());
        entity.setSource(dto.getSource());
        PGvector vector = new PGvector(dto.getVetor());
        entity.setVector(vector);
        return entity;
    }

}
