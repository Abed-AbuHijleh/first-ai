import java.io.*;

public class MnistDataReader {
    public MnistMatrix[] readData(String dataFilePath, String labelFilePath) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(
                new BufferedInputStream(new FileInputStream(dataFilePath)));
        int magicNumber = dataInputStream.readInt();
        int numberOfItems = dataInputStream.readInt();
        int nRows = dataInputStream.readInt();
        int nCols = dataInputStream.readInt();

        // System.out.println("");
        // System.out.println("");
        // System.out.println("Magic Number: " + magicNumber);
        // System.out.println("Item Count: " + numberOfItems);
        // System.out.println("Rows: " + nRows);
        // System.out.println("Columns: " + nCols);

        DataInputStream labelInputStream = new DataInputStream(
                new BufferedInputStream(new FileInputStream(labelFilePath)));
        int labelMagicNumber = labelInputStream.readInt();
        int numberOfLabels = labelInputStream.readInt();

        // System.out.println("");
        // System.out.println("Label Magic Number: " + labelMagicNumber);
        // System.out.println("Label Count: " + numberOfLabels);
        // System.out.println("");
        // System.out.println("");

        MnistMatrix[] data = new MnistMatrix[numberOfItems];

        assert numberOfItems == numberOfLabels;

        for (int i = 0; i < numberOfItems; i++) {
            MnistMatrix mnistMatrix = new MnistMatrix(nRows, nCols);
            mnistMatrix.setLabel(labelInputStream.readUnsignedByte());
            for (int r = 0; r < nRows; r++) {
                for (int c = 0; c < nCols; c++) {
                    mnistMatrix.setValue(r, c, dataInputStream.readUnsignedByte());
                }
            }
            data[i] = mnistMatrix;
        }
        dataInputStream.close();
        labelInputStream.close();
        return data;
    }
}
