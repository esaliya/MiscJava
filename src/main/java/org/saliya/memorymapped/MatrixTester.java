package org.saliya.memorymapped;

import com.google.common.base.Stopwatch;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MatrixTester {
    public static void main(String[] args) {

        int rows = 4000;
        int cols = 4000;
//        checkConsistency(rows, cols);

        /*Conclusion
        *  -- Array and memory map perform pretty similar
        *  -- Reading to array is slower than memory map
        */
        checkTiming(rows,cols);

    }
    private static void checkTiming(int rows, int cols) {
        String prefix = "distance_" + rows + "x" + cols + "_";
        String suffix = ".bin";

        // BigEndian = Java style, LittleEndian = C# style
        ByteOrder [] byteOrders = new ByteOrder[] {ByteOrder.LITTLE_ENDIAN, ByteOrder.BIG_ENDIAN};
        Boolean [] mappings = new Boolean[]{true}; // true indicates memory mapped


        Stream.of(byteOrders).forEach(bo -> {
            Stream.of(mappings).forEach(m -> {
                String signature = (bo == ByteOrder.BIG_ENDIAN ? "bigendian_" : "littleendian_") + (m ? "mmap" : "array");
                String distFile = prefix + signature + suffix;
//                generateDistanceFile(bo, rows, cols, distFile);
                DistanceReader reader = DistanceReader.readRowRange(distFile, new Range(0,rows-1),cols, Short.BYTES, bo, m);
                double [] sum = new double[]{0.0};
                Stopwatch timer = Stopwatch.createStarted();
                timer = Stopwatch.createStarted();
                for (int i = 0; i < rows; ++i){
                    for (int j = 0; j < cols; ++j){
                        sum[0] += reader.getDistance(i, j);
                    }
                }
                timer.stop();
                System.out.println(sum[0]  + " " + signature + " took " + timer.elapsed(TimeUnit.MILLISECONDS) + " ms");
            });
        });

        // Testing with usual for loops
     /*   for (ByteOrder bo : byteOrders) {
            for (Boolean m : mappings) {
                String signature = (bo == ByteOrder.BIG_ENDIAN ? "bigendian_" : "littleendian_") + (m ? "mmap" : "array");
                String distFile = prefix + signature + suffix;
//                generateDistanceFile(bo, rows, cols, distFile);
                DistanceReader reader = DistanceReader.readRowRange(distFile, new Range(0, rows - 1), cols, Short.BYTES,
                        bo, m);
                double[] sum = new double[]{0.0};
                Stopwatch timer = Stopwatch.createStarted();
                for (int i = 0; i < rows; ++i) {
                    for (int j = 0; j < cols; ++j) {
                        sum[0] += reader.getDistance(i, j);
                    }
                }
                timer.stop();
                System.out.println(sum[0] + " " + signature + " took " + timer.elapsed(TimeUnit.MILLISECONDS) + " ms");
            }
        }*/
    }

    private static void checkConsistency(int rows, int cols) {
        String prefix = "distance_" + rows + "x" + cols + "_";
        String suffix = ".bin";

        // BigEndian = Java style, LittleEndian = C# style
        ByteOrder [] byteOrders = new ByteOrder[] {ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN};
        Boolean [] mappings = new Boolean[]{true, false}; // true indicates memory mapped

        Stream.of(byteOrders).parallel().forEach(bo -> {
            Stream.of(mappings).parallel().forEach(m -> {
                String distFile = prefix + (bo == ByteOrder.BIG_ENDIAN ? "bigendian_" : "littleendian_") + (m ? "mmap" : "array") + suffix;
                short[][] array = new short[rows][cols];
                generateDistanceFile(bo, rows, cols, distFile,array);

                // Uncomment for verbose checks
                /*System.out.println();*/
                DistanceReader reader = DistanceReader.readRowRange(distFile, new Range(0,rows-1),cols, Short.BYTES, bo, m);
                boolean mismatch = IntStream.range(0,rows).parallel().anyMatch(r ->
                        IntStream.range(0,cols).parallel().anyMatch(c -> array[r][c]/(1.0*Short.MAX_VALUE) != reader.getDistance(r,c)));
                if (mismatch) System.out.println(distFile + " failed");

                // Uncomment for verbose checks
                /*IntStream.range(0, rows).forEach(r ->
                {IntStream.range(0, cols).forEach(c -> System.out.print(array[r][c] +"\t"));
                    System.out.println();});*/
            });
        });


        System.out.println();
    }

    private static void generateDistanceFile(ByteOrder byteOrder, int rows, int cols, String distFile) {
        generateDistanceFile(byteOrder, rows, cols, distFile, Optional.ofNullable(null));
    }

    private static void generateDistanceFile(ByteOrder byteOrder, int rows, int cols, String distFile, short[][] array) {
        generateDistanceFile(byteOrder, rows, cols, distFile, Optional.ofNullable(array));
    }

    private static void generateDistanceFile(ByteOrder byteOrder, int rows, int cols, String distFile, Optional<short[][]> optionalArray) {
        try (OutputStream os = Files.newOutputStream(Paths.get(distFile),
                StandardOpenOption.CREATE)) {
            DataOutput out = byteOrder == ByteOrder.BIG_ENDIAN ? new DataOutputStream(
                    os) : new LittleEndianDataOutputStream(os);
            IntStream.range(0,rows).forEach(i -> {IntStream.range(0, cols).forEach(j -> {
                short v = (short) (Math.random() * Short.MAX_VALUE);
                optionalArray.ifPresent(array -> array[i][j] = v);
                try {
                    // Uncomment for verbose checks
                    /*System.out.print(v/(1.0*Short.MAX_VALUE) + "\t");*/
                    out.writeShort(v);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
                // Uncomment for verbose checks
                /*System.out.println();*/
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
