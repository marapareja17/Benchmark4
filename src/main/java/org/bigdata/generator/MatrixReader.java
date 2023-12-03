package org.bigdata.generator;

import org.bigdata.model.Matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MatrixReader {

    private static final String BASE_DIRECTORY = "src/main/resources/matrixes/";

    public static Matrix readMatrixFromFile(String fileName) {
        File file = new File(BASE_DIRECTORY + fileName);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int rowCount = 0;
            int colCount = 0;
            int[][] data = null;

            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split("\\s+");

                if (data == null) {
                    colCount = values.length;
                    data = new int[colCount][colCount];
                }

                for (int col = 0; col < values.length; col++) {
                    data[rowCount][col] = Integer.parseInt(values[col]);
                }

                rowCount++;
            }

            return new Matrix(rowCount, colCount, data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}