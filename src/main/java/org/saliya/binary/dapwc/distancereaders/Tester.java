package org.saliya.binary.dapwc.distancereaders;

import com.google.common.base.Stopwatch;
import org.saliya.binary.Range;

import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

public class Tester {
    public static void main(String[] args) {
        String file = "/home/saliya/sali/csharpToJava/pwc/input/region7_10440_pidC#_3.bin";
        int n = 5703;

        Stopwatch mappedTimer = Stopwatch.createStarted();
        MatrixWithMappedBytes matrixWithMappedBytes = MatrixWithMappedBytes.readRowRange(file, new Range(0, n-1), n, 2,
                ByteOrder.LITTLE_ENDIAN, true);
        mappedTimer.stop();
        System.out.println(mappedTimer.elapsed(TimeUnit.MICROSECONDS) + " ms to load using memory mapped");

        Stopwatch arrayTimer = Stopwatch.createStarted();
        short [][] matrixWithShortArrays = MatrixWithShortArrays.readRowRange(file, 0, n, n, 2, ByteOrder.LITTLE_ENDIAN, true);
        arrayTimer.stop();
        System.out.println(arrayTimer.elapsed(TimeUnit.MICROSECONDS) + " ms to load using memory mapped and array");


        long times = (long) 1e6;
        int itrs = 100;

        long count;
        double tmp;

        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long avg = 0;
        for (int i = 0; i < itrs; ++i) {
            tmp = 0.0;
            count = 0;
            mappedTimer.reset();
            mappedTimer.start();
            while (count < times) {
                int row = (int) (Math.random() * n);
                int col = (int) (Math.random() * n);

                tmp += tmp * 0.001 + matrixWithMappedBytes.getDistance(row, col);
                ++count;
            }
            mappedTimer.stop();
            long t = mappedTimer.elapsed(TimeUnit.MILLISECONDS);
            if (t < min) min = t;
            if (t > max) max = t;
            avg += t;
        }
        System.out.println("[min:" + min + " max:" + max + " avg:" + ((double)avg)/itrs + "] ms to access " + times + " distances using memory mapped");

        min = Long.MAX_VALUE;
        max = Long.MIN_VALUE;
        avg = 0;
        for (int i = 0; i < itrs; ++i) {
            tmp = 0.0;
            count = 0;
            arrayTimer.reset();
            arrayTimer.start();
            while (count < times) {
                int row = (int) (Math.random() * n);
                int col = (int) (Math.random() * n);

                tmp += tmp * 0.001 + matrixWithShortArrays[row][col];
                ++count;
            }
            arrayTimer.stop();
            long t = arrayTimer.elapsed(TimeUnit.MILLISECONDS);
            if (t < min) min = t;
            if (t > max) max = t;
            avg += t;
        }
        System.out.println("[min:" + min + " max:" + max + " avg:" + ((double)avg)/itrs + "] ms to access " + times + " distances using memory mapped and short arrays");


    }
}
