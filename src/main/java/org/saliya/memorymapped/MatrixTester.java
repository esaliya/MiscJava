package org.saliya.memorymapped;

import com.google.common.io.LittleEndianDataOutputStream;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MatrixTester {
    public static void main(String[] args) {
        // true = C# style, false = Java style
        boolean isBigEndian = false;
        int rows = 4;
        int cols = 4;
        String distFile = "distance_" + rows + "x" + cols + ".bin";

        boolean isMemoryMapped = true;
        Range rowRange = new Range(2, 3);

        try (OutputStream os = Files.newOutputStream(Paths.get(distFile),
                StandardOpenOption.CREATE)) {
            DataOutput out = isBigEndian ? new DataOutputStream(os) : new LittleEndianDataOutputStream(os);
            for (int i = 0; i < rows; ++i) {
                for (int j = 0; j < cols; ++j){
                    short v = (short) (Math.random() * Short.MAX_VALUE);
                    System.out.print(v*1.0/Short.MAX_VALUE + "\t");
                    out.writeShort(v);
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();


        DistanceReader mat = DistanceReader.readRowRange(distFile, rowRange, cols, 2,
                isBigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN, isMemoryMapped);
        for (int i = 0; i < rowRange.getLength(); ++i) {
            for (int j = 0; j < cols; ++j){
                System.out.print(mat.getDistance(rowRange.getStartIndex() + i, j) + "\t");
            }
            System.out.println();
        }


    }
}
