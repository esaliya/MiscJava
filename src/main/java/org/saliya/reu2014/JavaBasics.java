package org.saliya.reu2014;

import java.util.ArrayList;

public class JavaBasics {
    public static void main(String[] args) {
        int x = 10;
        int [] al = new int[1];
        System.out.println("before m()");
        System.out.println(x);
        System.out.println(al);
        System.out.println(al[0]);
        m(x,al);
        System.out.println("after m()");
        System.out.println(x);
        System.out.println(al);
        System.out.println(al[0]);

    }

    public static void m(int x, int[] al){
        // Logic goes over here;
        x = 20;
//        al[0] = 35;
        System.out.println("\twithin m() before setting al to a new array");
        System.out.println("\t" + al);
        al = new int[1];
        System.out.println("\twithin m() after setting al to a new array");
        System.out.println("\t" + al);
        al[0] = 35;


    }
}
