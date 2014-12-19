package org.saliya.streams;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.stream.IntStream;

public class ColumnMajorSummarizer {
    public static void main(String[] args) {
        int numVec = 10;
        int vecLen = 5;
        double [][] vectors = new double[vecLen][numVec]; // column major vectors
        // Initialize vectors. This will simply make,
        // vectors[i][1] = i and vectors[i][j] = vectors[i][j-1]*10 where j > 0
        initializeVectors(numVec, vecLen, vectors);
        print2DArray(vectors);
        Object[] summaries = Arrays.stream(vectors).
                parallel().map(c -> Arrays.stream(c).parallel().summaryStatistics()).toArray();
        Arrays.stream(summaries).map(i -> ((DoubleSummaryStatistics)i).toString()).forEach(System.out::println);

    }

    private static void initializeVectors(int numVec, int vecLen, double[][] vectors) {
        IntStream.range(0, numVec).parallel().forEach(
                i -> IntStream.range(0,vecLen).parallel().forEach(
                        j -> vectors[j][i] = (i+1)*Math.pow(10,(j))));
    }


    private static void print2DArray(double[][] vectors) {
        Arrays.stream(vectors).forEach(v -> {
            Arrays.stream(v).forEach(c -> System.out.print(c + "\t"));
            System.out.println();
        });
    }
}
