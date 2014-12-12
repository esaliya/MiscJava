package org.saliya.streams;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.stream.IntStream;

public class ColumnMajorSummarizer {
    public static void main(String[] args) {
        int numVec = 1000;
        int vecLen = 100;
        double [][] vectors = new double[vecLen][numVec]; // column major vectors
        // Initialize vectors. This will simply make,
        // vectors[i][1] = i and vectors[i][j] = vectors[i][j-1]*10 where j > 0
        initializeVectors(numVec, vecLen, vectors);

        Object[] summaries = Arrays.stream(vectors).parallel().map(c -> Arrays.stream(c).parallel().summaryStatistics()).toArray();
        System.out.println(Arrays.toString(summaries));

    }

    private static void initializeVectors(int numVec, int vecLen, double[][] vectors) {
        IntStream.range(0, numVec).parallel().forEach(
                i -> IntStream.range(0,vecLen).parallel().forEach(
                        j -> vectors[j][i] = (i+1)*Math.pow(10,(j))));
    }
}
