package org.saliya.arrrays;

import java.util.Arrays;

public class ArrayTests {
    public static void main(String[] args) {
        float [][] arr = new float[2][3];
        for (float [] a : arr){
            System.out.println(Arrays.toString(a));
        }
    }
}
