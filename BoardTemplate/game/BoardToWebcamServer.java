package game;
import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class BoardToWebcamServer {

    private final String host;
    private final int port;

    public BoardToWebcamServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void sendBoardFrame(BufferedImage boardImage) {
        try (Socket socket = new Socket(host, port)) {
            //System.out.println("Connected to WebcamServer at " + host + ":" + port);

            if (boardImage == null) {
                System.out.println("boardImage is NULL — nothing to send!");
                return;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedImage safe = ensureBuffered(boardImage);
            boolean ok = ImageIO.write(safe, "jpg", baos);
            baos.flush();
           // System.out.println("ImageIO.write returned: " + ok + ", bytes=" + baos.size());

            byte[] bytes = baos.toByteArray();
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        	   dos.writeInt(bytes.length);
        	   dos.write(bytes);
        	   dos.flush();

           

           // System.out.println("Board frame sent successfully (" + bytes.length + " bytes)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static BufferedImage ensureBuffered(BufferedImage src) {
        if (src == null) return null;

        // JPG needs TYPE_INT_RGB, not ARGB (no alpha channel)
        BufferedImage copy = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = copy.createGraphics();
        g2d.drawImage(src, 0, 0, null);
        g2d.dispose();
        return copy;
    }

}
