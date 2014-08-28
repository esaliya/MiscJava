package org.saliya.buffers;

import com.google.common.primitives.Doubles;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

public class BufferPrimer {
    public static void main(String[] args) {
//        testBufferProperties();
//        testBufferCopy();
        testBufferInitialization();

    }

    private static void testBufferInitialization() {
        int numDoubles = 12;
        int numInts = 2;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(numDoubles*Doubles.BYTES + numInts*Integer.BYTES);
        System.out.println(
                "byteBuffer capacity:" + byteBuffer.capacity() + " limit:" + byteBuffer.limit() + " position:" + byteBuffer.position() + " remaining:" + byteBuffer.remaining());
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getInt());
        for (int i = 0; i < numDoubles; i++) {
            System.out.println(byteBuffer.getDouble());
        }
    }

    private static void testBufferCopy() {
        int numDoubles = 12;
        int numInts = 2;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(numDoubles*Doubles.BYTES + numInts*Integer.BYTES);
        byteBuffer.putInt(3);
        byteBuffer.putInt(27);
        for (int i = 0; i < numDoubles; i++) {
            byteBuffer.putDouble(i*0.1);
        }
        System.out.println("byteBuffer capacity:" + byteBuffer.capacity() + " limit:" + byteBuffer.limit() + " position:" + byteBuffer.position() + " remaining:" + byteBuffer.remaining());

        ByteBuffer deepCopy = ByteBuffer.allocateDirect(byteBuffer.capacity());
        byteBuffer.position(0);
        if (byteBuffer.limit() != byteBuffer.capacity()){
            byteBuffer.limit(byteBuffer.capacity());
        }
        System.out.println("byteBuffer capacity:" + byteBuffer.capacity() + " limit:" + byteBuffer.limit() + " position:" + byteBuffer.position() + " remaining:" + byteBuffer.remaining());
        System.out.println("deepCopy capacity:" + deepCopy.capacity() + " limit:" + deepCopy.limit() + " position:" + deepCopy.position() + " remaining:" + deepCopy.remaining());
        deepCopy.put(byteBuffer);
        System.out.println(
                "byteBuffer capacity:" + byteBuffer.capacity() + " limit:" + byteBuffer.limit() + " position:" + byteBuffer.position() + " remaining:" + byteBuffer.remaining());
        System.out.println(
                "deepCopy capacity:" + deepCopy.capacity() + " limit:" + deepCopy.limit() + " position:" + deepCopy.position() + " remaining:" + deepCopy.remaining());
        byteBuffer.flip();
        deepCopy.flip();
    }

    private static void testBufferProperties() {
        int sizeInDoubles = 12;
        // Allocate a buffer with space for 12 double values or 12*8 bytes
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(sizeInDoubles* Doubles.BYTES);

        System.out.println("byteBuffer capacity:" + byteBuffer.capacity() + " limit:" + byteBuffer.limit() + " position:" + byteBuffer.position() + " remaining:" + byteBuffer.remaining());
//          -- Output:  byteBuffer capacity:96 limit:96 position:0 remaining:96
        byteBuffer.putDouble(14.0);
        byteBuffer.putInt(20);
        System.out.println(
                "byteBuffer capacity:" + byteBuffer.capacity() + " limit:" + byteBuffer.limit() + " position:" + byteBuffer.position() + " remaining:" + byteBuffer.remaining());
//          -- Output:  byteBuffer capacity:96 limit:96 position:12 remaining:84
        byteBuffer.flip();
        System.out.println(
                "byteBuffer capacity:" + byteBuffer.capacity() + " limit:" + byteBuffer.limit() + " position:" + byteBuffer.position() + " remaining:" + byteBuffer.remaining());
//          -- Output:  byteBuffer capacity:96 limit:12 position:0 remaining:12
        System.out.println(byteBuffer.getDouble() + " " + byteBuffer.getInt());
//          -- Output   14.0 20
        System.out.println("byteBuffer capacity:" + byteBuffer.capacity() + " limit:" + byteBuffer.limit() + " position:" + byteBuffer.position() + " remaining:" + byteBuffer.remaining());
//        -- Output:   byteBuffer capacity:96 limit:12 position:12 remaining:0

        /*byteBuffer.putInt(13); -- will fail as we have limit=12 now*/
        byteBuffer.limit(byteBuffer.capacity());
        byteBuffer.putInt(13); // -- will WORK now
        byteBuffer.putInt(26); // -- will also WORK now
        System.out.println("byteBuffer capacity:" + byteBuffer.capacity() + " limit:" + byteBuffer.limit() + " position:" + byteBuffer.position() + " remaining:" + byteBuffer.remaining());
//      -- Output:  byteBuffer capacity:96 limit:96 position:20 remaining:76

        // Let's create a different view
        DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
        System.out.println("doubleBuffer capacity:" + doubleBuffer.capacity() + " limit:" + doubleBuffer.limit() + " position:" + doubleBuffer.position() + " remaining:" + doubleBuffer.remaining());
//        -- Output:  doubleBuffer capacity:9 limit:9 position:0 remaining:9
        doubleBuffer.put(30.34);

        /* Shows the indepence of limit and position in these two buffers even though they back the same data */
        System.out.println("doubleBuffer capacity:" + doubleBuffer.capacity() + " limit:" + doubleBuffer.limit() + " position:" + doubleBuffer.position() + " remaining:" + doubleBuffer.remaining());
//        -- Output:  doubleBuffer capacity:9 limit:9 position:1 remaining:8
        System.out.println("byteBuffer capacity:" + byteBuffer.capacity() + " limit:" + byteBuffer.limit() + " position:" + byteBuffer.position() + " remaining:" + byteBuffer.remaining());
//        -- Output:  byteBuffer capacity:96 limit:96 position:20 remaining:76
    }
}
