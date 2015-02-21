package org.saliya.binary;

import com.google.common.base.Stopwatch;

import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

public class DistanceReader {
    public static void main(String[] args) {
        String file = "/home/saliya/Downloads/w1_0";// 298 x 200078 short distance file
        int rows = 298;
        int cols = 200078;
        Range rowRange = new Range(0,rows -1);

        Stopwatch timer = Stopwatch.createStarted();
        Matrix memoryMapped = Matrix.readRowRange(file, rowRange, cols, 2, ByteOrder.BIG_ENDIAN, true);
        timer.stop();
        System.out.println(timer.elapsed(TimeUnit.MILLISECONDS));

        timer.reset();
        timer.start();
        double [][] array = new double[rows][cols];
        for (int i = 0; i < rows; ++i){
            for (int j = 0; j < cols; ++j){
                array[i][j] = memoryMapped.getDistance(i,j);
            }
        }
        timer.stop();
        System.out.println(timer.elapsed(TimeUnit.MILLISECONDS));
    }
}
