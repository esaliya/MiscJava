package org.saliya.threads;

import edu.rice.hj.runtime.forkjoin.ForkJoinThreadPool;
import org.saliya.threads.parallel.Parallel;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static edu.rice.hj.HJ.finalizeHabanero;
import static edu.rice.hj.HJ.initializeHabanero;

public class Tester {
    static int size;
    static int poolSize;
    static ExecutorService execSvc;
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());
        setupParallelism();
        // preparing for three point stencil
        double [] in = new double[size+2];
        double [] out = new double[size];

        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < size+2; i++){
            in[i] = r.nextInt();
        }

        HJMethods.method1(size,in, out);
        ParallelMethods.method1(size, in, out,execSvc);
        endParallelism();
    }

    private static void endParallelism() {
        finalizeHabanero();
        execSvc.shutdown();
    }

    private static void setupParallelism() {
        size = 8;
        poolSize = 8;
//        execSvc = Executors.newFixedThreadPool(poolSize);
        execSvc = new ForkJoinThreadPool(poolSize, false);
        initializeHabanero();
    }
}
