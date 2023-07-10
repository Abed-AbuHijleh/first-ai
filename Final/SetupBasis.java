import java.io.*;

public class SetupBasis {
    public static void main(String[] args) {
        run(10, 0, "networkData/BiasZero.txt");
        run(49, 0, "networkData/BiasOne.txt");
        run(196, 0, "networkData/BiasTwo.txt");
        run((49 * 10), 1, "networkData/WeightZero.txt");
        run((196 * 49), 1, "networkData/WeightOne.txt");
        run((784 * 196), 1, "networkData/WeightTwo.txt");
    }

    public static void run(int n, int type, String file) {
        int multiplier = 1;
        Double num;
        if (type == 0) {
            multiplier = 6;
        } else if (type == 1) {
            multiplier = 2;
        }
        //
        //
        //
        try {
            PrintWriter g = new PrintWriter(file);
            for (int i = 0; i < n; i++) {
                num = (Math.random() * multiplier) - (multiplier / 2);
                g.println(num);
            }
            g.close();
        } catch (IOException e) {
        }
    }
}