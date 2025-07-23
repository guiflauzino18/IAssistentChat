package br.com.iaassistentchat.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EmbeddingDTO {

    private String texto;
    private float[] vetor;
    private String source;
}
