import org.bigdata.operations.DenseMatrixMultiplier;
import org.bigdata.model.Matrix;
import org.bigdata.generator.MatrixReader;
import org.bigdata.operations.ParallelMatrixMultiplier;

import org.junit.Test;

public class MatrixMultiplicationPerformanceTest {

    @Test
    public void comparePerformance() {
        int size = 1024;
        Matrix matrixA = new Matrix(size, size);
        Matrix matrixB = new Matrix(size, size);
        fillMatrixForTesting(matrixA);
        fillMatrixForTesting(matrixB);

        long startSequential = System.nanoTime();
        Matrix resultSequential = multiplySequentially(matrixA, matrixB);
        long endSequential = System.nanoTime();

        long startParallel = System.nanoTime();
        Matrix resultParallel = ParallelMatrixMultiplier.multiply(matrixA, matrixB);
        long endParallel = System.nanoTime();

        long timeSequential = endSequential - startSequential;
        long timeParallel = endParallel - startParallel;
        System.out.println("Sequential time: " + timeSequential / 1_000_000.0 + " ms");
        System.out.println("Parallel time: " + timeParallel / 1_000_000.0 + " ms");
    }

    @Test
    public void comparePerformanceOfSelectedMatrices() {
        System.out.println("Testing performance of selected matrices...");
        String[] fileNames = {"matrix_128.txt", "matrix_256.txt", "matrix_512.txt", "matrix_1024.txt"};

        for (String fileName : fileNames) {
            System.out.println("\nTesting performance for file: " + fileName);

            Matrix matrix = MatrixReader.readMatrixFromFile(fileName);

            long startSequential = System.nanoTime();
            Matrix resultSequential = multiplySequentially(matrix, matrix);
            long endSequential = System.nanoTime();
            long timeSequential = endSequential - startSequential;

            long startParallel = System.nanoTime();
            Matrix resultParallel = ParallelMatrixMultiplier.multiply(matrix, matrix);
            long endParallel = System.nanoTime();
            long timeParallel = endParallel - startParallel;

            System.out.println("Sequential time for" + fileName + ": " + timeSequential / 1_000_000.0 + " ms");
            System.out.println("Parallel time for" + fileName + ": " + timeParallel / 1_000_000.0 + " ms\n");
        }
    }

    private void fillMatrixForTesting(Matrix matrix) {
        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                matrix.setValue(i, j, i + j);
            }
        }
    }

    private Matrix multiplySequentially(Matrix matrixA, Matrix matrixB) {
        Matrix result = new Matrix(matrixA.getRowCount(), matrixB.getColumnCount());
        for (int i = 0; i < matrixA.getRowCount(); i++) {
            for (int j = 0; j < matrixB.getColumnCount(); j++) {
                for (int k = 0; k < matrixA.getColumnCount(); k++) {
                    result.setValue(i, j, result.getValue(i, j) + matrixA.getValue(i, k) * matrixB.getValue(k, j));
                }
            }
        }
        return result;
    }
}
