package org.saliya.unsafe;

import net.openhft.lang.io.ByteBufferBytes;
import net.openhft.lang.io.Bytes;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MappedBytes {
    static Bytes mmapCollectiveBytes;
    static ByteBuffer mmapCollectiveByteBuffer;
    public static void main(String[] args) {
        int maxMsgSize = 4*Integer.BYTES;
        String mmapCollectiveFileName = "mmapId.mmapCollective.bin";
        try (FileChannel mmapCollectiveFc = FileChannel
                .open(Paths.get(".", mmapCollectiveFileName),
                        StandardOpenOption.CREATE, StandardOpenOption.READ,
                        StandardOpenOption.WRITE)) {

            mmapCollectiveBytes = ByteBufferBytes.wrap(mmapCollectiveFc.map(
                    FileChannel.MapMode.READ_WRITE, 0L, maxMsgSize));
            mmapCollectiveByteBuffer = mmapCollectiveBytes.sliceAsByteBuffer(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mmapCollectiveBytes.writeInt(0, 4);
        System.out.println(mmapCollectiveBytes.readInt(0));
    }
}
