package org.bigdata.model;

public class Matrix {
    private final int rows;
    private final int cols;
    private final int[][] data;

    // Constructor existente
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new int[rows][cols];
    }

    public Matrix(int rows, int cols, int[][] data) {
        this.rows = rows;
        this.cols = cols;
        this.data = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, this.data[i], 0, cols);
        }
    }

    public void setValue(int row, int col, int value) {
        data[row][col] = value;
    }

    public int getValue(int row, int col) {
        return data[row][col];
    }

    public int getRowCount() {
        return rows;
    }

    public int getColumnCount() {
        return cols;
    }

    public static void printMatrix(Matrix matrix) {
        int rows = matrix.getRowCount();
        int cols = matrix.getColumnCount();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix.getValue(i, j) + " ");
            }
            System.out.println();
        }
    }

    public int size() {
        return rows;
    }
}
