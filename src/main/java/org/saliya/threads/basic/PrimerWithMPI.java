package org.saliya.threads.basic;

import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;
import net.openhft.affinity.AffinityLock;
import org.saliya.common.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

public class PrimerWithMPI {
    public static void main(String[] args) throws MPIException, InterruptedException, IOException {
        args = MPI.Init(args);
        Intracomm comm = MPI.COMM_WORLD;
        int rank = comm.getRank();
        int size = comm.getSize();

        int numThreads = Integer.parseInt(args[0]);
        boolean bind = Boolean.parseBoolean(args[1]);

        int cusPerNode = Runtime.getRuntime().availableProcessors(); // computing units per node
        int pid = Integer.parseInt(Utils.getPid());
        String affinityMask = Long.toBinaryString(Utils.getProcAffinityMask(pid));

        assert cusPerNode >= affinityMask.length();

        System.out.println("Rank:" + rank + " pid:" + pid + " bound to: " + affinityMask);

        double [] results = new double[numThreads];
        Thread [] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; ++i){
            threads[i] = new Thread(new BusySqrt(results, i, affinityMask, bind), "BusySqrtThread-" + i);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        DoubleSummaryStatistics summary = Arrays.stream(results).collect(DoubleSummaryStatistics::new, DoubleSummaryStatistics::accept, DoubleSummaryStatistics::combine);
        System.out.println(summary);
        comm.barrier();
        if (rank == 0) {
            System.out.println(Arrays.stream(results).reduce(0.0, (sum, v)->sum+v));
            try {
                Files.delete(Paths.get("stop"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*AffinityLock al = AffinityLock.acquireLock();
        try {
            new Thread(new SleepRunnable(), "reader").start();
            new Thread(new SleepRunnable(), "writer").start();
            Thread.sleep(200);
        } finally {
            al.release();
        }
        new Thread(new SleepRunnable(), "engine").start();

        Thread.sleep(200);
        System.out.println("\nThe assignment of CPUs is\n" + AffinityLock.dumpLocks());*/


        MPI.Finalize();
    }

    private static class BusySqrt implements Runnable{
        private final double[] results;
        private final int index;
        private final boolean bind;
        private final AsProcAffinity affinityStrategy;

        private BusySqrt(double [] results, int index, String affinityMask, boolean bind) throws IOException {
            this.results = results;
            this.index = index;
            this.affinityStrategy = new AsProcAffinity(affinityMask);
            this.bind = bind;
        }

        @Override
        public void run() {
            AffinityLock al = null;
            if (bind) al = AffinityLock.acquireLock();
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

    private static class SleepRunnable implements Runnable {
        public void run() {
            AffinityLock al = AffinityLock.acquireLock();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            } finally {
                al.release();
            }
        }
    }

}
