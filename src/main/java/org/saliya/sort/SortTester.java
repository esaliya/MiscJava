package org.saliya.sort;

import java.util.Arrays;

public class SortTester {
    public static void main(String[] args) {
        int num = 10;
        Smaller [] smallers = new Smaller[num];
        for (int i = 0; i < smallers.length; i++) {
           smallers[i] = new Smaller();
           smallers[i].index = num - i;
           smallers[i].value = i;
        }

        System.out.println(Arrays.toString(smallers));
        Arrays.sort(smallers,0,num);
        System.out.println(Arrays.toString(smallers));

    }
}
