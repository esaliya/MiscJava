package org.saliya.tricks;

public class Tester {
    public static void main(String[] args) {
        int rows = 10, cols = 20;
        AbstractAndOverrides instance = AbstractAndOverrides.getMeANewInstance(rows, cols);
        System.out.println(instance.getValue(3,4));
        System.out.println(instance.getValue(4,5));
    }
}
