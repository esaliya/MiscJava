package org.saliya.threads.frameworks.pwcanalysis;

import mpi.MPI;

import java.nio.ByteBuffer;

public abstract class MPIPacket
{
    final int shift;
    private final int extent;
    private final int mArrayLength;
    ByteBuffer buffer;
    private static int firstPointOffset = 0;
    private static int numberOfPointsOffset = Integer.BYTES;
    private static int mArrayOffset = 2*Integer.BYTES;


    private MPIPacket(int mArrayLength, int extent, int shift, ByteBuffer buffer)
    {
        this.extent = extent;
        this.shift = shift;
        this.mArrayLength = mArrayLength;
        this.buffer =  (buffer == null) ? MPI.newByteBuffer(extent) : buffer; // new ByteBuffers are automatically initialized to zero
    }

    public static MPIPacket newIntegerPacket(int mArrayLength){
        return newIntegerPacket(mArrayLength, null);
    }

    private static MPIPacket newIntegerPacket(int mArrayLength, ByteBuffer buffer){
        return new MPIPacket(mArrayLength,(mArrayLength+2) * Integer.BYTES, Integer.BYTES, buffer){
            public int getMArrayIntAt(int idx){
                return this.buffer.getInt(mArrayOffset+idx*this.shift);
            }

            public void setMArrayIntAt(int idx, int value){
                this.buffer.putInt(mArrayOffset+idx*this.shift, value);
            }

            @Override
            public void Clear() {
                setFirstPoint(0);
                setNumberOfPoints(0);
                for (int i = 0; i < mArrayLength; ++i){
                    setMArrayIntAt(i, 0);
                }
            }
        };
    }

    public static MPIPacket loadIntegerPacket(ByteBuffer buffer){
        return newIntegerPacket(((buffer.limit()/Integer.BYTES) - 2), buffer); // buffer.limit() will be a multiplier of Interger.BYTES for this case
    }


    public static MPIPacket newDoublePacket(int mArrayLength){
        return newDoublePacket(mArrayLength, null);
    }

    private static MPIPacket newDoublePacket(int mArrayLength, ByteBuffer buffer){
        return new MPIPacket(mArrayLength, 2 * Integer.BYTES + mArrayLength * Double.BYTES, Double.BYTES, buffer){
            public double getMArrayDoubleAt(int idx){
                return this.buffer.getDouble(mArrayOffset + idx * this.shift);
            }

            public void setMArrayDoubleAt(int idx, double value){
                this.buffer.putDouble(mArrayOffset + idx * this.shift, value);
            }

            @Override
            public void Clear() {
                setFirstPoint(0);
                setNumberOfPoints(0);
                for (int i = 0; i < mArrayLength; ++i){
                    setMArrayDoubleAt(i, 0.0);
                }
            }
        };
    }

    public static MPIPacket loadDoublePacket(ByteBuffer buffer){
        return newDoublePacket(((buffer.limit() - 2*Integer.BYTES)/Double.BYTES), buffer);
    }

    public int getMArrayIntAt(int idx){
        throw new UnsupportedOperationException();
    }

    public void setMArrayIntAt(int idx, int value){
        throw new UnsupportedOperationException();
    }

    public double getMArrayDoubleAt(int idx){
        throw new UnsupportedOperationException();
    }

    public void setMArrayDoubleAt(int idx, double value){
        throw new UnsupportedOperationException();
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public int getExtent() {
        return extent;
    }

    public int getFirstPoint(){
        return buffer.getInt(firstPointOffset);
    }

    public void setFirstPoint(int firstPoint){
        buffer.putInt(firstPointOffset, firstPoint);
    }

    public int getNumberOfPoints(){
        return buffer.getInt(numberOfPointsOffset);
    }

    public void setNumberOfPoints(int numberOfPoints){
        buffer.putInt(numberOfPointsOffset, numberOfPoints);
    }

    public abstract void Clear();


    public enum Type {Integer, Double}
}
