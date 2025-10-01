package client.ui.admin;

import client.network.ClientConnector;
import client.ui.components.BarChartPanel;
import client.ui.components.PieChartPanel;
import client.ui.components.RoundedPanel;
import client.util.DialogUtil;
import client.util.UITheme;
import common.dto.DashboardClassSummary;
import common.dto.DashboardStats;
import common.dto.Khoa;
import common.dto.KhoaHoc;
import common.dto.Nganh;
import common.service.KhoaHocService;
import common.service.KhoaService;
import common.service.NganhService;
import common.service.SinhVienService;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DashboardPanel extends JPanel {
    private static final String ALL_FACULTIES_PLACEHOLDER = "Tất cả khoa";
    private static final String ALL_MAJORS_PLACEHOLDER = "Tất cả ngành";
    private static final String ALL_COHORTS_PLACEHOLDER = "Tất cả khóa";

    private final javax.swing.JComboBox<Khoa> khoaCombo = new javax.swing.JComboBox<>();
    private final javax.swing.JComboBox<Nganh> nganhCombo = new javax.swing.JComboBox<>();
    private final javax.swing.JComboBox<KhoaHoc> khoaHocCombo = new javax.swing.JComboBox<>();

    private final JLabel lastUpdatedLabel = new JLabel("Đang tải dữ liệu...");

    private final JLabel totalStudentsLabel = createMetricValueLabel();
    private final JLabel activeStudentsLabel = createMetricValueLabel();
    private final JLabel probationStudentsLabel = createMetricValueLabel();
    private final JLabel graduatedStudentsLabel = createMetricValueLabel();

    private final JLabel totalStudentsDetailLabel = createMetricDetailLabel();
    private final JLabel activeStudentsDetailLabel = createMetricDetailLabel();
    private final JLabel probationStudentsDetailLabel = createMetricDetailLabel();
    private final JLabel graduatedStudentsDetailLabel = createMetricDetailLabel();

    private final BarChartPanel enrollmentChart = new BarChartPanel();
    private final PieChartPanel genderChart = new PieChartPanel();

    private final DefaultTableModel topClassModel = new NonEditableTableModel(new String[]{"STT", "Lớp", "Sĩ số", "Cố vấn"});
    private final JTable topClassTable = new JTable(topClassModel);

    private final DefaultTableModel statusModel = new NonEditableTableModel(new String[]{"Trạng thái", "Số lượng"});
    private final JTable statusTable = new JTable(statusModel);

    private final List<Khoa> allKhoas = new ArrayList<>();
    private final List<Nganh> allNganhs = new ArrayList<>();
    private final List<KhoaHoc> allKhoaHocs = new ArrayList<>();

    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final DecimalFormat percentFormat = new DecimalFormat("0.0");

    private SinhVienService sinhVienService;
    private KhoaService khoaService;
    private NganhService nganhService;
    private KhoaHocService khoaHocService;

    private SwingWorker<DashboardStats, Void> statsWorker;
    private boolean updatingFilters;

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        initServices();
        add(buildScrollContent(), BorderLayout.CENTER);
        loadReferenceData();
    }

    private void initServices() {
        try {
            sinhVienService = ClientConnector.getSinhVienService();
            khoaService = ClientConnector.getKhoaService();
            nganhService = ClientConnector.getNganhService();
            khoaHocService = ClientConnector.getKhoaHocService();
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể kết nối máy chủ: " + e.getMessage());
        }
    }

    private JScrollPane buildScrollContent() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(24, 24, 24, 24));

        content.add(buildHeroCard());
        content.add(Box.createVerticalStrut(20));
        content.add(buildFilterCard());
        content.add(Box.createVerticalStrut(20));
        content.add(buildSummaryCards());
        content.add(Box.createVerticalStrut(20));
        content.add(buildChartsRow());
        content.add(Box.createVerticalStrut(20));
        content.add(buildTablesRow());
        content.add(Box.createVerticalStrut(24));

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel buildHeroCard() {
        RoundedPanel hero = new RoundedPanel(28, UITheme.PRIMARY);
        hero.setLayout(new BorderLayout());
        hero.setBorder(new EmptyBorder(28, 30, 28, 30));

        JLabel title = new JLabel("Trang thống kê tổng quan", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(UITheme.headerFont().deriveFont(28f));
        hero.add(title, BorderLayout.WEST);

        lastUpdatedLabel.setForeground(new Color(0xE8F1FF));
        lastUpdatedLabel.setFont(UITheme.bodyFont());
        hero.add(lastUpdatedLabel, BorderLayout.EAST);
        return hero;
    }

    private JPanel buildFilterCard() {
        RoundedPanel filterCard = new RoundedPanel(20, UITheme.CARD_BACKGROUND);
        filterCard.setLayout(new BorderLayout());
        filterCard.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel title = new JLabel("Bộ lọc thống kê");
        title.setFont(UITheme.subHeaderFont());
        title.setForeground(UITheme.TEXT_PRIMARY);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.WEST);

        filterCard.add(titlePanel, BorderLayout.NORTH);

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        filterRow.setOpaque(false);

        configureComboBox(khoaCombo, ALL_FACULTIES_PLACEHOLDER);
        configureComboBox(nganhCombo, ALL_MAJORS_PLACEHOLDER);
        configureComboBox(khoaHocCombo, ALL_COHORTS_PLACEHOLDER);

        filterRow.add(buildLabeledField("Khoa", khoaCombo));
        filterRow.add(buildLabeledField("Ngành", nganhCombo));
        filterRow.add(buildLabeledField("Khóa", khoaHocCombo));

        filterCard.add(filterRow, BorderLayout.CENTER);

        khoaCombo.addActionListener(e -> {
            if (!updatingFilters) {
                handleKhoaChanged();
                loadDashboardStats();
            }
        });
        nganhCombo.addActionListener(e -> {
            if (!updatingFilters) {
                loadDashboardStats();
            }
        });
        khoaHocCombo.addActionListener(e -> {
            if (!updatingFilters) {
                loadDashboardStats();
            }
        });

        return filterCard;
    }

    private JPanel buildSummaryCards() {
        JPanel container = new JPanel(new GridLayout(1, 4, 16, 0));
        container.setOpaque(false);

        container.add(buildMetricCard("Tổng số sinh viên", totalStudentsLabel, totalStudentsDetailLabel, new Color(0x2563EB)));
        container.add(buildMetricCard("Đang học", activeStudentsLabel, activeStudentsDetailLabel, new Color(0x10B981)));
        container.add(buildMetricCard("Cảnh báo", probationStudentsLabel, probationStudentsDetailLabel, new Color(0xF59E0B)));
        container.add(buildMetricCard("Tốt nghiệp", graduatedStudentsLabel, graduatedStudentsDetailLabel, new Color(0x6B21A8)));
        return container;
    }

    private JPanel buildChartsRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 18, 0));
        row.setOpaque(false);

        RoundedPanel barCard = new RoundedPanel(20, UITheme.CARD_BACKGROUND);
        barCard.setLayout(new BorderLayout());
        barCard.setBorder(new EmptyBorder(18, 24, 18, 24));
        enrollmentChart.setPreferredSize(new Dimension(380, 260));
        enrollmentChart.setTitle("Tuyển sinh theo năm học");
        barCard.add(enrollmentChart, BorderLayout.CENTER);

        RoundedPanel pieCard = new RoundedPanel(20, UITheme.CARD_BACKGROUND);
        pieCard.setLayout(new BorderLayout());
        pieCard.setBorder(new EmptyBorder(18, 24, 18, 24));
        genderChart.setPreferredSize(new Dimension(320, 260));
        genderChart.setTitle("Cơ cấu giới tính");
        pieCard.add(genderChart, BorderLayout.CENTER);

        row.add(barCard);
        row.add(pieCard);
        return row;
    }

    private JPanel buildTablesRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 18, 0));
        row.setOpaque(false);

        RoundedPanel topClassCard = new RoundedPanel(20, UITheme.CARD_BACKGROUND);
        topClassCard.setLayout(new BorderLayout());
        topClassCard.setBorder(new EmptyBorder(18, 24, 18, 24));
        JLabel topClassLabel = new JLabel("Top lớp có sĩ số cao");
        topClassLabel.setFont(UITheme.subHeaderFont());
        topClassLabel.setForeground(UITheme.TEXT_PRIMARY);
        topClassCard.add(topClassLabel, BorderLayout.NORTH);
        styleTable(topClassTable);
        JScrollPane topClassScroll = new JScrollPane(topClassTable);
        topClassScroll.setBorder(BorderFactory.createEmptyBorder());
        topClassCard.add(topClassScroll, BorderLayout.CENTER);

        RoundedPanel statusCard = new RoundedPanel(20, UITheme.CARD_BACKGROUND);
        statusCard.setLayout(new BorderLayout());
        statusCard.setBorder(new EmptyBorder(18, 24, 18, 24));
        JLabel statusLabel = new JLabel("Phân bố trạng thái học tập");
        statusLabel.setFont(UITheme.subHeaderFont());
        statusLabel.setForeground(UITheme.TEXT_PRIMARY);
        statusCard.add(statusLabel, BorderLayout.NORTH);
        styleTable(statusTable);
        JScrollPane statusScroll = new JScrollPane(statusTable);
        statusScroll.setBorder(BorderFactory.createEmptyBorder());
        statusCard.add(statusScroll, BorderLayout.CENTER);

        row.add(topClassCard);
        row.add(statusCard);
        return row;
    }

    private JPanel buildLabeledField(String label, Component component) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(label);
        title.setFont(UITheme.smallFont());
        title.setForeground(UITheme.TEXT_SECONDARY);
        panel.add(title);
        panel.add(Box.createVerticalStrut(6));
        panel.add(component);
        return panel;
    }

    private JPanel buildMetricCard(String title, JLabel valueLabel, JLabel detailLabel, Color accentColor) {
        RoundedPanel card = new RoundedPanel(20, UITheme.CARD_BACKGROUND);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(UITheme.TEXT_SECONDARY);
        titleLabel.setFont(UITheme.smallFont());

        valueLabel.setForeground(accentColor);

        detailLabel.setFont(UITheme.smallFont());
        detailLabel.setForeground(new Color(0x2563EB));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(detailLabel, BorderLayout.SOUTH);
        return card;
    }

    private JLabel createMetricValueLabel() {
        JLabel label = new JLabel("0");
        label.setFont(UITheme.headerFont().deriveFont(Font.BOLD, 28f));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private JLabel createMetricDetailLabel() {
        JLabel label = new JLabel("—");
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private void configureComboBox(javax.swing.JComboBox<?> comboBox, String placeholder) {
        comboBox.setFont(UITheme.bodyFont());
        comboBox.setBackground(Color.WHITE);
        comboBox.setPreferredSize(new Dimension(210, 36));
        comboBox.setRenderer(new javax.swing.plaf.basic.BasicComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(new EmptyBorder(4, 10, 4, 10));
                setFont(UITheme.bodyFont());
                if (value == null) {
                    setText(placeholder);
                }
                return c;
            }
        });
    }

    private void setComboItems(javax.swing.JComboBox<Khoa> comboBox, List<Khoa> items, Khoa previousSelection) {
        DefaultComboBoxModel<Khoa> model = new DefaultComboBoxModel<>();
        model.addElement(null);
        for (Khoa item : items) {
            model.addElement(item);
        }
        comboBox.setModel(model);
        if (previousSelection != null && items.contains(previousSelection)) {
            comboBox.setSelectedItem(previousSelection);
        } else {
            comboBox.setSelectedIndex(0);
        }
    }

    private void setComboItems(javax.swing.JComboBox<Nganh> comboBox, List<Nganh> items, Nganh previousSelection) {
        DefaultComboBoxModel<Nganh> model = new DefaultComboBoxModel<>();
        model.addElement(null);
        for (Nganh item : items) {
            model.addElement(item);
        }
        comboBox.setModel(model);
        if (previousSelection != null && items.contains(previousSelection)) {
            comboBox.setSelectedItem(previousSelection);
        } else {
            comboBox.setSelectedIndex(0);
        }
    }

    private void setComboItems(javax.swing.JComboBox<KhoaHoc> comboBox, List<KhoaHoc> items, KhoaHoc previousSelection) {
        DefaultComboBoxModel<KhoaHoc> model = new DefaultComboBoxModel<>();
        model.addElement(null);
        for (KhoaHoc item : items) {
            model.addElement(item);
        }
        comboBox.setModel(model);
        if (previousSelection != null && items.contains(previousSelection)) {
            comboBox.setSelectedItem(previousSelection);
        } else {
            comboBox.setSelectedIndex(0);
        }
    }

    private void handleKhoaChanged() {
        if (updatingFilters) {
            return;
        }
        updatingFilters = true;
        Khoa selectedKhoa = (Khoa) khoaCombo.getSelectedItem();
        List<Nganh> filtered = allNganhs.stream()
            .filter(ng -> selectedKhoa == null || (ng.getKhoa() != null && Objects.equals(ng.getKhoa().getMaKhoa(), selectedKhoa.getMaKhoa())))
            .collect(Collectors.toList());
        Nganh previous = (Nganh) nganhCombo.getSelectedItem();
        setComboItems(nganhCombo, filtered, previous);
        updatingFilters = false;
    }

    private void loadReferenceData() {
        if (sinhVienService == null) {
            return;
        }
        setLoadingState(true);
        new SwingWorker<ReferenceData, Void>() {
            @Override
            protected ReferenceData doInBackground() throws Exception {
                List<Khoa> khoas = khoaService != null ? khoaService.getAll() : Collections.emptyList();
                List<Nganh> nganhs = nganhService != null ? nganhService.getAll() : Collections.emptyList();
                List<KhoaHoc> khoaHocs = khoaHocService != null ? khoaHocService.getAll() : Collections.emptyList();
                return new ReferenceData(khoas, nganhs, khoaHocs);
            }

            @Override
            protected void done() {
                try {
                    ReferenceData data = get();
                    allKhoas.clear();
                    allKhoas.addAll(data.khoas);
                    allNganhs.clear();
                    allNganhs.addAll(data.nganhs);
                    allKhoaHocs.clear();
                    allKhoaHocs.addAll(data.khoaHocs);

                    updatingFilters = true;
                    setComboItems(khoaCombo, allKhoas, (Khoa) khoaCombo.getSelectedItem());
                    setComboItems(nganhCombo, allNganhs, (Nganh) nganhCombo.getSelectedItem());
                    setComboItems(khoaHocCombo, allKhoaHocs, (KhoaHoc) khoaHocCombo.getSelectedItem());
                    updatingFilters = false;

                    handleKhoaChanged();
                    loadDashboardStats();
                } catch (Exception e) {
                    setLoadingState(false);
                    DialogUtil.showError(DashboardPanel.this, "Không thể tải dữ liệu bộ lọc: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void loadDashboardStats() {
        if (sinhVienService == null) {
            return;
        }
        if (statsWorker != null && !statsWorker.isDone()) {
            statsWorker.cancel(true);
        }
        setLoadingState(true);

        Khoa selectedKhoa = (Khoa) khoaCombo.getSelectedItem();
        Nganh selectedNganh = (Nganh) nganhCombo.getSelectedItem();
        KhoaHoc selectedKhoaHoc = (KhoaHoc) khoaHocCombo.getSelectedItem();

        final String khoaId = selectedKhoa != null ? selectedKhoa.getMaKhoa() : null;
        final String nganhId = selectedNganh != null ? selectedNganh.getMaNganh() : null;
        final String khoaHocId = selectedKhoaHoc != null ? selectedKhoaHoc.getMaKhoaHoc() : null;

        statsWorker = new SwingWorker<>() {
            @Override
            protected DashboardStats doInBackground() throws Exception {
                return sinhVienService.getDashboardStats(khoaId, nganhId, khoaHocId);
            }

            @Override
            protected void done() {
                try {
                    DashboardStats stats = get();
                    applyStats(stats);
                } catch (Exception e) {
                    DialogUtil.showError(DashboardPanel.this, "Không thể tải thống kê: " + e.getMessage());
                } finally {
                    setLoadingState(false);
                }
            }
        };
        statsWorker.execute();
    }

    private void applyStats(DashboardStats stats) {
        if (stats == null) {
            lastUpdatedLabel.setText("Cập nhật lần cuối: —");
            totalStudentsLabel.setText("0");
            activeStudentsLabel.setText("0");
            probationStudentsLabel.setText("0");
            graduatedStudentsLabel.setText("0");
            totalStudentsDetailLabel.setText("Không có dữ liệu");
            activeStudentsDetailLabel.setText("—");
            probationStudentsDetailLabel.setText("—");
            graduatedStudentsDetailLabel.setText("—");
            enrollmentChart.setData(Collections.emptyMap());
            genderChart.setData(Collections.emptyMap());
            topClassModel.setRowCount(0);
            statusModel.setRowCount(0);
            return;
        }

        Date generatedAt = stats.getGeneratedAt();
        if (generatedAt != null) {
            lastUpdatedLabel.setText("Cập nhật lần cuối: " + dateTimeFormat.format(generatedAt));
        } else {
            lastUpdatedLabel.setText("Cập nhật lần cuối: —");
        }

        Map<String, Integer> enrollment = stats.getEnrollmentByYear();
        Map<String, Integer> statusMap = stats.getStatusBreakdown();
        Map<String, Integer> genderMap = stats.getGenderDistribution();

        int total = enrollment.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0) {
            total = statusMap.values().stream().mapToInt(Integer::intValue).sum();
        }

        int active = 0;
        int probation = 0;
        int graduated = 0;
        for (Map.Entry<String, Integer> entry : statusMap.entrySet()) {
            String key = entry.getKey() != null ? entry.getKey().toLowerCase(Locale.ROOT) : "";
            int value = entry.getValue();
            if (key.contains("tốt nghiệp") || key.contains("tot nghiep")) {
                graduated += value;
            } else if (key.contains("bảo lưu") || key.contains("bao luu") || key.contains("cảnh báo") || key.contains("canh bao") || key.contains("đình chỉ") || key.contains("dinh chi")) {
                probation += value;
            } else {
                active += value;
            }
        }

        totalStudentsLabel.setText(String.valueOf(total));
        activeStudentsLabel.setText(String.valueOf(active));
        probationStudentsLabel.setText(String.valueOf(probation));
        graduatedStudentsLabel.setText(String.valueOf(graduated));

        totalStudentsDetailLabel.setText(total > 0 ? "Toàn bộ kết quả theo bộ lọc" : "Không có dữ liệu");
        activeStudentsDetailLabel.setText(formatShare(active, total));
        probationStudentsDetailLabel.setText(formatShare(probation, total));
        graduatedStudentsDetailLabel.setText(formatShare(graduated, total));

        enrollmentChart.setData(enrollment);
        genderChart.setData(genderMap);

        topClassModel.setRowCount(0);
        int index = 1;
        for (DashboardClassSummary cls : stats.getTopClasses()) {
            String advisor = cls.getAdvisorName();
            if (advisor == null || advisor.isBlank()) {
                advisor = "—";
            }
            topClassModel.addRow(new Object[]{index++, cls.getClassName(), cls.getStudentCount(), advisor});
        }

        statusModel.setRowCount(0);
        for (Map.Entry<String, Integer> entry : statusMap.entrySet()) {
            statusModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    private String formatShare(int value, int total) {
        if (total <= 0) {
            return "0% tổng số";
        }
        double ratio = (double) value / (double) total * 100.0;
        return percentFormat.format(ratio) + "% tổng số";
    }

    private void setLoadingState(boolean loading) {
        khoaCombo.setEnabled(!loading);
        nganhCombo.setEnabled(!loading);
        khoaHocCombo.setEnabled(!loading);
        if (loading) {
            lastUpdatedLabel.setText("Đang tải dữ liệu...");
        }
    }

    private void styleTable(JTable table) {
        table.setRowHeight(36);
        table.setFont(UITheme.bodyFont());
        table.getTableHeader().setFont(UITheme.subHeaderFont());
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        headerRenderer.setBackground(new Color(0xE5EDFF));
        headerRenderer.setBorder(new EmptyBorder(10, 12, 10, 12));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(0xDBEAFE));
        table.setSelectionForeground(UITheme.TEXT_PRIMARY);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(new EmptyBorder(4, 12, 4, 12));
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private static class ReferenceData {
        final List<Khoa> khoas;
        final List<Nganh> nganhs;
        final List<KhoaHoc> khoaHocs;

        ReferenceData(List<Khoa> khoas, List<Nganh> nganhs, List<KhoaHoc> khoaHocs) {
            this.khoas = khoas;
            this.nganhs = nganhs;
            this.khoaHocs = khoaHocs;
        }
    }

    private static class NonEditableTableModel extends DefaultTableModel {
        NonEditableTableModel(String[] columnNames) {
            super(columnNames, 0);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}
