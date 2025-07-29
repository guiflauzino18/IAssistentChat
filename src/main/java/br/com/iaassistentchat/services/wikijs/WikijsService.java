package br.com.iaassistentchat.services.wikijs;

import br.com.iaassistentchat.DTO.PageContentDTO;
import br.com.iaassistentchat.DTO.PageListDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WikijsService {

    @Autowired
    private WikijsClient wikijsClient;

    public Mono<PageContentDTO> pageContent(int id){
        String query = String.format("""
                    {
                      pages {
                        single (id: %d) {
                          title
                          content
                        }
                      }
                    }
                """, id);

        return wikijsClient.runQuery(query)
                .map(response -> {
                    try{
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode jsonTree = mapper.readTree(response);
                        JsonNode jsonContent = jsonTree.path("data").path("pages").path("single");
                        return new PageContentDTO(
                                jsonContent.path("title").asText(),
                                jsonContent.path("content").asText()
                        );

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public Mono<List<PageListDTO>> pageList(){
        var query = """
                    {
                      pages {
                        list {
                          id
                          title
                          path
                          updatedAt
                        }
                      }
                    }
                """;

        return wikijsClient.runQuery(query)
                .map(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode jsonTree = mapper.readTree(response);
                        JsonNode jsonPages = jsonTree.path("data").path("pages").path("list");

                        List<PageListDTO> pages = new ArrayList<>();
                        for(JsonNode page : jsonPages){
                            pages.add(new PageListDTO(
                                    page.path("id").asInt(),
                                    page.path("title").asText(),
                                    page.path("path").asText(),
                                    LocalDateTime.parse(page.path("updatedAt").asText().split("\\.")[0])
                            ));
                        }

                        return pages;

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}