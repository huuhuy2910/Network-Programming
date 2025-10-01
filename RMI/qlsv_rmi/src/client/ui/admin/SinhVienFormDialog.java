package client.ui.admin;

import client.ui.components.RequiredLabel;
import client.ui.components.RoundedPanel;
import client.util.DialogUtil;
import client.util.UITheme;
import client.util.ValidationUtil;
import common.dto.HocKy;
import common.dto.KhoaHoc;
import common.dto.Lop;
import common.dto.NamHoc;
import common.dto.SinhVien;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SinhVienFormDialog extends JDialog {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField dobField = new JTextField();
    private final JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
    private final JTextField queQuanField = new JTextField();
    private final JTextField sdtField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextArea diaChiArea = new JTextArea(3, 20);
    private final JTextField anhField = new JTextField();
    private final JComboBox<Lop> lopCombo = new JComboBox<>();
    private final JComboBox<KhoaHoc> khoaHocCombo = new JComboBox<>();
    private final JComboBox<HocKy> hocKyCombo = new JComboBox<>();
    private final JComboBox<NamHoc> namHocCombo = new JComboBox<>();
    private final JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Đang học", "Tạm dừng", "Đã tốt nghiệp"});

    private boolean confirmed = false;
    private SinhVien sinhVien;

    public SinhVienFormDialog(Window owner, List<Lop> lopList, List<KhoaHoc> khoaHocList,
                              List<HocKy> hocKyList, List<NamHoc> namHocList, SinhVien sinhVien) {
        super(owner, sinhVien == null ? "Thêm sinh viên" : "Cập nhật sinh viên", ModalityType.APPLICATION_MODAL);
        this.sinhVien = sinhVien != null ? sinhVien : new SinhVien();
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setSize(720, 620);
        setLocationRelativeTo(owner);

        lopCombo.setEditable(false);
        lopCombo.removeAllItems();
        if (lopList != null) {
            lopList.forEach(lopCombo::addItem);
        }
        khoaHocCombo.removeAllItems();
        if (khoaHocList != null) {
            khoaHocList.forEach(khoaHocCombo::addItem);
        }
        hocKyCombo.removeAllItems();
        if (hocKyList != null) {
            hocKyList.forEach(hocKyCombo::addItem);
        }
        namHocCombo.removeAllItems();
        if (namHocList != null) {
            namHocList.forEach(namHocCombo::addItem);
        }
        statusCombo.setEditable(true);

        configureInputs();

        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        root.setBackground(UITheme.BACKGROUND);

        JPanel headerPanel = createHeaderPanel();
        root.add(headerPanel, BorderLayout.NORTH);

        RoundedPanel formCard = new RoundedPanel(24, UITheme.CARD_BACKGROUND);
        formCard.setLayout(new BorderLayout());
        formCard.setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        addSectionTitle(formPanel, gbc, "Thông tin cá nhân");
        addField(formPanel, gbc, new RequiredLabel("🆔 Mã sinh viên"), idField);
        addField(formPanel, gbc, new RequiredLabel("👤 Họ tên"), nameField);
        addField(formPanel, gbc, new RequiredLabel("🎂 Ngày sinh (dd/MM/yyyy)"), dobField);
        addField(formPanel, gbc, new JLabel("⚧ Giới tính"), genderCombo);
        addField(formPanel, gbc, new JLabel("🏠 Quê quán"), queQuanField);
        addField(formPanel, gbc, new JLabel("📞 Số điện thoại"), sdtField);
        addField(formPanel, gbc, new JLabel("📧 Email"), emailField);

        addSectionTitle(formPanel, gbc, "Thông tin học tập");
        addField(formPanel, gbc, new JLabel("🏫 Lớp"), lopCombo);
        addField(formPanel, gbc, new JLabel("🎓 Khóa học"), khoaHocCombo);
        addField(formPanel, gbc, new JLabel("📅 Học kỳ hiện tại"), hocKyCombo);
        addField(formPanel, gbc, new JLabel("📆 Năm học"), namHocCombo);
        addField(formPanel, gbc, new JLabel("📊 Trạng thái"), statusCombo);

        addSectionTitle(formPanel, gbc, "Thông tin bổ sung");
        JScrollPane addressScroll = new JScrollPane(diaChiArea);
        addressScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        addressScroll.setPreferredSize(new java.awt.Dimension(0, 96));
        addField(formPanel, gbc, new JLabel("📍 Địa chỉ"), addressScroll);

        JPanel imagePanel = new JPanel(new BorderLayout(8, 0));
        imagePanel.setOpaque(false);
        imagePanel.add(anhField, BorderLayout.CENTER);
        JButton browseBtn = new JButton("🖼️ Chọn ảnh");
        browseBtn.addActionListener(e -> chooseImage());
        styleSecondaryButton(browseBtn);
        imagePanel.add(browseBtn, BorderLayout.EAST);
        addField(formPanel, gbc, new JLabel("🗂️ Đường dẫn ảnh"), imagePanel);

        populateData();

        formCard.add(formPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(formCard);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        root.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setBackground(UITheme.CARD_BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(16, 24, 16, 24));
    JButton cancelBtn = new JButton("✖ Hủy");
    styleSecondaryButton(cancelBtn);
    cancelBtn.setBackground(UITheme.DANGER);
    cancelBtn.setForeground(Color.WHITE);
    cancelBtn.addActionListener(e -> dispose());
        JButton saveBtn = new JButton("💾 Lưu");
        stylePrimaryButton(saveBtn);
        saveBtn.addActionListener(e -> onSave());
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        root.add(buttonPanel, BorderLayout.SOUTH);

        add(root, BorderLayout.CENTER);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, Component labelComponent, Component fieldComponent) {
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.insets = new Insets(8, 0, 8, 16);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(styleLabel(labelComponent), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(8, 0, 8, 0);
        panel.add(styleField(fieldComponent), gbc);

        gbc.gridy++;
    }

    private void configureInputs() {
        styleTextField(idField);
        styleTextField(nameField);
        styleTextField(dobField);
        styleTextField(queQuanField);
        styleTextField(sdtField);
        styleTextField(emailField);
        styleTextField(anhField);

        styleComboBox(genderCombo);
        styleComboBox(lopCombo);
        styleComboBox(khoaHocCombo);
        styleComboBox(hocKyCombo);
        styleComboBox(namHocCombo);
        styleComboBox(statusCombo);

        styleTextArea(diaChiArea);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, UITheme.PRIMARY, getWidth(), getHeight(), UITheme.PRIMARY_DARK);
                g2.setPaint(gradient);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 28, 28));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        header.setOpaque(false);
        header.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(24, 28, 24, 28));

        String titleText = sinhVien.getSvId() != null ? "🎓 Cập nhật sinh viên" : "🎓 Thêm sinh viên";
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(UITheme.headerFont().deriveFont(Font.BOLD, 26f));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Quản lý đầy đủ thông tin hồ sơ và học tập");
        subtitleLabel.setFont(UITheme.bodyFont());
        subtitleLabel.setForeground(new Color(255, 255, 255, 210));

        content.add(titleLabel);
        content.add(Box.createVerticalStrut(6));
        content.add(subtitleLabel);

        header.add(content, BorderLayout.CENTER);
        return header;
    }

    private void addSectionTitle(JPanel panel, GridBagConstraints gbc, String title) {
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.insets = new Insets(gbc.gridy == 0 ? 0 : 20, 0, 6, 0);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UITheme.subHeaderFont());
        titleLabel.setForeground(UITheme.PRIMARY_DARK);
        panel.add(titleLabel, gbc);
        gbc.gridy++;

    JPanel separator = new JPanel();
    separator.setOpaque(true);
    separator.setBackground(new Color(0xE2E8F0));
    separator.setPreferredSize(new Dimension(0, 2));
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(separator, gbc);
        gbc.gridy++;

        gbc.gridwidth = 1;
    }

    private Component styleLabel(Component component) {
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            label.setFont(UITheme.smallFont().deriveFont(Font.BOLD));
            label.setForeground(UITheme.TEXT_SECONDARY);
        }
        return component;
    }

    private Component styleField(Component component) {
        if (component instanceof JTextField) {
            styleTextField((JTextField) component);
        } else if (component instanceof JComboBox<?>) {
            styleComboBox((JComboBox<?>) component);
        } else if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xD1D9E6), 1, true));
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(true);
            scrollPane.getViewport().setBackground(Color.WHITE);
            Component view = scrollPane.getViewport().getView();
            if (view instanceof JTextArea) {
                styleTextArea((JTextArea) view);
            }
        } else if (component instanceof JTextArea) {
            styleTextArea((JTextArea) component);
        } else if (component instanceof JLabel) {
            JLabel valueLabel = (JLabel) component;
            valueLabel.setFont(UITheme.bodyFont());
            valueLabel.setForeground(UITheme.TEXT_PRIMARY);
        }
        return component;
    }

    private void styleTextField(JTextField field) {
        field.setFont(UITheme.bodyFont());
        field.setPreferredSize(new Dimension(280, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xD1D9E6), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(UITheme.bodyFont());
        comboBox.setPreferredSize(new Dimension(280, 38));
    }

    private void styleTextArea(JTextArea area) {
        area.setFont(UITheme.bodyFont());
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(10, 12, 10, 12));
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(UITheme.ACCENT);
        button.setForeground(Color.WHITE);
        button.setFont(UITheme.buttonFont().deriveFont(Font.BOLD, 14f));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(new Color(0xE2E8F0));
        button.setForeground(UITheme.PRIMARY_DARK);
        button.setFont(UITheme.buttonFont().deriveFont(Font.BOLD, 14f));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    private void populateData() {
        if (sinhVien.getSvId() != null) {
            idField.setText(sinhVien.getSvId());
            idField.setEnabled(false);
        }
        if (sinhVien.getTenSv() != null) {
            nameField.setText(sinhVien.getTenSv());
        }
        if (sinhVien.getNgaySinh() != null) {
            dobField.setText(DATE_FORMAT.format(sinhVien.getNgaySinh()));
        }
        if (sinhVien.getGioiTinh() != null) {
            genderCombo.setSelectedItem(sinhVien.getGioiTinh());
        }
        queQuanField.setText(sinhVien.getQueQuan());
        sdtField.setText(sinhVien.getSdt());
        emailField.setText(sinhVien.getEmail());
        diaChiArea.setText(sinhVien.getDiaChi());
        anhField.setText(sinhVien.getAnh());
        if (sinhVien.getLop() != null) {
            lopCombo.setSelectedItem(sinhVien.getLop());
        }
        if (sinhVien.getKhoaHoc() != null) {
            khoaHocCombo.setSelectedItem(sinhVien.getKhoaHoc());
        }
        if (sinhVien.getHocKyHienTai() != null) {
            hocKyCombo.setSelectedItem(sinhVien.getHocKyHienTai());
        }
        if (sinhVien.getNamHocHienTai() != null) {
            namHocCombo.setSelectedItem(sinhVien.getNamHocHienTai());
        }
        if (sinhVien.getStatus() != null) {
            statusCombo.setSelectedItem(sinhVien.getStatus());
        }
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            anhField.setText(path);
        }
    }

    private void onSave() {
        if (!ValidationUtil.isNotBlank(idField.getText()) || !ValidationUtil.isNotBlank(nameField.getText())) {
            DialogUtil.showError(this, "Mã sinh viên và họ tên là bắt buộc.");
            return;
        }
        if (!ValidationUtil.isValidEmail(emailField.getText())) {
            DialogUtil.showError(this, "Email không hợp lệ.");
            return;
        }
        if (!ValidationUtil.isValidPhone(sdtField.getText())) {
            DialogUtil.showError(this, "Số điện thoại không hợp lệ.");
            return;
        }

        Date dob = null;
        if (ValidationUtil.isNotBlank(dobField.getText())) {
            try {
                dob = DATE_FORMAT.parse(dobField.getText().trim());
            } catch (ParseException e) {
                DialogUtil.showError(this, "Định dạng ngày sinh không hợp lệ. Sử dụng dd/MM/yyyy");
                return;
            }
        }

        Lop selectedLop = (Lop) lopCombo.getSelectedItem();
        if (selectedLop == null) {
            DialogUtil.showError(this, "Vui lòng chọn lớp.");
            return;
        }
        KhoaHoc selectedKhoaHoc = (KhoaHoc) khoaHocCombo.getSelectedItem();
        HocKy selectedHocKy = (HocKy) hocKyCombo.getSelectedItem();
        NamHoc selectedNamHoc = (NamHoc) namHocCombo.getSelectedItem();
        if (selectedKhoaHoc == null || selectedHocKy == null || selectedNamHoc == null) {
            DialogUtil.showError(this, "Vui lòng chọn đầy đủ khóa học, học kỳ và năm học.");
            return;
        }

        sinhVien.setSvId(idField.getText().trim());
        sinhVien.setTenSv(nameField.getText().trim());
        sinhVien.setNgaySinh(dob);
        sinhVien.setGioiTinh((String) genderCombo.getSelectedItem());
        sinhVien.setQueQuan(queQuanField.getText().trim());
        sinhVien.setSdt(sdtField.getText().trim());
        sinhVien.setEmail(emailField.getText().trim());
        sinhVien.setDiaChi(diaChiArea.getText().trim());
        sinhVien.setAnh(anhField.getText().trim());
        sinhVien.setLop(selectedLop);
        sinhVien.setKhoaHoc(selectedKhoaHoc);
        sinhVien.setHocKyHienTai(selectedHocKy);
        sinhVien.setNamHocHienTai(selectedNamHoc);
        String status = (String) statusCombo.getSelectedItem();
        sinhVien.setStatus(status != null && !status.isBlank() ? status.trim() : "Đang học");
        if (sinhVien.getNgayTao() == null) {
            sinhVien.setNgayTao(new Date());
        }

        confirmed = true;
        dispose();
    }

    public SinhVien showDialog() {
        setVisible(true);
        return confirmed ? sinhVien : null;
    }
}
