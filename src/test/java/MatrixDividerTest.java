import org.bigdata.model.Matrix;
import org.bigdata.operations.SubmatrixesDivider;

import org.junit.Test;
import static org.junit.Assert.*;

public class MatrixDividerTest {

    @Test
    public void testMatrixDivision() {
        int size = 6;
        Matrix matrix = new Matrix(size, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix.setValue(i, j, i * size + j);
            }
        }

        Matrix[] submatrices = SubmatrixesDivider.divideMatrix(matrix);

        int divisor = findDivisor(size, size, Runtime.getRuntime().availableProcessors());
        int submatrixSize = size / divisor;

        System.out.println("Divided into" + submatrices.length + " submatrices of" + submatrixSize + "x" + submatrixSize);

        int expectedNumberOfSubmatrices = divisor * divisor;
        assertEquals(expectedNumberOfSubmatrices, submatrices.length);

        for (Matrix submatrix : submatrices) {
            assertNotNull(submatrix);}
    }

    private static int findDivisor(int rows, int cols, int numberOfThreads) {
        int maxDivisor = (int) Math.sqrt(numberOfThreads);
        for (int i = Math.min(maxDivisor, Math.min(rows, cols)); i > 1; i--) {
            if (rows % i == 0 && cols % i == 0) {
                return i;
            }
        }
        return 1;
    }
}