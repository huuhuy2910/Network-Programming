package client.ui.components;

import client.util.FormUIUtil;
import client.util.UITheme;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

public class ModernFormDialog extends JDialog {
    private final JPanel formPanel;
    private final JButton saveButton;
    private final JButton cancelButton;
    private int currentRow = 0;
    private Runnable onSave;

    public ModernFormDialog(Window owner, String title, String subtitle) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(560, 420);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel background = new JPanel(new BorderLayout());
        background.setBorder(new EmptyBorder(20, 20, 20, 20));
        background.setBackground(UITheme.BACKGROUND);

        RoundedPanel card = new RoundedPanel(24, UITheme.CARD_BACKGROUND);
        card.setBorder(new EmptyBorder(24, 28, 24, 28));
        card.setLayout(new BorderLayout(18, 18));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UITheme.subHeaderFont());
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        header.add(titleLabel);

        if (subtitle != null && !subtitle.isBlank()) {
            header.add(Box.createVerticalStrut(6));
            JLabel subtitleLabel = new JLabel(subtitle);
            subtitleLabel.setFont(UITheme.smallFont());
            subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);
            header.add(subtitleLabel);
        }

        card.add(header, BorderLayout.NORTH);

        formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        card.add(formPanel, BorderLayout.CENTER);

        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonBar.setOpaque(false);
        cancelButton = FormUIUtil.createSecondaryButton("Hủy");
        cancelButton.addActionListener(e -> dispose());
        buttonBar.add(cancelButton);

        saveButton = FormUIUtil.createPrimaryButton("Lưu");
        saveButton.addActionListener(e -> {
            if (onSave != null) {
                onSave.run();
            }
        });
        buttonBar.add(saveButton);

        card.add(buttonBar, BorderLayout.SOUTH);

        background.add(card, BorderLayout.CENTER);
        setContentPane(background);
    }

    public void addField(Component labelComponent, Component fieldComponent) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = currentRow;
        labelConstraints.weightx = 0;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(6, 0, 6, 12);
        FormUIUtil.styleLabel(labelComponent);
        formPanel.add(labelComponent, labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = currentRow;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(6, 0, 6, 0);
        formPanel.add(fieldComponent, fieldConstraints);
        currentRow++;
    }

    public void addFullWidthComponent(Component component) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        formPanel.add(component, gbc);
        currentRow++;
    }

    public void setPreferredFormWidth(int width) {
        formPanel.setPreferredSize(new Dimension(width, formPanel.getPreferredSize().height));
    }

    public void setSaveAction(String text, Runnable action) {
        if (text != null && !text.isBlank()) {
            saveButton.setText(text);
        }
        this.onSave = action;
    }

    public void setCancelText(String text) {
        if (text != null && !text.isBlank()) {
            cancelButton.setText(text);
        }
    }

    public void focusLater(Component component) {
        SwingUtilities.invokeLater(component::requestFocusInWindow);
    }

    public void showDialog() {
        setVisible(true);
    }

    public void close() {
        dispose();
    }
}
