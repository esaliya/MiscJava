package org.saliya.numbers;

public class Overflows {
    public static void main(String[] args) {
        int max = 8;
        long dataTypeSize = Short.BYTES;
        for (int i = 0; i < max - 1;){
            if (i < 0){
                System.out.println("error here at i="+i);
            }
            System.out.println(i);
            i += ((int)dataTypeSize);
        }
    }
}
