package game;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class BoardSnapshot {

    // 1) Capture exactly what the label is showing (full label area)
    public static BufferedImage captureLabel(JLabel imageForSeed) {
        int w = imageForSeed.getWidth();
        int h = imageForSeed.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        // High-quality rasterization
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        imageForSeed.printAll(g); // snapshot the component
        g.dispose();
        return out;
    }

    // 2) Capture just the chessboard area (cropped), if you know tile size & offsets
    //    Pass the SAME xOffset/yOffset/tileSize you use when drawing tiles.
    public static BufferedImage captureBoardRegion(JLabel imageForSeed, int tileSize, double xOffset, double yOffset) {
        // The board you draw is 8x8 tiles at (xOffset, yOffset)
        double boardLeft = xOffset;
        double boardTop  = yOffset;
        double boardW    = 8 * tileSize;
        double boardH    = 8 * tileSize;

        // First capture full label
        BufferedImage full = captureLabel(imageForSeed);

        // Clamp to avoid AIOOB if offsets are slightly off
        boardLeft = Math.max(0, Math.min(boardLeft, full.getWidth()  - 1));
        boardTop  = Math.max(0, Math.min(boardTop,  full.getHeight() - 1));
        boardW    = Math.max(1, Math.min(boardW,    full.getWidth()  - boardLeft));
        boardH    = Math.max(1, Math.min(boardH,    full.getHeight() - boardTop));

        return full.getSubimage((int)boardLeft, (int)boardTop, (int)boardW, (int)boardH);
    }

    // Helper to save (for quick testing)
    public static void savePng(BufferedImage img, String filePath) {
        try {
            javax.imageio.ImageIO.write(img, "png", new java.io.File(filePath));
            System.out.println("Saved: " + filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
