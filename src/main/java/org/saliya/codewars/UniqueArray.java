package org.saliya.codewars;

import java.util.Arrays;
import java.util.HashSet;

public class UniqueArray {
    public static int[] unique(int[] integers) {
        int length = integers.length;
        int [] uniqs = new int[length];
        HashSet<Integer> hs = new HashSet<>(length);
        int count = 0;
        for (int i = 0; i < length; ++i){
            int num = integers[i];
            if (hs.contains(num)) continue;
            hs.add(num);
            uniqs[count] = num;
            ++count;
        }

        return Arrays.copyOf(uniqs, count);
    }

    public static void main(String[] args) {
        int itr = 100;
        for (int j = 0; j < itr; ++j) {
            int num = 100000;
            int[] arr = new int[num];
            for (int i = 0; i < num; ++i) {
                arr[i] = i;
            }

            int[] newArr = new int[num];
            int count = 0;
            for (int i : arr) {
                newArr[count] = i;
                ++count;
            }

            for (int i = num - 1; i > 0; --i) {
                if (newArr[i - 1] == newArr[i] - 1) continue;
                System.out.println("Foreach doesn't gurantee order");
                break;
            }
        }
    }
}
