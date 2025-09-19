package vn.edu.app.client.ui.student;

import vn.edu.app.client.remote.RMIConnector;
import vn.edu.app.client.util.UIUtils;
import vn.edu.app.common.dto.CourseDTO;
import vn.edu.app.common.dto.GradeDTO;
import vn.edu.app.common.dto.UserDTO;
import vn.edu.app.common.remote.AdminService;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentGradesPanel extends JPanel {
    private DefaultTableModel model = new DefaultTableModel(
            new String[]{"Course ID","Course Name","Credits","Score","Note"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // khóa edit trực tiếp
        }
    };
    private JTable table = new JTable(model);
    private final UserDTO currentUser;

    public StudentGradesPanel(UserDTO user) {
        this.currentUser = user;
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // ===== TOP BAR =====
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setBackground(new Color(236,240,241));

        JButton btnRefresh = styledBtn("Refresh", "icons/refresh.png", new Color(52,152,219));
        top.add(btnRefresh);
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
        btnRefresh.addActionListener(e -> load());

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
            List<CourseDTO> courses = admin.getAllCourses();
            Map<String, CourseDTO> courseMap = courses.stream()
                    .collect(Collectors.toMap(CourseDTO::getCourseId, c->c));

            List<GradeDTO> grades = admin.getGradesByStudent(currentUser.getStudentId());
            model.setRowCount(0);
            for (GradeDTO g : grades) {
                CourseDTO c = courseMap.get(g.getCourseId());
                model.addRow(new Object[]{
                        g.getCourseId(),
                        c != null ? c.getCourseName() : "",
                        c != null ? c.getCredits() : "",
                        g.getScore(),
                        g.getGradeNote()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.error(this, "Cannot load grades: " + e.getMessage());
        }
    }
}
