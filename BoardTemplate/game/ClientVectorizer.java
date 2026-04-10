package game;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class ClientVectorizer implements Runnable {

    private final String host = "localhost";
    private final int port = 5020;

    private volatile double[] receivedVectors = new double[]{ -1, -1, -1, -1 };
    private double[] lastReturned = new double[]{ -1, -1, -1, -1 };

    @Override
    public void run() {
        System.out.println("VectorClient started...");

        while (true) {
            try (Socket socket = new Socket(host, port)) {
                socket.setTcpNoDelay(true);
                socket.setKeepAlive(true);

                DataInputStream in = new DataInputStream(socket.getInputStream());

                while (true) {
                    int length = in.readInt();  // length prefix
                    double[] vector = new double[length];

                    for (int i = 0; i < length; i++) {
                        vector[i] = in.readDouble();
                    }

                    receivedVectors = vector; // atomic publish
                }

            } catch (EOFException e) {
                // server closed — immediate reconnect
            } catch (IOException e) {
                // server unreachable — retry instantly
            }
        }
    }

    public synchronized double[] getFeatureVector() {
        double[] current = receivedVectors.clone();

        if (Arrays.equals(current, lastReturned)) {
            return new double[]{ -1, -1, -1, -1 };
        }

        lastReturned = current;
        return current;
    }
}
