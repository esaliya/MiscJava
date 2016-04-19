package org.saliya.memorymapped;

import edu.indiana.soic.spidal.common.*;
import edu.indiana.soic.spidal.common.Range;

import java.nio.ByteOrder;

public class DistanceReaderWithThreads {
    public static void main(String[] args) {
        String path = "/scratch/sekanaya/sali/projects/salsabio/nucleotide/data2/200k/input/whiten_dataonly_fullData.2320738e24c8993d6d723d2fd05ca2393bb25fa4.4mer.dist.c#_200k.bin";
        int numberOfDataPoints = 200000;
        int repetitions = 1;
        int numTasks = 24*24;

        int numRowsPerTask = numberOfDataPoints/numTasks;
        int elementCount = numRowsPerTask*numberOfDataPoints;
        short[] distances = new short[elementCount];

        BinaryReader1D.readRowRange(path, new Range(0,numRowsPerTask-1), numberOfDataPoints, ByteOrder.BIG_ENDIAN, true, null, 1, distances);
        System.out.println("Came here and distances[10][200]=" + distances[10*numberOfDataPoints+200]);
    }
}
