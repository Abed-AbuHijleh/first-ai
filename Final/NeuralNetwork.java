import java.io.*;

public class NeuralNetwork {

    //
    // Section Vars
    public Double eVar = 2.7182818284590452353602875;
    public int[] sets = new int[4];
    // Weights
    private Double[][] weightSetZero = new Double[49][10];
    private Double[][] weightSetOne = new Double[196][49];
    private Double[][] weightSetTwo = new Double[784][196];
    // Biases
    private Double[] biasSetZero = new Double[10];
    private Double[] biasSetOne = new Double[49];
    private Double[] biasSetTwo = new Double[196];
    // Activations and Costs
    // Only segements matters for running; rest are for BP
    public Double[][] data = new Double[784][4];
    public Double[] segments = new Double[784];
    public Double[] costs = new Double[196];

    public void read() {
        sets[3] = 784;
        sets[2] = 196;
        sets[1] = 49;
        sets[0] = 10;
        for (int i = 0; i < sets[2]; i++) {
            costs[i] = 0d;
        }

        BufferedReader f;
        try {
            f = new BufferedReader(new FileReader("networkData/WeightZero.txt"));
            for (int i = 0; i < sets[0]; i++) {
                for (int j = 0; j < sets[1]; j++) {
                    weightSetZero[j][i] = Double.parseDouble(f.readLine());
                }
            }
            f.close();
        } catch (IOException e) {
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            f = new BufferedReader(new FileReader("networkData/WeightOne.txt"));
            for (int i = 0; i < sets[1]; i++) {
                for (int j = 0; j < sets[2]; j++) {
                    weightSetOne[j][i] = Double.parseDouble(f.readLine());
                }
            }
            f.close();
        } catch (IOException e) {
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            f = new BufferedReader(new FileReader("networkData/WeightTwo.txt"));
            for (int i = 0; i < sets[2]; i++) {
                for (int j = 0; j < sets[3]; j++) {
                    weightSetTwo[j][i] = Double.parseDouble(f.readLine());
                }
            }
            f.close();
        } catch (IOException e) {
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            f = new BufferedReader(new FileReader("networkData/BiasZero.txt"));
            for (int j = 0; j < sets[0]; j++) {
                biasSetZero[j] = Double.parseDouble(f.readLine());
            }
            f.close();
        } catch (IOException e) {
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            f = new BufferedReader(new FileReader("networkData/BiasOne.txt"));
            for (int j = 0; j < sets[1]; j++) {
                biasSetOne[j] = Double.parseDouble(f.readLine());
            }
            f.close();
        } catch (IOException e) {
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            f = new BufferedReader(new FileReader("networkData/BiasTwo.txt"));
            for (int j = 0; j < sets[2]; j++) {
                biasSetTwo[j] = Double.parseDouble(f.readLine());
            }
            f.close();
        } catch (IOException e) {
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    //
    //
    //
    //
    //
    public void save() {
        try {
            PrintWriter g = new PrintWriter("networkData/WeightTwo.txt");
            for (int i = 0; i < sets[2]; i++) {
                for (int j = 0; j < sets[3]; j++) {
                    g.println(weightSetTwo[j][i]);
                }
            }
            g.close();
        } catch (IOException e) {
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            PrintWriter g = new PrintWriter("networkData/WeightOne.txt");
            for (int i = 0; i < sets[1]; i++) {
                for (int j = 0; j < sets[2]; j++) {
                    g.println(weightSetOne[j][i]);
                }
            }
            g.close();
        } catch (IOException e) {
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            PrintWriter g = new PrintWriter("networkData/WeightZero.txt");
            for (int i = 0; i < sets[0]; i++) {
                for (int j = 0; j < sets[1]; j++) {
                    g.println(weightSetZero[j][i]);
                }
            }
            g.close();
        } catch (IOException e) {
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            PrintWriter g = new PrintWriter("networkData/BiasTwo.txt");
            for (int i = 0; i < sets[2]; i++) {
                g.println(biasSetTwo[i]);
            }
            g.close();
        } catch (IOException e) {
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            PrintWriter g = new PrintWriter("networkData/BiasOne.txt");
            for (int i = 0; i < sets[1]; i++) {
                g.println(biasSetOne[i]);
            }
            g.close();
        } catch (IOException e) {
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            PrintWriter g = new PrintWriter("networkData/BiasZero.txt");
            for (int i = 0; i < sets[0]; i++) {
                g.println(biasSetZero[i]);
            }
            g.close();
        } catch (IOException e) {
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        System.out.println("Saved");
    }

    //
    //
    //
    //
    //
    public Double[] networkRun(int neuronSet, Double[] oldNeurons) {
        Double[] newNeurons = new Double[sets[neuronSet]];
        // Calculate new new neurons
        for (int i = 0; i < sets[neuronSet]; i++) {
            newNeurons[i] = 0d;
            for (int j = 0; j < sets[neuronSet + 1]; j++) {
                if (neuronSet == 2) {
                    newNeurons[i] += weightSetTwo[j][i] * oldNeurons[j];
                } else if (neuronSet == 1) {
                    newNeurons[i] += weightSetOne[j][i] * oldNeurons[j];
                } else if (neuronSet == 0) {
                    newNeurons[i] += weightSetZero[j][i] * oldNeurons[j];
                }
            }
            if (neuronSet == 2) {
                newNeurons[i] += biasSetTwo[i];
            } else if (neuronSet == 1) {
                newNeurons[i] += biasSetOne[i];
            } else if (neuronSet == 0) {
                newNeurons[i] += biasSetZero[i];
            }
            newNeurons[i] = Math.floor((1 / (1 + Math.pow(eVar, newNeurons[i] * -1))) * 1000000000) / 1000000000;
            data[i][neuronSet] = newNeurons[i];
        }

        if (neuronSet > 0) {
            newNeurons = networkRun(neuronSet - 1, newNeurons);
        }
        return newNeurons;
    }

    //
    //
    //
    //
    //
    public void backPropagation(int n) {
        for (int jNeuron = 0; jNeuron < sets[n]; jNeuron++) {
            Integer[] tempDes = new Integer[sets[n + 1]];
            Double[] tempDeriv = new Double[sets[n + 1]];
            Double derivBench = 0d;
            Double register;

            for (int kNeuron = 0; kNeuron < sets[n + 1]; kNeuron++) {
                //
                // Weights
                //
                // Calculate Derivitives
                register = (data[jNeuron][n] - costs[jNeuron]) * (data[jNeuron][n] - costs[jNeuron]);
                if (n == 0) {
                    weightSetZero[kNeuron][jNeuron] += 1;
                    networkRun(2, segments);
                    weightSetZero[kNeuron][jNeuron] -= 1;
                } else if (n == 1) {
                    weightSetOne[kNeuron][jNeuron] += 1;
                    networkRun(2, segments);
                    weightSetOne[kNeuron][jNeuron] -= 1;
                } else if (n == 2) {
                    weightSetTwo[kNeuron][jNeuron] += 1;
                    networkRun(2, segments);
                    weightSetTwo[kNeuron][jNeuron] -= 1;
                }
                tempDeriv[kNeuron] = (((data[jNeuron][n] - costs[jNeuron]) * (data[jNeuron][n] - costs[jNeuron]))
                        - register)
                        * (((data[jNeuron][n] - costs[jNeuron]) * (data[jNeuron][n] - costs[jNeuron])) - register);
                // Add to average count depending on negative or positive
                if (tempDeriv[kNeuron] < 0) {
                    derivBench -= tempDeriv[kNeuron];
                } else {
                    derivBench += tempDeriv[kNeuron];
                }
            }
            derivBench = derivBench / sets[n + 1];
            // Figure out cost
            for (int kNeuron = 0; kNeuron < sets[n + 1]; kNeuron++) {
                if (tempDeriv[kNeuron] < 0) {
                    if ((tempDeriv[kNeuron] * (-1)) > derivBench) {
                        tempDes[kNeuron] = 1;
                        costs[kNeuron] += 1;
                    } else {
                        tempDes[kNeuron] = 0;
                    }
                } else {
                    if (tempDeriv[kNeuron] > derivBench) {
                        tempDes[kNeuron] = 1;
                    } else {
                        tempDes[kNeuron] = 0;
                    }
                }
            }
            // Adjust Weights According to Costs
            for (int kNeuron = 0; kNeuron < sets[n + 1]; kNeuron++) {
                // WEIGHT DOWN
                if (data[jNeuron][n] > costs[jNeuron]) {
                    if (n == 0) {
                        weightSetZero[kNeuron][jNeuron] = ((weightSetZero[kNeuron][jNeuron] * 99) + tempDeriv[kNeuron])
                                / 100;
                    } else if (n == 1) {
                        weightSetOne[kNeuron][jNeuron] = ((weightSetOne[kNeuron][jNeuron] * 99) + tempDeriv[kNeuron])
                                / 100;
                        ;
                    } else if (n == 2) {
                        weightSetTwo[kNeuron][jNeuron] = ((weightSetTwo[kNeuron][jNeuron] * 99) + tempDeriv[kNeuron])
                                / 100;
                        ;
                    }
                }
                // WEIGHT UP
                else if (data[jNeuron][n] < costs[jNeuron]) {
                    if (n == 0) {
                        weightSetZero[kNeuron][jNeuron] = ((weightSetZero[kNeuron][jNeuron] * 99) - tempDeriv[kNeuron])
                                / 100;
                        ;
                    } else if (n == 1) {
                        weightSetOne[kNeuron][jNeuron] = ((weightSetOne[kNeuron][jNeuron] * 99) - tempDeriv[kNeuron])
                                / 100;
                        ;
                    } else if (n == 2) {
                        weightSetTwo[kNeuron][jNeuron] = ((weightSetTwo[kNeuron][jNeuron] * 99) - tempDeriv[kNeuron])
                                / 100;
                        ;
                    }
                } else {
                }
            }
            //
            // Biases
            //
            // Find Derivitives
            register = (data[jNeuron][n] - costs[jNeuron]) * (data[jNeuron][n] - costs[jNeuron]);
            if (n == 0) {
                biasSetZero[jNeuron] += 1;
                networkRun(2, segments);
                biasSetZero[jNeuron] -= 1;
            } else if (n == 1) {
                biasSetOne[jNeuron] += 1;
                networkRun(2, segments);
                biasSetOne[jNeuron] -= 1;
            } else if (n == 2) {
                biasSetTwo[jNeuron] += 1;
                networkRun(2, segments);
                biasSetTwo[jNeuron] -= 1;
            }
            // Adjust Appropiate J Bias

            // BIAS DOWN
            if (data[jNeuron][n] > costs[jNeuron]) {
                if (n == 0) {
                    biasSetZero[jNeuron] += (register
                            - (data[jNeuron][n] - costs[jNeuron]) * (data[jNeuron][n] - costs[jNeuron])) * 5;
                } else if (n == 1) {
                    biasSetOne[jNeuron] += (register
                            - (data[jNeuron][n] - costs[jNeuron]) * (data[jNeuron][n] - costs[jNeuron])) * 5;
                } else if (n == 2) {
                    biasSetTwo[jNeuron] += (register
                            - (data[jNeuron][n] - costs[jNeuron]) * (data[jNeuron][n] - costs[jNeuron])) * 5;
                }
            }
            // BIAS UP
            else if (data[jNeuron][n] < costs[jNeuron]) {
                if (n == 0) {
                    biasSetZero[jNeuron] -= (register
                            - (data[jNeuron][n] - costs[jNeuron]) * (data[jNeuron][n] - costs[jNeuron])) * 5;
                } else if (n == 1) {
                    biasSetOne[jNeuron] -= (register
                            - (data[jNeuron][n] - costs[jNeuron]) * (data[jNeuron][n] - costs[jNeuron])) * 5;
                } else if (n == 2) {
                    biasSetTwo[jNeuron] -= (register
                            - (data[jNeuron][n] - costs[jNeuron]) * (data[jNeuron][n] - costs[jNeuron])) * 5;
                }
            } else {
            }
        }

        if (n < 2) {
            for (int i = 0; i < sets[n + 1]; i++) {
                costs[i] = (data[i][n + 1] - (costs[i] / sets[n + 1])) * (data[i][n + 1] - (costs[i] / sets[n + 1]));
            }
            backPropagation(n + 1);
        }
    }
}
