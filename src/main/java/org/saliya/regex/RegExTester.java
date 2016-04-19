package org.saliya.regex;

import java.util.Arrays;
import java.util.regex.Pattern;

public class RegExTester {
    public static void main(String[] args) {
//        String str = "0@j-1#~~1@j-002#14@j-43#~";
        String str = "64j-001#~~";
        Pattern nodeSep = Pattern.compile("#~*");
        String[] nodeSplits = nodeSep.split(str);
        System.out.println(Arrays.toString(nodeSplits));
        Pattern rankSep = Pattern.compile("@");
        for(String node:nodeSplits){
            String[] splits = rankSep.split(node);
            System.out.printf(Arrays.toString(splits));
        }
    }
}
