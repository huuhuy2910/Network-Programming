package vn.edu.app.client.ui.admin;

import vn.edu.app.client.remote.RMIConnector;
import vn.edu.app.client.ui.dialog.StudentFormDialog;
import vn.edu.app.client.util.UIUtils;
import vn.edu.app.common.dto.StudentDTO;
import vn.edu.app.common.remote.AdminService;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class StudentManagementPanel extends JPanel {
    private JTextField tfSearch = new JTextField(22);
    private DefaultTableModel model = new DefaultTableModel(
            new String[]{"Student ID","Full name","Gender","Class ID","Phone","Email","Hometown","Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // không cho sửa trực tiếp
        }
    };
    private JTable table = new JTable(model);

    private List<StudentDTO> cached; // cache để filter client-side

    public StudentManagementPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // ===== TOP BAR =====
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(new Color(236, 240, 241));

        JButton btnSearch  = styledBtn("Search",  "icons/search.png", new Color(52, 152, 219));
        JButton btnAdd     = styledBtn("Add",     "icons/add.png",    new Color(46, 204, 113));
        JButton btnEdit    = styledBtn("Edit",    "icons/edit.png",   new Color(241, 196, 15));
        JButton btnDelete  = styledBtn("Delete",  "icons/delete.png", new Color(231, 76, 60));
        JButton btnReload  = styledBtn("Reload",  "icons/refresh.png",new Color(155, 89, 182));

        top.add(tfSearch);
        top.add(btnSearch); top.add(btnAdd); top.add(btnEdit); top.add(btnDelete); top.add(btnReload);

        add(top, BorderLayout.NORTH);

        // ===== TABLE CUSTOM =====
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(220, 220, 220));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(41, 128, 185));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // alternating row colors + highlight status
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                } else {
                    c.setBackground(new Color(52, 152, 219));
                    c.setForeground(Color.WHITE);
                }

                if (col == 7 && value != null) { // Status column
                    String status = value.toString();
                    if ("ACTIVE".equalsIgnoreCase(status)) {
                        c.setForeground(new Color(39, 174, 96));
                    } else {
                        c.setForeground(new Color(192, 57, 43));
                    }
                } else if (!isSelected) {
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== EVENT HANDLER =====
        btnSearch.addActionListener(e -> filter());
        btnReload.addActionListener(e -> load());
        btnAdd.addActionListener(e -> openForm(null));
        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());

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
            AdminService admin = RMIConnector.admin();
            cached = admin.getAllStudents();
            render(cached);
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.error(this, "Load students failed: " + e.getMessage());
        }
    }

    private void render(List<StudentDTO> list) {
        model.setRowCount(0);
        for (StudentDTO s : list) {
            model.addRow(new Object[]{
                    s.getStudentId(), s.getFullName(), s.getGender(), s.getClassId(),
                    s.getPhone(), s.getEmail(), s.getHometown(), s.getStatus()
            });
        }
    }

    private void filter() {
        String q = tfSearch.getText().trim().toLowerCase(Locale.ROOT);
        if (q.isEmpty()) { render(cached); return; }
        List<StudentDTO> f = cached.stream().filter(s ->
                s.getStudentId().toLowerCase(Locale.ROOT).contains(q) ||
                (s.getFullName()!=null && s.getFullName().toLowerCase(Locale.ROOT).contains(q)) ||
                (s.getEmail()!=null && s.getEmail().toLowerCase(Locale.ROOT).contains(q)) ||
                (s.getPhone()!=null && s.getPhone().toLowerCase(Locale.ROOT).contains(q))
        ).collect(Collectors.toList());
        render(f);
    }

    private void openForm(StudentDTO seed) {
        StudentFormDialog dlg = new StudentFormDialog(SwingUtilities.getWindowAncestor(this), seed);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) load();
    }

    private void editSelected() {
        int r = table.getSelectedRow();
        if (r < 0) { UIUtils.error(this, "Please select a row"); return; }
        StudentDTO s = new StudentDTO();
        s.setStudentId((String) model.getValueAt(r,0));
        s.setFullName((String) model.getValueAt(r,1));
        s.setGender((String) model.getValueAt(r,2));
        s.setClassId((String) model.getValueAt(r,3));
        s.setPhone((String) model.getValueAt(r,4));
        s.setEmail((String) model.getValueAt(r,5));
        s.setHometown((String) model.getValueAt(r,6));
        s.setStatus((String) model.getValueAt(r,7));
        openForm(s);
    }

    private void deleteSelected() {
        int r = table.getSelectedRow();
        if (r < 0) { UIUtils.error(this, "Please select a row"); return; }
        String id = (String) model.getValueAt(r,0);
        int ok = JOptionPane.showConfirmDialog(this, "Delete student " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            try {
                boolean done = RMIConnector.admin().deleteStudent(id);
                if (done) { UIUtils.info(this, "Deleted"); load(); }
                else UIUtils.error(this, "Cannot delete");
            } catch (Exception e) { e.printStackTrace(); UIUtils.error(this, e.getMessage()); }
        }
    }
}
