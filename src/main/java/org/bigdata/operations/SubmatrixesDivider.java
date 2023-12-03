package org.bigdata.operations;

import org.bigdata.model.Matrix;

public class SubmatrixesDivider {

    public static Matrix[] divideMatrix(Matrix matrix) {
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        int rows = matrix.getRowCount();
        int cols = matrix.getColumnCount();

        // Find the largest divisor that is smaller than the number of rows/columns and divides the total size.
        int divisor = findDivisor(rows, cols, numberOfThreads);

        int submatrixRows = rows / divisor;
        int submatrixCols = cols / divisor;
        Matrix[] submatrices = new Matrix[divisor * divisor];

        for (int i = 0; i < divisor; i++) {
            for (int j = 0; j < divisor; j++) {
                submatrices[i * divisor + j] = extractSubmatrix(matrix, i * submatrixRows, j * submatrixCols, submatrixRows, submatrixCols);
            }
        }

        return submatrices;
    }

    public static Matrix extractSubmatrix(Matrix matrix, int startRow, int startCol, int numRows, int numCols) {
        Matrix submatrix = new Matrix(numRows, numCols);

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                submatrix.setValue(i, j, matrix.getValue(startRow + i, startCol + j));
            }
        }

        return submatrix;
    }

    public static int findDivisor(int rows, int cols, int numberOfThreads) {
        int maxDivisor = (int) Math.sqrt(numberOfThreads);
        for (int i = Math.min(maxDivisor, Math.min(rows, cols)); i > 1; i--) {
            if (rows % i == 0 && cols % i == 0) {
                return i;
            }
        }
        return 1;// If no suitable divisor is found, 1 is returned (the whole matrix as a single sub-matrix).
    }
}