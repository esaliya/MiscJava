package org.saliya.threads;

import edu.rice.hj.api.SuspendableException;

import static edu.rice.hj.Module1.forallChunked;

public class HJMethods {
    public static void method1(int size, double [] in, double [] out){
        double t1 = System.currentTimeMillis();
        try {
            forallChunked(1, size - 1,
                          (index) -> {
                              out[index - 1] = (in[index - 1] + in[index + 1]) / 2;
                          }
            );
        } catch (SuspendableException e) {
            e.printStackTrace();
        }
        double time = System.currentTimeMillis() - t1;
        System.out.println("HJ took: " + time + "ms");
    }


}
