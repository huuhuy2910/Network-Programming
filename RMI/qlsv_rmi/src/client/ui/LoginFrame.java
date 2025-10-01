package client.ui;

import client.network.ClientConnector;
import client.ui.admin.AdminMainFrame;
import client.ui.student.StudentMainFrame;
import client.util.DialogUtil;
import client.util.FlatLafSetup;
import common.dto.SinhVien;
import common.dto.TaiKhoan;
import common.service.SinhVienService;
import common.service.TaiKhoanService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private TaiKhoanService taiKhoanService;
    private SinhVienService sinhVienService;

    public LoginFrame() {
        super("Đăng nhập - Quản lý sinh viên");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        initServices();
        buildUI();
    }

    private void initServices() {
        try {
            taiKhoanService = ClientConnector.getTaiKhoanService();
            sinhVienService = ClientConnector.getSinhVienService();
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể kết nối tới server: " + e.getMessage());
        }
    }

    private void buildUI() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("🎓 Hệ thống quản lý sinh viên") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x0A3981), getWidth(), getHeight(), new Color(0x0A6CF7));
                g2.setPaint(gp);
                g2.fill(shape);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setOpaque(false);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
        container.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;
        gbc.insets = new java.awt.Insets(8, 8, 8, 8);

        formPanel.add(new JLabel("Tên đăng nhập"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        usernameField.setPreferredSize(new Dimension(180, 32));
        formPanel.add(usernameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Mật khẩu"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        passwordField.setPreferredSize(new Dimension(180, 32));
        formPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("🔐 Đăng nhập");
        loginButton.setBackground(new Color(0x0A6CF7));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(new EmptyBorder(12, 20, 12, 20));
        loginButton.addActionListener(e -> performLogin());

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });

        gbc.gridy++;
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(loginButton, gbc);

        container.add(formPanel, BorderLayout.CENTER);

        add(container);
    }

    private void performLogin() {
        if (taiKhoanService == null) {
            DialogUtil.showError(this, "Chưa kết nối được tới server.");
            return;
        }
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            DialogUtil.showError(this, "Vui lòng nhập tên đăng nhập và mật khẩu.");
            return;
        }
        try {
            TaiKhoan taiKhoan = taiKhoanService.login(username, password);
            if (taiKhoan == null) {
                DialogUtil.showError(this, "Đăng nhập thất bại. Kiểm tra thông tin.");
                return;
            }
            dispose();
            if (taiKhoan.isAdmin()) {
                SwingUtilities.invokeLater(() -> new AdminMainFrame(taiKhoan));
            } else if (taiKhoan.isSinhVien()) {
                SinhVien sinhVien = sinhVienService.getById(taiKhoan.getSvId());
                SwingUtilities.invokeLater(() -> new StudentMainFrame(taiKhoan, sinhVien));
            } else {
                DialogUtil.showError(this, "Role không hợp lệ.");
                showLogin();
            }
        } catch (Exception e) {
            DialogUtil.showError(this, "Lỗi: " + e.getMessage());
        }
    }

    public static void showLogin() {
        FlatLafSetup.install();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
