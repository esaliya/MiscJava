package org.saliya.excel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class SplitToColumns {
    public static void main(String[] args) {
        try (BufferedReader reader = Files.newBufferedReader(
            Paths.get("src/main/java/org/saliya/excel/data.txt"));
            BufferedWriter writer = Files
                .newBufferedWriter(Paths.get("src/main/java/org/saliya/excel/processed.txt"))) {
            PrintWriter pw = new PrintWriter(writer, true);
            String s = "";
            Pattern pat = Pattern.compile("\t");
            String machine = "";
            while ((s = reader.readLine()) != null) {
                String[] splits = pat.split(s.trim());
                if (!splits[1].equals(machine)){
                    machine = splits[1];
                    pw.print("\n" + machine + "\t");
                }
                pw.print(splits[0] + "\t");
            }
            pw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
