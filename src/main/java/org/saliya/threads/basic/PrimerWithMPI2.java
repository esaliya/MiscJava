package org.saliya.threads.basic;

import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;
import net.openhft.affinity.AffinitySupport;
import org.saliya.common.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static edu.rice.hj.Module0.*;
import static edu.rice.hj.Module1.forallChunked;

public class PrimerWithMPI2 {
    public static void main(String[] args) throws MPIException, IOException, InterruptedException {
        args = MPI.Init(args);
        Intracomm comm = MPI.COMM_WORLD;
        int rank = comm.getRank();
        int size = comm.getSize();

        int numThreads = Integer.parseInt(args[0]);
        boolean bind = Boolean.parseBoolean(args[1]);
        boolean useHj = Boolean.parseBoolean(args[2]);

        int cusPerNode = Runtime.getRuntime().availableProcessors(); // computing units per node
        int pid = Integer.parseInt(Utils.getPid());

        String [] output = new String[numThreads+1];
        double [] results = new double[numThreads];

        String threadAffinityMask = getAffinityForMainThread(rank, cusPerNode, pid, output);

        if (useHj) {
            if (rank == 0) System.out.println("***Habanero Java***");
            computeWithHJThreads(comm, rank, size, numThreads, bind, cusPerNode, threadAffinityMask, results,
                    output);
        } else {
            if (rank == 0) System.out.println("***Affinity Threads***");
            Thread [] threads = new Thread[numThreads];
            computeWithAffinityThreads(comm, rank, size, numThreads, bind, cusPerNode, threadAffinityMask, results, threads, output);
        }

        if (rank == 0) {
            System.out.println(Arrays.stream(results).reduce(0.0, (sum, v)->sum+v));
            if (Files.exists(Paths.get("stop"))){
                Files.delete(Paths.get("stop"));
            }
        }
        MPI.Finalize();
    }

    private static void computeWithHJThreads(Intracomm comm, int rank, int size, int numThreads, boolean bind,
                                             int cusPerNode, String parentThreadAffinityMask, double[] results,
                                             String[] output) {

        CountDownLatch latch = new CountDownLatch(numThreads);
        CountDownLatch go = new CountDownLatch(1);
        launchHabaneroApp(() -> {
            forallChunked(0, numThreads - 1, (threadIndex) ->
            {
                int bindTo = -1;
                int count = 0;
                for (int i = parentThreadAffinityMask.length() - 1; i >= 0; --i) {
                    if (parentThreadAffinityMask.charAt(i) == '1') {
                        if (count == threadIndex) {
                            bindTo = ((parentThreadAffinityMask.length() - 1) - i);
                            break;
                        } else {
                            ++count;
                        }
                    }
                }
                int threadId = AffinitySupport.getThreadId();
                try {
                    String threadAffinityMask = getPaddedString(cusPerNode,
                            Long.toBinaryString(Utils.getProcAffinityMask(threadId)));
                    if (bind) {
//                        AffinitySupport.setAffinity(1L << bindTo);
                    }
                   /* output[threadIndex + 1] =
                            "  Thread : " + Thread.currentThread().getName() + " id: " + threadId + " originally bound to: " + getHumanReadableString(
                                    threadAffinityMask) + (bind ? (" changed to: " + getHumanReadableString(
                                    getPaddedString(
                                            cusPerNode, Long.toBinaryString(AffinitySupport.getAffinity())))) : "");*/
                    latch.countDown();
                    latch.await();
                    if (threadIndex == 0) {
                        printOutputInOrder(output, comm, rank, size);
                        go.countDown();
                    }
                    go.await();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                compute(results, threadIndex);
            });});

    }

    private static String getAffinityForMainThread(int rank, int cusPerNode, int pid,
                                                   String[] output) throws IOException {
        String procAffinityMask = Long.toBinaryString(Utils.getProcAffinityMask(pid));
        procAffinityMask = getPaddedString(cusPerNode, procAffinityMask);

        assert cusPerNode >= procAffinityMask.length();

        int threadId = AffinitySupport.getThreadId();
        String threadAffinityMask = getPaddedString(cusPerNode, Long.toBinaryString(Utils.getProcAffinityMask(threadId)));
        output[0] = "Rank: " + rank + " pid: " + pid + " bound to: " + procAffinityMask + "\n  Thread: " + Thread.currentThread().getName() + " tid: " + threadId + " bound to: " + getHumanReadableString(threadAffinityMask);
        return threadAffinityMask;
    }

    private static void computeWithAffinityThreads(Intracomm comm, int rank, int size, int numThreads, boolean bind,
                                                   int cusPerNode,
                                                   String threadAffinityMask, double[] results,
                                                   Thread[] threads, String[] output) throws IOException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(numThreads);
        int prevCore = threadAffinityMask.length();
        for (int i = 0; i < numThreads; ++i){
            prevCore -= 1;
            for (int j = prevCore; j >= 0; --j){
                if (threadAffinityMask.charAt(j) == '1'){
                    prevCore = j;
                    break;
                }
            }
            threads[i] = new Thread(new BusySqrt(rank, results, i, cusPerNode, bind, ((threadAffinityMask.length() - 1) - prevCore), output, latch), "BusySqrtThread-" + i);
            threads[i].start();
        }

        latch.await();
        printOutputInOrder(output, comm, rank, size);

        for (Thread thread : threads) {
            thread.join();
        }
    }

    private static void printOutputInOrder(String[] output, Intracomm comm, int rank, int size) {
        String out = "";
        for (String anOutput : output) {
            out += anOutput + "\n";
        }

        try {
            out = allReduce(out, rank, size, comm);
            if (rank == 0) System.out.println(out);
        } catch (MPIException e) {
            e.printStackTrace();
        }
    }

    private static String allReduce(String value, int rank, int size, Intracomm comm) throws MPIException {
        int [] lengths = new int[size];
        int length = value.length();
        lengths[rank] = length;
        comm.allGather(lengths, 1, MPI.INT);
        int [] displas = new int[size];
        displas[0] = 0;
        System.arraycopy(lengths, 0, displas, 1, size - 1);
        Arrays.parallelPrefix(displas, (m, n) -> m + n);
        int count = IntStream.of(lengths).sum(); // performs very similar to usual for loop, so no harm done
        char [] recv = new char[count];
        System.arraycopy(value.toCharArray(), 0,recv, displas[rank], length);
        comm.allGatherv(recv, lengths, displas, MPI.CHAR);
        return  new String(recv);
    }

    private static String getPaddedString(int cusPerNode, String affinityMask) {
        if (affinityMask.length() < cusPerNode){
            int diff = cusPerNode - affinityMask.length();
            for (int i = 0; i < diff; ++i){
                affinityMask = "0" + affinityMask;
            }
        }
        return affinityMask;
    }

    private static String getHumanReadableString(String affinityMask) {
        String humanReadable = "";
        for (int i = 0; i < affinityMask.length(); i++) {
             if (affinityMask.charAt(i) == '1'){
                 humanReadable += ((affinityMask.length() - 1) - i) + " ";
             }

        }
        return humanReadable;
    }

    private static void compute(double [] results, int index) {
        int i = (int)(Integer.MAX_VALUE*0.9);
        double x=0.01;
        while (i < Integer.MAX_VALUE){
            x = Math.sqrt(Math.random()+x);
            ++i;
        }
        results[index] = x;
    }

    private static class BusySqrt implements Runnable{
        private final int rank;
        private final double[] results;
        private final int index;
        private final boolean bind;
        private final int bindTo;
        private final int cusPerNode;
        private final String[] output;
        private final CountDownLatch latch;

        private BusySqrt(int rank, double[] results, int index, int cusPerNode, boolean bind, int bindTo,
                         String[] output, CountDownLatch latch) throws IOException {
            this.rank = rank;
            this.results = results;
            this.index = index;
            this.bind = bind;
            this.bindTo = bindTo;
            this.cusPerNode = cusPerNode;
            this.output = output;
            this.latch = latch;
        }

        @Override
        public void run() {
            int threadId = AffinitySupport.getThreadId();
            try {
                String threadAffinityMask = getPaddedString(cusPerNode,
                        Long.toBinaryString(Utils.getProcAffinityMask(threadId)));
                if (bind) {
//                    AffinitySupport.setAffinity(1L<<bindTo);
                }
                /*output[index + 1] =
                        "  Thread : " + Thread.currentThread().getName() + " id: " + threadId + " originally bound to: " + getHumanReadableString(
                                threadAffinityMask) + (bind ? (" changed to: " + getHumanReadableString(
                                getPaddedString(
                                        cusPerNode, Long.toBinaryString(AffinitySupport.getAffinity()))
                        )) : "");*/
                latch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            }
            compute();
        }

        private void compute() {
            int i = (int)(Integer.MAX_VALUE*0.9);
            double x=0.01;
            while (i < Integer.MAX_VALUE){
                x = Math.sqrt(Math.random()+x);
                ++i;
            }
            results[index] = x;
        }
    }
}
