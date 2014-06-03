package org.saliya.files;

import java.io.File;

public class FileTest {
    public static void main(String[] args) {
        File f = new File("what.txt");
        System.out.println(f.isFile());
    }
}
