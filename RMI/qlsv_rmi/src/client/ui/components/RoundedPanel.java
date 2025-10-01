package client.ui.components;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel {
    private int cornerRadius;
    private Color backgroundColor;

    public RoundedPanel(int radius, Color backgroundColor) {
        this.cornerRadius = radius;
        this.backgroundColor = backgroundColor;
        setOpaque(false);
    }

    public RoundedPanel() {
        this(16, new Color(255, 255, 255));
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        Shape shape = new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius);
        g2.setColor(backgroundColor != null ? backgroundColor : getBackground());
        g2.fill(shape);
        g2.dispose();
        super.paintComponent(g);
    }
}
