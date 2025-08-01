package br.com.iaassistentchat.repository;

import br.com.iaassistentchat.DTO.EmbeddingDTO;
import br.com.iaassistentchat.DTO.EmbeddingResultDTO;
import br.com.iaassistentchat.model.EmbeddingEntity;
import com.pgvector.PGvector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class EmbeddingRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    private final Logger logger = LoggerFactory.getLogger(EmbeddingRepository.class);

    public void save(EmbeddingEntity embedding){

        Map<String, Object> params = new HashMap<>();
        params.put("id", UUID.randomUUID());
        params.put("page_id", embedding.getPageId());
        params.put("text", embedding.getText());
        params.put("vector", embedding.getVector());
        params.put("source", embedding.getSource());
        params.put("last_modified", embedding.getLastModified());

        String sql = """
                    INSERT INTO embeddings (id, page_id, text, vector, source, last_modified) VALUES (:id, :page_id, :text, :vector, :source, :last_modified)
                """;

        jdbc.update(sql, params);
    }

    public List<EmbeddingResultDTO> findBySimilarity(PGvector vector, int topK){

        logger.info("Realizando busca veterial por Similaridade");
        String sql = """
                        SELECT id, text, source FROM embeddings
                        ORDER BY vector <=> :vector
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

    public List<EmbeddingEntity> findBySource(String source){
        String sql = """
                        SELECT * FROM embeddings
                        WHERE source = :source
                    """;

        var params = new MapSqlParameterSource().addValue("source", source);


        return jdbc.query(sql, params, (result, rowNum ) -> new EmbeddingEntity(
            UUID.fromString(result.getString("id")),
            result.getInt("page_id"),
            result.getString("text"),
            new PGvector(result.getString("vector")),
            result.getString("source"),
            LocalDateTime.parse(result.getString("last_modified").replaceAll(" ", "T"))

        ));

    }

    public boolean existByPageId(int id){
        String sql = """
                        SELECT page_id FROM embeddings
                        WHERE page_id = :pageId
                    """;
        var param = new MapSqlParameterSource().addValue("pageId", id);

        return !jdbc.query(sql, param, (result, rowNum) -> rowNum > 0).isEmpty();
    }

    public int deleteByPageId(int id){
        String sql = """
                        DELETE FROM embeddings
                        WHERE page_id = :id
                    """;

        var params = new MapSqlParameterSource().addValue("id", id);
        return jdbc.update(sql, params);
    }

    public Optional<EmbeddingEntity> findByPageId(int id) {
        String sql = """
                        SELECT * FROM embeddings
                        WHERE page_id = :page_id
                    """;

        var params = new MapSqlParameterSource().addValue("page_id", id);

        EmbeddingEntity entity = jdbc.queryForObject(sql, params, (result, rowNum) -> new EmbeddingEntity(
                UUID.fromString(result.getString("id")),
                result.getInt("page_id"),
                result.getString("text"),
                new PGvector(result.getString("vector")),
                result.getString("source"),
                LocalDateTime.parse(result.getString("last_modified").replaceAll(" ", "T"))
        ));

        return Optional.ofNullable(entity);
    }
}