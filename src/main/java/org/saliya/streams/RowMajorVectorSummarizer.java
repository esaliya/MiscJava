package org.saliya.streams;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Represents a class that will use java.util.stream.* classes to
 * summarize a set of vectors represented in the row major form
 */
public class RowMajorVectorSummarizer {
    public static void main(String[] args) {
        int numVec = 100000;
        int vecLen = 100;
        double [][] vectors = new double[numVec][vecLen]; // row major vectors
        // Initialize vectors. This will simply make,
        // vectors[i][1] = i and vectors[i][j] = vectors[i][j-1]*10 where j > 0
        initializeVectors(numVec, vecLen, vectors);

        /*Summarize*/
        // We are performing a mutable reduction, hence the use of collect().
        // We could use reduce(), but unlike in collect() where the container
        // supplier is invoked for every sub stream of vectors, the reduce() will use the
        // same identity object provided to it for all the sub streams.
        // In the mutable reduction scenario this will produce undeterministic results
        // when run in parallel. See summarizeWithSequentialReduce() for an example
        // on using reduce() with a sequential stream of vectors to achieve the same results.
        // A sub stream in this case is simply a split of the vectors stream such that
        // each thread (only when parallel stream is used as here) will operate on a
        // sub set of vectors sequentially using the given accumulator . The results
        // form each thread are combined using the given combiner.
        DoubleSummaryStatisticsContainer container = Arrays.stream(vectors).parallel()
                                                           .collect(() -> new DoubleSummaryStatisticsContainer(vecLen),
                                                                    DoubleSummaryStatisticsContainer::accept,
                                                                    DoubleSummaryStatisticsContainer::combine);
        // Print averages
        printAverages(container);

    }

    private static void printAverages(DoubleSummaryStatisticsContainer container) {
        System.out.println(Arrays.toString(
                Arrays.stream(container.getSummaries()).map(DoubleSummaryStatistics::getAverage).toArray()));
    }

    private static void initializeVectors(int numVec, int vecLen, double[][] vectors) {
        IntStream.range(0, numVec).parallel().forEach(
                i -> IntStream.range(0,vecLen).parallel().forEach(
                        j -> vectors[i][j] = (i+1)*Math.pow(10,(j))));
    }

    private void summarizeWithSequentialReduce(int numVec, int vecLen, double [][] vectors){
        // An array of summary objects to keep track of statistics for each vector component
        // This will be the 'store' that we'll mutate
        DoubleSummaryStatistics [] summaries = new DoubleSummaryStatistics[vecLen];
        IntStream.range(0,vecLen).parallel().forEach(i -> summaries[i] = new DoubleSummaryStatistics());

        Arrays.stream(vectors).reduce(summaries,
                                      (csA, vec) -> {
                                          IntStream.range(0, vecLen).parallel().forEach(i -> csA[i].accept(vec[i]));
                                          return csA;
                                      }, (csA1, csA2) -> {
                                          IntStream.range(0, vecLen).parallel().forEach(i -> csA1[i].combine(csA2[i]));
                                          return csA1;
                                      });
    }
}
