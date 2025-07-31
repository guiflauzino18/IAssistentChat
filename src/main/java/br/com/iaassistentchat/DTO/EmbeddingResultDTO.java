package br.com.iaassistentchat.DTO;

import lombok.AllArgsConstructor;

import java.util.UUID;


public record EmbeddingResultDTO(
        UUID id,
        String text,
        String source
) {

}
