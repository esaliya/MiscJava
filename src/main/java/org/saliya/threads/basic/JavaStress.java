package org.saliya.threads.basic;

public class JavaStress {
    public static void main(String[] args) {
        int i = -1;
        double x=0.01;
        while (i < Integer.MAX_VALUE){
            x = Math.sqrt(Math.random()+x);
            ++i;
        }
        System.out.println(x);
    }
}
