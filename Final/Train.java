import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class Train extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    Double corrects = 0d;
    Double total = 0d;
    JPanel panel = new JPanel(new BorderLayout());
    JPanel eastPan = new JPanel(new BorderLayout());
    JPanel inner = new JPanel(new BorderLayout());
    JLabel succ, guessNumber, trueNumber;
    JButton button, save;
    NeuralNetwork network = new NeuralNetwork();

    public Train() {
        super("Train");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(200, 200, 500, 300);
        this.setTitle("train");
        this.setResizable(false);
        // Init Display
        succ = new JLabel("Success: null%;");
        guessNumber = new JLabel("Guess: null;\n");
        trueNumber = new JLabel("Answer: null;");
        button = new JButton("Run");
        save = new JButton("Save");
        save.addActionListener(this);
        button.addActionListener(this);
        // Append
        Color White = new Color(255, 255, 255);
        inner.add(guessNumber, BorderLayout.NORTH);
        inner.add(trueNumber, BorderLayout.SOUTH);
        eastPan.add(inner, BorderLayout.CENTER);
        eastPan.add(succ, BorderLayout.NORTH);
        eastPan.add(save, BorderLayout.SOUTH);
        panel.add(eastPan, BorderLayout.EAST);
        panel.add(button, BorderLayout.SOUTH);
        //
        eastPan.setBackground(White);
        inner.setBackground(White);
        panel.setBackground(White);
        this.setContentPane(panel);
        this.setVisible(true);
        network.read();
    }

    private static Double[] printMnistMatrix(final MnistMatrix matrix) {
        Double sections[] = new Double[784];
        int t = 0;
        for (int r = 0; r < matrix.getNumberOfRows(); r++) {
            for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
                int register = matrix.getValue(r, c);
                sections[t] = register / 255d;
                t++;
            }
        }
        return sections;
    }

    public static void main(String[] args) throws IOException {
        new Train();
    }

    public void paint(Graphics g) {
        super.paint(g);
        try {
            int x, y, val;
            for (int i = 1; i <= 28; i++) {
                y = (i * 7) + 20;
                for (int f = 1; f <= 28; f++) {
                    x = (f * 7) + 10;
                    val = 255 - ((int) Math.floor(network.segments[((i - 1) * 28) + (f - 1)] * 255));
                    Color color = new Color(val, val, val);
                    g.setColor(color);
                    g.fillRect(x, y, 7, 7);
                }
            }
        } catch (NullPointerException e) {
        }
    }

    public void run() {
        button.setText("Please Wait");
        try {
            MnistMatrix[] mnistMatrix = new MnistDataReader().readData("data/train-images-idx3-ubyte",
                    "data/train-labels-idx1-ubyte");
            Integer count = (int) Math.floor(Math.random() * mnistMatrix.length);
            System.out.println(mnistMatrix.length);
            // Get numerical values and draw image
            Double costDisplay = 0d;
            network.segments = printMnistMatrix(mnistMatrix[count]);
            repaint();
            // Get answer
            Double[] answer = new Double[10];
            answer = network.networkRun(2, network.segments);
            // Find guessed answer
            int guess = 0;
            for (int i = 0; i < 10; i++) {
                if (answer[guess] < answer[i]) {
                    guess = i;
                }
                // Input costs
                if (i == mnistMatrix[count].getLabel()) {
                    network.costs[i] = (1 - answer[i]) * (1 - answer[i]);
                    costDisplay += (answer[i] - 1) * (answer[i] - 1);
                } else {
                    network.costs[i] = answer[i] * answer[i];
                    costDisplay += answer[i] * answer[i];
                }
            }
            for (int i = 0; i < 784; i++) {
                network.data[i][3] = network.segments[i];
            }
            // Change text
            guessNumber.setText("Guess: " + guess + "\n");
            trueNumber.setText("Answer: " + mnistMatrix[count].getLabel());
            if (mnistMatrix[count].getLabel() == guess) {
                corrects++;
            }
            total++;
            succ.setText("Success: " + (Math.floor((corrects / total) * 10000d) / 100) + "%");
            // Call back prop
            System.out.println(costDisplay);
            network.backPropagation(0);
        } catch (IOException args) {
        }
        button.setText("Run");
    }

    public void actionPerformed(ActionEvent e) {
        // Network Run
        if (e.getSource() == button) {
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 10; i++) {
                    System.out.println((j * 10) + i + 1);
                    run();
                }
                network.save();
            }
        }
        // Network Save
        else if (e.getSource() == save) {
            network.save();
        }
    }
}