package vn.edu.app.client.ui.admin;

import vn.edu.app.client.remote.RMIConnector;
import vn.edu.app.client.util.UIUtils;
import vn.edu.app.common.dto.ClassDTO;
import vn.edu.app.common.dto.MajorDTO;
import vn.edu.app.common.remote.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassManagementPanel extends JPanel {
    private DefaultTableModel model = new DefaultTableModel(
            new String[]{"Class ID","Class Name","Course","Major"},0
    );
    private JTable table = new JTable(model);

    public ClassManagementPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // ===== TOP TOOLBAR =====
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        top.setBackground(new Color(236,240,241));

        JButton btnReload = styledBtn("Reload", "icons/refresh.png", new Color(52, 152, 219));
        top.add(btnReload);

        add(top, BorderLayout.NORTH);

        // ===== TABLE =====
        styleTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== EVENTS =====
        btnReload.addActionListener(e -> load());

        load();
    }

    private JButton styledBtn(String text, String icon, Color color) {
        JButton btn = new JButton(text, UIUtils.icon(icon,18,18));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }

    private void styleTable() {
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);

        // Căn giữa tất cả cell
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i=0;i<table.getColumnCount();i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        // Tô màu xen kẽ hàng
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, row, col);
                if (!isSel) {
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                } else {
                    c.setBackground(new Color(174,214,241));
                }
                return c;
            }
        });
    }

    private void load() {
        try {
            AdminService admin = RMIConnector.admin();
            List<ClassDTO> classes = admin.getAllClasses();
            List<MajorDTO> majors = admin.getAllMajors();
            Map<String,String> majorMap = majors.stream()
                    .collect(Collectors.toMap(MajorDTO::getMajorId, MajorDTO::getMajorName));

            model.setRowCount(0);
            for (ClassDTO c : classes) {
                model.addRow(new Object[]{
                        c.getClassId(), c.getClassName(),
                        c.getCourse(), majorMap.getOrDefault(c.getMajorId(), c.getMajorId())
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.error(this, "Load failed: " + e.getMessage());
        }
    }
}
