package org.saliya.threads.basic;

import edu.rice.hj.api.SuspendableException;

import static edu.rice.hj.Module0.launchHabaneroApp;
import static edu.rice.hj.Module1.forallChunked;

public class HJTester {
    public static void main(String[] args) throws SuspendableException {
        launchHabaneroApp(() -> {
            forallChunked(0, 3, (threadIndex) -> {
                System.out.println("Hi from thread index: " + threadIndex);
            });
        });


        final int startInc = 0;
        final int endInc = 8;
        launchHabaneroApp(() -> {

            // example with default chunk size
            forallChunked(startInc, endInc, (index) -> {
                System.out.printf("Default Chunk: Executing iteration-%d \n", index);
            });

            // example with explicit chunk size
            forallChunked(startInc, endInc, 1, (index) -> {
                System.out.printf("Custom Chunk: Executing iteration-%d \n", index);
            });
        });

    }


}
