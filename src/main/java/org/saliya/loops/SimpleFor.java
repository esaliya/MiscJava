package org.saliya.loops;

public class SimpleFor {
    public static void main(String[] args) {
        int maxMsgSize = 524288;
        for (int numBytes = 0; numBytes <= maxMsgSize; numBytes = (numBytes == 0 ? 1 : numBytes*2)){
            System.out.println(numBytes);
        }
    }
}
