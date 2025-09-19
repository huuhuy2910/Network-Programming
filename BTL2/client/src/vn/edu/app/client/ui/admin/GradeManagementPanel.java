package vn.edu.app.client.ui.admin;

import vn.edu.app.client.remote.RMIConnector;
import vn.edu.app.client.ui.dialog.GradeFormDialog;
import vn.edu.app.client.util.UIUtils;
import vn.edu.app.common.dto.GradeDTO;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class GradeManagementPanel extends JPanel {
    private JTextField tfStudentId = new JTextField(16);
    private DefaultTableModel model = new DefaultTableModel(
            new String[]{"Course ID","Score","Note"},0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JTable table = new JTable(model);

    public GradeManagementPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // ===== TOP BAR =====
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(new Color(236,240,241));

        JLabel lblStudent = new JLabel("Student ID:");
        JButton btnLoad   = styledBtn("Load",          "icons/search.png", new Color(52,152,219));
        JButton btnAssign = styledBtn("Assign/Update", "icons/save.png",   new Color(46,204,113));

        top.add(lblStudent);
        top.add(tfStudentId);
        top.add(btnLoad);
        top.add(btnAssign);

        add(top, BorderLayout.NORTH);

        // ===== TABLE =====
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(220,220,220));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(41,128,185));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // alternating row colors + highlight score
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(248,248,248) : Color.WHITE);
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(52,152,219));
                    c.setForeground(Color.WHITE);
                }

                // highlight score column
                if (col == 1 && value != null) {
                    try {
                        double score = Double.parseDouble(value.toString());
                        if (!isSelected) {
                            if (score >= 8.0) c.setForeground(new Color(39,174,96)); // xanh lá
                            else if (score < 5.0) c.setForeground(new Color(192,57,43)); // đỏ
                            else c.setForeground(new Color(243,156,18)); // vàng
                        }
                    } catch (NumberFormatException ignored) {}
                }

                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== EVENTS =====
        btnLoad.addActionListener(e -> load());
        btnAssign.addActionListener(e -> openAssignDialog());
    }

    private JButton styledBtn(String text, String icon, Color color) {
        JButton btn = new JButton(text, UIUtils.icon(icon, 18,18));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }

    private void load() {
        String sid = tfStudentId.getText().trim();
        if (sid.isEmpty()) {
            UIUtils.error(this,"Enter student ID");
            return;
        }
        try {
            List<GradeDTO> list = RMIConnector.admin().getGradesByStudent(sid);
            model.setRowCount(0);
            for (GradeDTO g : list) {
                model.addRow(new Object[]{ g.getCourseId(), g.getScore(), g.getGradeNote() });
            }
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.error(this,"Load grades failed: "+e.getMessage());
        }
    }

    private void openAssignDialog() {
        String sid = tfStudentId.getText().trim();
        if (sid.isEmpty()) {
            UIUtils.error(this,"Enter student ID");
            return;
        }
        GradeFormDialog dlg = new GradeFormDialog(SwingUtilities.getWindowAncestor(this), sid);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) load();
    }
}
