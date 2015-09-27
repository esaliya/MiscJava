package org.saliya.distances;

import java.util.stream.IntStream;

public class VerifyDistances{
    public static void main(String[] args) {

        double v = 0.1;
        double x = v * Short.MAX_VALUE;
        short sx = (short) (v*Short.MAX_VALUE);
        double dsx = sx * 1.0 / Short.MAX_VALUE;
        System.out.println(x + " " + sx + " " + dsx);
        double m = 3277 *1.0 / Short.MAX_VALUE;
        System.out.println(m);


        int size = 6435;
        double[][] values = new double[size][];
        IntStream.range(0, size).parallel().forEach(i -> {
            values[i] = new double[size];
            IntStream.range(0, size).parallel().forEach(j->values[i][j]=Math.random());
        });


        long count1=0;
        long count2=0;
        long count3=0;
        long count4=0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < values[i].length; j++) {
                double doubleValue = values[i][j];
                short shortValue = (short) (doubleValue * Short.MAX_VALUE);
                if (shortValue < 3277) {
                    count2++;
                    if (doubleValue < .1) {
                        count1++;
                    }
                }
                if (doubleValue < .1) {
                    count3++;
                    if (shortValue > 3277) {
                        count4++;
                    }
                }
            }
        }

        System.out.println("Count1 " + count1);
        System.out.println("Count2 " + count2);
        System.out.println("Count3 " + count3);
        System.out.println("Count4 " + count4);

    }
}
