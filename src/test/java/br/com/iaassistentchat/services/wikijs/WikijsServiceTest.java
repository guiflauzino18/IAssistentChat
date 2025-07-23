package br.com.iaassistentchat.services.wikijs;

import br.com.iaassistentchat.DTO.PageContentDTO;
import br.com.iaassistentchat.DTO.PageListDTO;
import br.com.iaassistentchat.mocks.WikijsMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WikijsServiceTest {


    @InjectMocks
    private WikijsService service;

    @Mock
    private WikijsClient client;

    private String mockJsonPageList;
    private String mockJsonPageContent;

    @BeforeEach
    void setUp() {
        var wikijsMocks = new WikijsMocks();
        this.mockJsonPageList = wikijsMocks.getPageListJsonMock();
        this.mockJsonPageContent = wikijsMocks.getPageContentJsonMock();

    }

    @Test
    void pageContent() {

        Mockito.when(client.runQuery(Mockito.anyString()))
                .thenReturn(Mono.just(this.mockJsonPageContent));

        PageContentDTO result = service.pageContent(Mockito.anyInt()).block();
        assertNotNull(result);

        assertEquals("Home", result.getTitle());
        assertEquals("Home Content", result.getContent());

    }

    @Test
    void pageList() {
//       Quando chamar client.runQuery retorna o mock JsonPageList
        Mockito.when(client.runQuery(Mockito.anyString()))
                .thenReturn(Mono.just(this.mockJsonPageList));

        List<PageListDTO> result = service.pageList().block();
        assertNotNull(result);
        assertEquals(2, result.size()); //Espera 2 e o tamanho deve ser 2

        assertEquals("Página A", result.get(0).getTitle());
        assertEquals("/a", result.get(0).getPath());

        assertEquals("Página B", result.get(1).getTitle());
        assertEquals("/b", result.get(1).getPath());

    }
}