package org.saliya.refobject;

public class DumberObject {
    public int dumbness;

    public DumberObject(int dumbness) {
        this.dumbness = dumbness;
    }

    @Override
    public String toString() {
        return "DumberObject{" +
                "dumbness=" + dumbness +
                '}';
    }
}
