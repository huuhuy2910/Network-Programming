package client.util;

import client.ui.components.RoundedPanel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.awt.Dimension;

public final class FormUIUtil {
    private FormUIUtil() {
    }

    public static JTextField createTextField() {
        JTextField field = new JTextField();
        styleTextComponent(field);
        return field;
    }

    public static void styleTextComponent(JTextComponent component) {
        component.setFont(UITheme.bodyFont());
        component.setPreferredSize(new Dimension(280, 38));
        component.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xD1D9E6), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
    }

    public static <T> JComboBox<T> createComboBox() {
        JComboBox<T> comboBox = new JComboBox<>();
        comboBox.setFont(UITheme.bodyFont());
        comboBox.setPreferredSize(new Dimension(280, 38));
        return comboBox;
    }

    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(UITheme.ACCENT);
        button.setForeground(Color.WHITE);
        button.setFont(UITheme.buttonFont());
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        return button;
    }

    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0xE2E8F0));
        button.setForeground(UITheme.PRIMARY_DARK);
        button.setFont(UITheme.buttonFont());
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        return button;
    }

    public static void styleLabel(java.awt.Component label) {
        if (label instanceof JLabel) {
            JLabel lbl = (JLabel) label;
            lbl.setFont(UITheme.smallFont().deriveFont(java.awt.Font.BOLD));
            lbl.setForeground(UITheme.TEXT_SECONDARY);
        }
    }

    public static RoundedPanel createFormCard() {
        RoundedPanel card = new RoundedPanel(24, UITheme.CARD_BACKGROUND);
        card.setBorder(new EmptyBorder(24, 28, 24, 28));
        card.setLayout(new java.awt.BorderLayout(16, 16));
        return card;
    }
}
