package org.bigdata.operations;

import org.bigdata.model.Matrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.util.List;

public class ParallelMatrixMultiplier {

    public static Matrix multiply(Matrix matrixA, Matrix matrixB) {
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        int divisor = SubmatrixesDivider.findDivisor(matrixA.getRowCount(), matrixA.getColumnCount(), numberOfThreads);
        int subMatrixSize = matrixA.getRowCount() / divisor;
        Matrix resultMatrix = new Matrix(matrixA.getRowCount(), matrixB.getColumnCount());

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < divisor; i++) {
            for (int j = 0; j < divisor; j++) {
                for (int k = 0; k < divisor; k++) {
                    int fI = i;
                    int fJ = j;
                    int fK = k;

                    Callable<?> task = () -> {
                        Matrix submatrixA = SubmatrixesDivider.extractSubmatrix(matrixA, fI * subMatrixSize, fK * subMatrixSize, subMatrixSize, subMatrixSize);
                        Matrix submatrixB = SubmatrixesDivider.extractSubmatrix(matrixB, fK * subMatrixSize, fJ * subMatrixSize, subMatrixSize, subMatrixSize);
                        multiplySubmatrices(submatrixA, submatrixB, resultMatrix, fI * subMatrixSize, fJ * subMatrixSize);
                        return null;
                    };
                    futures.add(executor.submit(task));
                }
            }
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        return resultMatrix;
    }

    private static void multiplySubmatrices(Matrix submatrixA, Matrix submatrixB, Matrix resultMatrix, int startRow, int startCol) {
        int size = submatrixA.getRowCount();
        int[][] tempResult = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    tempResult[i][j] += submatrixA.getValue(i, k) * submatrixB.getValue(k, j);
                }
            }
        }

        synchronized (resultMatrix) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    resultMatrix.setValue(startRow + i, startCol + j, resultMatrix.getValue(startRow + i, startCol + j) + tempResult[i][j]);
                }
            }
        }
    }

}
