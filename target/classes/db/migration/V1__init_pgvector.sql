-- Ativa extensão pgvector
CREATE EXTENSION IF NOT EXISTS vector;

-- Cria tabela de embeddings
CREATE TABLE IF NOT EXISTS embeddings (
    id UUID PRIMARY KEY,
    text TEXT,
    vector VECTOR(768),
    source VARCHAR,
    last_modified TIMESTAMP
);

--CREATE INDEX idx_embeddings_vetor ON embeddings USING ivfflat (vetor vector_cosine_ops) WITH (lists = 100);