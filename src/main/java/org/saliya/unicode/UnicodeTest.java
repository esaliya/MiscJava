package org.saliya.unicode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class UnicodeTest {
    public static void main(String[] args) {

        String parasStr = "ijdikd il, laf,aYhka iuqÑfþokh";
        System.out.println("\u0D9A\u200D\u0DBB");
        System.out.println("\u0DB4\u0DCA\u200D\u0DBB\u0DDE\u0DAA");
        System.out.println("\u0D9C\u0DCA\u200D\u0DBB");

        String outputfile = "E:\\Sali\\git\\github\\esaliya\\MiscJava\\src"
                            + "\\main\\java\\org\\saliya\\test.txt";
        try(BufferedWriter bw = Files.newBufferedWriter(
            Paths.get(outputfile), Charset.forName("UTF-8"),
            StandardOpenOption.CREATE)) {
            PrintWriter writer = new PrintWriter(bw, true);
            writer.println("\u0D9C\u0DCA\u200D\u0DBB");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
