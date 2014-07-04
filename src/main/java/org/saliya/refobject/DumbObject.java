package org.saliya.refobject;

public class DumbObject {
    public int dumbness;
    public DumberObject dumberObject;

    public DumbObject(int dumbness, DumberObject dumberObject) {
        this.dumbness = dumbness;
        this.dumberObject = dumberObject;
    }

    @Override
    public String toString() {
        return "DumbObject{" +
                "dumbness=" + dumbness +
                ", dumberObject=" + dumberObject +
                '}';
    }
}
