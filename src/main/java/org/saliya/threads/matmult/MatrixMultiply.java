package org.saliya.threads.matmult;

import com.google.common.base.Stopwatch;
import edu.rice.hj.Module0;
import edu.rice.hj.api.SuspendableException;
import org.saliya.threads.parallel.Parallel;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static edu.rice.hj.Module0.finalizeHabanero;
import static edu.rice.hj.Module0.initializeHabanero;

public class MatrixMultiply {
    static int poolSize;
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
        Module0.forallChunked(0, matARows-1, (i) ->
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

    public static void Multiply(int colCount, int rowCount, int colCount2, boolean hj) throws SuspendableException {
        // Set up matrices. Use small values to better view  
        // result matrix. Increase the counts to see greater  
        // speedup in the parallel loop vs. the sequential loop. 

        double[][] m1 = InitializeMatrix(rowCount, colCount);
        double[][] m2 = InitializeMatrix(colCount, colCount2);
        double[][] result = new double[rowCount][colCount2];

        // First do the sequential version.
        System.out.println("Executing sequential loop...");
        Stopwatch stopwatch = Stopwatch.createStarted();

        MultiplyMatricesSequential(m1, m2, result);
        stopwatch.stop();
        System.out.println("Sequential loop time in milliseconds: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        // Reset timer and results matrix.
        stopwatch.reset();
        result = new double[rowCount][colCount2];

        // Do the parallel loop.
        System.out.println("Executing parallel loop... with " + (hj? "hj" : "parallel"));
        stopwatch.start();
        if (hj) {
            MultiplyMatricesHJ(m1, m2, result);
        } else {
            MultiplyMatricesParallel(m1,m2,result);
        }
        stopwatch.stop();
        System.out.println("Parallel loop time in milliseconds: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        // Keep the console window open in debug mode.
        System.out.println("Press any key to exit.");
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
        if (args[3].equals("hj")) {
            initializeHabanero();
            int colCount = Integer.parseInt(args[0]); // default used to be 180
            int rowCount = Integer.parseInt(args[1]); // default used to be 2000
            int colCount2 = Integer.parseInt(args[2]); // default used to be 270;
            Multiply(colCount, rowCount, colCount2, true);
            finalizeHabanero();
        } else {
            poolSize = Integer.parseInt(args[4]);
            execSvc = Executors.newFixedThreadPool(poolSize);
            int colCount = Integer.parseInt(args[0]); // default used to be 180
            int rowCount = Integer.parseInt(args[1]); // default used to be 2000
            int colCount2 = Integer.parseInt(args[2]); // default used to be 270;
            Multiply(colCount, rowCount, colCount2, false);
            execSvc.shutdown();
        }

    }

}
