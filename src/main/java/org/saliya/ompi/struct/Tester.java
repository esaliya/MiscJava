package org.saliya.ompi.struct;

import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;
import org.saliya.ompi.struct.types.Complex;

import java.nio.ByteBuffer;

public class Tester {
    public static void main(String[] args) throws MPIException {
        args = MPI.Init(args);

        Intracomm world = MPI.COMM_WORLD;
        int size = world.getSize();
        int rank = world.getRank();

        int numOfItems = 2;

        Complex typeComplex = new Complex();
        ByteBuffer buffer = MPI.newByteBuffer(typeComplex.getExtent() * 2);
        if (rank == 0){
            for(int i = 0; i < numOfItems; ++i){
                Complex.Data c = typeComplex.getData(buffer, i);
                c.putReal(i);
                c.putImg(2*i);
            }
        }
        world.bcast(buffer, numOfItems, typeComplex.getType(), 0);

        if (rank == size - 1){
            for(int i = 0; i < numOfItems; ++i){
                Complex.Data c = typeComplex.getData(buffer, i);
                System.out.println("Complex " + i  + " is " + c);
            }
        }
        MPI.Finalize();
    }
}
