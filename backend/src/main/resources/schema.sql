CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS notes (
                                     id BIGSERIAL PRIMARY KEY,
                                     title TEXT NOT NULL,
                                     created_at TIMESTAMP NOT NULL DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS note_chunks (
                                           id BIGSERIAL PRIMARY KEY,
                                           note_id BIGINT NOT NULL REFERENCES notes(id) ON DELETE CASCADE,
    chunk_index INT NOT NULL,
    content TEXT NOT NULL,
    embedding vector(768) NOT NULL
    );

CREATE INDEX IF NOT EXISTS idx_note_chunks_note_id ON note_chunks(note_id);