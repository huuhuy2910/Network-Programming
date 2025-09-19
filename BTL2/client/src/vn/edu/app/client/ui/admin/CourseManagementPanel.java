package vn.edu.app.client.ui.admin;

import vn.edu.app.client.remote.RMIConnector;
import vn.edu.app.client.ui.dialog.CourseFormDialog;
import vn.edu.app.client.util.UIUtils;
import vn.edu.app.common.dto.CourseDTO;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class CourseManagementPanel extends JPanel {
    private DefaultTableModel model = new DefaultTableModel(
            new String[]{"Course ID","Course Name","Credits","Semester"},0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // khóa edit trực tiếp trên bảng
        }
    };
    private JTable table = new JTable(model);

    public CourseManagementPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // ===== TOP BAR =====
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(new Color(236,240,241));

        JButton btnAdd    = styledBtn("Add",    "icons/add.png",     new Color(46,204,113));
        JButton btnEdit   = styledBtn("Edit",   "icons/edit.png",    new Color(241,196,15));
        JButton btnDelete = styledBtn("Delete", "icons/delete.png",  new Color(231,76,60));
        JButton btnReload = styledBtn("Reload", "icons/refresh.png", new Color(52,152,219));

        top.add(btnAdd); top.add(btnEdit); top.add(btnDelete); top.add(btnReload);
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

        // alternating row colors
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
                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== EVENTS =====
        btnAdd.addActionListener(e -> openForm(null));
        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnReload.addActionListener(e -> load());

        load();
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
        try {
            List<CourseDTO> list = RMIConnector.admin().getAllCourses();
            model.setRowCount(0);
            for (CourseDTO c : list) {
                model.addRow(new Object[]{
                        c.getCourseId(), c.getCourseName(), c.getCredits(), c.getSemester()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.error(this, "Load courses failed: " + e.getMessage());
        }
    }

    private void openForm(CourseDTO seed) {
        CourseFormDialog dlg = new CourseFormDialog(SwingUtilities.getWindowAncestor(this), seed);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) load();
    }

    private void editSelected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            UIUtils.error(this,"Select a row");
            return;
        }
        CourseDTO c = new CourseDTO();
        c.setCourseId((String) model.getValueAt(r,0));
        c.setCourseName((String) model.getValueAt(r,1));
        c.setCredits((Integer) model.getValueAt(r,2));
        c.setSemester((String) model.getValueAt(r,3));
        openForm(c);
    }

    private void deleteSelected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            UIUtils.error(this,"Select a row");
            return;
        }
        String id = (String) model.getValueAt(r,0);
        int ok = JOptionPane.showConfirmDialog(this, "Delete course " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            try {
                boolean done = RMIConnector.admin().deleteCourse(id);
                if (done) {
                    UIUtils.info(this,"Deleted");
                    load();
                } else UIUtils.error(this,"Delete failed");
            } catch (Exception e) {
                e.printStackTrace();
                UIUtils.error(this, e.getMessage());
            }
        }
    }
}
