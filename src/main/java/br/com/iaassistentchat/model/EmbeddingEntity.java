package br.com.iaassistentchat.model;
import com.pgvector.PGvector;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "embeddings")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EmbeddingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    private String text;

    private PGvector vector;

    private String source;
}