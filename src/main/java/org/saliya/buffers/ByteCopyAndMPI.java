package org.saliya.buffers;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteCopyAndMPI {
    public static void main(String[] args) {
        int vectorDimension = 3;
        int numVectors = 10;
        double [][] pointsArray = new double[numVectors][];
        for (int i = 0; i < pointsArray.length; i++) {
            pointsArray[i] = new double[vectorDimension];
            for (int j = 0; j < pointsArray[i].length; j++) {
                pointsArray[i][j] = Math.random();
            }
        }

        for (double[] aPointsArray : pointsArray) {
            System.out.println(Arrays.toString(aPointsArray));
        }

        ByteBuffer buffer = ByteBuffer.allocateDirect(vectorDimension*numVectors*Double.BYTES);
        for (int i = 0; i < pointsArray.length; i++) {
            buffer.put(pointsArray[i];

        }
    }
}
