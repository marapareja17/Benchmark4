import org.bigdata.operations.DenseMatrixMultiplier;
import org.bigdata.model.Matrix;
import org.bigdata.generator.MatrixReader;
import org.bigdata.operations.ParallelMatrixMultiplier;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParallelMatrixMultiplierTest {

    @Test
    public void testMatrixMultiplication() {
        System.out.println("Testing matrix multiplication correct result...");
        Matrix matrixA = new Matrix(3, 3);
        Matrix matrixB = new Matrix(3, 3);
        fillMatrixForTesting(matrixA);
        fillMatrixForTesting(matrixB);

        Matrix expected = DenseMatrixMultiplier.multiply(matrixA, matrixB);
        Matrix result = ParallelMatrixMultiplier.multiply(matrixA, matrixB);

        // Check if the results are the same
        for (int i = 0; i < matrixA.getRowCount(); i++) {
            for (int j = 0; j < matrixB.getColumnCount(); j++) {
                assertEquals(expected.getValue(i, j), result.getValue(i, j), 0.01);
            }
        }
        System.out.println("Test passed!");
    }

    @Test
    public void testSelectedMatrices() {
        System.out.println("Testing selected matrices...\n");
        String[] fileNames = {"matrix_128.txt", "matrix_256.txt", "matrix_512.txt", "matrix_1024.txt"};

        for (String fileName : fileNames) {
            System.out.println("Testing file: " + fileName);

            Matrix matrix = MatrixReader.readMatrixFromFile(fileName);
            assertNotNull(matrix);

            Matrix resultSequential = multiplySequentially(matrix, matrix);
            Matrix resultParallel = ParallelMatrixMultiplier.multiply(matrix, matrix);

            for (int i = 0; i < matrix.getRowCount(); i++) {
                for (int j = 0; j < matrix.getColumnCount(); j++) {
                    assertEquals(resultSequential.getValue(i, j), resultParallel.getValue(i, j), 0.01);
                }
            }
            System.out.println("Test passed!\n");
        }
    }

    private void fillMatrixForTesting(Matrix matrix) {
        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                matrix.setValue(i, j, i + j); // Un ejemplo simple de valores para la matriz
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

    private void printMatrix(Matrix matrix) {
        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                System.out.print(matrix.getValue(i, j) + " ");
            }
            System.out.println();
        }
    }
}