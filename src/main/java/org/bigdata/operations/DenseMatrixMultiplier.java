package org.bigdata.operations;

import org.bigdata.model.Matrix;

public class DenseMatrixMultiplier {
    public static Matrix multiply(Matrix matrixA, Matrix matrixB) {
        Matrix a = matrixA;
        Matrix b = matrixB;
        int[][] c = new int[a.size()][b.size()];

        for (int i = 0; i < a.size(); i++) {
            for (int k = 0; k < b.size(); k++) {
                int aik = a.getValue(i, k);
                for (int j = 0; j < a.size(); j++) {
                    c[i][j] += aik * b.getValue(k, j);
                }
            }
        }

        return new Matrix(a.size(), b.size(), c);
    }
}
