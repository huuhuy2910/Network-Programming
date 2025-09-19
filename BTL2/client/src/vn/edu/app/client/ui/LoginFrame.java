package vn.edu.app.client.ui;

import vn.edu.app.client.remote.RMIConnector;
import vn.edu.app.client.util.UIUtils;
import vn.edu.app.common.dto.UserDTO;
import vn.edu.app.common.remote.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField tfUsername = new JTextField(15);
    private JPasswordField tfPassword = new JPasswordField(15);

    public LoginFrame() {
        setTitle("Student Management - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(380, 280);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBorder(new EmptyBorder(20, 30, 20, 30));
        root.setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("User Login", SwingConstants.CENTER);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 20f));
        root.add(lblTitle, BorderLayout.NORTH);

        // Form panel
        JPanel form = new JPanel(new GridLayout(2, 1, 15, 15));
        form.setOpaque(false);

        // Username with icon
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(new Color(240, 240, 240));
        userPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel userIcon = new JLabel(UIUtils.icon("icons/user.png", 20, 20));
        userIcon.setBorder(new EmptyBorder(0, 10, 0, 10));
        userPanel.add(userIcon, BorderLayout.WEST);
        tfUsername.setBorder(null);
        tfUsername.setOpaque(false);
        userPanel.add(tfUsername, BorderLayout.CENTER);

        // Password with icon
        JPanel passPanel = new JPanel(new BorderLayout());
        passPanel.setBackground(new Color(240, 240, 240));
        passPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel passIcon = new JLabel(UIUtils.icon("icons/password.png", 20, 20));
        passIcon.setBorder(new EmptyBorder(0, 10, 0, 10));
        passPanel.add(passIcon, BorderLayout.WEST);
        tfPassword.setBorder(null);
        tfPassword.setOpaque(false);
        passPanel.add(tfPassword, BorderLayout.CENTER);

        form.add(userPanel);
        form.add(passPanel);

        root.add(form, BorderLayout.CENTER);

        // Login button
        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(76, 175, 80));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(100, 35));

        btnLogin.addActionListener(this::doLogin);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actions.setOpaque(false);
        actions.add(btnLogin);

        root.add(actions, BorderLayout.SOUTH);

        setContentPane(root);
        getRootPane().setDefaultButton(btnLogin);
    }

    private void doLogin(ActionEvent e) {
        try {
            AuthService auth = RMIConnector.auth();
            String u = tfUsername.getText().trim();
            String p = new String(tfPassword.getPassword());
            if (u.isEmpty() || p.isEmpty()) {
                UIUtils.error(this, "Please enter username & password");
                return;
            }

            UserDTO user = auth.login(u, p);
            if (user != null) {
                UIUtils.info(this, "Welcome " + user.getUsername());
                new MainFrame(user).setVisible(true);
                dispose();
            } else {
                UIUtils.error(this, "Invalid username or password");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            UIUtils.error(this, "Connection error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
