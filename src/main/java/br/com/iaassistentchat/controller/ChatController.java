package br.com.iaassistentchat.controller;

import br.com.iaassistentchat.model.EmbeddingEntity;
import br.com.iaassistentchat.services.rag.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private RagService ragService;

    @PostMapping("/ask")
    public Mono<ResponseEntity<String>> ask(@RequestBody Map<String, String> body){

        String pergunta = body.get("pergunta");

        return ragService.chatResponse(pergunta)
                .map(result -> {
                    System.out.println("Result "+result);

                    return new ResponseEntity<>(result, HttpStatus.OK);

                })
                .defaultIfEmpty(ResponseEntity.badRequest().body("Erro ao gerar resposta"));

    }
}
