package org.saliya.bignumbers;

public class DoubleAddition {
    public static void main(String[] args) {
        int count = 10000*10000;
        double sum = 0.0d;
        for (int i = 0; i < count; i++) {
             sum += Math.random();
        }
        System.out.println(sum);
    }
}
