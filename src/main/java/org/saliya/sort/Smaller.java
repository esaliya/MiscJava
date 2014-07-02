package org.saliya.sort;

class Smaller implements Comparable<Smaller>{
    int index;
    double value;

    @Override
    public int compareTo(Smaller o) {
        return Integer.compare(index, o.index);
    }

    @Override
    public String toString() {
        return "Smaller{" +
                "index=" + index +
                ", value=" + value +
                '}';
    }
}
