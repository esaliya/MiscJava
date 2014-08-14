package org.saliya.buffers;

import com.google.common.base.Stopwatch;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class BufferWrap {
    public static void main(String[] args) {
        int size = 100000;
        byte [] arr = new byte[size];
        fillByteArray(arr, (byte)1);

        ByteBuffer buffer = ByteBuffer.wrap(arr);

        byte x = (byte)0;
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < size; i++) {
            byte b = arr[i];
            x = b;
        }
        stopwatch.stop();
        System.out.println("array get " + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        System.out.println(x);

        stopwatch.reset();
        stopwatch.start();
        for (int i = 0; i < size; i++) {
            byte b = buffer.get(i);
            x = b;
        }
        stopwatch.stop();
        System.out.println("buffer get " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        System.out.println(x);
    }

    public static void fillByteArray(byte [] arr, byte value){
        for (int i = 0; i < arr.length; i++) {
            arr[i] = value;

        }
    }
}
