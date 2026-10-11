package com.nazrin.notex.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkingService {

    private static final int SENTENCES_PER_CHUNK = 4;
    private static final int OVERLAP_SENTENCES = 1;

    public List<String> chunk(String text) {
        String[] sentences = text.split("(?<=[.!?])\\s+");
        List<String> chunks = new ArrayList<>();

        int i = 0;
        while (i < sentences.length) {
            int end = Math.min(i + SENTENCES_PER_CHUNK, sentences.length);
            StringBuilder chunk = new StringBuilder();
            for (int j = i; j < end; j++) {
                chunk.append(sentences[j]).append(" ");
            }
            chunks.add(chunk.toString().trim());

            if (end == sentences.length) break;
            i = end - OVERLAP_SENTENCES;
        }

        return chunks;
    }
}
