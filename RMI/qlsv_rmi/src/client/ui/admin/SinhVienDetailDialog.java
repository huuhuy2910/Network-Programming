package client.ui.admin;

import client.ui.components.RoundedPanel;
import client.util.AcademicStandingUtil;
import client.util.DialogUtil;
import client.util.ImageUtil;
import client.util.UITheme;
import common.dto.Diem;
import common.dto.HocKy;
import common.dto.HocPhan;
import common.dto.NamHoc;
import common.dto.SinhVien;
import common.service.DiemService;
import common.service.HocKyService;
import common.service.HocPhanService;
import common.service.NamHocService;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class SinhVienDetailDialog extends JDialog {
    private static final SimpleDateFormat DOB_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private final SinhVien sinhVien;
    private final DiemService diemService;
    private final HocPhanService hocPhanService;
    private final HocKyService hocKyService;
    private final NamHocService namHocService;
    private final boolean gradeServicesAvailable;

    private JLabel gpaValueLabel;
    private JLabel academicRankLabel;
    private DefaultTableModel gradeTableModel;
    private JTable gradeTable;
    private JComboBox<HocPhan> hocPhanCombo;
    private JComboBox<HocKy> hocKyCombo;
    private JComboBox<NamHoc> namHocCombo;
    private JTextField diemQuaTrinhField;
    private JTextField diemThiField;
    private JButton saveGradeButton;
    private JButton deleteGradeButton;
    private final List<Diem> currentGrades = new ArrayList<>();
    private Diem editingDiem;

    public SinhVienDetailDialog(Window owner, SinhVien sinhVien, DiemService diemService,
                                HocPhanService hocPhanService, HocKyService hocKyService, NamHocService namHocService) {
        super(owner, "Chi tiết sinh viên", ModalityType.APPLICATION_MODAL);
        this.sinhVien = sinhVien;
        this.diemService = diemService;
        this.hocPhanService = hocPhanService;
        this.hocKyService = hocKyService;
        this.namHocService = namHocService;
        this.gradeServicesAvailable = diemService != null && hocPhanService != null && hocKyService != null && namHocService != null;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 760);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(20, 20));
        root.setBorder(new EmptyBorder(20, 20, 20, 20));
        root.setBackground(UITheme.BACKGROUND);

        root.add(buildHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UITheme.bodyFont());
        tabs.addTab("Thông tin chung", buildInfoTab());
        tabs.addTab("Bảng điểm", buildGradesTab());
        root.add(tabs, BorderLayout.CENTER);

        setContentPane(root);

        if (gradeServicesAvailable) {
            populateReferenceData();
            loadGrades();
        } else {
            updateAcademicSummary(null);
            DialogUtil.showError(this, "Không thể kết nối dịch vụ điểm. Chỉ hiển thị thông tin sinh viên.");
        }
    }

    private JPanel buildHeader() {
        RoundedPanel header = new RoundedPanel(28, UITheme.PRIMARY);
        header.setLayout(new BorderLayout(20, 20));
        header.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(140, 160));
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(new Color(255, 255, 255, 40));
        avatarLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 2, true));
        BufferedImage avatarImage = loadAvatarImage(sinhVien.getAnh());
        if (avatarImage != null) {
            javax.swing.ImageIcon icon = ImageUtil.toIcon(avatarImage, 140, 160);
            avatarLabel.setIcon(icon != null ? icon : new javax.swing.ImageIcon(avatarImage));
            avatarLabel.setText(null);
            avatarLabel.setToolTipText(sinhVien.getAnh());
            avatarLabel.setBackground(Color.WHITE);
        } else {
            avatarLabel.setIcon(null);
            avatarLabel.setText(computeInitials());
            avatarLabel.setForeground(Color.WHITE);
            avatarLabel.setToolTipText(null);
            avatarLabel.setBackground(new Color(255, 255, 255, 40));
        }
        header.add(avatarLabel, BorderLayout.WEST);

        JPanel description = new JPanel(new BorderLayout(12, 12));
        description.setOpaque(false);

        JLabel nameLabel = new JLabel(safe(sinhVien.getTenSv()));
        nameLabel.setFont(UITheme.headerFont());
        nameLabel.setForeground(Color.WHITE);
        description.add(nameLabel, BorderLayout.NORTH);

        JPanel chips = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        chips.setOpaque(false);
        chips.add(createChip("Mã SV: " + safe(sinhVien.getSvId()), new Color(255, 255, 255, 30)));
        chips.add(createChip("Lớp: " + safe(sinhVien.getLop() != null ? sinhVien.getLop().getTenLop() : null), new Color(255, 255, 255, 30)));
        chips.add(createChip("Ngành: " + safe(sinhVien.getLop() != null && sinhVien.getLop().getNganh() != null ? sinhVien.getLop().getNganh().getTenNganh() : null), new Color(255, 255, 255, 30)));
        chips.add(createChip("Khoa: " + safe(sinhVien.getLop() != null && sinhVien.getLop().getKhoa() != null ? sinhVien.getLop().getKhoa().getTenKhoa() : null), new Color(255, 255, 255, 30)));
        description.add(chips, BorderLayout.CENTER);

        JLabel statusLabel = new JLabel("Trạng thái: " + safe(sinhVien.getStatus()));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(UITheme.bodyFont());
        description.add(statusLabel, BorderLayout.SOUTH);

        header.add(description, BorderLayout.CENTER);
        header.add(buildGpaCard(), BorderLayout.EAST);
        return header;
    }

    private JPanel buildInfoTab() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setOpaque(false);
        panel.add(buildInfoSection(), BorderLayout.NORTH);
        return panel;
    }

    private JPanel buildInfoSection() {
        RoundedPanel infoCard = new RoundedPanel(22, UITheme.CARD_BACKGROUND);
        infoCard.setLayout(new BorderLayout(16, 16));
        infoCard.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel statRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        statRow.setOpaque(false);
        statRow.add(createStatCard("Khóa học", safe(sinhVien.getKhoaHoc() != null ? sinhVien.getKhoaHoc().getTenKhoaHoc() : null), UITheme.PRIMARY));
        statRow.add(createStatCard("Học kỳ", safe(sinhVien.getHocKyHienTai() != null ? sinhVien.getHocKyHienTai().getTenHocKy() : null), UITheme.ACCENT));
        statRow.add(createStatCard("Năm học", safe(sinhVien.getNamHocHienTai() != null ? sinhVien.getNamHocHienTai().getTenNamHoc() : null), UITheme.PRIMARY_DARK));
        infoCard.add(statRow, BorderLayout.NORTH);

        JPanel detailsGrid = new JPanel(new GridBagLayout());
        detailsGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(6, 0, 6, 14);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;

        addInfoRow(detailsGrid, gbc, "Ngày sinh", formatDate(sinhVien.getNgaySinh()));
        addInfoRow(detailsGrid, gbc, "Giới tính", safe(sinhVien.getGioiTinh()));
        addInfoRow(detailsGrid, gbc, "Quê quán", safe(sinhVien.getQueQuan()));
        addInfoRow(detailsGrid, gbc, "Email", safe(sinhVien.getEmail()));
        addInfoRow(detailsGrid, gbc, "Số điện thoại", safe(sinhVien.getSdt()));
        addInfoRow(detailsGrid, gbc, "Địa chỉ", safe(sinhVien.getDiaChi()));
        addInfoRow(detailsGrid, gbc, "Ngày ghi nhận", sinhVien.getNgayTao() != null ? DATETIME_FORMAT.format(sinhVien.getNgayTao()) : "-");

        infoCard.add(detailsGrid, BorderLayout.CENTER);
        return infoCard;
    }

    private JPanel buildGradesTab() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setOpaque(false);

        if (!gradeServicesAvailable) {
            JLabel fallback = new JLabel("Không thể tải bảng điểm do lỗi kết nối dịch vụ.");
            fallback.setHorizontalAlignment(SwingConstants.CENTER);
            fallback.setFont(UITheme.bodyFont());
            fallback.setForeground(UITheme.TEXT_SECONDARY);
            panel.add(fallback, BorderLayout.CENTER);
            return panel;
        }

        panel.add(buildGradeFormCard(), BorderLayout.NORTH);
        panel.add(buildGradeTableCard(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildGradeFormCard() {
        RoundedPanel formCard = new RoundedPanel(22, UITheme.CARD_BACKGROUND);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(20, 24, 12, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        hocPhanCombo = new JComboBox<>();
        configureComboBox(hocPhanCombo, "Chọn học phần");
        addFormField(formCard, gbc, 0, 0, "📖 Học phần", hocPhanCombo);

        hocKyCombo = new JComboBox<>();
        configureComboBox(hocKyCombo, "Chọn học kỳ");
        addFormField(formCard, gbc, 1, 0, "📅 Học kỳ", hocKyCombo);

        namHocCombo = new JComboBox<>();
        configureComboBox(namHocCombo, "Chọn năm học");
        addFormField(formCard, gbc, 2, 0, "📆 Năm học", namHocCombo);

        diemQuaTrinhField = new JTextField();
        diemQuaTrinhField.setFont(UITheme.bodyFont());
        addFormField(formCard, gbc, 0, 1, "📊 Điểm quá trình", diemQuaTrinhField);

        diemThiField = new JTextField();
        diemThiField.setFont(UITheme.bodyFont());
        addFormField(formCard, gbc, 1, 1, "📈 Điểm thi", diemThiField);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
    saveGradeButton = createActionButton("💾 Lưu điểm", UITheme.PRIMARY);
        saveGradeButton.addActionListener(e -> saveGrade());
    deleteGradeButton = createActionButton("🗑️ Xóa điểm", UITheme.DANGER);
        deleteGradeButton.addActionListener(e -> deleteGrade());
    JButton resetButton = createActionButton("🧹 Làm mới form", UITheme.ACCENT);
        resetButton.addActionListener(e -> clearGradeForm());
        buttons.add(saveGradeButton);
        buttons.add(deleteGradeButton);
        buttons.add(resetButton);
        formCard.add(buttons, gbc);

        deleteGradeButton.setEnabled(false);
        return formCard;
    }

    private JPanel buildGradeTableCard() {
        RoundedPanel tableCard = new RoundedPanel(22, UITheme.CARD_BACKGROUND);
        tableCard.setLayout(new BorderLayout(12, 12));
        tableCard.setBorder(new EmptyBorder(18, 24, 20, 24));

        JLabel title = new JLabel("Bảng điểm theo học phần");
        title.setFont(UITheme.subHeaderFont());
        title.setForeground(UITheme.TEXT_PRIMARY);
        tableCard.add(title, BorderLayout.NORTH);

        gradeTableModel = new DefaultTableModel(new Object[]{"Mã HP", "Tên học phần", "Tín chỉ", "Điểm quá trình", "Điểm thi", "Điểm tổng", "Học kỳ", "Năm học"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        gradeTable = new JTable(gradeTableModel);
        gradeTable.setFillsViewportHeight(true);
        gradeTable.setRowHeight(32);
        gradeTable.setFont(UITheme.bodyFont());
        gradeTable.setGridColor(new Color(0xE5E9F2));
        gradeTable.setShowHorizontalLines(true);
        gradeTable.setShowVerticalLines(false);
        gradeTable.getTableHeader().setReorderingAllowed(false);
        gradeTable.getTableHeader().setFont(UITheme.subHeaderFont());
        gradeTable.getTableHeader().setForeground(UITheme.TEXT_PRIMARY);
        gradeTable.getTableHeader().setBackground(new Color(0xF0F4FF));
        gradeTable.getSelectionModel().addListSelectionListener(this::handleGradeSelection);

        JScrollPane scrollPane = new JScrollPane(gradeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UITheme.CARD_BACKGROUND);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        return tableCard;
    }

    private JPanel buildGpaCard() {
        RoundedPanel card = new RoundedPanel(18, new Color(0xF8FAFF));
        card.setLayout(new BorderLayout(6, 4));
        card.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel titleLabel = new JLabel("GPA");
        titleLabel.setFont(UITheme.smallFont().deriveFont(Font.BOLD));
        titleLabel.setForeground(UITheme.SUCCESS.darker());
        card.add(titleLabel, BorderLayout.NORTH);

        gpaValueLabel = new JLabel("-");
        gpaValueLabel.setFont(UITheme.headerFont());
        gpaValueLabel.setForeground(UITheme.TEXT_PRIMARY);
        gpaValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(gpaValueLabel, BorderLayout.CENTER);

        academicRankLabel = new JLabel("Học lực: -");
        academicRankLabel.setFont(UITheme.smallFont());
        academicRankLabel.setForeground(UITheme.TEXT_SECONDARY);
        academicRankLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(academicRankLabel, BorderLayout.SOUTH);
        return card;
    }

    private void handleGradeSelection(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }
        int viewRow = gradeTable.getSelectedRow();
        if (viewRow < 0 || viewRow >= gradeTable.getRowCount()) {
            return;
        }
        int modelRow = gradeTable.convertRowIndexToModel(viewRow);
        if (modelRow < 0 || modelRow >= currentGrades.size()) {
            return;
        }
        Diem selected = currentGrades.get(modelRow);
        populateGradeForm(selected);
    }

    private void populateReferenceData() {
        try {
            List<HocPhan> hocPhans = hocPhanService.getAll();
            hocPhanCombo.setModel(new DefaultComboBoxModel<>(hocPhans.toArray(new HocPhan[0])));
        } catch (Exception e) {
            hocPhanCombo.setModel(new DefaultComboBoxModel<>());
            DialogUtil.showError(this, "Không thể tải học phần: " + e.getMessage());
        }

        try {
            List<HocKy> hocKys = hocKyService.getAll();
            hocKyCombo.setModel(new DefaultComboBoxModel<>(hocKys.toArray(new HocKy[0])));
        } catch (Exception e) {
            hocKyCombo.setModel(new DefaultComboBoxModel<>());
            DialogUtil.showError(this, "Không thể tải học kỳ: " + e.getMessage());
        }

        try {
            List<NamHoc> namHocs = namHocService.getAll();
            namHocCombo.setModel(new DefaultComboBoxModel<>(namHocs.toArray(new NamHoc[0])));
        } catch (Exception e) {
            namHocCombo.setModel(new DefaultComboBoxModel<>());
            DialogUtil.showError(this, "Không thể tải năm học: " + e.getMessage());
        }

        clearGradeForm();
    }

    private void loadGrades() {
        if (!gradeServicesAvailable) {
            return;
        }
        boolean gradeLoaded = true;
        try {
            currentGrades.clear();
            currentGrades.addAll(diemService.getBySinhVien(sinhVien.getSvId()));
        } catch (Exception e) {
            gradeLoaded = false;
            currentGrades.clear();
            DialogUtil.showError(this, "Không thể tải bảng điểm: " + e.getMessage());
        }

        gradeTableModel.setRowCount(0);
        for (Diem diem : currentGrades) {
            HocPhan hocPhan = diem.getHocPhan();
            gradeTableModel.addRow(new Object[]{
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
        gradeTable.clearSelection();

        if (gradeLoaded && currentGrades.isEmpty()) {
            sinhVien.setGpa(null);
            sinhVien.setAcademicRank(null);
            updateAcademicSummary(null);
        } else if (gradeLoaded) {
            try {
                double gpa = diemService.calculateGpa(sinhVien.getSvId());
                if (Double.isNaN(gpa)) {
                    sinhVien.setGpa(null);
                    sinhVien.setAcademicRank(null);
                    updateAcademicSummary(null);
                } else {
                    sinhVien.setGpa(gpa);
                    String rank = AcademicStandingUtil.classifyByGpa(gpa);
                    sinhVien.setAcademicRank(rank);
                    updateAcademicSummary(gpa);
                }
            } catch (Exception e) {
                sinhVien.setGpa(null);
                sinhVien.setAcademicRank(null);
                updateAcademicSummary(null);
                DialogUtil.showError(this, "Không thể tính GPA: " + e.getMessage());
            }
        } else {
            sinhVien.setGpa(null);
            sinhVien.setAcademicRank(null);
            updateAcademicSummary(null);
        }
        editingDiem = null;
        updateFormButtons();
    }

    private void saveGrade() {
        if (!gradeServicesAvailable) {
            DialogUtil.showError(this, "Không có kết nối dịch vụ điểm.");
            return;
        }
        HocPhan hocPhan = (HocPhan) hocPhanCombo.getSelectedItem();
        HocKy hocKy = (HocKy) hocKyCombo.getSelectedItem();
        NamHoc namHoc = (NamHoc) namHocCombo.getSelectedItem();
        if (hocPhan == null || hocKy == null || namHoc == null) {
            DialogUtil.showError(this, "Vui lòng chọn đầy đủ học phần, học kỳ và năm học.");
            return;
        }
        double diemQt;
        double diemThi;
        try {
            diemQt = Double.parseDouble(diemQuaTrinhField.getText().trim());
            diemThi = Double.parseDouble(diemThiField.getText().trim());
        } catch (NumberFormatException ex) {
            DialogUtil.showError(this, "Điểm phải là số.");
            return;
        }
        if (!isValidScore(diemQt) || !isValidScore(diemThi)) {
            DialogUtil.showError(this, "Điểm phải nằm trong khoảng 0 - 10.");
            return;
        }

        if (editingDiem == null) {
            Diem existing = findExistingGrade(hocPhan, hocKy, namHoc);
            if (existing != null) {
                if (!DialogUtil.confirm(this, "Điểm cho học phần này đã tồn tại. Bạn muốn cập nhật lại?")) {
                    return;
                }
                editingDiem = existing;
            }
        }

        boolean isUpdate = editingDiem != null;
        Diem target = isUpdate ? editingDiem : new Diem();
        target.setSinhVien(sinhVien);
        target.setHocPhan(hocPhan);
        target.setHocKy(hocKy);
        target.setNamHoc(namHoc);
        target.setDiemQuaTrinh(diemQt);
        target.setDiemThi(diemThi);
        target.recalculateTongKet();

        try {
            boolean success = isUpdate ? diemService.update(target) : diemService.insert(target);
            if (success) {
                DialogUtil.showInfo(this, isUpdate ? "Cập nhật điểm thành công." : "Thêm điểm thành công.");
                loadGrades();
                clearGradeForm();
            } else {
                DialogUtil.showError(this, "Không thể lưu dữ liệu điểm.");
            }
        } catch (Exception e) {
            DialogUtil.showError(this, e.getMessage());
        }
    }

    private void deleteGrade() {
        if (!gradeServicesAvailable) {
            DialogUtil.showError(this, "Không có kết nối dịch vụ điểm.");
            return;
        }
        if (editingDiem == null) {
            DialogUtil.showError(this, "Vui lòng chọn một dòng điểm để xóa.");
            return;
        }
        if (!DialogUtil.confirm(this, "Bạn chắc chắn muốn xóa điểm đã chọn?")) {
            return;
        }
        try {
            if (diemService.delete(editingDiem.getId())) {
                DialogUtil.showInfo(this, "Đã xóa điểm thành công.");
                loadGrades();
                clearGradeForm();
            } else {
                DialogUtil.showError(this, "Không thể xóa điểm.");
            }
        } catch (Exception e) {
            DialogUtil.showError(this, e.getMessage());
        }
    }

    private void populateGradeForm(Diem diem) {
        if (diem == null) {
            return;
        }
        editingDiem = diem;
        if (diem.getHocPhan() != null) {
            hocPhanCombo.setSelectedItem(diem.getHocPhan());
        }
        if (diem.getHocKy() != null) {
            hocKyCombo.setSelectedItem(diem.getHocKy());
        }
        if (diem.getNamHoc() != null) {
            namHocCombo.setSelectedItem(diem.getNamHoc());
        }
        diemQuaTrinhField.setText(formatScore(diem.getDiemQuaTrinh()));
        diemThiField.setText(formatScore(diem.getDiemThi()));
        updateFormButtons();
    }

    private void clearGradeForm() {
        editingDiem = null;
        if (hocPhanCombo.getItemCount() > 0) {
            hocPhanCombo.setSelectedIndex(-1);
        }
        if (hocKyCombo.getItemCount() > 0) {
            hocKyCombo.setSelectedIndex(-1);
        }
        if (namHocCombo.getItemCount() > 0) {
            namHocCombo.setSelectedIndex(-1);
        }
        diemQuaTrinhField.setText("");
        diemThiField.setText("");
        gradeTable.clearSelection();
        updateFormButtons();
    }

    private void updateFormButtons() {
        deleteGradeButton.setEnabled(editingDiem != null);
        if (editingDiem != null) {
            saveGradeButton.setText("📝 Cập nhật điểm");
        } else {
            saveGradeButton.setText("💾 Lưu điểm");
        }
    }

    private Diem findExistingGrade(HocPhan hocPhan, HocKy hocKy, NamHoc namHoc) {
        return currentGrades.stream()
            .filter(d -> Objects.equals(idOf(d.getHocPhan()), idOf(hocPhan))
                && Objects.equals(idOf(d.getHocKy()), idOf(hocKy))
                && Objects.equals(idOf(d.getNamHoc()), idOf(namHoc)))
            .findFirst()
            .orElse(null);
    }

    private String idOf(HocPhan hocPhan) {
        return hocPhan != null ? hocPhan.getMaHocPhan() : null;
    }

    private String idOf(HocKy hocKy) {
        return hocKy != null ? hocKy.getMaHocKy() : null;
    }

    private String idOf(NamHoc namHoc) {
        return namHoc != null ? namHoc.getMaNamHoc() : null;
    }

    private void addFormField(JPanel container, GridBagConstraints gbc, int column, int row, String label, Component component) {
        gbc.gridx = column;
        gbc.gridy = row * 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(UITheme.smallFont());
        labelComponent.setForeground(UITheme.TEXT_SECONDARY);
        container.add(labelComponent, gbc);

        gbc.gridy = row * 2 + 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        component.setPreferredSize(new Dimension(240, 32));
        container.add(component, gbc);

        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
    }

    private JButton createActionButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setFont(UITheme.buttonFont());
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        return button;
    }

    private void configureComboBox(JComboBox<?> comboBox, String placeholder) {
        comboBox.setPreferredSize(new Dimension(240, 32));
        comboBox.setFont(UITheme.bodyFont());
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    ((JLabel) component).setText(placeholder);
                }
                return component;
            }
        });
    }

    private JPanel createChip(String text, Color background) {
        RoundedPanel chip = new RoundedPanel(20, background);
        chip.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 4));
        JLabel label = new JLabel(text);
        label.setFont(UITheme.smallFont());
        label.setForeground(Color.WHITE);
        chip.add(label);
        return chip;
    }

    private JPanel createStatCard(String title, String value, Color accent) {
        RoundedPanel card = new RoundedPanel(18, new Color(0xF8FAFF));
        card.setLayout(new BorderLayout(6, 4));
        card.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel titleLabel = new JLabel(title.toUpperCase());
        titleLabel.setFont(UITheme.smallFont().deriveFont(Font.BOLD));
        titleLabel.setForeground(accent.darker());
        card.add(titleLabel, BorderLayout.NORTH);

        JLabel valueLabel = new JLabel(value != null && !value.isBlank() ? value : "-");
        valueLabel.setFont(UITheme.subHeaderFont());
        valueLabel.setForeground(UITheme.TEXT_PRIMARY);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void addInfoRow(JPanel panel, GridBagConstraints gbc, String label, String value) {
        JLabel labelComp = new JLabel(label + ":");
        labelComp.setFont(UITheme.smallFont().deriveFont(Font.BOLD));
        labelComp.setForeground(UITheme.TEXT_SECONDARY);

        gbc.weightx = 0.2;
        gbc.gridwidth = 1;
        panel.add(labelComp, gbc);

        JLabel valueComp = new JLabel(value != null && !value.isBlank() ? value : "-");
        valueComp.setFont(UITheme.bodyFont());
        valueComp.setForeground(UITheme.TEXT_PRIMARY);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        panel.add(valueComp, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
    }

    private void updateAcademicSummary(Double gpa) {
        if (gpa == null || Double.isNaN(gpa)) {
            if (gpaValueLabel != null) {
                gpaValueLabel.setText("-");
            }
            if (academicRankLabel != null) {
                academicRankLabel.setText("Học lực: -");
            }
        } else {
            if (gpaValueLabel != null) {
                gpaValueLabel.setText(String.format("%.2f", gpa));
            }
            if (academicRankLabel != null) {
                academicRankLabel.setText("Học lực: " + AcademicStandingUtil.classifyByGpa(gpa));
            }
        }
    }

    private BufferedImage loadAvatarImage(String path) {
        if (path == null || path.isBlank()) {
            return null;
        }
        String trimmed = path.trim();

        // Data URI support
        if (trimmed.startsWith("data:image")) {
            int commaIndex = trimmed.indexOf(',');
            if (commaIndex > 0 && commaIndex < trimmed.length() - 1) {
                String base64 = trimmed.substring(commaIndex + 1);
                try {
                    byte[] bytes = Base64.getDecoder().decode(base64);
                    try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
                        return ImageIO.read(in);
                    }
                } catch (IllegalArgumentException | IOException ignored) {
                }
            }
        }

        // Remote URL (http, https, ftp)
        if (trimmed.matches("(?i)^(https?|ftp)://.*")) {
            try {
                URL url = URI.create(trimmed).toURL();
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                try (InputStream in = connection.getInputStream()) {
                    return ImageIO.read(in);
                }
            } catch (IOException ignored) {
            }
        }

        // file URI
        if (trimmed.startsWith("file:")) {
            try (InputStream in = URI.create(trimmed).toURL().openStream()) {
                return ImageIO.read(in);
            } catch (IOException ignored) {
            }
        }

        // Local filesystem absolute/relative path
        File file = new File(trimmed);
        if (file.exists() && file.isFile()) {
            try {
                return ImageIO.read(file);
            } catch (IOException ignored) {
            }
        }

        // Classpath resource fallback
        String resourcePath = trimmed.startsWith("/") ? trimmed : "/" + trimmed;
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in != null) {
                return ImageIO.read(in);
            }
        } catch (IOException ignored) {
        }

        return null;
    }

    private String computeInitials() {
        String name = sinhVien != null ? sinhVien.getTenSv() : null;
        if (name == null || name.isBlank()) {
            return "SV";
        }
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }
        String first = parts[0];
        String last = parts[parts.length - 1];
        return (first.substring(0, 1) + last.substring(0, 1)).toUpperCase();
    }

    private boolean isValidScore(double score) {
        return score >= 0.0 && score <= 10.0;
    }

    private String safe(String value) {
        return value != null && !value.isBlank() ? value : "-";
    }

    private String formatDate(java.util.Date date) {
        return date != null ? DOB_FORMAT.format(date) : "-";
    }

    private String formatScore(double score) {
        if (Double.isNaN(score)) {
            return "";
        }
        return String.format("%.2f", score);
    }
}
