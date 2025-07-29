package br.com.iaassistentchat.services.wikijs;

import br.com.iaassistentchat.DTO.PageListDTO;
import br.com.iaassistentchat.repository.EmbeddingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component

public class WikijsPageVersionCompare {
    @Autowired
    private EmbeddingRepository repository;

    public boolean updated(PageListDTO pageDTO){


        var page = repository.findBySource(pageDTO.getTitle());
        if (page.isEmpty())

            return true;

        if (page.getFirst().getLastModified() == null) {
            return true;
        }

        if (page.getFirst().getLastModified().isBefore(pageDTO.getLastModified())) {
            return true;
        }

        return false;
    }
}
