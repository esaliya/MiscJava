package org.saliya.threads;



import edu.rice.hj.api.SuspendableException;
import java.util.Random;

import static edu.rice.hj.Module0.finalizeHabanero;
import static edu.rice.hj.Module0.forallChunked;
import static edu.rice.hj.Module0.initializeHabanero;


public class SimpleHJLoop {
    public static void main(String[] args) {
        initializeHabanero();

        int size = 4;
        // preparing for three point stencil
        double [] in = new double[size+2];
        double [] out = new double[size];

        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < size+2; i++){
            in[i] = r.nextInt();
        }

        method1(size, in, out);

        finalizeHabanero();
    }

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
