package org.saliya.tpn;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ParallelPatternGenerator {
    private static final Pattern pattern = Pattern.compile(",");
    private static final char x = 'x';
    public static void main(String[] args) {
        // TODO - read these from command line
        int maxParallelismPerNode = 24; // Should be a power of 2
        int maxNumberOfNodes = 1;
        int limitNumberOfThreadsTo = maxParallelismPerNode;
        int limitNumberOfProcsTo = maxParallelismPerNode;
        String parallelismsString = "1,2,4,8,16,32,64,128,256"; // Each parallelism has to be a power of 2

        int [] parallelisms = getParallelisms(parallelismsString);
        int maxThreads = Math.min(limitNumberOfThreadsTo, maxParallelismPerNode);
        int maxProcs = Math.min(limitNumberOfProcsTo, maxParallelismPerNode);
        for(int parallelism : parallelisms){
            System.out.println("Parallelism: " + parallelism);
            int maxThreadsForParallelism = Math.min(maxThreads, parallelism);
            int maxProcsForParallelism = Math.min(maxProcs, parallelism);
            for (int t = 1; t <= maxThreadsForParallelism; t*=2){
                int maxProcsForParallelismForThisManyThreads = maxProcsForParallelism / t;
                for (int p = 1; p <= maxProcsForParallelismForThisManyThreads; p*=2){
                    int n = parallelism / (t * p);
                    if (n <= maxNumberOfNodes){
                        System.out.println("\t" + t  + x + p + x + n);
                    }
                }
            }
        }

    }

    private static int[] getParallelisms(String parallelismsString) {
        String [] splits = pattern.split(parallelismsString);
        int [] arr = new int[splits.length];
        for(int i = 0; i < splits.length; ++i){
            // TODO - add check to see if integers and of power 2
            arr[i] = Integer.parseInt(splits[i]);
        }
        return arr;
    }
}
