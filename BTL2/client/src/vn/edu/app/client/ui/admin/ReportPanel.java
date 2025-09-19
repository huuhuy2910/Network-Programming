package vn.edu.app.client.ui.admin;

import vn.edu.app.client.remote.RMIConnector;
import vn.edu.app.client.util.UIUtils;
import vn.edu.app.common.remote.ReportService;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportPanel extends JPanel {
    private BarChartPanel chart = new BarChartPanel();

    public ReportPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // ===== TOP BAR =====
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(new Color(236, 240, 241));

        JButton btnClassByMajor   = styledBtn("Classes by Major",  "icons/reports.png", new Color(52, 152, 219));
        JButton btnStudentByMajor = styledBtn("Students by Major", "icons/students.png", new Color(46, 204, 113));
        JButton btnStudentByStatus= styledBtn("Students by Status","icons/status.png",   new Color(241, 196, 15));

        top.add(btnClassByMajor);
        top.add(btnStudentByMajor);
        top.add(btnStudentByStatus);

        add(top, BorderLayout.NORTH);
        add(chart, BorderLayout.CENTER);

        // ===== EVENTS =====
        btnClassByMajor.addActionListener(e -> load("Classes by Major", 1));
        btnStudentByMajor.addActionListener(e -> load("Students by Major", 2));
        btnStudentByStatus.addActionListener(e -> load("Students by Status", 3));

        load("Students by Major", 2);
    }

    private JButton styledBtn(String text, String icon, Color color) {
        JButton btn = new JButton(text, UIUtils.icon(icon, 18,18));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }

    private void load(String title, int mode) {
        try {
            ReportService svc = RMIConnector.report();
            Map<String,Integer> data = switch (mode) {
                case 1 -> svc.countClassesByMajor();
                case 2 -> svc.countStudentsByMajor();
                case 3 -> svc.countStudentsByStatus();
                default -> new LinkedHashMap<>();
            };
            chart.setData(title, data);
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.error(this, "Load report failed: " + e.getMessage());
        }
    }

    // ===== Simple Bar Chart =====
    static class BarChartPanel extends JPanel {
        private String title = "";
        private Map<String,Integer> data = new LinkedHashMap<>();

        public void setData(String title, Map<String,Integer> data) {
            this.title = title;
            this.data = data != null ? data : new LinkedHashMap<>();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            g.setColor(new Color(250,252,255));
            g.fillRect(0,0,w,h);

            // Title
            g.setColor(new Color(44,62,80));
            g.setFont(getFont().deriveFont(Font.BOLD, 18f));
            g.drawString(title, 16, 28);

            if (data.isEmpty()) {
                g.drawString("No data", 16, 56);
                return;
            }

            int left=70, right=30, top=60, bottom=50;
            int gw = w - left - right, gh = h - top - bottom;

            int max = data.values().stream().mapToInt(i->i).max().orElse(1);
            int n = data.size();
            int barW = Math.max(30, gw / Math.max(1, n*2));

            int i=0;
            int x = left + (gw - n*(barW+20)) / 2;

            for (var e : data.entrySet()) {
                int val = e.getValue();
                int barH = (int) ((val * 1.0 / max) * (gh - 20));
                int y = top + gh - barH;

                // Gradient màu cho bar
                Color base = new Color(52+(i*40)%180, 100+(i*30)%120, 200-(i*50)%120);
                GradientPaint gp = new GradientPaint(x, y, base.brighter(), x, y+barH, base.darker());
                g.setPaint(gp);
                g.fillRoundRect(x, y, barW, barH, 10,10);

                // Viền bar
                g.setColor(new Color(50,50,70,120));
                g.drawRoundRect(x, y, barW, barH, 10,10);

                // Giá trị
                g.setColor(new Color(30,30,30));
                g.setFont(getFont().deriveFont(Font.BOLD, 13f));
                String valStr = String.valueOf(val);
                int valW = g.getFontMetrics().stringWidth(valStr);
                g.drawString(valStr, x + (barW - valW)/2, y - 6);

                // Label
                g.setFont(getFont().deriveFont(Font.PLAIN, 12f));
                String label = shorten(e.getKey(), 15);
                int lw = g.getFontMetrics().stringWidth(label);
                g.drawString(label, x + (barW-lw)/2, top+gh+20);

                x += barW + 40;
                i++;
            }

            // Trục X-Y
            g.setColor(new Color(180,190,210));
            g.drawLine(left, top+gh, w-right, top+gh);
            g.drawLine(left, top, left, top+gh);
        }

        private String shorten(String s, int max) {
            return (s!=null && s.length()>max) ? s.substring(0, max-1) + "…" : s;
        }
    }
}
