package br.com.iaassistentchat.mocks;


import br.com.iaassistentchat.DTO.PageContentDTO;
import br.com.iaassistentchat.DTO.PageListDTO;

public class WikijsMocks {


    private String pageListJsonMock = """
                {
                  "data": {
                    "pages": {
                      "list": [
                        { "id": "1", "title": "Página A", "path": "/a", "updatedAt": "2025-05-13T20:13:22.786Z" },
                        { "id": "2", "title": "Página B", "path": "/b", "updatedAt": "2025-05-13T20:13:22.786Z" }
                      ]
                    }
                  }
                }
            """;

    private String pageContentJsonMock = """
            {
              "data": {
                "pages": {
                  "single": {
                    "title": "Home",
                    "content": "Home Content"
                  }
                }
              }
            }
            """;

    public String getPageListJsonMock() {
        return pageListJsonMock;
    }

    public String getPageContentJsonMock() {
        return pageContentJsonMock;
    }

}