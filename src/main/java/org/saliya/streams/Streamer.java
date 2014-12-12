package org.saliya.streams;

import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.regex.Pattern;
import java.util.stream.CloseableStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * See
 * http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/
 * and
 * http://winterbe.com/posts/2014/03/16/java-8-tutorial/
 * for great explanations on streams and Java 8 features in general
 */
public class Streamer {
    public static void main(String[] args) {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        // This can be set via -Djava.util.concurrent.ForkJoinPool.common.parallelism
        System.out.println(commonPool.getParallelism());

        /*Stream<Integer> numbers = Stream.of(1,2,3,4,5,6,7,8,9);
        numbers.forEach(i -> System.out.println(i * 100));

        DoubleStream randomDoubles = DoubleStream.generate(
                () -> new BigDecimal(Math.random() * 100).setScale(3, BigDecimal.ROUND_UP).doubleValue());
        // randomDoubles.forEach(System.out::println);  // will run for infinity

        processVectors();*/

        int numVec = 100000;
        int vecLen = 100;
        double [][] vectors = new double[numVec][vecLen];
        IntStream.range(0,numVec).parallel().forEach(i -> IntStream.range(0,vecLen).parallel().forEach(j -> vectors[i][j] = (i+1)*Math.pow(10,(j))));
//        print2DArray(vectors);

        /*DoubleSummaryStatistics [] summaries = new DoubleSummaryStatistics[vecLen];
        IntStream.range(0,vecLen).parallel().forEach(i -> summaries[i] = new DoubleSummaryStatistics());
        List<DoubleSummaryStatistics []> summariesList = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger ai = new AtomicInteger(0);
        Arrays.stream(vectors).reduce(getNewSummaries(summariesList, vecLen),
                                      (csA, vec) -> {
                                          System.out.println(
                                                  "csA:" + Arrays.toString(csA) + " -- vec:" + Arrays.toString(vec));
                                          IntStream.range(0, vecLen).parallel().forEach(i -> csA[i].accept(vec[i]));
                                          return csA;
                                      }, (csA1, csA2) -> {
                                          System.out.println(
                                                  ai.incrementAndGet() + "csA1:" + Arrays.toString(csA1) + " -- csA2:" +
                                                          Arrays.toString(csA2));
                                          IntStream.range(0, vecLen).parallel().forEach(i -> csA1[i].combine(csA2[i]));
                                          return csA1;
                                      });

        Optional<DoubleSummaryStatistics[]> finalSummaries = summariesList.stream().parallel().max(new Comparator<DoubleSummaryStatistics[]>() {
            @Override
            public int compare(DoubleSummaryStatistics[] o1, DoubleSummaryStatistics[] o2) {
                return  o1[0].getCount() < o2[0].getCount() ? -1 :  (o1[0].getCount() < o2[0].getCount() ? 0 : 1);
            }
        });
        finalSummaries.ifPresent(fs -> System.out.println(Arrays.toString(fs)));*/


        DoubleSummaryStatisticsContainer container = Arrays.stream(
                vectors).parallel().collect(()-> new DoubleSummaryStatisticsContainer(vecLen),
                                            DoubleSummaryStatisticsContainer::accept,
                                            DoubleSummaryStatisticsContainer::combine);
        System.out.println(Arrays.toString(Arrays.stream(container.getSummaries()).map(
                DoubleSummaryStatistics::getAverage).toArray()));

        /*int [] as = new int[] {1,2,3};
        int [] bs = new int[] {3,2,1};
        IntStream.range(0,as.length).forEach(i -> as[i] = as[i] + bs[i]);
        System.out.println(Arrays.toString(as));
        IntStream.range(0,as.length).forEach(i -> as[i] = as[i] + bs[i]);*/

        /*IntStream.range(0,vecLen).parallel().forEach(c -> {
            Arrays.stream(vectors).forEach(v -> ));
        });*/
    }

    private static DoubleSummaryStatistics[] getNewSummaries(List<DoubleSummaryStatistics[]> summariesList, int vecLen) {
        DoubleSummaryStatistics[] ss = new DoubleSummaryStatistics[vecLen];
        IntStream.range(0,vecLen).parallel().forEach(i -> ss[i] = new DoubleSummaryStatistics());
        summariesList.add(ss);
        return ss;
    }

    private static void print2DArray(double[][] vectors) {
        Arrays.stream(vectors).forEach(v -> {
            Arrays.stream(v).forEach(c -> System.out.print(c + "\t"));
            System.out.println();
        });
    }

    /**
     * A tester method to process data from Doug (nucleotide data) with streams
     */
    public static void processVectors(){
       /* int numVec = 67566;
        int vecLen = 512;
        double [][] vectorComps = new double[vecLen][numVec]; // column major storage of vectors
        Path file = Paths.get("E:\\Sali\\InCloud\\IUBox\\Box Sync\\SalsaBio\\nucleotide\\dataonly_dense_fullData" +
                ".ed4adcc9814cc333ac3fb782e474d5e6f014f4ca.5mer.txt");
        try (BufferedReader reader = Files.newBufferedReader(file, Charset.defaultCharset())) {
            Pattern pat = Pattern.compile("[\t]");
            Optional<String> line;
            while ((line = Optional.of(reader.readLine())).isPresent()){
                String
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }
}
