package br.com.iaassistentchat.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PageListDTO {

    private int id;
    private String title;
    private String path;
    private LocalDateTime lastModified;


}
