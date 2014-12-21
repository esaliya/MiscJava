package org.saliya.threads.frameworks;

import edu.rice.hj.runtime.forkjoin.ForkJoinThreadPool;

import java.util.Random;
import java.util.concurrent.ExecutorService;

import static edu.rice.hj.Module0.finalizeHabanero;
import static edu.rice.hj.Module0.initializeHabanero;

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
        execSvc.shutdown();
        finalizeHabanero();
    }

    private static void setupParallelism() {
        size = 8;
        poolSize = 8;
//        execSvc = Executors.newFixedThreadPool(poolSize);
        execSvc = new ForkJoinThreadPool(poolSize, false);
        initializeHabanero();
    }
}
