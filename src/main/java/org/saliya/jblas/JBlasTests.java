package org.saliya.jblas;

import org.jblas.*;

import java.io.IOException;
import java.util.Arrays;

public class JBlasTests {
    public static void main(String[] args) throws IOException {
        int size = 4;
        double [][] mat = new double[size][size];
        initializeMatrix(size, size, mat);
        printMatrix(mat);

//        DoubleMatrix matrix = new DoubleMatrix(mat);
        DoubleMatrix matrix = DoubleMatrix.loadAsciiFile("src/main/resources/SquareMatA");
        matrix.print();

        ComplexDoubleMatrix eigenvalues = Eigen.eigenvalues(matrix);
        eigenvalues.print();
        int length = eigenvalues.length;
        for (int i = 0; i < length; ++i){
            ComplexDouble eigenvalue = eigenvalues.get(i);
            if (eigenvalue.isReal()){
                System.out.println(eigenvalue.real());
            }
        }

        DoubleMatrix A = DoubleMatrix.loadAsciiFile("src/main/resources/SquareMatB");
        DoubleMatrix B = DoubleMatrix.loadAsciiFile("src/main/resources/RectC");
        DoubleMatrix X = Solve.solve(A,B);
        System.out.println(X);

    }

    private static void initializeMatrix(int rows, int cols, double[][] mat) {
        for (int i = 0; i < rows; ++i){
            for (int j = 0; j < cols; ++j){
                mat[i][j] = Math.random();
            }
        }
    }

    public static void printMatrix(double [][] mat){
        int rows = mat.length;
        int cols = rows > 0 ? mat[0].length : 0;
        for (int i = 0; i < rows; ++i){
            for (int j = 0; j < cols; ++j){
                System.out.print(String.format("%.4f", mat[i][j]) + " ");
            }
            System.out.println();
        }
    }
}
