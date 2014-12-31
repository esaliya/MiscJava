package org.saliya.binary;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class WeightsWriter {
    public static void main(String[] args) {
        int rows = Integer.parseInt(args[0]);
        Path path = Paths.get("weights.bin");
        try (FileChannel fc = (FileChannel) Files.newByteChannel(path, StandardOpenOption.CREATE_NEW)) {
            MappedByteBuffer mappedBytes = fc.map(FileChannel.MapMode.READ_WRITE, 0,
                                                  rows * rows * Short.BYTES);
            mappedBytes.order(ByteOrder.BIG_ENDIAN);
            if (rows < Math.sqrt(Integer.MAX_VALUE)){
                int count = rows * rows;
                for (int i = 0; i < count; ++i){
                    mappedBytes.putShort((short)1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
