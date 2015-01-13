package org.saliya.threads.basic;

import net.openhft.affinity.AffinityLock;
import net.openhft.affinity.AffinitySupport;
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
            threads[i] = new Thread(new BusySqrt(results, i, numCores, bind), "BusySqrtThread-" + i);
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

    private static class BusySqrt implements Runnable{
        private final double[] results;
        private final int index;
        private final int numCores;
        private final boolean bind;

        private BusySqrt(double [] results, int index, int numCores, boolean bind) {
            this.results = results;
            this.index = index;
            this.numCores = numCores;
            this.bind = bind;
        }

        @Override
        public void run() {
            AffinityLock al = null;
            if (bind) {
                AffinitySupport.setThreadId();
                System.out.println(
                        "Thread:" + Thread.currentThread().getName() + " native-id:" + Thread.currentThread().getId());
                /* Couldn't find documentation on how to get setAffinity to work, so ignoring that for now*/
                /*AffinitySupport.setAffinity(index%2==0 ? (index/2)+1 : (index/2)+(numCores+1));*/
                al = AffinityLock.acquireLock();
            }
            compute();
            if (bind && al != null) al.release();
        }

        private void compute() {
            double x = Math.random()*1e10;
            while (!Files.exists(Paths.get("stop"))){
                for (int i = 0; i < 1000; ++i){
                    x = Math.sqrt(x);
                }
                x = Math.random()*1e10;
            }
            results[index] = x;
        }
    }
}
