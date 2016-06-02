package org.saliya.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class MemCopy {
    public static  Unsafe UNSAFE;

    static {
        try {
            @SuppressWarnings("ALL")
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public static void main(String[] args) {
        int [] x = new int[]{1, 2, 3};
        int [] y = (int []) shallowCopy(x);
//        System.out.println(y.length);
    }

    static Object shallowCopy(Object obj) {
        long size = sizeOf(obj);
        long start = toAddress(obj);
        long address = UNSAFE.allocateMemory(size);
        UNSAFE.copyMemory(start, address, size);
        return fromAddress(address);
    }

    static long toAddress(Object obj) {
        Object[] array = new Object[] {obj};
        long baseOffset = UNSAFE.arrayBaseOffset(Object[].class);
        return normalize(UNSAFE.getInt(array, baseOffset));
    }

    static Object fromAddress(long address) {
        Object[] array = new Object[] {null};
        long baseOffset = UNSAFE.arrayBaseOffset(Object[].class);
        UNSAFE.putLong(array, baseOffset, address);
        return array[0];
    }

    private static long normalize(int value) {
        if(value >= 0) return value;
        return (~0L >>> 32) & value;
    }

    public static long sizeOf(Object object){
        return UNSAFE.getAddress(
                normalize(UNSAFE.getInt(object, 4L)) + 12L);
    }
}
