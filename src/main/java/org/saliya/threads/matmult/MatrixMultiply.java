package org.saliya.threads.matmult;

import com.google.common.base.Stopwatch;
import com.google.common.primitives.Ints;
import edu.rice.hj.api.SuspendableException;
import edu.rice.hj.runtime.config.HjConfiguration;
import edu.rice.hj.runtime.config.HjSystemProperty;
import org.saliya.threads.parallel.Parallel;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import static edu.rice.hj.Module0.finalizeHabanero;
import static edu.rice.hj.Module0.initializeHabanero;
import static edu.rice.hj.Module1.forallChunked;

public class MatrixMultiply {
    static ExecutorService execSvc;

    static void MultiplyMatricesSequential(double[][] matA, double[][] matB, double[][] result) {
        int matACols = matA[0].length;
        int matBCols = matB[0].length;
        int matARows = matA.length;

        for (int i = 0; i < matARows; i++) {
            for (int j = 0; j < matBCols; j++) {
                for (int k = 0; k < matACols; k++) {
                    result[i][j] += matA[i][k] * matB[k][j];
                }
            }
        }
    }

    static void MultiplyMatricesHJ(double[][] matA, double[][] matB, double[][] result) throws SuspendableException {
        int matACols = matA[0].length;
        int matBCols = matB[0].length;
        int matARows = matA.length;

        // A basic matrix multiplication.
        // Parallelize the outer loop to partition the source array by rows.
        forallChunked(0, matARows - 1, (i) ->
        {
            for (int j = 0; j < matBCols; j++) {
                // Use a temporary to improve parallel performance.
                double temp = 0;
                for (int k = 0; k < matACols; k++) {
                    temp += matA[i][k] * matB[k][j];
                }
                result[i][j] = temp;
            }
        }); 
    }

    static void MultiplyMatricesParallel(double[][] matA, double[][] matB, double[][] result) throws SuspendableException {
        int matACols = matA[0].length;
        int matBCols = matB[0].length;
        int matARows = matA.length;

        // A basic matrix multiplication.
        // Parallelize the outer loop to partition the source array by rows.
        try {
            Parallel.For(0, matARows, execSvc, (i) ->
            {
                for (int j = 0; j < matBCols; j++) {
                    // Use a temporary to improve parallel performance.
                    double temp = 0;
                    for (int k = 0; k < matACols; k++) {
                        temp += matA[i][k] * matB[k][j];
                    }
                    result[i][j] = temp;
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static long Multiply(int colCount, int rowCount, int colCount2, ExecutionType executionType) throws SuspendableException {
        // Set up matrices. Use small values to better view  
        // result matrix. Increase the counts to see greater  
        // speedup in the parallel loop vs. the sequential loop. 

        double[][] m1 = InitializeMatrix(rowCount, colCount);
        double[][] m2 = InitializeMatrix(colCount, colCount2);
        double[][] result = new double[rowCount][colCount2];

        long timeInMilliseconds = 0;
        Stopwatch stopwatch = Stopwatch.createUnstarted();
        switch (executionType){
            default:
            case sequential:
                stopwatch.start();
                MultiplyMatricesSequential(m1, m2, result);
                stopwatch.stop();
                timeInMilliseconds = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                stopwatch.reset();
                break;
            case habanero:
                stopwatch.start();
                MultiplyMatricesHJ(m1, m2, result);
                stopwatch.stop();
                timeInMilliseconds = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                stopwatch.reset();
                break;
            case parallel:
                stopwatch.start();
                MultiplyMatricesParallel(m1, m2, result);
                stopwatch.stop();
                timeInMilliseconds = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                stopwatch.reset();
                break;
        }

       return timeInMilliseconds;
    }



    static double[][] InitializeMatrix(int rows, int cols)
    {
        double[][] matrix = new double[rows][cols];

        Random r = new Random();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                matrix[i][j] = r.nextDouble() * 100;
            }
        }
        return matrix;
    }

    public static void main(String[] args) throws SuspendableException {
        Integer colCount = Ints.tryParse(args[0]); // default used to be 180
        colCount = colCount == null ? 180 : colCount;
        Integer rowCount = Ints.tryParse(args[1]); // default used to be 2000
        rowCount = rowCount == null ? 2000 : rowCount;
        Integer colCount2 = Ints.tryParse(args[2]); // default used to be 270
        colCount2 = colCount2 == null ? 270 : colCount2;
        Integer iterations = Ints.tryParse(args[3]); // default used to be 100
        iterations = iterations == null ? 1000 : iterations;
        Integer skip = Ints.tryParse(args[4]);
        skip = skip == null ? 100 : skip;
        Integer poolSize = Ints.tryParse(args[5]);
        poolSize = poolSize == null ? 8 : poolSize;

        double timeInMilliseconds = 0.0;
        long t = 0;

        /* Sequential timing *//*
        for (int i = 0; i < iterations; i++) {
            t = Multiply(colCount, rowCount, colCount2, ExecutionType.sequential);
            if (i < skip) continue;
            timeInMilliseconds += t;
        }
        timeInMilliseconds /= (iterations - skip);
        System.out.println("Average time for " + ExecutionType.sequential + " is " + timeInMilliseconds + "ms");
        timeInMilliseconds = 0.0;*/

        /* Habanero timing */
        HjSystemProperty.numWorkers.set(16);
        initializeHabanero();
        HjConfiguration.printConfiguredOptions();

        System.out.println(HjConfiguration.runtime().numWorkerThreads());
        for (int i = 0; i < iterations; i++) {
            t = Multiply(colCount, rowCount, colCount2, ExecutionType.habanero);
            if (i < skip) continue;
            timeInMilliseconds += t;
        }
        timeInMilliseconds /= (iterations - skip);
        System.out.println("Average time for " + ExecutionType.habanero + " is " + timeInMilliseconds + "ms");
        timeInMilliseconds = 0.0;
        finalizeHabanero();

        /* Parallel timing */
        execSvc = new ForkJoinPool(poolSize);
        for (int i = 0; i < iterations; i++) {
            t = Multiply(colCount, rowCount, colCount2, ExecutionType.parallel);
            if (i < skip) continue;
            timeInMilliseconds += t;
        }
        timeInMilliseconds /= (iterations - skip);
        System.out.println("Average time for " + ExecutionType.parallel + " is " + timeInMilliseconds + "ms");
        execSvc.shutdown();
    }

    private enum ExecutionType{
        sequential, habanero, parallel
    }


}
