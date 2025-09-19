package vn.edu.app.client.ui.dialog;

import vn.edu.app.client.remote.RMIConnector;
import vn.edu.app.client.util.UIUtils;
import vn.edu.app.common.dto.CourseDTO;
import vn.edu.app.common.dto.GradeDTO;
import vn.edu.app.common.dto.StudentDTO;
import vn.edu.app.common.remote.AdminService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GradeFormDialog extends JDialog {
    private final String fixedStudentId; // nếu truyền sẵn studentId
    private JComboBox<StudentDTO> cbStudent = new JComboBox<>();
    private JComboBox<CourseDTO> cbCourse = new JComboBox<>();
    private JSpinner spScore = new JSpinner(new SpinnerNumberModel(8.0, 0.0, 10.0, 0.1));
    private JTextField tfNote = new JTextField(22);
    private boolean confirmed=false;

    public GradeFormDialog(Window owner, String fixedStudentId) {
        super(owner, "Assign / Update Grade", ModalityType.APPLICATION_MODAL);
        this.fixedStudentId = fixedStudentId;
        setSize(480, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10,10));
        setIconImage(UIUtils.icon("icons/grades.png", 20,20).getImage());

        // ===== FORM =====
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(250,252,255));
        form.setBorder(BorderFactory.createTitledBorder("Grade Information"));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8,8,8,8);
        g.fill = GridBagConstraints.HORIZONTAL;

        int r=0;
        addRow(form, g, r++, "Student", cbStudent);
        addRow(form, g, r++, "Course", cbCourse);
        addRow(form, g, r++, "Score", spScore);
        addRow(form, g, r++, "Note", tfNote);

        // ===== BUTTONS =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(250,252,255));

        JButton btnSave   = styledBtn("Save", "icons/save.png", new Color(46,204,113));
        JButton btnCancel = styledBtn("Cancel", "icons/exit.png", new Color(231,76,60));

        bottom.add(btnSave); bottom.add(btnCancel);

        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> onSave());

        loadData();
    }

    private void addRow(JPanel form, GridBagConstraints g, int row, String label, Component comp) {
        g.gridx=0; g.gridy=row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        form.add(lbl, g);

        g.gridx=1;
        form.add(comp, g);

        if (comp instanceof JTextField tf) {
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200,200,200)),
                    BorderFactory.createEmptyBorder(4,6,4,6)
            ));
        }
    }

    private JButton styledBtn(String text, String icon, Color color) {
        JButton btn = new JButton(text, UIUtils.icon(icon,18,18));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }

    private void loadData() {
        try {
            AdminService admin = RMIConnector.admin();

            // Load students
            List<StudentDTO> students = admin.getAllStudents();
            cbStudent.removeAllItems();
            for (StudentDTO s : students) cbStudent.addItem(s);

            // Pre-select nếu có fixedStudentId
            if (fixedStudentId != null && !fixedStudentId.isBlank()) {
                for (int i=0;i<cbStudent.getItemCount();i++) {
                    if (cbStudent.getItemAt(i).getStudentId().equals(fixedStudentId)) {
                        cbStudent.setSelectedIndex(i); break;
                    }
                }
            }

            // Load courses
            List<CourseDTO> courses = admin.getAllCourses();
            cbCourse.removeAllItems();
            for (CourseDTO c : courses) cbCourse.addItem(c);

        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.error(this,"Load data failed: " + e.getMessage());
        }
    }

    private void onSave() {
        try {
            StudentDTO s = (StudentDTO) cbStudent.getSelectedItem();
            CourseDTO c = (CourseDTO) cbCourse.getSelectedItem();
            if (s == null || c == null) { UIUtils.error(this,"Missing student or course"); return; }

            GradeDTO g = new GradeDTO();
            g.setStudentId(s.getStudentId());
            g.setCourseId(c.getCourseId());
            g.setScore(((Double) spScore.getValue()).floatValue());
            g.setGradeNote(tfNote.getText().trim());

            boolean ok = RMIConnector.admin().assignGrade(g);
            if (ok) {
                confirmed=true;
                UIUtils.info(this,"Saved successfully");
                dispose();
            } else UIUtils.error(this,"Save failed");
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.error(this,e.getMessage());
        }
    }

    public boolean isConfirmed(){ return confirmed; }
}
