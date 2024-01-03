import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import ai.NeuralNetwork;
import ai.NeuralNetwork.OpType;
import test.draw_xor.DrawXorFrame;

public class App {
    public static void main(String[] args) throws Exception {
        // orTest();
        // xorTest();
        // drawXor();
        absenteeismTest();
    }

    private static double[][] read_csv_double(String filename) {
        List<String[]> rows = null;
        try (Scanner sc = new Scanner(new File(new App().getClass().getResource(filename).toURI()))) {
            rows = new ArrayList<String[]>();
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                rows.add(line.split(","));
            }

            double[][] data = new double[rows.size()][];
            int n = rows.get(0).length;
            for (int i = 0; i < data.length; i++) {
                data[i] = new double[n];
                for (int j = 0; j < n; j++) {
                    data[i][j] = Double.parseDouble(rows.get(i)[j]);
                }
            }
            return data;
        } catch (FileNotFoundException e) {
            System.err.println(e);
            // e.printStackTrace();
        } catch (URISyntaxException e) {
            System.err.println(e);
        }
        return null;
    }

    private static void absenteeismTest() {
        double[][] inputs = read_csv_double("/data_files/Absenteeism_inputs.csv");
        double[][] targets = read_csv_double("/data_files/Absenteeism_targets.csv");

        // Shuffle
        int index;
        double[] temp;
        Random random = new Random();
        for (int i = inputs.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);

            temp = inputs[index];
            inputs[index] = inputs[i];
            inputs[i] = temp;

            temp = targets[index];
            targets[index] = targets[i];
            targets[i] = temp;
        }

        // Train Test Split
        int train_size = (int) Math.round(0.8 * inputs.length);
        double[][] xtrain = new double[train_size][],
                ytrain = new double[train_size][],
                xtest = new double[inputs.length - train_size][],
                ytest = new double[inputs.length - train_size][];
        for (int i = 0; i < inputs.length; i++) {
            if (i < train_size) {
                xtrain[i] = inputs[i];
                ytrain[i] = targets[i];
            } else {
                xtest[i - train_size] = inputs[i];
                ytest[i - train_size] = targets[i];
            }
        }

        NeuralNetwork nn = new NeuralNetwork(14, 1, 2, 100, OpType.BOOL_CLASSIFICATION, 0.5);
        for (int i = 0; i < 150; i++) {
            // System.out.print(i + " ");
            nn.train(xtrain, ytrain);
        }

        // System.out.println();
        double acc = nn.test(xtest, ytest);
        System.out.println("Absenteeism Accuracy = " + (Math.round(acc * 10000) / 100.0) + "%");
    }

    private static void drawXor() {
        new DrawXorFrame();
    }

    private static void xorTest() {
        double[][] inputs = {
                { 1, 0 },
                { 0, 1 },
                { 1, 1 },
                { 0, 0 },
        };

        double[][] outputs = { { 1 }, { 1 }, { 0 }, { 0 } };

        NeuralNetwork nn = new NeuralNetwork(2, 1, 1, 3, NeuralNetwork.OpType.BOOL_CLASSIFICATION, 0.3);

        for (int i = 0; i < 10000; i++) {
            int j = (int) (Math.random() * 4);
            // j = 1;̥
            nn.train(inputs[j], outputs[j]);
            // nn.train(inputs, outputs);
        }

        System.out.println("---------------------------------------------");
        int correct = 0;
        for (int i = 0; i < inputs.length; i++) {
            double[] guess = nn.feedForward(inputs[i]);
            double[] p = nn.predict(inputs[i]);
            if (p[0] == outputs[i][0])
                correct++;
            System.out.println(
                    inputs[i][0] + "\t" + inputs[i][1] + "\t|\t" + outputs[i][0] + "\t|\t" + guess[0] + "\t|\t" + p[0]);
        }
        System.out.println("Accuracy = " + (correct / 4.0));
    }

    private static void orTest() {
        double[][] inputs = {
                { 1, 0 },
                { 0, 1 },
                { 1, 1 },
                { 0, 0 },
        };

        double[][] outputs = { { 1 }, { 1 }, { 1 }, { 0 } };

        NeuralNetwork nn = new NeuralNetwork(2, 1, 1, 2, NeuralNetwork.OpType.BOOL_CLASSIFICATION, 1);

        for (int i = 0; i < 1000; i++) {
            int j = (int) (Math.random() * 4);
            // j = 1;
            nn.train(inputs[j], outputs[j]);
            // nn.train(inputs, outputs);
        }

        System.out.println("---------------------------------------------");
        for (int i = 0; i < inputs.length; i++) {
            double[] guess = nn.predict(inputs[i]);
            System.out.println(inputs[i][0] + "\t" + inputs[i][1] + "\t|\t" + outputs[i][0] + "\t|\t" + guess[0]);
        }
    }

    private static void dispArray(double[] a) {
        for (double d : a) {
            System.out.print(d + " \t");
        }
        System.out.println();
    }

    private static void dispMatrix(double[][] m) {
        for (double[] ds : m) {
            for (double d : ds) {
                System.out.print(d + " \t");
            }
            System.out.println();
        }
        System.out.println();
    }

}
