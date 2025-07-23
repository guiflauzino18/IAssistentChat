package br.com.iaassistentchat.services.embeddings;

import br.com.iaassistentchat.DTO.EmbeddingResultDTO;
import br.com.iaassistentchat.repository.EmbeddingRepository;
import com.pgvector.PGvector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmbeddingSearchService {

    @Autowired
    private EmbeddingRepository repository;

    public List<EmbeddingResultDTO> findBySimilarity(PGvector vector, int topK){
        return repository.findBySimilarity(vector, topK);
    }
}
