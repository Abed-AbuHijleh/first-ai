import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import java.io.*;

public class userDraw extends JFrame implements ActionListener, MouseListener {
    private static final long serialVersionUID = 1L;
    Color white = new Color(255, 255, 255);
    JLabel guess;
    JButton done, clear;
    JPanel panel = new JPanel(new BorderLayout());
    JPanel eastPan = new JPanel(new BorderLayout());
    NeuralNetwork network = new NeuralNetwork();
    Timer timer;
    BufferedImage bi;
    int radius = 20;
    int status = 2;

    public userDraw() {
        super("userDraw");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("User Draw");
        this.setBounds(200, 200, 500, 400);
        this.setResizable(false);
        //
        bi = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        timer = new Timer(1, this);
        guess = new JLabel("Guess: null");
        done = new JButton("Done");
        clear = new JButton("Clear");
        clear.addActionListener(this);
        done.addActionListener(this);
        network.read();
        repaint();
        //
        eastPan.add(done, BorderLayout.EAST);
        eastPan.add(guess, BorderLayout.NORTH);
        eastPan.add(clear, BorderLayout.SOUTH);
        panel.add(eastPan, BorderLayout.EAST);
        panel.addMouseListener(this);
        //
        this.setContentPane(panel);
        this.setVisible(true);
    }

    public static void main(String[] e) throws Exception {
        new userDraw();
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics gi = bi.getGraphics();
        gi.setColor(Color.black);
        if (status == 1) {
            gi.fillOval((int) MouseInfo.getPointerInfo().getLocation().getX() - 200,
                    (int) MouseInfo.getPointerInfo().getLocation().getY() - 200, 2 * radius, 2 * radius);
            gi.dispose();
        } else if (status == 2) {
            gi.setColor(Color.white);
            gi.fillRect(0, 0, 400, 400);
            gi.dispose();
        }
        g.drawLine(401, 0, 400, 400);
        g.drawImage(bi, 0, 0, null);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == done) {
            Double sectionVal = 0d;
            for (int i = 0; i < 28; i++) {
                for (int j = 0; j < 28; j++) {
                    network.segments[(i * 28) + j] = sectionVal;
                }
            }
            for (int i = 0; i < 784; i++) {
                network.data[i][3] = network.segments[i];
            }
            //
            // X segments
            for (int x = 0; x < 28; x++) {
                // Y segments
                for (int y = 0; y < 28; y++) {
                    // Micro Segments
                    network.segments[((x * 28) + y)] = 0d;
                    for (int i = 0; i < 14; i++) {
                        for (int j = 0; j < 14; j++) {
                            int clr = bi.getRGB(((x * 14) + i), ((y * 14) + j));
                            int value = (clr & 0x00ff0000) >> 16;
                            network.segments[((x * 28) + y)] += 255 - value;
                        }
                    }
                    // Average value
                    network.segments[((x * 28) + y)] = network.segments[((x * 28) + y)] / 196d;
                    // Sigmoid
                    network.segments[((x * 28) + y)] = Math.floor(
                            (1 / (1 + Math.pow(network.eVar, network.segments[((x * 28) + y)] * -1))) * 1000000000)
                            / 1000000000;
                }
            }
            Double[] answer = new Double[10];
            answer = network.networkRun(2, network.segments);
            int prdctn = 0;
            for (int m = 0; m < 10; m++) {
                if (answer[prdctn] < answer[m]) {
                    prdctn = m;
                }
            }
            guess.setText("Guess: " + prdctn);
        } else if (e.getSource() == timer) {
            repaint();
        } else if (e.getSource() == clear) {
            status = 2;
            repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
        status = 1;
        timer.start();
    }

    public void mouseReleased(MouseEvent e) {
        timer.stop();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

}