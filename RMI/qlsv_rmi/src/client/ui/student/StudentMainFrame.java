package client.ui.student;

import client.ui.LoginFrame;
import client.ui.components.RoundedPanel;
import client.util.DialogUtil;
import client.util.UITheme;
import common.dto.SinhVien;
import common.dto.TaiKhoan;
import common.service.DiemService;
import common.service.SinhVienService;
import client.network.ClientConnector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;

public class StudentMainFrame extends JFrame {
    private SinhVien sinhVien;
    private final SinhVienService sinhVienService;
    private final DiemService diemService;

    private JPanel profilePanel;
    private StudentScorePanel scorePanel;

    public StudentMainFrame(TaiKhoan taiKhoan, SinhVien sinhVien) {
    super("Sinh viên - Quản lý sinh viên");
    this.sinhVien = sinhVien;
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1500, 820);
    setLocationRelativeTo(null);

        SinhVienService svService;
        DiemService dService;
        try {
            svService = ClientConnector.getSinhVienService();
            dService = ClientConnector.getDiemService();
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể kết nối server: " + e.getMessage());
            svService = null;
            dService = null;
        }
        this.sinhVienService = svService;
        this.diemService = dService;

        // Always refresh student data from server to get complete information
        if (taiKhoan.getSvId() != null && sinhVienService != null) {
            try {
                SinhVien freshData = sinhVienService.getById(taiKhoan.getSvId());
                if (freshData != null) {
                    this.sinhVien = freshData;
                } else if (this.sinhVien == null) {
                    DialogUtil.showError(this, "Không tìm thấy thông tin sinh viên.");
                    dispose();
                    LoginFrame.showLogin();
                    return;
                }
            } catch (Exception e) {
                DialogUtil.showError(this, "Không tìm thấy thông tin sinh viên: " + e.getMessage());
                if (this.sinhVien == null) {
                    dispose();
                    LoginFrame.showLogin();
                    return;
                }
            }
        }

        if (this.sinhVien == null) {
            DialogUtil.showError(this, "Không thể khởi tạo màn hình sinh viên.");
            dispose();
            LoginFrame.showLogin();
            return;
        }

    profilePanel = new StudentProfilePanel(this.sinhVien, sinhVienService, diemService);
        scorePanel = new StudentScorePanel(this.sinhVien, diemService);

        buildUI();
        // Thông báo đăng nhập thành công
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        });
        setVisible(true);

        if (profilePanel instanceof StudentProfilePanel studentProfile) {
            studentProfile.refresh();
        }
    }

    private void buildUI() {
        getContentPane().setBackground(UITheme.BACKGROUND);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.PRIMARY);
        header.setBorder(new EmptyBorder(18, 24, 18, 24));

        javax.swing.JLabel titleLabel = new javax.swing.JLabel("Xin chào, " + sinhVien.getTenSv());
        titleLabel.setFont(UITheme.headerFont());
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Đăng xuất");
        logoutBtn.setBackground(UITheme.DANGER);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(UITheme.buttonFont());
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(new EmptyBorder(10, 18, 10, 18));
        logoutBtn.addActionListener(e -> {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                dispose();
                SwingUtilities.invokeLater(LoginFrame::showLogin);
            }
        });
        header.add(logoutBtn, BorderLayout.EAST);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.subHeaderFont());
        tabbedPane.setForeground(UITheme.TEXT_PRIMARY);
        tabbedPane.setOpaque(false);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());
        tabbedPane.addTab("Thông tin cá nhân", profilePanel);
        tabbedPane.addTab("Bảng điểm", scorePanel);

        RoundedPanel tabWrapper = new RoundedPanel(24, UITheme.CARD_BACKGROUND);
        tabWrapper.setLayout(new BorderLayout());
        tabWrapper.setBorder(new EmptyBorder(24, 24, 24, 24));
        tabWrapper.add(tabbedPane, BorderLayout.CENTER);

        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(24, 24, 24, 24));
        body.add(tabWrapper, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
    }
}
