package org.saliya.threads;

import org.saliya.threads.parallel.Parallel;
import org.saliya.threads.parallel.Task;

import java.util.concurrent.ExecutorService;

public class ParallelMethods {
    public static void method1(int size, double [] in, double [] out, ExecutorService execSvc) throws
            InterruptedException {
        long t1 = System.currentTimeMillis();
        Parallel.For(1, size, execSvc, taskIndex -> {
            out[taskIndex - 1] = (in[taskIndex - 1] + in[taskIndex + 1]) / 2;
        });
        long time = System.currentTimeMillis() - t1;
        System.out.println("Parallel took: " + time + "ms");
    }
}
