package org.saliya.tricks;

public abstract class AbstractAndOverrides {
    public static AbstractAndOverrides getMeANewInstance(int rows, int cols){
        int [][] array = new int[rows][];
        for (int i = 0; i < rows; ++i){
            array[i] = new int[cols];
            for (int j = 0; j < cols; ++j){
                array[i][j] = i*rows+j;
            }
        }
        return new AbstractAndOverrides() {
            @Override
            public double getValue(int row, int col) {
                return array[row][col];
            }
        };
    }
    public double getValue(int row, int col){
        throw new UnsupportedOperationException();
    }

}
