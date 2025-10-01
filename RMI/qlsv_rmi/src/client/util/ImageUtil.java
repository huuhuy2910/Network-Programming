package client.util;

import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public final class ImageUtil {
    private ImageUtil() {
    }

    public static ImageIcon toIcon(BufferedImage image, int width, int height) {
        if (image == null) {
            return null;
        }
        BufferedImage scaled = scaleImage(image, width, height);
        return scaled != null ? new ImageIcon(scaled) : null;
    }

    public static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        if (image == null) {
            return null;
        }
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        double scale = Math.min((double) width / originalWidth, (double) height / originalHeight);
        int targetWidth = (int) Math.round(originalWidth * scale);
        int targetHeight = (int) Math.round(originalHeight * scale);

        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int x = (width - targetWidth) / 2;
        int y = (height - targetHeight) / 2;
        g2.setComposite(java.awt.AlphaComposite.Clear);
        g2.fillRect(0, 0, width, height);
        g2.setComposite(java.awt.AlphaComposite.Src);
        g2.drawImage(image, x, y, targetWidth, targetHeight, null);
        g2.dispose();
        return scaled;
    }
}
