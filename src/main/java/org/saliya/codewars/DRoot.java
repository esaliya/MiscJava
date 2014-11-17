package org.saliya.codewars;

public class DRoot {
    public static int digitalRoot(int n) {
        int sum = 0;
        while (n > 0){
            sum += n % 10;
            n = n / 10;
        }
        return sum > 9 ? digitalRoot(sum) : sum;
    }

    public static void main(String[] args) {
        System.out.println(digitalRoot(167));
    }
}
