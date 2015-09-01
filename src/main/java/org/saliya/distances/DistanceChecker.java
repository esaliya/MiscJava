package org.saliya.distances;

import edu.indiana.soic.spidal.common.BinaryReader2D;
import edu.indiana.soic.spidal.common.Range;

import java.io.*;
import java.nio.ByteOrder;

public class DistanceChecker {
    public static void main(String[] args) {
//        readByDistanceReader();
        readManually();
    }

    private static void readByDistanceReader() {
        int size = 6435;
        String file = "C:\\Users\\Saliya\\Downloads\\distances\\2004_1_2005_1"
                      + ".csv";
        short[][] distance = BinaryReader2D.readRowRange(file, new Range(0, size
                                                                            -
                                                                            1),
                                                         size,
                                                         ByteOrder.BIG_ENDIAN,
                                                         true, 1.0);
        double cut = 0.03;
        long count = 0;
        for (int i = 0; i < size; ++i){
            for (int j = 0; j < size; ++j){
                double d = distance[i][j] * 1.0 / Short.MAX_VALUE;
                if (d >= cut) continue;
                ++count;
            }
        }

        System.out.println(count);
    }

    public static void readManually(){
        int size = 6435;
        String file = "C:\\Users\\Saliya\\Downloads\\distances\\2004_1_2005_1"
                      + ".csv";
        try(DataInputStream dis = new DataInputStream(new FileInputStream(new File(file)))){
            double cut = 0.03;
            long count = 0;
            for (int i = 0; i < size; ++i){
                for (int j = 0; j < size; ++j){
                    double d = dis.readShort() * 1.0 / Short.MAX_VALUE;
                    if (d >= cut) continue;
                    ++count;
                }
            }

            System.out.println(count);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
