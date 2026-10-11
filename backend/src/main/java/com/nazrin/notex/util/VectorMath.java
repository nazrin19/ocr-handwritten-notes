
package com.nazrin.notex.util;

import java.util.List;

public final class VectorMath {

    private VectorMath() {
    }

    public static double cosineSimilarity(List<Double> a, List<Double> b) {
        double dot = 0;
        double normA = 0;
        double normB = 0;
        for (int i = 0; i < a.size(); i++) {
            dot += a.get(i) * b.get(i);
            normA += a.get(i) * a.get(i);
            normB += b.get(i) * b.get(i);
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
