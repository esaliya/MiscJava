package org.saliya.threads.basic;

import net.openhft.affinity.AffinityLock;
import net.openhft.affinity.AffinitySupport;
import org.saliya.common.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

public class AffinityThreadsBasics {
    public static void main(String[] args) throws IOException, InterruptedException {
        int numThreads = 5;
        boolean bind = true;

        AffinitySupport.setThreadId();
        System.out.println("PID:" + Utils.getPid() + " thread:" + Thread.currentThread().getName() + " native-id:" + Thread.currentThread().getId());

        AffinityLock al = AffinityLock.acquireLock();

        double [] results = new double[numThreads];
        Thread [] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; ++i){
            threads[i] = new Thread(new BusySqrt(results, i, bind), "BusySqrtThread-" + i);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        DoubleSummaryStatistics summary = Arrays.stream(results).collect(DoubleSummaryStatistics::new,
                DoubleSummaryStatistics::accept, DoubleSummaryStatistics::combine);
        System.out.println(summary);
        Files.delete(Paths.get("stop"));
        al.release();
    }
}
