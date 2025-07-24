package br.com.iaassistentchat.services.embeddings;

import br.com.iaassistentchat.DTO.EmbeddingDTO;
import br.com.iaassistentchat.mocks.EmbeddingMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmbeddingRepositoryGenerateTest {

    @Mock
    private OpenAiEmbeddingModel embeddingModel;

    @InjectMocks
    private EmbeddingGenerate embeddingGenerate;

    private EmbeddingMocks embeddingMocks = new EmbeddingMocks();

    @BeforeEach
    void setup(){

    }


    @Test
    void testEmbeddingGenerate(){

        List<String> chunks = List.of("Texto 1", "Texto2 ", "Texto 3");
        EmbeddingDTO dto = embeddingMocks.getMockEmbeddingDTO(1);

        Embedding emb1 = new Embedding(dto.getVetor(), 1);
        Embedding emb2 = new Embedding(dto.getVetor(), 2);
        Embedding emb3 = new Embedding(dto.getVetor(), 3);

        EmbeddingResponse embeddingResponse = new EmbeddingResponse(List.of(emb1, emb2, emb3));
        when(embeddingModel.embedForResponse(anyList())).thenReturn(embeddingResponse);

         List<EmbeddingDTO> result = embeddingGenerate.embeddingsGenerate(chunks, dto.getSource()).block();

         assertNotNull(result);
         assertEquals(3, result.size());
         assertEquals(0.1f, result.getFirst().getVetor()[0]);
         assertEquals(0.2f, result.getFirst().getVetor()[1]);
         assertEquals(0.3f, result.getFirst().getVetor()[2]);
         assertEquals("Source mock "+1, result.getFirst().getSource());
    }

}