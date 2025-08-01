-- Ativa extensão pgvector
CREATE EXTENSION IF NOT EXISTS vector;

-- Cria tabela de embeddings
CREATE TABLE IF NOT EXISTS embeddings (
    id UUID PRIMARY KEY,
    page_id INTEGER,
    text TEXT,
    vector VECTOR(768),
    source VARCHAR,
    last_modified TIMESTAMP
);

CREATE INDEX idx_embeddings_vector ON embeddings USING ivfflat (vector vector_cosine_ops);

--Executar abaixo após popular o Banco.
--REINDEX INDEX idx_embeddings_vector;