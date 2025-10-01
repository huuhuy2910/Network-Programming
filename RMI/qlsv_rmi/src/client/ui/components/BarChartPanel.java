package client.ui.components;

import client.util.UITheme;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simple reusable bar chart panel for categorical values.
 */
public class BarChartPanel extends JPanel {
    private final List<Map.Entry<String, Number>> data = new ArrayList<>();
    private String title;

    public BarChartPanel() {
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
        int padding = 32;
        int titleHeight = 0;

        if (title != null && !title.isBlank()) {
            g2.setFont(UITheme.subHeaderFont());
            g2.setColor(UITheme.TEXT_PRIMARY);
            g2.drawString(title, padding, padding);
            titleHeight = padding + g2.getFontMetrics().getHeight();
        }

        int chartTop = titleHeight + 8;
        int chartHeight = height - chartTop - padding;
        int chartLeft = padding;
        int chartWidth = width - padding * 2;

        g2.setColor(new Color(0xE2E8F0));
        g2.drawLine(chartLeft, chartTop + chartHeight, chartLeft + chartWidth, chartTop + chartHeight);

        if (data.isEmpty()) {
            g2.setFont(UITheme.bodyFont());
            g2.setColor(UITheme.TEXT_SECONDARY);
            g2.drawString("Không có dữ liệu", chartLeft, chartTop + chartHeight / 2);
            g2.dispose();
            return;
        }

        double max = data.stream()
                .map(Map.Entry::getValue)
                .mapToDouble(Number::doubleValue)
                .max()
                .orElse(1);
        if (max <= 0) {
            max = 1;
        }

        int barCount = data.size();
        int gap = 12;
        int barWidth = Math.max(20, (chartWidth - gap * (barCount - 1)) / barCount);

        Font labelFont = UITheme.smallFont();
        g2.setFont(labelFont);
        for (int i = 0; i < barCount; i++) {
            Map.Entry<String, Number> entry = data.get(i);
            double value = entry.getValue().doubleValue();
            double ratio = value / max;
            int barHeight = (int) (chartHeight * ratio);
            int x = chartLeft + i * (barWidth + gap);
            int y = chartTop + chartHeight - barHeight;

            Color barColor = interpolateColor(UITheme.PRIMARY, UITheme.ACCENT, barCount <= 1 ? 0 : (double) i / (barCount - 1));
            g2.setColor(barColor);
            g2.fillRoundRect(x, y, barWidth, barHeight, 12, 12);

            g2.setColor(new Color(0x25406F));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(x, y, barWidth, barHeight, 12, 12);

            String valueLabel = String.valueOf(entry.getValue());
            int valueWidth = g2.getFontMetrics().stringWidth(valueLabel);
            g2.setColor(UITheme.TEXT_PRIMARY);
            g2.drawString(valueLabel, x + (barWidth - valueWidth) / 2, y - 6);

            String category = entry.getKey();
            int categoryWidth = g2.getFontMetrics().stringWidth(category);
            int labelX = x + (barWidth - categoryWidth) / 2;
            int labelY = chartTop + chartHeight + g2.getFontMetrics().getHeight();
            g2.drawString(category, labelX, labelY);
        }

        g2.dispose();
    }

    private Color interpolateColor(Color start, Color end, double ratio) {
        ratio = Math.max(0, Math.min(1, ratio));
        int r = (int) (start.getRed() + (end.getRed() - start.getRed()) * ratio);
        int g = (int) (start.getGreen() + (end.getGreen() - start.getGreen()) * ratio);
        int b = (int) (start.getBlue() + (end.getBlue() - start.getBlue()) * ratio);
        return new Color(r, g, b);
    }
}
