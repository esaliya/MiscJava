package org.saliya.ompi.struct.types;

import mpi.Struct;

public class Complex extends Struct {

    private int real = addDouble();
    private int img = addDouble();

    @Override
    protected Data newData() {
        return new Data();
    }

    public class Data extends Struct.Data{
        public double getReal(){return getDouble(real);}
        public double getImg(){return getDouble(img);}

        public void putReal(double value){putDouble(real, value);}
        public void putImg(double value){putDouble(img, value);}

        public String toString(){
            return "(" + getReal() + "," + getImg() + "i)";

        }
    }
}
