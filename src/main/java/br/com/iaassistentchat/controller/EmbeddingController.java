package br.com.iaassistentchat.controller;

import br.com.iaassistentchat.StartupRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/embedding")
public class EmbeddingController {

    @Autowired
    private StartupRunner start;

    @GetMapping("/generate")
    public Mono<String> generateEmbedding(){

        return Mono.fromSupplier(() -> {
                start.startEmbedding();
                return "Gerando Embeddings";
            });
    }
}
