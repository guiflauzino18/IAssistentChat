package br.com.iaassistentchat.repository;

import br.com.iaassistentchat.DTO.EmbeddingResultDTO;
import br.com.iaassistentchat.model.EmbeddingEntity;
import com.pgvector.PGvector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class EmbeddingRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    private final Logger logger = LoggerFactory.getLogger(EmbeddingRepository.class);

    public void save(EmbeddingEntity embedding){

        logger.info(String.format("Persistindos %s no Banco de Dados", embedding.getSource()));

        Map<String, Object> params = new HashMap<>();
        params.put("id", UUID.randomUUID());
        params.put("text", embedding.getText());
        params.put("vector", embedding.getVector());
        params.put("source", embedding.getSource());

        String sql = """
                    INSERT INTO embeddings (id, text, vector, source) VALUES (:id, :text, :vector, :source)
                """;

        jdbc.update(sql, params);
    }

    public List<EmbeddingResultDTO> findBySimilarity(PGvector vector, int topK){

        logger.info("Realizando busca veterial por Similaridade");
        String sql = """
                        SELECT id, text, source FROM embeddings
                        ORDER BY vector <-> :vector
                        LIMIT :topK
                     """;

        var params = new MapSqlParameterSource()
                .addValue("vector", vector)
                .addValue("topK", topK);

        return jdbc.query(sql, params, (result, rowNum) -> new EmbeddingResultDTO(
                UUID.fromString(result.getString("id")),
                result.getString("text"),
                result.getString("source")
        ));
    }
}