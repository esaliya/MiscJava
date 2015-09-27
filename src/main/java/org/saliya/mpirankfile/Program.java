package org.saliya.mpirankfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class Program {
    public static void main(String[] args) {
        try (BufferedReader reader = Files.newBufferedReader(
            Paths.get("src/main/java/org/saliya/mpirankfile/nodes.txt"));
            BufferedWriter writer = Files
                .newBufferedWriter(Paths.get(
                    "src/main/java/org/saliya/mpirankfile/ranks.txt"))) {
            int ranksPerNode = 12;

            PrintWriter pw = new PrintWriter(writer, true);
            String s = "";
            String machine = "";
            int count = 0;
            while ((s = reader.readLine()) != null) {
                machine = s.trim();
                for (int i =0; i < ranksPerNode; ++i){
                    pw.println("rank " + (count*ranksPerNode+i) + "=" + machine + " slot=" + (2*i) + "," + (2*i+1));
                }
                ++count;
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
