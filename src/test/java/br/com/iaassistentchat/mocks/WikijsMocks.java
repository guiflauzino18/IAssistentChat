package br.com.iaassistentchat.mocks;


import br.com.iaassistentchat.DTO.PageContentDTO;
import br.com.iaassistentchat.DTO.PageListDTO;

public class WikijsMocks {


    private String pageListJsonMock = """
                {
                  "data": {
                    "pages": {
                      "list": [
                        { "id": "1", "title": "Página A", "path": "/a" },
                        { "id": "2", "title": "Página B", "path": "/b" }
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