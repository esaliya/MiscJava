package org.saliya.threads.basic;

import net.openhft.affinity.AffinityLock;
import net.openhft.affinity.AffinitySupport;

import java.nio.file.Files;
import java.nio.file.Paths;

public class BusySqrt implements Runnable{
    private final double[] results;
    private final int threadIdx;
    private final boolean bind;

    public BusySqrt(double [] results, int threadIdx, boolean bind) {
        this.results = results;
        this.threadIdx = threadIdx;
        this.bind = bind;
    }

    @Override
    public void run() {
        AffinityLock al = null;
        if (bind) {
            AffinitySupport.setThreadId();
            System.out.println(
                    "Thread:" + Thread.currentThread().getName() + " native-id:" + Thread.currentThread().getId());
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
        results[threadIdx] = x;
    }
}
