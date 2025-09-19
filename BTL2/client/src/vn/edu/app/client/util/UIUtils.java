package vn.edu.app.client.util;

import javax.swing.*;
import java.awt.*;

public class UIUtils {
	public static ImageIcon icon(String path, int w, int h) {
	    try {
	        java.net.URL url = UIUtils.class.getClassLoader().getResource(path);
	        if (url == null) {
	            System.err.println("⚠ Icon not found: " + path);
	            return null;
	        }
	        ImageIcon icon = new ImageIcon(url);
	        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
	        return new ImageIcon(img);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}


    public static void styleSidebarButton(AbstractButton btn) {
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(60,65,85));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 10));
        btn.addChangeListener(e -> {
            if (btn.getModel().isRollover()) btn.setBackground(new Color(85,90,110));
            else btn.setBackground(new Color(60,65,85));
        });
    }

    public static void info(java.awt.Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Info",
                JOptionPane.INFORMATION_MESSAGE, icon("icons/success.png", 28, 28));
    }

    public static void error(java.awt.Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error",
                JOptionPane.ERROR_MESSAGE, icon("icons/error.png", 28, 28));
    }
    
}
