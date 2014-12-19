package org.saliya.ompi.comm;

import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.IntStream;

public class UserFunctionTest{
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        Intracomm comm = MPI.COMM_WORLD;
        int rank = comm.getRank();
        int size = comm.getSize();

        int vecLen = 2;
        Object [] userObjects = new UserObject[vecLen];
        IntStream.range(0,vecLen).forEach(
                i -> userObjects[i] = new UserObject((i+1)+(rank*10),(i+1)+(rank*10),(i+1)+(rank*10)));
        System.out.println("Rank: " + rank + " " + Arrays.toString(userObjects));

        int capacity = vecLen * UserObject.extent;
        ByteBuffer buffer = MPI.newByteBuffer(capacity);
        IntStream.range(0,vecLen).forEach(i -> ((UserObject)userObjects[i]).addToBuffer(buffer, i*UserObject.extent));

        ByteBuffer outBuffer = MPI.newByteBuffer(capacity);
        comm.reduce(buffer, outBuffer, capacity, MPI.BYTE, UserObject.reduceUserObjects(), 0);

//        comm.sendRecv(buffer, capacity, MPI.BYTE, (rank+1)%size, 0, outBuffer, capacity, MPI.BYTE, (rank+1)%size, 0);

        IntStream.range(0,vecLen).forEach(i -> userObjects[i] = UserObject.getFromBuffer(outBuffer, i*UserObject.extent));
        if (rank == 0){
            System.out.println("Final: " + Arrays.toString(userObjects));
        }
        MPI.Finalize();
    }

}