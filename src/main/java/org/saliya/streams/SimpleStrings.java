package org.saliya.streams;


import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.IntStream;

public class SimpleStrings {
    public static void main(String[] args) {
        System.out.println("Parameters...");
        String[] params =
                new String[]{"Num map tasks", "Input Folder", "Input File Prefix", "Weighted File Prefix", "IDs File",
                        "Label Data File", "Output File", "Threshold value", "The Target Dimension",
                        "Cooling parameter (alpha)", "Input Data Size", "Final Weight Prefix", "CG Iterations",
                        "CG Threshold", "Sammon mapping"};
        String [] values = new String[params.length];
        IntStream.range(0,params.length).forEach(i -> values[i] = String.valueOf(i));
        Optional<Integer> maxLength = Arrays.stream(params).map(String::length).reduce(Math::max);
        if (!maxLength.isPresent()) return;
        final int max = maxLength.get();
        final String prefix = "  ";
        IntStream.range(0, params.length).forEach(i -> {
            String param = params[i];
            System.out.println(getPadding(max - param.length(), prefix) + param + ": " + values[i]);
        });
        IntStream.range(0, params.length).forEach(i -> {
            String param = params[i];
            System.out.println(prefix + param + ":" + getPadding(max - param.length(), "") + values[i]);
        });


    }

    public static String getPadding(int count, String prefix){
        StringBuilder sb = new StringBuilder(prefix);
        IntStream.range(0,count).forEach(i -> sb.append(" "));
        return sb.toString();
    }
}
