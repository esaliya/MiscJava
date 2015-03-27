package org.saliya.strings;

public class IndexOf {
    public static void main(String[] args) {
        int prev = -1;
        String s = "110011";
        for (int i = 0; i < 4; ++i) {
            prev = s.indexOf('1', prev+1);
            System.out.println(prev);
        }
    }
}
