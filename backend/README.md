# Notex Backend

Spring Boot service that turns notes into a searchable knowledge base and answers
questions grounded only in the user's own notes (RAG).

## How it works
1. Upload a note: it is split into overlapping chunks, each chunk is embedded
   with Gemini, and the vectors are stored in PostgreSQL (pgvector).
2. Ask a question: the question is embedded, the closest chunks are retrieved,
   and only those are sent to Gemini with an instruction to answer from them alone.
   The response includes the source chunks used.

## Stack
Java 21, Spring Boot, PostgreSQL + pgvector, Gemini API, Maven

## Run locally
1. Start the database (needs Docker):
```
   docker run -d --name notex-db -e POSTGRES_USER=notex -e POSTGRES_PASSWORD=notex_dev_pw -e POSTGRES_DB=notex -p 5432:5432 -v notex-pgdata:/var/lib/postgresql/data pgvector/pgvector:pg17
```
2. Set the environment variable `GEMINI_API_KEY` (key from Google AI Studio).
3. Run `NotexApplication`. Tables are created automatically from `schema.sql`.

## API
- `POST /notes` with `{ "title": "...", "text": "..." }` returns `{ "noteId", "chunkCount" }`
- `POST /notes/ask` with `{ "noteId": 1, "question": "..." }` returns `{ "answer", "sources" }`.
  Omit `noteId` to search across all notes.

## Design notes
- Model names and RAG settings live in `application.properties`, so changing models needs no code change.
- Gemini calls retry on transient failures and fall back to a second model.
- Embeddings are generated before the database write, so a failed upload never leaves a partial note.