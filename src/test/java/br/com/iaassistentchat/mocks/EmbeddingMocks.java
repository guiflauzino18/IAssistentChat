package br.com.iaassistentchat.mocks;


import br.com.iaassistentchat.DTO.EmbeddingDTO;
import br.com.iaassistentchat.DTO.EmbeddingResultDTO;
import org.springframework.ai.embedding.Embedding;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class EmbeddingMocks {


    public EmbeddingDTO getMockEmbeddingDTO(int i){
        return this.mockEmbeddingDTO(i);
    }

    public EmbeddingResultDTO getMockEmbeddingResultDTO(int i){
        return this.mockEmbeddingResultDTO(i);
    }



    private EmbeddingResultDTO mockEmbeddingResultDTO(int id){

        return new EmbeddingResultDTO(
                UUID.randomUUID(),
                "Text Mock "+id,
                "Source Mock "+id
        );

    }

    private EmbeddingDTO mockEmbeddingDTO(int id){

        float[] vector = {0.1f, 0.2f, 0.3f};

        EmbeddingDTO mock = new EmbeddingDTO();
        mock.setTexto("Text mock "+id);
        mock.setSource("Source mock "+id);
        mock.setVetor(vector);
        mock.setLastModified(LocalDateTime.now());

        return mock;

    }

}
