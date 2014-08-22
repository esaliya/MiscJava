package org.saliya.ompi.comm;

import com.google.common.base.Stopwatch;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BroadcastArrayVsBuffer {
    public static void main(String[] args) throws MPIException {

        args = MPI.Init(args);

        Intracomm comm = MPI.COMM_WORLD;
        int size = comm.getSize();
        int rank = comm.getRank();

        int pointDimensions = Integer.parseInt(args[0]);
        int numPoints = Integer.parseInt(args[1]);

        int [] pointCountsForProcesses = getPointCountsForProcesses(numPoints, size);
        int pointCountForProcess = pointCountsForProcesses[rank];
        int [] pointStartIdxsForProcesses = getPointStartIdxsForProcesses(numPoints, size);
        double [][] localPoints = new double[pointCountForProcess][];
        initializePoints(rank, pointDimensions, localPoints);

        double [][] globalPoints = new double[numPoints][pointDimensions];

        // No direct way to send a 2D array with MPI
        gatherLocalPointsUsingArrayAndBcast(comm, size, rank, pointDimensions, pointCountsForProcesses, pointStartIdxsForProcesses, localPoints, globalPoints);
        gatherLocalPointsUsingArrayAndGather(comm,size, rank, numPoints, pointDimensions, pointCountsForProcesses, pointStartIdxsForProcesses, localPoints, globalPoints);
//        printPoints(rank, globalPoints);
        MPI.Finalize();
    }

    private static void gatherLocalPointsUsingArrayAndBcast(Intracomm comm, int size, int rank, int pointDimensions, int[] pointCountsForProcesses, int[] pointStartIdxsForProcesses, double[][] localPoints, double[][] globalPoints) throws MPIException {
        double mainDuration = 0.0, flattenDuration = 0.0, explodeDuration = 0.0, bcastDuration = 0.0;
        Stopwatch flattenTimer = Stopwatch.createUnstarted();
        Stopwatch explodeTimer = Stopwatch.createUnstarted();
        Stopwatch bcastTimer = Stopwatch.createUnstarted();
        Stopwatch mainTimer = Stopwatch.createStarted();
        for (int commSteps = 0; commSteps < size; ++commSteps){
            double [] flattenedLocalPoints = new double[pointCountsForProcesses[commSteps]*pointDimensions];
            if (rank == commSteps){
                flattenTimer.start();
                flattenPoints2DTo1D(localPoints, flattenedLocalPoints, pointDimensions);
                flattenTimer.stop();
                flattenDuration += flattenTimer.elapsed(TimeUnit.MILLISECONDS);
                flattenTimer.reset();
            }
            bcastTimer.start();
            comm.bcast(flattenedLocalPoints, flattenedLocalPoints.length, MPI.DOUBLE, commSteps);
            bcastTimer.stop();
            bcastDuration += bcastTimer.elapsed(TimeUnit.MILLISECONDS);
            bcastTimer.reset();

            explodeTimer.start();
            explode1DPointsTo2D(flattenedLocalPoints, globalPoints, pointDimensions, pointStartIdxsForProcesses[commSteps]);
            explodeTimer.stop();
            explodeDuration += explodeTimer.elapsed(TimeUnit.MILLISECONDS);
            explodeTimer.reset();
        }
        mainTimer.stop();
        mainDuration += mainTimer.elapsed(TimeUnit.MILLISECONDS);

        double [] buffer = new double[]{mainDuration};
        comm.allReduce(buffer, 1, MPI.DOUBLE, MPI.SUM);
        mainDuration = buffer[0] / size;

        buffer[0] = flattenDuration;
        comm.allReduce(buffer, 1, MPI.DOUBLE, MPI.SUM);
        flattenDuration = buffer[0] / size;

        buffer[0] = explodeDuration;
        comm.allReduce(buffer, 1, MPI.DOUBLE, MPI.SUM);
        explodeDuration = buffer[0] / size;

        buffer[0] = bcastDuration;
        comm.allReduce(buffer, 1, MPI.DOUBLE, MPI.SUM);
        bcastDuration = buffer[0] / size;

        if (rank == 0){
            System.out.println("Average timing for array bcast main " + mainDuration + " flatten " + flattenDuration + " explode " + explodeDuration + " bcast " + bcastDuration);
        }
    }

    private static void gatherLocalPointsUsingArrayAndGather(Intracomm comm, int size, int rank, int numPoints, int pointDimensions, int[] pointCountsForProcesses, int[] pointStartIdxsForProcesses, double[][] localPoints, double[][] globalPoints) throws MPIException {
        double mainDuration = 0.0, flattenDuration = 0.0, explodeDuration = 0.0, commDuration = 0.0;
        Stopwatch flattenTimer = Stopwatch.createUnstarted();
        Stopwatch explodeTimer = Stopwatch.createUnstarted();
        Stopwatch commTimer = Stopwatch.createUnstarted();
        Stopwatch mainTimer = Stopwatch.createStarted();


        double [] flattenedGlobalPoints = new double[numPoints*pointDimensions];
        double [] flattenedLocalPoints = new double[pointCountsForProcesses[rank]*pointDimensions];
        flattenTimer.start();
        flattenPoints2DTo1D(localPoints, flattenedLocalPoints, pointDimensions);
        flattenTimer.stop();
        flattenDuration += flattenTimer.elapsed(TimeUnit.MILLISECONDS);
        flattenTimer.reset();

        int [] lengths = new int[size];
        int length = flattenedLocalPoints.length;
        lengths[rank] = length;
        comm.allGather(lengths, 1, MPI.INT);
        int [] displas = new int[size];
        displas[0] = 0;
        System.arraycopy(lengths, 0, displas, 1, size - 1);
        Arrays.parallelPrefix(displas, (m, n) -> m + n);

        commTimer.start();
        comm.allGatherv(flattenedLocalPoints, flattenedLocalPoints.length, MPI.DOUBLE, flattenedGlobalPoints, lengths, displas, MPI.DOUBLE);
        commTimer.stop();
        commDuration += commTimer.elapsed(TimeUnit.MILLISECONDS);
        commTimer.reset();

        explodeTimer.start();
        explode1DPointsTo2D(flattenedGlobalPoints, globalPoints, pointDimensions, 0);
        explodeTimer.stop();
        explodeDuration += explodeTimer.elapsed(TimeUnit.MILLISECONDS);
        explodeTimer.reset();

        mainTimer.stop();
        mainDuration += mainTimer.elapsed(TimeUnit.MILLISECONDS);

        double [] buffer = new double[]{mainDuration};
        comm.allReduce(buffer, 1, MPI.DOUBLE, MPI.SUM);
        mainDuration = buffer[0] / size;

        buffer[0] = flattenDuration;
        comm.allReduce(buffer, 1, MPI.DOUBLE, MPI.SUM);
        flattenDuration = buffer[0] / size;

        buffer[0] = explodeDuration;
        comm.allReduce(buffer, 1, MPI.DOUBLE, MPI.SUM);
        explodeDuration = buffer[0] / size;

        buffer[0] = commDuration;
        comm.allReduce(buffer, 1, MPI.DOUBLE, MPI.SUM);
        commDuration = buffer[0] / size;

        if (rank == 0){
            System.out.println("Average timing for array gather main " + mainDuration + " flatten " + flattenDuration + " explode " + explodeDuration + " comm " + commDuration);
        }
    }

    private static int[] getPointCountsForProcesses(int numPoints, int size) {
        int q = numPoints/size;
        int r = numPoints % size;
        int [] arr = new int[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i < r ? q+1 : q;
        }
        return  arr;
    }

    private static void explode1DPointsTo2D(double[] flattenedPoints, double[][] explodedPoints, int pointDimensions, int offset) {
        for (int i = 0; i < flattenedPoints.length; i+=pointDimensions) {
            System.arraycopy(flattenedPoints,i,explodedPoints[offset+ (i/pointDimensions)],0,pointDimensions);
        }
    }

    private static int[] getPointStartIdxsForProcesses(int numPoints, int size) {
        int q = numPoints/size;
        int r = numPoints % size;
        int [] arr = new int[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i < r ? q*i : q*i+r;
        }
        return arr;
    }

    private static void flattenPoints2DTo1D(double[][] points, double[] flattenedPoints, int pointDimensions) {
        for (int i = 0; i < points.length; i++) {
            System.arraycopy(points[i],0,flattenedPoints,i*pointDimensions,pointDimensions);
        }
    }

    private static void printPoints(int rank, double[][] localPoints) {
        StringBuilder builder = new StringBuilder("Rank: ");
        builder.append(rank).append("\n");
        for (double[] localPoint : localPoints) {
            builder.append(Arrays.toString(localPoint)).append("\n");
        }

        System.out.println(builder.toString());
    }

    private static void initializePoints(int rank, int pointDimensions, double[][] points) {
        for (int i = 0; i < points.length; i++) {
            points[i] = new double[pointDimensions];
            points[i][0] = rank;
            for (int j = 1; j < points[i].length; j++) {
                points[i][j] = i;
            }
        }
    }
}
