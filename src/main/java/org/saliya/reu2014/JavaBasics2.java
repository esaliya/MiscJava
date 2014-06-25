package org.saliya.reu2014;

public class JavaBasics2 {
    public static void main(String[] args) {
//        int [] ar = new int[1];

        int [] ar = m(new int[1]);
    }

    public static int [] m(int [] ar){
        ar[0] = 30;
        return ar;
    }
}
