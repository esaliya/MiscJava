package org.saliya.memorymapped;

import edu.indiana.soic.spidal.common.*;
import edu.indiana.soic.spidal.common.BinaryReader1D;
import edu.indiana.soic.spidal.common.Range;

import java.nio.ByteOrder;
import java.util.stream.IntStream;

import static edu.rice.hj.Module0.launchHabaneroApp;
import static edu.rice.hj.Module1.forallChunked;

public class DistanceReaderWithThreads {
    public static void main(String[] args) {
        String path = "/scratch/sekanaya/sali/projects/salsabio/nucleotide/data2/200k/input/whiten_dataonly_fullData.2320738e24c8993d6d723d2fd05ca2393bb25fa4.4mer.dist.c#_200k.bin";
        int globalRowCount = 200000;
        int repetitions = 1;
        int numTasks = 24*24;
        int threadCount = Integer.parseInt(args[0]);
        
        /* THIS WORKS!! */
        /*int numRowsPerTask = globalRowCount/numTasks;
        int elementCount = numRowsPerTask*globalRowCount;
        short[] distances = new short[elementCount];
        BinaryReader1D.readRowRange(path, new Range(0,numRowsPerTask-1), globalRowCount, ByteOrder.BIG_ENDIAN, true, null, repetitions, distances);
        System.out.println("Came here and distances[10][200]=" + distances[10*globalRowCount+200]);*/


        int worldProcsCount = numTasks/threadCount;
        int worldProcRank = worldProcsCount-1;
        Range[] procRowRanges = RangePartitioner.partition(globalRowCount,
                worldProcsCount);
        Range rowRange = procRowRanges[worldProcRank]; // The range of points for this process

        Range procRowRange = rowRange;
        int procRowStartOffset = rowRange.getStartIndex();
        int procRowCount = rowRange.getLength();
        int globalColCount = globalRowCount;

        // Next partition points per process among threads
        Range[] threadRowRanges = RangePartitioner.partition(procRowCount, threadCount);
        int[] threadRowCounts = new int[threadCount];
        int[] threadRowStartOffsets = new int[threadCount];
        IntStream.range(0, threadCount)
                .parallel()
                .forEach(threadIdx -> {
                    Range threadRowRange = threadRowRanges[threadIdx];
                    threadRowCounts[threadIdx] =
                            threadRowRange.getLength();
                    threadRowStartOffsets[threadIdx] =
                            threadRowRange.getStartIndex();
                });

        launchHabaneroApp(
                () -> forallChunked(
                        0, threadCount - 1,
                        (threadId) -> {
                            System.out.println("Really: tid="  + threadId);
                            try {
                                final int threadRowCount = threadRowCounts[threadId];
                                final int threadLocalRowStartOffset =
                                        threadRowStartOffsets[threadId];
                                final int globalThreadRowStartOffset = procRowStartOffset
                                        + threadLocalRowStartOffset;
                                Range globalThreadRowRange = new Range(
                                        globalThreadRowStartOffset, globalThreadRowStartOffset +
                                        threadRowCount - 1);
                                Range threadLocalRowRange = new Range(
                                        threadLocalRowStartOffset, (
                                        threadLocalRowStartOffset + threadRowCount - 1));

                                int elementCount = globalThreadRowRange.getLength() * globalColCount;
                                short[] distances = new short[elementCount];
                                if (repetitions == 1) {
                                    System.out.println("Thread " + threadId + " came before here");
    //                                edu.indiana.soic.spidal.common.BinaryReader1D.readRowRange(path,
    //                                        globalThreadRowRange, globalColCount, ByteOrder.BIG_ENDIAN,
    //                                        true, null, distances);
                                    System.out.println("Thread " + threadId + " came here and distances[" + globalThreadRowRange.getStartIndex() + "][203]=" + distances[203]);
                                } else {
                                    System.out.println("**Thread " + threadId + " came before here");
    //                                BinaryReader1D.readRowRange(null,
    //                                        globalThreadRowRange, globalColCount, ByteOrder.BIG_ENDIAN,
    //                                        true, null, repetitions, distances);
                                    System.out.println("**Thread " + threadId + " came here and distances[" + globalThreadRowRange.getStartIndex() + "][203]=" + distances[203]);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }));
        System.out.println("Came to the end");
    }
}
