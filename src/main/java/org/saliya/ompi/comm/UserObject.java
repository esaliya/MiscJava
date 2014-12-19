package org.saliya.ompi.comm;

import mpi.Datatype;
import mpi.MPI;
import mpi.MPIException;
import mpi.UserFunction;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;

public class UserObject{
    public static int extent = Integer.BYTES + 2*Double.BYTES;
    private static int countOffset = 0;
    private static int sumOffset = Integer.BYTES;
    private static int maxOffset = sumOffset+Double.BYTES;
    public int count;
    public double sum;
    public double max;

    public UserObject(int count, double sum, double max) {
        this.count = count;
        this.sum = sum;
        this.max = max;
    }

    public void addToBuffer(ByteBuffer buffer, int index){
        buffer.putInt(index, count).putDouble(index+sumOffset, sum).putDouble(index+maxOffset, max);
    }

    public static UserObject getFromBuffer(ByteBuffer buffer, int index){
        return new UserObject(buffer.getInt(index+countOffset), buffer.getDouble(index+sumOffset), buffer.getDouble(index+maxOffset));
    }

    public static mpi.Op reduceUserObjects() throws MPIException {
        return new mpi.Op(new UserFunction() {
            @Override
            public void call(Object inVec, Object inOutVec, int count, Datatype datatype) throws MPIException {
                // Nothing to do here
            }

            @Override
            public void call(ByteBuffer in, ByteBuffer inOut, int count, Datatype datatype) throws MPIException {
                if (count % extent != 0) {
                    System.out.println(
                            "invalid extent on reduce operation " + count + " expected " + extent + "*n where n>=0");
                    MPI.COMM_WORLD.abort(1);
                }
                int vecLen = count / extent;
                IntStream.range(0,vecLen).forEach(i -> {
                    int index = i*extent;

                    inOut.putInt(index+countOffset, in.getInt(index+countOffset) + inOut.getInt(index+countOffset));
                    inOut.putDouble(index + sumOffset,
                            in.getDouble(index + sumOffset) + inOut.getDouble(index + sumOffset));

                    double max = in.getDouble(index + maxOffset);
                    double inOutMax = inOut.getDouble(index + maxOffset);
                    if (max > inOutMax){
                        inOut.putDouble(index + maxOffset, max);
                    }
                });
            }
        }, true);
    }

    @Override
    public String toString() {
        return "UserObject{" +
                "count=" + count +
                ", sum=" + sum +
                ", max=" + max +
                '}';
    }
}