package br.com.iaassistentchat.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EmbeddingDTO {

    private String texto;
    private float[] vetor;
    private String source;
    private LocalDateTime lastModified;
}
