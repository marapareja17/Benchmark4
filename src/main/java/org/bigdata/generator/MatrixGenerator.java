package org.bigdata.generator;

import org.bigdata.model.Matrix;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MatrixGenerator {
    public static void main(String[] args) {
        generateAndSaveMatrix(4);
        generateAndSaveMatrix(128);
        generateAndSaveMatrix(256);
        generateAndSaveMatrix(512);
        generateAndSaveMatrix(1024);
    }

    public static void generateAndSaveMatrix(int size) {
        Matrix generatedMatrix = generateMatrix(size);
        System.out.println("Matriz generada de tamaño " + size);
        saveMatrixToFile(generatedMatrix, size);
    }

    public static Matrix generateMatrix(int size) {
        Matrix matrix = new Matrix(size, size);
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix.setValue(i, j, random.nextInt(10));
            }
        }

        return matrix;
    }

    public static void saveMatrixToFile(Matrix matrix, int size) {
        String fileName = "matrix_" + size + ".txt";

        File directory = new File("src/main/resources/matrixes/");

        try (FileWriter writer = new FileWriter(new File(directory, fileName))) {
            int rows = matrix.getRowCount();
            int cols = matrix.getColumnCount();

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    writer.write(matrix.getValue(i, j) + " ");
                }
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}