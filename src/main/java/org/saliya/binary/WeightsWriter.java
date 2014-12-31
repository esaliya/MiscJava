package org.saliya.binary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class WeightsWriter {
    public static void main(String[] args) throws IOException {
        int rows = Integer.parseInt(args[0]);
        String file = "weights.bin";

        SeekableByteChannel bc = Files.newByteChannel(Paths.get(file), StandardOpenOption.CREATE);
        ByteBuffer buffer = ByteBuffer.allocateDirect(rows * Short.BYTES);
        buffer.position(0);
        for (int i = 0; i < rows; ++i){
            buffer.putShort((short)1);
        }
        for (int i = 0; i < rows; ++i) {
            buffer.position(0);
            bc.write(buffer);
        }
        bc.close();
    }
}
