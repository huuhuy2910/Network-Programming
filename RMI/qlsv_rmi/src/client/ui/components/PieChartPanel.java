package client.ui.components;

import client.util.UITheme;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simple pie chart component for two or more categories.
 */
public class PieChartPanel extends JPanel {
    private final List<Map.Entry<String, Number>> data = new ArrayList<>();
    private final DecimalFormat percentFormat = new DecimalFormat("0.0%");
    private String title;

    public PieChartPanel() {
        setOpaque(false);
    }

    public void setTitle(String title) {
        this.title = title;
        repaint();
    }

    public void setData(Map<String, ? extends Number> values) {
        data.clear();
        if (values != null) {
            for (Map.Entry<String, ? extends Number> entry : values.entrySet()) {
                data.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 24;
        int titleHeight = 0;

        if (title != null && !title.isBlank()) {
            g2.setFont(UITheme.subHeaderFont());
            g2.setColor(UITheme.TEXT_PRIMARY);
            g2.drawString(title, padding, padding + g2.getFontMetrics().getAscent());
            titleHeight = padding + g2.getFontMetrics().getHeight();
        }

        double total = data.stream().mapToDouble(entry -> entry.getValue().doubleValue()).sum();
        if (total <= 0) {
            g2.setColor(UITheme.TEXT_SECONDARY);
            g2.setFont(UITheme.bodyFont());
            g2.drawString("Không có dữ liệu", padding, height / 2);
            g2.dispose();
            return;
        }

        int diameter = Math.min(width - padding * 2, height - padding * 2 - titleHeight);
        int x = padding;
        int y = titleHeight + padding;

        float startAngle = 90f;
        Color[] palette = buildPalette(data.size());
        Font labelFont = UITheme.smallFont();
        g2.setFont(labelFont);

        for (int i = 0; i < data.size(); i++) {
            Map.Entry<String, Number> entry = data.get(i);
            double value = entry.getValue().doubleValue();
            float angle = (float) (value / total * 360f);
            g2.setColor(palette[i % palette.length]);
            g2.fill(new Arc2D.Double(x, y, diameter, diameter, startAngle, -angle, Arc2D.PIE));
            startAngle -= angle;
        }

        // Legend
        int legendX = x + diameter + 24;
        int legendY = y;
        int legendBoxSize = 14;
        int legendSpacing = 18;

        for (int i = 0; i < data.size(); i++) {
            Map.Entry<String, Number> entry = data.get(i);
            double value = entry.getValue().doubleValue();
            double ratio = value / total;
            g2.setColor(palette[i % palette.length]);
            g2.fillRoundRect(legendX, legendY + i * legendSpacing, legendBoxSize, legendBoxSize, 6, 6);
            g2.setColor(UITheme.TEXT_PRIMARY);
            String label = entry.getKey() + " - " + percentFormat.format(ratio);
            g2.drawString(label, legendX + legendBoxSize + 8, legendY + i * legendSpacing + legendBoxSize);
        }

        g2.dispose();
    }

    private Color[] buildPalette(int size) {
        Color base = UITheme.PRIMARY;
        Color accent = UITheme.ACCENT;
        Color success = UITheme.SUCCESS;
        Color dark = UITheme.PRIMARY_DARK;
        Color[] defaults = new Color[]{base, accent, success, new Color(0x10B981), new Color(0xF59E0B), dark};
        if (size <= defaults.length) {
            Color[] palette = new Color[size];
            System.arraycopy(defaults, 0, palette, 0, size);
            return palette;
        }
        Color[] palette = new Color[size];
        for (int i = 0; i < size; i++) {
            double ratio = (double) i / Math.max(1, size - 1);
            palette[i] = interpolateColor(base, accent, ratio);
        }
        return palette;
    }

    private Color interpolateColor(Color start, Color end, double ratio) {
        ratio = Math.max(0, Math.min(1, ratio));
        int r = (int) (start.getRed() + (end.getRed() - start.getRed()) * ratio);
        int g = (int) (start.getGreen() + (end.getGreen() - start.getGreen()) * ratio);
        int b = (int) (start.getBlue() + (end.getBlue() - start.getBlue()) * ratio);
        return new Color(r, g, b);
    }
}
