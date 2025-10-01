package client.util;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Component;

public final class DialogUtil {
    private DialogUtil() {
    }

    public static void showInfo(Component parent, String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(parent, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE));
    }

    public static void showError(Component parent, String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(parent, message, "Lỗi", JOptionPane.ERROR_MESSAGE));
    }

    public static boolean confirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
}
