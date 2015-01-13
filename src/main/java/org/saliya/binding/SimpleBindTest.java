package org.saliya.binding;

import org.saliya.common.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleBindTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("I am a simple program doing nothing but busy waiting with PID " + Utils.getPid());
        int n = Integer.parseInt(args[0]);
        int delay = Integer.parseInt(args[1]);
        int interval = 100;
        long count = 0;
        Path stop = Paths.get("stop");
        Path pause = Paths.get("pause");
        while (!Files.exists(stop)){
            System.out.println("Iteration " + count);
            double x = 0.0;
            for (int i = 0; i < n; ++i){
                if (n%interval == 0){
                    if (Files.exists(stop)) {
                        break;
                    } else if (Files.exists(pause)){
                        System.out.println("**Paused**");
                        Files.delete(pause);
                        Thread.sleep(delay);
                        System.out.println("**Resumed**");
                    }
                }
                x += Math.random() * n;
            }
            if (x == Math.random() * n * n){ // probably will not happen and that's the idea
                System.out.println("\twhat a coincident!!!");
            }
            ++count;
        }
        Files.delete(stop);
        System.out.println();
    }
}
