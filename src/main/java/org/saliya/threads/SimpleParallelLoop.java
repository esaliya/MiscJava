package org.saliya.threads;

import org.saliya.threads.parallel.Parallel;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleParallelLoop {
    static int size;
    static int poolSize;
    static ExecutorService execSvc;
    public static void main(String[] args) {

        setupParallelism();


        method1(size);
        method1(size);

        endParallelism();

    }

    public static void method1(int size){

        System.out.println("Main thread: " + Thread.currentThread().getId());
        // preparing for three point stencil
        double [] in = new double[size+2];
        double [] out = new double[size];

        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < size+2; i++){
            in[i] = r.nextInt();
        }
        double t1 = System.currentTimeMillis();
        try {
            Parallel.For(1, size+1, execSvc, taskIndex -> {
                out[taskIndex - 1] = (in[taskIndex - 1] + in[taskIndex + 1]) / 2;
                System.out.println(Thread.currentThread().getId());
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double time = System.currentTimeMillis() - t1;
        System.out.println("Parallel took: " + time + "ms");
    }



    private static void endParallelism() {
        execSvc.shutdown();
    }

    private static void setupParallelism() {
        size = 8;
        poolSize = 8;
        execSvc = Executors.newFixedThreadPool(poolSize);
//        execSvc = new ForkJoinThreadPool(poolSize, false);
    }

}
