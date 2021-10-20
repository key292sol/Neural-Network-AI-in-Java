import ai.NeuralNetwork;
import test.draw_network.DrawNetFrame;
import test.draw_xor.DrawXorFrame;

public class App {
    public static void main(String[] args) throws Exception {
        // lineTest();
        // orTest();
        // drawNN();
        // xorTest();
        drawXor();
    }

    private static void drawXor() {
        new DrawXorFrame();
    }

    private static void orTest() {
        double[][] inputs = {
            {1, 0},
            {0, 1}, 
            {1, 1},
            {0, 0},
        };

        double[][] outputs = { {1}, {1}, {1}, {0} };

        NeuralNetwork nn = new NeuralNetwork(2, 1, 1, 2, 1);

        for (int i = 0; i < 1000; i++) {
            int j = (int) (Math.random() * 4);
            // j = 1;
            nn.train(inputs[j], outputs[j]);
            // nn.train(inputs, outputs);
        }

        System.out.println("----------------------------------");
        for (int i = 0; i < inputs.length; i++) {
            double[] guess = nn.feedForward(inputs[i]);
            System.out.println(outputs[i][0] + "\t" + guess[0]);
        }
    }

    private static void xorTest() {
        double[][] inputs = {
            {0, 0},
            {0, 1}, 
            {1, 0},
            {1, 1}
        };

        double[][] outputs = {
            {0}, {1}, {1}, {0}
        };

        NeuralNetwork nn = new NeuralNetwork(2, 1, 1, 2, 0.6);

        System.out.println("Before training: ");
        for (int i = 0; i < inputs.length; i++) {
            double[] guess = nn.feedForward(inputs[i]);
            System.out.println(outputs[i][0] + "\t" + guess[0]);
        }

        System.out.println("----------------------------------");

        for (int i = 0; i < 500; i++) {
            int j = (int) (Math.random() * 3);
            // j = 1;
            // nn.train(inputs[j], outputs[j]);
            nn.train(inputs, outputs);
        }

        System.out.println("After training: ");
        for (int i = 0; i < inputs.length; i++) {
            double guess = nn.feedForward(inputs[i])[0];
            double op = outputs[i][0];
            System.out.println(op + "\t" + guess);
            // System.out.println(op + "\t" + guess + "\t" + (op - guess));
        }
    }

    public static void lineTest() {
        new MyFrame();
    }

    public static void drawNN() {
        new DrawNetFrame();
    }


    private static void dispArray(double[] a) {
        for (double d : a) {
            System.out.print(d + " \t");
        }
        System.out.println();
    }

    private static void dispMatrix(double[][] m) {
        // System.out.println(Arrays.deepToString(m).replace("], ", "]\n"));
        for (double[] ds : m) {
            for (double d : ds) {
                System.out.print(d + " \t");
            }
            System.out.println();
        }
        System.out.println();
    }

}
