package org.saliya.threads;

import static edu.rice.hj.HJ.*;

public class HJMethods {
    public static void method1(int size, double [] in, double [] out){
        double t1 = System.currentTimeMillis();
        forallChunked(1, size - 1,
                      (index) -> {
                          out[index - 1] = (in[index - 1] + in[index + 1]) / 2;
                      }
        );
        double time = System.currentTimeMillis() - t1;
        System.out.println("HJ took: " + time + "ms");
    }


}
