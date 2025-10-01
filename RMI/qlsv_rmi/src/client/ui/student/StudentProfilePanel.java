package client.ui.student;

import client.ui.components.RoundedPanel;
import client.util.AcademicStandingUtil;
import client.util.DialogUtil;
import client.util.ImageUtil;
import client.util.UITheme;
import client.util.ValidationUtil;
import common.dto.Lop;
import common.dto.SinhVien;
import common.service.DiemService;
import common.service.SinhVienService;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 * Student-facing profile panel that mirrors the admin detail dialog.
 * Shows full student information and allows editing of permitted fields.
 */
public class StudentProfilePanel extends JPanel {
    private final SinhVienService sinhVienService;
    private final DiemService diemService;
    private SinhVien sinhVien;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    // Hero components
    private final JLabel avatarLabel = new JLabel();
    private final JLabel heroNameLabel = new JLabel();
    private final JLabel heroStatusLabel = new JLabel();
    private final JLabel chipMaSvLabel = new JLabel();
    private final JLabel chipLopLabel = new JLabel();
    private final JLabel chipNganhLabel = new JLabel();
    private final JLabel chipKhoaLabel = new JLabel();

    // Statistic card values
    private final JLabel statKhoaHocLabel = createStatValueLabel();
    private final JLabel statHocKyLabel = createStatValueLabel();
    private final JLabel statNamHocLabel = createStatValueLabel();
    private final JLabel statGpaLabel = createStatValueLabel();
    private final JLabel statHocLucLabel = createStatValueLabel();
    private final JLabel statTrangThaiLabel = createStatValueLabel();

    // Detail section values
    private final JLabel maSvValueLabel = createDetailValueLabel();
    private final JLabel fullNameValueLabel = createDetailValueLabel();
    private final JLabel dobValueLabel = createDetailValueLabel();
    private final JLabel genderValueLabel = createDetailValueLabel();
    private final JLabel queQuanValueLabel = createDetailValueLabel();
    private final JLabel emailValueLabel = createDetailValueLabel();
    private final JLabel phoneValueLabel = createDetailValueLabel();
    private final JLabel addressValueLabel = createDetailValueLabel();
    private final JLabel lopValueLabel = createDetailValueLabel();
    private final JLabel maLopValueLabel = createDetailValueLabel();
    private final JLabel nganhValueLabel = createDetailValueLabel();
    private final JLabel khoaValueLabel = createDetailValueLabel();
    private final JLabel khoaHocValueLabel = createDetailValueLabel();
    private final JLabel gpaValueLabel = createDetailValueLabel();
    private final JLabel hocLucValueLabel = createDetailValueLabel();
    private final JLabel hocKyValueLabel = createDetailValueLabel();
    private final JLabel namHocValueLabel = createDetailValueLabel();
    private final JLabel trangThaiValueLabel = createDetailValueLabel();
    private final JLabel ngayTaoValueLabel = createDetailValueLabel();
    private final JLabel avatarPathValueLabel = createDetailValueLabel();
    private boolean academicDataErrorShown = false;

    public StudentProfilePanel(SinhVien sinhVien, SinhVienService sinhVienService, DiemService diemService) {
        this.sinhVien = sinhVien;
        this.sinhVienService = sinhVienService;
        this.diemService = diemService;

        setLayout(new BorderLayout(24, 24));
        setBorder(new EmptyBorder(24, 24, 24, 24));
        setBackground(UITheme.BACKGROUND);

        buildUI();
        refresh();
    }

    private void buildUI() {
        add(buildHeroPanel(), BorderLayout.NORTH);
        add(buildDetailsScrollPane(), BorderLayout.CENTER);
    }

    private JPanel buildHeroPanel() {
        RoundedPanel heroPanel = new RoundedPanel(30, UITheme.PRIMARY) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x667EEA), getWidth(), getHeight(), new Color(0x764BA2));
                g2.setPaint(gp);
                g2.fill(shape);
                g2.dispose();
                super.paintComponent(g);
            }
        };
    heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.X_AXIS));
    heroPanel.setBorder(new EmptyBorder(24, 28, 24, 28));

    // Avatar
    avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
    avatarLabel.setPreferredSize(new Dimension(140, 160));
    avatarLabel.setOpaque(true);
    avatarLabel.setBackground(new Color(255, 255, 255, 28));
    avatarLabel.setForeground(Color.WHITE);
    avatarLabel.setFont(UITheme.headerFont().deriveFont(Font.BOLD, 36f));
    avatarLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 2, true));
    heroPanel.add(avatarLabel);

    heroPanel.add(Box.createHorizontalStrut(18));

    // Tên sinh viên sát avatar, to hơn nữa
    JPanel nameAndChips = new JPanel();
    nameAndChips.setOpaque(false);
    nameAndChips.setLayout(new BoxLayout(nameAndChips, BoxLayout.Y_AXIS));

    heroNameLabel.setFont(UITheme.headerFont().deriveFont(Font.BOLD, 48f));
    heroNameLabel.setForeground(Color.WHITE);
    heroNameLabel.setAlignmentX(0f);
    nameAndChips.add(heroNameLabel);
    nameAndChips.add(Box.createVerticalStrut(12));

    JPanel chipRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
    chipRow.setOpaque(false);
    chipRow.add(createChip(chipMaSvLabel));
    chipRow.add(createChip(chipLopLabel));
    chipRow.add(createChip(chipNganhLabel));
    chipRow.add(createChip(chipKhoaLabel));
    nameAndChips.add(chipRow);

    heroPanel.add(nameAndChips);
    heroPanel.add(Box.createHorizontalGlue());
    return heroPanel;
    }

    private JScrollPane buildDetailsScrollPane() {
        RoundedPanel detailCard = new RoundedPanel(24, UITheme.CARD_BACKGROUND);
        detailCard.setLayout(new BoxLayout(detailCard, BoxLayout.Y_AXIS));
        detailCard.setBorder(new EmptyBorder(24, 28, 28, 28));

        detailCard.add(buildStatRow());
        detailCard.add(Box.createVerticalStrut(20));
        detailCard.add(buildSection("Thông tin cá nhân", new JLabel[][]{
            {new JLabel("Mã sinh viên"), maSvValueLabel},
            {new JLabel("Họ và tên"), fullNameValueLabel},
            {new JLabel("Ngày sinh"), dobValueLabel},
            {new JLabel("Giới tính"), genderValueLabel}
        }, new Color(0xE3F2FD)));

        detailCard.add(Box.createVerticalStrut(16));
        detailCard.add(buildSection("Thông tin học tập", new JLabel[][]{
            {new JLabel("Lớp"), lopValueLabel},
            {new JLabel("Mã lớp"), maLopValueLabel},
            {new JLabel("Ngành"), nganhValueLabel},
            {new JLabel("Khoa"), khoaValueLabel},
            {new JLabel("Khóa học"), khoaHocValueLabel},
            {new JLabel("GPA tích lũy"), gpaValueLabel},
            {new JLabel("Học lực (thang điểm 10)"), hocLucValueLabel},
            {new JLabel("Học kỳ hiện tại"), hocKyValueLabel},
            {new JLabel("Năm học"), namHocValueLabel},
            {new JLabel("Trạng thái học tập"), trangThaiValueLabel}
        }, new Color(0xF3E5F5)));

        detailCard.add(Box.createVerticalStrut(16));
        detailCard.add(buildSection("Liên hệ", new JLabel[][]{
            {new JLabel("Email"), emailValueLabel},
            {new JLabel("Số điện thoại"), phoneValueLabel},
            {new JLabel("Quê quán"), queQuanValueLabel},
            {new JLabel("Địa chỉ hiện tại"), addressValueLabel}
        }, new Color(0xFFF3E0)));

        detailCard.add(Box.createVerticalStrut(16));
        detailCard.add(buildSection("Thông tin hồ sơ", new JLabel[][]{
            {new JLabel("Ngày tạo hồ sơ"), ngayTaoValueLabel},
            {new JLabel("Đường dẫn ảnh đại diện"), avatarPathValueLabel}
        }, new Color(0xE8F5E8)));

        detailCard.add(Box.createVerticalStrut(20));
        detailCard.add(buildActionRow());

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.add(detailCard);
        wrapper.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel buildStatRow() {
        JPanel statRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        statRow.setOpaque(false);
        statRow.add(createStatCard("Khóa học", statKhoaHocLabel, UITheme.PRIMARY));
        statRow.add(createStatCard("Học kỳ", statHocKyLabel, UITheme.ACCENT));
        statRow.add(createStatCard("Năm học", statNamHocLabel, UITheme.PRIMARY_DARK));
        statRow.add(createStatCard("GPA", statGpaLabel, UITheme.SUCCESS));
        statRow.add(createStatCard("Học lực", statHocLucLabel, UITheme.ACCENT));
        statRow.add(createStatCard("Trạng thái", statTrangThaiLabel, UITheme.SUCCESS));
        return statRow;
    }

    private JPanel buildSection(String title, JLabel[][] rows, Color bgColor) {
        RoundedPanel section = new RoundedPanel(18, bgColor);
        section.setLayout(new BorderLayout(0, 16));
        section.setBorder(new EmptyBorder(20, 20, 20, 20));

        String icon = getIconForSection(title);
        JLabel titleLabel = new JLabel(icon + " " + title);
        titleLabel.setFont(UITheme.subHeaderFont());
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        section.add(titleLabel, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(0, 1, 12, 8));
        content.setOpaque(false);
        for (JLabel[] row : rows) {
            content.add(createDetailRow(row[0], row[1]));
        }
        section.add(content, BorderLayout.CENTER);
        return section;
    }

    private JPanel createDetailRow(JLabel label, JLabel valueLabel) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(4, 0, 4, 0));

        label.setText(label.getText() + ":");
        label.setFont(UITheme.smallFont().deriveFont(Font.BOLD));
        label.setForeground(UITheme.TEXT_SECONDARY);
        label.setPreferredSize(new Dimension(170, label.getPreferredSize().height));
        row.add(label, BorderLayout.WEST);

        valueLabel.setFont(UITheme.bodyFont());
        valueLabel.setForeground(UITheme.TEXT_PRIMARY);
        valueLabel.setBorder(new EmptyBorder(0, 8, 0, 0));
        row.add(valueLabel, BorderLayout.CENTER);
        return row;
    }

    private JPanel buildActionRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        row.setOpaque(false);

        JButton refreshButton = createSecondaryActionButton("🔄 Làm mới dữ liệu");
        refreshButton.addActionListener(e -> refreshFromServer());
        row.add(refreshButton);

        JButton editButton = createPrimaryActionButton("✏️ Chỉnh sửa thông tin");
        editButton.addActionListener(e -> openEditDialog());
        row.add(editButton);

        return row;
    }

    private JLabel createDetailValueLabel() {
        JLabel label = new JLabel("-");
        label.setFont(UITheme.bodyFont());
        label.setForeground(UITheme.TEXT_PRIMARY);
        return label;
    }

    private JLabel createStatValueLabel() {
        JLabel label = new JLabel("-");
        label.setFont(UITheme.subHeaderFont());
        label.setForeground(UITheme.TEXT_PRIMARY);
        return label;
    }

    private RoundedPanel createStatCard(String title, JLabel valueLabel, Color accent) {
        RoundedPanel card = new RoundedPanel(18, new Color(0xFFFFFF));
        card.setLayout(new BorderLayout(6, 4));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setPreferredSize(new Dimension(180, 90));

        String icon = getIconForTitle(title);
        JLabel titleLabel = new JLabel(icon + " " + title.toUpperCase());
        titleLabel.setFont(UITheme.smallFont().deriveFont(Font.BOLD));
        titleLabel.setForeground(accent.darker());
        card.add(titleLabel, BorderLayout.NORTH);

        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private RoundedPanel createChip(JLabel label) {
        label.setFont(UITheme.smallFont());
        label.setForeground(Color.WHITE);
        RoundedPanel chip = new RoundedPanel(20, new Color(255, 255, 255, 28));
        chip.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 4));
        chip.add(label);
        return chip;
    }

    private void openEditDialog() {
        javax.swing.JTextField nameField = new javax.swing.JTextField(sinhVien.getTenSv());
        javax.swing.JTextField emailField = new javax.swing.JTextField(sinhVien.getEmail());
        javax.swing.JTextField phoneField = new javax.swing.JTextField(sinhVien.getSdt());
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        String currentGender = sinhVien.getGioiTinh();
        if (currentGender != null && !currentGender.trim().isEmpty()) {
            boolean matched = false;
            for (int i = 0; i < genderCombo.getItemCount(); i++) {
                String item = genderCombo.getItemAt(i);
                if (item.equalsIgnoreCase(currentGender.trim())) {
                    genderCombo.setSelectedIndex(i);
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                genderCombo.addItem(currentGender.trim());
                genderCombo.setSelectedItem(currentGender.trim());
            }
        }
        javax.swing.JTextField queField = new javax.swing.JTextField(sinhVien.getQueQuan());
        javax.swing.JTextField addressField = new javax.swing.JTextField(sinhVien.getDiaChi());
        javax.swing.JTextField avatarField = new javax.swing.JTextField(sinhVien.getAnh());

        JButton browseBtn = new JButton("Chọn ảnh");
        browseBtn.setFont(UITheme.buttonFont());
        browseBtn.setBackground(UITheme.ACCENT);
        browseBtn.setForeground(Color.WHITE);
        browseBtn.setFocusPainted(false);
        browseBtn.setBorder(new EmptyBorder(8, 16, 8, 16));
        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                avatarField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        // Nâng cấp form: BoxLayout, label to, căn lề đẹp, đồng bộ theme
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(28, 32, 28, 32));
        form.setBackground(UITheme.BACKGROUND);

        form.add(createFormRow("👤 Họ và tên", nameField));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("👤 Giới tính", genderCombo));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("📧 Email", emailField));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("📞 Số điện thoại", phoneField));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("🏠 Quê quán", queField));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("🏡 Địa chỉ", addressField));
        form.add(Box.createVerticalStrut(12));
        JPanel avatarRow = new JPanel();
        avatarRow.setLayout(new BoxLayout(avatarRow, BoxLayout.X_AXIS));
        avatarRow.setOpaque(false);
        JLabel avatarLabelF = new JLabel("🖼️ Ảnh");
        avatarLabelF.setFont(UITheme.bodyFont().deriveFont(Font.BOLD, 16f));
        avatarLabelF.setPreferredSize(new Dimension(120, 32));
        avatarRow.add(avatarLabelF);
        avatarRow.add(Box.createHorizontalStrut(12));
        avatarField.setFont(UITheme.bodyFont());
        avatarRow.add(avatarField);
        avatarRow.add(Box.createHorizontalStrut(8));
        avatarRow.add(browseBtn);
        form.add(avatarRow);

        int result = JOptionPane.showConfirmDialog(this, form, "Cập nhật thông tin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (nameField.getText().trim().isEmpty()) {
            DialogUtil.showError(this, "Họ và tên không được để trống");
            return;
        }
        if (!ValidationUtil.isValidEmail(emailField.getText())) {
            DialogUtil.showError(this, "Email không hợp lệ");
            return;
        }
        if (!ValidationUtil.isValidPhone(phoneField.getText())) {
            DialogUtil.showError(this, "Số điện thoại không hợp lệ");
            return;
        }

        sinhVien.setTenSv(nameField.getText().trim());
        sinhVien.setEmail(emailField.getText().trim());
        sinhVien.setSdt(phoneField.getText().trim());
        sinhVien.setGioiTinh(genderCombo.getSelectedItem() != null ? genderCombo.getSelectedItem().toString() : null);
        sinhVien.setQueQuan(queField.getText().trim());
        sinhVien.setDiaChi(addressField.getText().trim());
        sinhVien.setAnh(avatarField.getText().trim());

        if (sinhVienService == null) {
            DialogUtil.showError(this, "Không có kết nối tới dịch vụ sinh viên.");
            return;
        }
        try {
            if (sinhVienService.update(sinhVien)) {
                DialogUtil.showInfo(this, "Cập nhật thành công.");
                refresh();
            } else {
                DialogUtil.showError(this, "Không thể cập nhật thông tin.");
            }
        } catch (Exception ex) {
            DialogUtil.showError(this, ex.getMessage());
        }

    }

    // Helper cho form row đẹp
    private JPanel createFormRow(String label, java.awt.Component field) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.bodyFont().deriveFont(Font.BOLD, 16f));
        lbl.setPreferredSize(new Dimension(120, 32));
        row.add(lbl);
        row.add(Box.createHorizontalStrut(12));
        field.setFont(UITheme.bodyFont());
        row.add(field);
        return row;
    }

    public void refresh() {
        if (sinhVien == null) {
            return;
        }

    heroNameLabel.setText(safeText(sinhVien.getTenSv()));
    heroStatusLabel.setText("");

        setChipText(chipMaSvLabel, "Mã SV: " + safeText(sinhVien.getSvId()));
        Lop lop = sinhVien.getLop();
        String tenLop = lop != null ? lop.getTenLop() : null;
        setChipText(chipLopLabel, "Lớp: " + safeText(tenLop));
        setChipText(chipNganhLabel, "Ngành: " + safeText(lop != null && lop.getNganh() != null ? lop.getNganh().getTenNganh() : null));
        setChipText(chipKhoaLabel, "Khoa: " + safeText(lop != null && lop.getKhoa() != null ? lop.getKhoa().getTenKhoa() : null));

        setLabelValue(maSvValueLabel, sinhVien.getSvId());
        setLabelValue(fullNameValueLabel, sinhVien.getTenSv());
        dobValueLabel.setText(formatDate(sinhVien.getNgaySinh()));
        genderValueLabel.setText(safeText(sinhVien.getGioiTinh()));

        setLabelValue(lopValueLabel, tenLop);
        setLabelValue(maLopValueLabel, lop != null ? lop.getMaLop() : null);
        setLabelValue(nganhValueLabel, lop != null && lop.getNganh() != null ? lop.getNganh().getTenNganh() : null);
        setLabelValue(khoaValueLabel, lop != null && lop.getKhoa() != null ? lop.getKhoa().getTenKhoa() : null);
        setLabelValue(khoaHocValueLabel, sinhVien.getKhoaHoc() != null ? sinhVien.getKhoaHoc().getTenKhoaHoc() : null);
        setLabelValue(hocKyValueLabel, sinhVien.getHocKyHienTai() != null ? sinhVien.getHocKyHienTai().getTenHocKy() : null);
        setLabelValue(namHocValueLabel, sinhVien.getNamHocHienTai() != null ? sinhVien.getNamHocHienTai().getTenNamHoc() : null);
        setLabelValue(trangThaiValueLabel, sinhVien.getStatus());

        setLabelValue(emailValueLabel, sinhVien.getEmail());
        setLabelValue(phoneValueLabel, sinhVien.getSdt());
        setLabelValue(queQuanValueLabel, sinhVien.getQueQuan());
        setLabelValue(addressValueLabel, sinhVien.getDiaChi());

        ngayTaoValueLabel.setText(formatDateTime(sinhVien.getNgayTao()));
        avatarPathValueLabel.setText(safeText(sinhVien.getAnh()));

        setLabelValue(statKhoaHocLabel, sinhVien.getKhoaHoc() != null ? sinhVien.getKhoaHoc().getTenKhoaHoc() : null);
        setLabelValue(statHocKyLabel, sinhVien.getHocKyHienTai() != null ? sinhVien.getHocKyHienTai().getTenHocKy() : null);
        setLabelValue(statNamHocLabel, sinhVien.getNamHocHienTai() != null ? sinhVien.getNamHocHienTai().getTenNamHoc() : null);
        setLabelValue(statTrangThaiLabel, sinhVien.getStatus());

        updateAcademicStanding();

        updateAvatar();
        revalidate();
        repaint();
    }

    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
        refresh();
    }

    public void refreshFromServer() {
        if (sinhVien == null || sinhVien.getSvId() == null) {
            DialogUtil.showError(this, "Không thể làm mới vì thiếu thông tin sinh viên.");
            return;
        }
        if (sinhVienService == null) {
            DialogUtil.showError(this, "Không có kết nối tới dịch vụ sinh viên.");
            return;
        }
        try {
            SinhVien updated = sinhVienService.getById(sinhVien.getSvId());
            if (updated != null) {
                this.sinhVien = updated;
                refresh();
                DialogUtil.showInfo(this, "Thông tin đã được làm mới.");
            } else {
                DialogUtil.showError(this, "Không tìm thấy thông tin sinh viên.");
            }
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể cập nhật thông tin: " + e.getMessage());
        }
    }

    private void updateAcademicStanding() {
        Double gpa = sinhVien != null ? sinhVien.getGpa() : null;
        if (sinhVien != null && sinhVien.getSvId() != null && diemService != null) {
            try {
                double computed = diemService.calculateGpa(sinhVien.getSvId());
                gpa = Double.isNaN(computed) ? null : computed;
                academicDataErrorShown = false;
            } catch (Exception e) {
                if (!academicDataErrorShown) {
                    DialogUtil.showError(this, "Không thể tính GPA: " + e.getMessage());
                    academicDataErrorShown = true;
                }
            }
        }

        String gpaText = (gpa != null) ? String.format("%.2f", gpa) : "-";
        setLabelValue(gpaValueLabel, gpaText);
        setLabelValue(statGpaLabel, gpaText);

        String rank = AcademicStandingUtil.classifyByGpa(gpa);
        if ("-".equals(rank) && sinhVien != null && sinhVien.getAcademicRank() != null && !sinhVien.getAcademicRank().isBlank()) {
            rank = sinhVien.getAcademicRank();
        }
        setLabelValue(hocLucValueLabel, rank);
        setLabelValue(statHocLucLabel, rank);

        if (sinhVien != null) {
            sinhVien.setGpa(gpa);
            sinhVien.setAcademicRank("-".equals(rank) ? null : rank);
        }
    }

    private String safeText(String value) {
        return value != null && !value.trim().isEmpty() ? value.trim() : "-";
    }

    private void setLabelValue(JLabel label, String value) {
        String text = safeText(value);
        label.setText(text);
        label.setToolTipText("-".equals(text) ? null : value);
    }

    private String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : "-";
    }

    private String formatDateTime(Date date) {
        return date != null ? dateTimeFormat.format(date) : "-";
    }

    private void setChipText(JLabel label, String text) {
        label.setText(text);
        label.setToolTipText(text.contains(": -") ? null : text);
    }

    private void updateAvatar() {
        String rawPath = sinhVien != null ? sinhVien.getAnh() : null;
        if (rawPath != null && !rawPath.isBlank()) {
            BufferedImage image = loadAvatarImage(rawPath.trim());
            if (image != null) {
                ImageIcon icon = ImageUtil.toIcon(image, 140, 160);
                avatarLabel.setIcon(icon != null ? icon : new ImageIcon(image));
                avatarLabel.setText(null);
                avatarLabel.setBackground(Color.WHITE);
                avatarLabel.setToolTipText(rawPath);
                return;
            }
        }
        avatarLabel.setIcon(null);
        avatarLabel.setText(getInitials());
        avatarLabel.setBackground(new Color(255, 255, 255, 28));
        avatarLabel.setToolTipText(null);
    }

    private BufferedImage loadAvatarImage(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        // Data URI (base64 embedded image)
        if (path.startsWith("data:image")) {
            int commaIndex = path.indexOf(',');
            if (commaIndex > 0 && commaIndex < path.length() - 1) {
                String base64 = path.substring(commaIndex + 1);
                try {
                    byte[] bytes = Base64.getDecoder().decode(base64);
                    try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
                        return ImageIO.read(in);
                    }
                } catch (IllegalArgumentException | IOException ignored) {
                }
            }
        }

        // Remote URLs (http, https, ftp)
        if (path.matches("(?i)^(https?|ftp)://.*")) {
            try {
                URL url = URI.create(path).toURL();
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                try (InputStream in = connection.getInputStream()) {
                    return ImageIO.read(in);
                }
            } catch (IOException ignored) {
            }
        }

        // file: URI
        if (path.startsWith("file:")) {
            try (InputStream in = URI.create(path).toURL().openStream()) {
                return ImageIO.read(in);
            } catch (IOException ignored) {
            }
        }

        // Local filesystem fall-back
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            try {
                return ImageIO.read(file);
            } catch (IOException ignored) {
            }
        }

        // Classpath resource (allow relative resource names)
        String resourcePath = path.startsWith("/") ? path : "/" + path;
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in != null) {
                return ImageIO.read(in);
            }
        } catch (IOException ignored) {
        }

        return null;
    }

    private String getInitials() {
        String name = sinhVien != null ? sinhVien.getTenSv() : null;
        if (name == null || name.trim().isEmpty()) {
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

    private String getIconForSection(String title) {
        switch (title) {
            case "Thông tin cá nhân": return "👤";
            case "Thông tin học tập": return "🎓";
            case "Liên hệ": return "📞";
            case "Thông tin hồ sơ": return "📄";
            default: return "";
        }
    }

    private String getIconForTitle(String title) {
        switch (title) {
            case "Khóa học": return "🎓";
            case "Học kỳ": return "📅";
            case "Năm học": return "📆";
            case "GPA": return "📈";
            case "Học lực": return "🏅";
            case "Trạng thái": return "📊";
            default: return "";
        }
    }

    private JButton createPrimaryActionButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(UITheme.ACCENT);
        button.setForeground(Color.WHITE);
        button.setFont(UITheme.buttonFont().deriveFont(Font.BOLD, 14f));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        return button;
    }

    private JButton createSecondaryActionButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0xE2E8F0));
        button.setForeground(UITheme.PRIMARY_DARK);
        button.setFont(UITheme.buttonFont().deriveFont(Font.BOLD, 14f));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        return button;
    }

}
