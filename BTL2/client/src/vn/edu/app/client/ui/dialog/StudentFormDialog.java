package vn.edu.app.client.ui.dialog;

import vn.edu.app.client.remote.RMIConnector;
import vn.edu.app.client.util.UIUtils;
import vn.edu.app.client.util.Validators;
import vn.edu.app.common.dto.ClassDTO;
import vn.edu.app.common.dto.StudentDTO;
import vn.edu.app.common.remote.AdminService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentFormDialog extends JDialog {
    private JTextField tfId = new JTextField(18);
    private JTextField tfName = new JTextField(18);
    private JComboBox<String> cbGender = new JComboBox<>(new String[]{"M","F"});
    private JComboBox<ClassDTO> cbClass = new JComboBox<>();
    private JTextField tfPhone = new JTextField(18);
    private JTextField tfEmail = new JTextField(18);
    private JTextField tfAddress = new JTextField(18);
    private JTextField tfHometown = new JTextField(18);
    private JComboBox<String> cbStatus = new JComboBox<>(new String[]{"ACTIVE","GRADUATED","DROPPED","SUSPENDED"});

    private boolean confirmed = false;
    private final StudentDTO seed; // null => add

    public StudentFormDialog(Window owner, StudentDTO seed) {
        super(owner, (seed==null?"Add":"Edit") + " Student", ModalityType.APPLICATION_MODAL);
        this.seed = seed;
        setSize(460, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10,10));
        setIconImage(UIUtils.icon("icons/students.png", 20,20).getImage());

        // ===== FORM =====
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(250,252,255));
        form.setBorder(BorderFactory.createTitledBorder("Student Information"));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8,8,8,8);
        g.fill = GridBagConstraints.HORIZONTAL;

        int r=0;
        addRow(form, g, r++, "Student ID", tfId);
        addRow(form, g, r++, "Full name", tfName);
        addRow(form, g, r++, "Gender", cbGender);
        addRow(form, g, r++, "Class", cbClass);
        addRow(form, g, r++, "Phone", tfPhone);
        addRow(form, g, r++, "Email", tfEmail);
        addRow(form, g, r++, "Address", tfAddress);
        addRow(form, g, r++, "Hometown", tfHometown);
        addRow(form, g, r++, "Status", cbStatus);

        // ===== BUTTONS =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(250,252,255));

        JButton btnSave = styledBtn("Save", "icons/save.png", new Color(46,204,113));
        JButton btnCancel = styledBtn("Cancel", "icons/exit.png", new Color(231,76,60));

        bottom.add(btnSave); bottom.add(btnCancel);

        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> onSave());

        loadClasses();
        if (seed != null) fill(seed);
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

    private void loadClasses() {
        try {
            List<ClassDTO> list = RMIConnector.admin().getAllClasses();
            cbClass.removeAllItems();
            for (ClassDTO c : list) cbClass.addItem(c);
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.error(this,"Load classes failed: " + e.getMessage());
        }
    }

    private void fill(StudentDTO s) {
        tfId.setText(s.getStudentId()); tfId.setEditable(false);
        tfName.setText(s.getFullName());
        cbGender.setSelectedItem(s.getGender());

        for (int i=0;i<cbClass.getItemCount();i++) {
            if (cbClass.getItemAt(i).getClassId().equals(s.getClassId())) {
                cbClass.setSelectedIndex(i); break;
            }
        }

        tfPhone.setText(s.getPhone());
        tfEmail.setText(s.getEmail());
        tfAddress.setText(s.getAddress());
        tfHometown.setText(s.getHometown());
        cbStatus.setSelectedItem(s.getStatus());
    }

    private void onSave() {
        try {
            if (!Validators.isEmail(tfEmail.getText())) { UIUtils.error(this,"Invalid email"); return; }
            if (!Validators.isPhone(tfPhone.getText())) { UIUtils.error(this,"Invalid phone"); return; }

            StudentDTO s = new StudentDTO();
            s.setStudentId(tfId.getText().trim());
            s.setFullName(tfName.getText().trim());
            s.setGender((String) cbGender.getSelectedItem());
            s.setClassId(((ClassDTO) cbClass.getSelectedItem()).getClassId());
            s.setPhone(tfPhone.getText().trim());
            s.setEmail(tfEmail.getText().trim());
            s.setAddress(tfAddress.getText().trim());
            s.setHometown(tfHometown.getText().trim());
            s.setStatus((String) cbStatus.getSelectedItem());

            AdminService admin = RMIConnector.admin();
            boolean ok = (seed == null) ? admin.addStudent(s) : admin.updateStudent(s);
            if (ok) {
                confirmed = true;
                UIUtils.info(this,"Saved successfully");
                dispose();
            } else {
                UIUtils.error(this,"Save failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.error(this,e.getMessage());
        }
    }

    public boolean isConfirmed() { return confirmed; }
}
