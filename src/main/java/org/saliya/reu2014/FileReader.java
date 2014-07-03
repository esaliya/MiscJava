package org.saliya.reu2014;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class FileReader {
    /**
     * Reads a points file, which has the structure as below
     * pnum\tx\ty\t\z\t..\tcnum
     * @param fileName - path to the points file
     * @param n - number of points
     * @param m - number of dimensions
     * @return a two dimensional array holding the points
     */
    public static double [][] readPointFile(String fileName, int n, int m){
        double [][] points = new double[n][m];
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName), Charset.defaultCharset())){
            Pattern pattern = Pattern.compile("[\t]");
            int pnum = 0;
            String line;
            while ((line = reader.readLine()) != null && pnum < n){
                String [] splits = pattern.split(line);
                for (int i = 1; i < splits.length - 1; ++i){
                    points[pnum][i] = Double.parseDouble(splits[i]);
                }
                ++pnum;
            }
        } catch (IOException e) {
            System.out.println("Error! Could not read points file " + fileName);
        }
        return points;
    }
}
