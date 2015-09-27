package org.saliya.arrrays;

import java.util.Arrays;

public class ArrayTests {
    public static void main(String[] args) {
        float [][] arr = new float[2][3];
        arr[1][2] = 3.0f;
        for (float [] a : arr){
            System.out.println(Arrays.toString(a));
        }
    }
}
