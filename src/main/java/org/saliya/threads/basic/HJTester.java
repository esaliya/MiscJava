package org.saliya.threads.basic;

import edu.rice.hj.api.SuspendableException;

import static edu.rice.hj.Module0.finalizeHabanero;
import static edu.rice.hj.Module0.initializeHabanero;
import static edu.rice.hj.Module1.forallChunked;

public class HJTester {
    public static void main(String[] args) throws SuspendableException {
        initializeHabanero();
        forallChunked(0, 3, (threadIndex) -> {
            System.out.println("Hi from thread index: " + threadIndex);
        });
        finalizeHabanero();

    }


}
