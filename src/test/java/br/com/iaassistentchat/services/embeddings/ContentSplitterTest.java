package br.com.iaassistentchat.services.embeddings;

import br.com.iaassistentchat.utils.ContentNormalizeSpace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;

class ContentSplitterServiceTest {

    private ContentNormalizeSpace normalizeSpace = new ContentNormalizeSpace();

    private final ContentSplitterService splitter = new ContentSplitterService();

    @BeforeEach
    void setUp() {
    }

    @Disabled
    @Test
    void split() {

        String texto = "A".repeat(100);
        Mockito.when(normalizeSpace.normalize(Mockito.anyString())).thenReturn(texto);
        List<String> chunks = splitter.split(texto, 40, 10);

        assertEquals(3, chunks.size());
        assertEquals(40, chunks.get(0).length());
        assertEquals(40, chunks.get(1).length());
    }
}