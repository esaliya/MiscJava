package org.saliya.mpirankfile;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

public class OtherNodes {
    public static void main(String[] args) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(
            "src/main/java/org/saliya/mpirankfile/nodes.txt"));
            BufferedReader allReader = Files.newBufferedReader(Paths.get(
                "src/main/java/org/saliya/mpirankfile/allnodes.txt"));
            BufferedWriter writer = Files
                .newBufferedWriter(Paths.get(
                    "src/main/java/org/saliya/mpirankfile/other.nodes.txt"))) {

            PrintWriter pw = new PrintWriter(writer, true);
            String s = "";
            String machine = "";
            HashSet<String> hs = new HashSet<>();
            while ((s = reader.readLine()) != null) {
                machine = s.trim();
                if (!hs.contains(machine)){
                    hs.add(machine);
                }
            }

            while ((s = allReader.readLine()) != null){
                machine = s.trim();
                if (hs.contains(machine)) {
                    continue;
                }
                pw.println(machine);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
