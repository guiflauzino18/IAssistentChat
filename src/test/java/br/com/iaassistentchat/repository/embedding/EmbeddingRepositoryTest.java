package br.com.iaassistentchat.repository.embedding;

import br.com.iaassistentchat.DTO.EmbeddingDTO;
import br.com.iaassistentchat.DTO.EmbeddingResultDTO;
import br.com.iaassistentchat.mocks.EmbeddingMocks;
import br.com.iaassistentchat.model.EmbeddingEntity;
import br.com.iaassistentchat.repository.EmbeddingRepository;
import br.com.iaassistentchat.services.embeddings.EmbeddingConveter;
import com.pgvector.PGvector;
import org.assertj.core.api.ObjectEnumerableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmbeddingRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate jdbc;
    @InjectMocks
    private EmbeddingRepository repository;
    @InjectMocks
    private EmbeddingConveter conveter;

    private EmbeddingMocks mocks = new EmbeddingMocks();
    private EmbeddingResultDTO resultDTO;
    private EmbeddingDTO dto;
    private EmbeddingEntity entity;

    @BeforeEach
    void setUp() {
        resultDTO = mocks.getMockEmbeddingResultDTO(1);
        dto = mocks.getMockEmbeddingDTO(1);
        entity = conveter.toEntity(dto);

    }

    @Test
    void save() {
        repository.save(entity);

        ArgumentCaptor<Map<String, Object>> paramsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(jdbc, times(1)).update(anyString(), paramsCaptor.capture());

        var result = paramsCaptor.getValue();
        assertEquals("Text mock 1", result.get("text"));
        assertEquals("Source mock 1", result.get("source"));
        assertInstanceOf(PGvector.class, result.get("vector"));
        assertInstanceOf(UUID.class, result.get("id"));

    }

    @Test
    void findBySimilarity() {
        when(jdbc.query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(List.of(resultDTO));

        List<EmbeddingResultDTO> result = repository.findBySimilarity(entity.getVector(), 5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Text Mock 1", result.getFirst().text());
        assertEquals("Source Mock 1", result.getFirst().source());

        verify(jdbc, times(1)).query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class));
    }
}