package org.saliya.files;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;

public class FileTest {
    public static void main(String[] args) {
        File f = new File("what.txt");
        System.out.println(f.isFile());

        File dir = new File("tmpdir");
        boolean success = dir.mkdir();
        if (success){
            System.out.println(new Date(dir.lastModified()));
        } else {
            dir.delete();
        }

        dir = new File("/home/saliya");
        System.out.println(Arrays.toString(dir.listFiles()));

        Path path = Paths.get("/home/saliya/sali/software/jdk-8-linux-x64.tar.gz");
        System.out.println(path.getFileName());

    }
}
