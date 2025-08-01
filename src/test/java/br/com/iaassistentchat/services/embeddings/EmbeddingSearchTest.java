package br.com.iaassistentchat.services.embeddings;

import br.com.iaassistentchat.DTO.EmbeddingDTO;
import br.com.iaassistentchat.DTO.EmbeddingResultDTO;
import br.com.iaassistentchat.mocks.EmbeddingMocks;
import br.com.iaassistentchat.model.EmbeddingEntity;
import br.com.iaassistentchat.repository.EmbeddingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmbeddingSearchTest {


    @Mock
    private EmbeddingRepository repository;

    @InjectMocks
    private EmbeddingSearch service;
    @InjectMocks
    private EmbeddingConveter conveter;

    private EmbeddingResultDTO resultDTO;
    private EmbeddingDTO dto;
    private final EmbeddingMocks mocks = new EmbeddingMocks();
    private EmbeddingEntity entity;

    @BeforeEach
    void setUp() {
        resultDTO = mocks.getMockEmbeddingResultDTO(1);
        dto = mocks.getMockEmbeddingDTO(1);
        entity = conveter.toEntity(dto);
    }

    @Test
    void findBySimilarity() {
        when(repository.findBySimilarity(entity.getVector(), 5))
                .thenReturn(List.of(resultDTO));

        List<EmbeddingResultDTO> result = service.findBySimilarity(entity.getVector(), 5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Text Mock 1", result.getFirst().text());
        assertEquals("Source Mock 1", result.getFirst().source());

        verify(repository, times(1)).findBySimilarity(entity.getVector(), 5);
    }
}