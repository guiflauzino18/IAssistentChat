package br.com.iaassistentchat.services.embeddings;

import br.com.iaassistentchat.DTO.EmbeddingDTO;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmbeddingPersitenceTest {

    @Mock
    private EmbeddingRepository repository;
    @Mock
    private EmbeddingConveter conveter;

    @InjectMocks
    private EmbeddingPersitence persitence;

    private EmbeddingDTO dto;
    private EmbeddingEntity entity;
    private EmbeddingMocks embeddingMocks = new EmbeddingMocks();

    @BeforeEach
    void setUp() {
        dto = embeddingMocks.getMockEmbeddingDTO(1);
        entity = mock(EmbeddingEntity.class);
        entity.setText(dto.getTexto());
        entity.setSource(dto.getSource());

    }

    @Test
    void save() {
        List<EmbeddingDTO> listDTO = List.of(dto, dto);
        when(conveter.toEntity(any(EmbeddingDTO.class))).thenReturn(entity);

        persitence.save(listDTO);

        //Verifica que o converter foi chamado duas vezes
        verify(conveter, times(2)).toEntity(any(EmbeddingDTO.class));

        //Verifica que o repository salvou duas vezes
        verify(repository, times(2)).save(entity);
    }
}