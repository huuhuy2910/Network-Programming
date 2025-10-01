package client.ui.admin;

import client.ui.components.RoundedPanel;
import client.util.UITheme;
import common.dto.TaiKhoan;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdminMainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private final Map<String, JPanel> panels = new LinkedHashMap<>();
    private final TaiKhoan currentUser;

    public AdminMainFrame(TaiKhoan taiKhoan) {
        super("Quản trị - Quản lý sinh viên");
        this.currentUser = taiKhoan;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 820);
        setLocationRelativeTo(null);
        buildMenu();
        buildContent();
        initPanels();
        if (!panels.isEmpty()) {
            cardLayout.show(contentPanel, panels.keySet().iterator().next());
        }
        // Thông báo đăng nhập thành công
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        });
        setVisible(true);
    }

    private void buildMenu() {
        panels.put("📊 Trang thống kê", new DashboardPanel());
        panels.put("🎓 Quản lý Sinh viên", new SinhVienPanel());
        panels.put("🏫 Quản lý Lớp", new LopPanel());
        panels.put("🏫 Quản lý Ngành", new NganhPanel());
        panels.put("🏛️ Quản lý Khoa", new KhoaPanel());
        panels.put("📚 Quản lý Khóa học", new KhoaHocPanel());
        panels.put("📅 Quản lý Học kỳ", new HocKyPanel());
        panels.put("📆 Quản lý Năm học", new NamHocPanel());
        panels.put("📖 Quản lý Học phần", new HocPhanPanel());
        panels.put("👥 Quản lý Tài khoản", new TaiKhoanPanel());
    }

    private void buildContent() {
        JList<String> menuList = new JList<>(panels.keySet().toArray(new String[0]));
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setFont(UITheme.subHeaderFont());
        menuList.setBackground(UITheme.PRIMARY_DARK);
        menuList.setForeground(Color.WHITE);
        menuList.setFixedCellHeight(54);
        menuList.setCellRenderer(new MenuListRenderer());
        menuList.setSelectedIndex(0);
        menuList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = menuList.getSelectedValue();
                if (selected != null) {
                    cardLayout.show(contentPanel, selected);
                }
            }
        });
        JScrollPane menuScroll = new JScrollPane(menuList);
        menuScroll.setBorder(BorderFactory.createEmptyBorder());


        JButton logoutButton = new JButton("🚪 Đăng xuất");
        logoutButton.setFont(UITheme.buttonFont());
        logoutButton.setBackground(UITheme.DANGER);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(new EmptyBorder(10, 18, 10, 18));
        logoutButton.addActionListener(e -> {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                dispose();
                SwingUtilities.invokeLater(() -> client.ui.LoginFrame.showLogin());
            }
        });

        JPanel sidePanel = new JPanel(new BorderLayout(0, 12));
        sidePanel.setBackground(UITheme.PRIMARY_DARK);
        sidePanel.setBorder(new EmptyBorder(24, 16, 24, 16));
        sidePanel.add(menuScroll, BorderLayout.CENTER);
        sidePanel.add(logoutButton, BorderLayout.SOUTH);

        contentPanel.setOpaque(false);
        RoundedPanel contentWrapper = new RoundedPanel(24, UITheme.CARD_BACKGROUND);
        contentWrapper.setLayout(new BorderLayout());
        contentWrapper.setBorder(new EmptyBorder(24, 24, 24, 24));
        contentWrapper.add(contentPanel, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidePanel, contentWrapper);
        splitPane.setDividerLocation(240);
        splitPane.setResizeWeight(0);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setBackground(UITheme.BACKGROUND);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(UITheme.BACKGROUND);
        rootPanel.add(buildHeader(), BorderLayout.NORTH);
        rootPanel.add(splitPane, BorderLayout.CENTER);
        setContentPane(rootPanel);
    }

    private void initPanels() {
        panels.forEach((name, panel) -> contentPanel.add(panel, name));
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.PRIMARY);
        header.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel titleLabel = new JLabel("Bảng điều khiển quản trị");
        titleLabel.setFont(UITheme.headerFont());
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);

        JLabel userLabel = new JLabel("Xin chào, " + (currentUser != null ? currentUser.getDisplayName() : "Admin"));
        userLabel.setFont(UITheme.bodyFont());
        userLabel.setForeground(Color.WHITE);
        header.add(userLabel, BorderLayout.EAST);

        return header;
    }

    private static class MenuListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setBorder(new EmptyBorder(10, 16, 10, 16));
            setFont(UITheme.bodyFont().deriveFont(Font.BOLD, UITheme.bodyFont().getSize2D() + 2f));
            if (isSelected) {
                setBackground(UITheme.PRIMARY);
                setForeground(Color.WHITE);
            } else {
                setBackground(UITheme.PRIMARY_DARK);
                setForeground(new Color(0xE8F1FF));
            }
            return component;
        }
    }
}
