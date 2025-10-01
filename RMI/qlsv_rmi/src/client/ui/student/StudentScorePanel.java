package client.ui.student;

import client.ui.components.RoundedPanel;
import client.util.AcademicStandingUtil;
import client.util.DialogUtil;
import client.util.UITheme;
import common.dto.Diem;
import common.dto.HocKy;
import common.dto.HocPhan;
import common.dto.NamHoc;
import common.dto.SinhVien;
import common.service.DiemService;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentScorePanel extends JPanel {
    private final SinhVien sinhVien;
    private final DiemService diemService;
    private final JComboBox<HocKy> hocKyCombo = new JComboBox<>();
    private final JComboBox<NamHoc> namHocCombo = new JComboBox<>();
    private final JLabel overallGpaLabel = new JLabel("GPA tích lũy: 0.00");
    private final JLabel filteredSummaryLabel = new JLabel("Tổng tín chỉ: 0 | GPA bộ lọc: - | Học lực: -");
    private final JLabel overallRankLabel = new JLabel("Học lực: -");
    private final DefaultTableModel tableModel;
    private final JTable table;

    private List<Diem> allScores = new ArrayList<>();

    public StudentScorePanel(SinhVien sinhVien, DiemService diemService) {
        this.sinhVien = sinhVien;
        this.diemService = diemService;
        this.tableModel = new DefaultTableModel(new Object[]{"Mã học phần", "Tên học phần", "Tín chỉ", "Điểm quá trình", "Điểm thi", "Điểm tổng", "Học kỳ", "Năm học"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.table = new JTable(tableModel);
        setOpaque(false);
        setLayout(new BorderLayout(16, 16));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
        loadScores();
    }

    private void buildUI() {
        RoundedPanel headerCard = new RoundedPanel(22, UITheme.CARD_BACKGROUND);
        headerCard.setLayout(new BorderLayout(16, 12));
        headerCard.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel("Bảng điểm cá nhân");
        title.setFont(UITheme.subHeaderFont());
        title.setForeground(UITheme.TEXT_PRIMARY);
        headerCard.add(title, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 6));
        filterPanel.setOpaque(false);
        filterPanel.add(createFilterGroup("Học kỳ", hocKyCombo));
        filterPanel.add(createFilterGroup("Năm học", namHocCombo));
        headerCard.add(filterPanel, BorderLayout.EAST);

        add(headerCard, BorderLayout.NORTH);

    RoundedPanel statsPanel = new RoundedPanel(18, new Color(0xF0F7FF));
        statsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 18, 10));
        statsPanel.setBorder(new EmptyBorder(12, 18, 12, 18));
        styleStatLabel(overallGpaLabel);
        styleStatLabel(overallRankLabel);
        styleStatLabel(filteredSummaryLabel);
        statsPanel.add(overallGpaLabel);
        statsPanel.add(overallRankLabel);
        statsPanel.add(filteredSummaryLabel);

    JPanel contentPanel = new JPanel(new BorderLayout(16, 16));
    contentPanel.setOpaque(false);
    contentPanel.add(statsPanel, BorderLayout.NORTH);

        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.setFont(UITheme.bodyFont());
        table.setGridColor(new Color(0xE5E9F2));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(UITheme.smallFont().deriveFont(Font.BOLD));
        table.getTableHeader().setForeground(UITheme.TEXT_PRIMARY);
        table.getTableHeader().setBackground(new Color(0xEFF3FF));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UITheme.CARD_BACKGROUND);

    RoundedPanel tableWrapper = new RoundedPanel(24, UITheme.CARD_BACKGROUND);
        tableWrapper.setLayout(new BorderLayout());
        tableWrapper.setBorder(new EmptyBorder(16, 16, 16, 16));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);

    contentPanel.add(tableWrapper, BorderLayout.CENTER);
    add(contentPanel, BorderLayout.CENTER);
    }

    private void loadScores() {
        if (diemService == null) {
            DialogUtil.showError(this, "Không có kết nối tới dịch vụ điểm.");
            return;
        }
        try {
            allScores = diemService.getBySinhVien(sinhVien.getSvId());
            updateTable(allScores);
            double overallGpa = diemService.calculateGpa(sinhVien.getSvId());
            String gpaText = Double.isNaN(overallGpa) ? "-" : String.format("%.2f", overallGpa);
            overallGpaLabel.setText("GPA tích lũy: " + gpaText);
            overallRankLabel.setText("Học lực: " + AcademicStandingUtil.classifyByGpa(Double.isNaN(overallGpa) ? null : overallGpa));
            populateFilters();
            updateFilteredSummary(allScores);
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể tải bảng điểm: " + e.getMessage());
        }
    }

    private void populateFilters() {
        Set<HocKy> hocKySet = new LinkedHashSet<>();
        Set<NamHoc> namHocSet = new LinkedHashSet<>();
        hocKySet.add(null);
        namHocSet.add(null);
        for (Diem d : allScores) {
            if (d.getHocKy() != null) {
                hocKySet.add(d.getHocKy());
            }
            if (d.getNamHoc() != null) {
                namHocSet.add(d.getNamHoc());
            }
        }
        HocKy previousHocKy = (HocKy) hocKyCombo.getSelectedItem();
        NamHoc previousNamHoc = (NamHoc) namHocCombo.getSelectedItem();
        hocKyCombo.removeAllItems();
        namHocCombo.removeAllItems();
        hocKySet.forEach(hocKyCombo::addItem);
        namHocSet.forEach(namHocCombo::addItem);
        if (previousHocKy != null && hocKySet.contains(previousHocKy)) {
            hocKyCombo.setSelectedItem(previousHocKy);
        } else {
            hocKyCombo.setSelectedItem(null);
        }
        if (previousNamHoc != null && namHocSet.contains(previousNamHoc)) {
            namHocCombo.setSelectedItem(previousNamHoc);
        } else {
            namHocCombo.setSelectedItem(null);
        }
    }

    private void applyFilter() {
        HocKy selectedHocKy = (HocKy) hocKyCombo.getSelectedItem();
        NamHoc selectedNamHoc = (NamHoc) namHocCombo.getSelectedItem();
        List<Diem> filtered = allScores.stream()
                .filter(d -> {
                    boolean matchesHocKy = selectedHocKy == null || (d.getHocKy() != null && selectedHocKy.equals(d.getHocKy()));
                    boolean matchesNamHoc = selectedNamHoc == null || (d.getNamHoc() != null && selectedNamHoc.equals(d.getNamHoc()));
                    return matchesHocKy && matchesNamHoc;
                })
                .collect(Collectors.toList());
        updateTable(filtered);
        updateFilteredSummary(filtered);
    }

    public void refresh() {
        loadScores();
    }

    private void updateTable(List<Diem> data) {
        tableModel.setRowCount(0);
        for (Diem diem : data) {
            HocPhan hocPhan = diem.getHocPhan();
            tableModel.addRow(new Object[]{
                hocPhan != null ? hocPhan.getMaHocPhan() : "",
                hocPhan != null ? hocPhan.getTenHocPhan() : "",
                hocPhan != null ? hocPhan.getSoTinChi() : "",
                formatScore(diem.getDiemQuaTrinh()),
                formatScore(diem.getDiemThi()),
                formatScore(diem.getDiemTongKet()),
                diem.getHocKy() != null ? diem.getHocKy().getTenHocKy() : "",
                diem.getNamHoc() != null ? diem.getNamHoc().getTenNamHoc() : ""
            });
        }
    }

    private void updateFilteredSummary(List<Diem> data) {
        int totalCredits = data.stream()
            .map(Diem::getHocPhan)
            .filter(hp -> hp != null)
            .mapToInt(HocPhan::getSoTinChi)
            .sum();
        double weightedScore = data.stream()
            .filter(d -> d.getHocPhan() != null)
            .mapToDouble(d -> d.getDiemTongKet() * d.getHocPhan().getSoTinChi())
            .sum();
        double gpa = totalCredits > 0 ? weightedScore / totalCredits : Double.NaN;
        String gpaText = Double.isNaN(gpa) ? "-" : String.format("%.2f", gpa);
        String rank = AcademicStandingUtil.classifyByGpa(Double.isNaN(gpa) ? null : gpa);
        filteredSummaryLabel.setText(String.format("Tổng tín chỉ: %d | GPA bộ lọc: %s | Học lực: %s", totalCredits, gpaText, rank));
    }

    private JPanel createFilterGroup(String label, JComboBox<?> comboBox) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.smallFont().deriveFont(Font.BOLD));
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        panel.add(lbl, BorderLayout.NORTH);

        comboBox.setPreferredSize(new java.awt.Dimension(180, 32));
        comboBox.setFont(UITheme.bodyFont());
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String text;
                if (value == null) {
                    text = "Tất cả";
                } else if (value instanceof HocKy) {
                    text = ((HocKy) value).getTenHocKy();
                } else if (value instanceof NamHoc) {
                    text = ((NamHoc) value).getTenNamHoc();
                } else {
                    text = value.toString();
                }
                setText(text);
                return component;
            }
        });
        comboBox.addActionListener(e -> applyFilter());

        panel.add(comboBox, BorderLayout.CENTER);
        return panel;
    }

    private void styleStatLabel(JLabel label) {
        label.setFont(UITheme.bodyFont().deriveFont(Font.BOLD));
        label.setForeground(UITheme.TEXT_PRIMARY);
        label.setHorizontalAlignment(SwingConstants.LEFT);
    }

    private String formatScore(double score) {
        return Double.isNaN(score) ? "" : String.format("%.2f", score);
    }
}
