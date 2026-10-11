package com.nazrin.notex.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class NoteRepository {

    private final JdbcTemplate jdbc;

    public NoteRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    public long saveNote(String title, List<String> chunks, List<List<Double>> vectors) {
        Long noteId = jdbc.queryForObject(
                "INSERT INTO notes (title) VALUES (?) RETURNING id", Long.class, title);

        for (int i = 0; i < chunks.size(); i++) {
            jdbc.update(
                    "INSERT INTO note_chunks (note_id, chunk_index, content, embedding) "
                            + "VALUES (?, ?, ?, CAST(? AS vector))",
                    noteId, i, chunks.get(i), toVectorLiteral(vectors.get(i)));
        }
        return noteId;
    }

    private String toVectorLiteral(List<Double> vector) {
        return vector.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));
    }

    public List<String> findSimilarChunks(List<Double> queryVector, Long noteId, int limit) {
        String vector = toVectorLiteral(queryVector);
        if (noteId == null) {
            return jdbc.queryForList(
                    "SELECT content FROM note_chunks ORDER BY embedding <=> CAST(? AS vector) LIMIT ?",
                    String.class, vector, limit);
        }
        return jdbc.queryForList(
                "SELECT content FROM note_chunks WHERE note_id = ? "
                        + "ORDER BY embedding <=> CAST(? AS vector) LIMIT ?",
                String.class, noteId, vector, limit);
    }
}