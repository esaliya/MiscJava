package org.saliya.threads.basic;

import org.saliya.common.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

public class AffinityThreads {
    public static void main(String[] args) throws InterruptedException, IOException {
//        int numThreads = Integer.parseInt(args[0]);
//        int numCores = Integer.parseInt(args[1]);
//        boolean bind = Boolean.parseBoolean(args[2]);
        int numThreads = 6;
        int numCores = 4;
        boolean bind = true;

        System.out.println("PID:" + Utils.getPid());

//        AffinityLock al = null;
//        if (bind) al = AffinityLock.acquireLock();
        double [] results = new double[numThreads];
        Thread [] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; ++i){
            threads[i] = new Thread(new BusySqrt(results, i, bind), "BusySqrtThread-" + i);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        DoubleSummaryStatistics summary = Arrays.stream(results).collect(DoubleSummaryStatistics::new, DoubleSummaryStatistics::accept, DoubleSummaryStatistics::combine);
        System.out.println(summary);
//        if (bind && al != null) al.release();
        Files.delete(Paths.get("stop"));
    }


}
