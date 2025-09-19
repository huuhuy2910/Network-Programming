package vn.edu.app.client.ui.dialog;

import vn.edu.app.client.remote.RMIConnector;
import vn.edu.app.client.util.UIUtils;
import vn.edu.app.common.dto.CourseDTO;
import vn.edu.app.common.remote.AdminService;

import javax.swing.*;
import java.awt.*;

public class CourseFormDialog extends JDialog {
    private JTextField tfId = new JTextField(16);
    private JTextField tfName = new JTextField(22);
    private JSpinner spCredits = new JSpinner(new SpinnerNumberModel(3,1,10,1));
    private JTextField tfSemester = new JTextField(10);
    private boolean confirmed=false;
    private final CourseDTO seed;

    public CourseFormDialog(Window owner, CourseDTO seed) {
        super(owner, (seed==null?"Add":"Edit")+" Course", ModalityType.APPLICATION_MODAL);
        this.seed = seed;
        setSize(460, 280);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10,10));
        setIconImage(UIUtils.icon("icons/courses.png", 20,20).getImage());

        // ===== FORM =====
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(250,252,255));
        form.setBorder(BorderFactory.createTitledBorder("Course Information"));

        GridBagConstraints g = new GridBagConstraints();
        g.insets=new Insets(8,8,8,8);
        g.fill=GridBagConstraints.HORIZONTAL;

        int r=0;
        addRow(form, g, r++, "Course ID", tfId);
        addRow(form, g, r++, "Course Name", tfName);
        addRow(form, g, r++, "Credits", spCredits);
        addRow(form, g, r++, "Semester", tfSemester);

        // ===== BUTTONS =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(250,252,255));

        JButton btnSave   = styledBtn("Save", "icons/save.png", new Color(46,204,113));
        JButton btnCancel = styledBtn("Cancel", "icons/exit.png", new Color(231,76,60));

        bottom.add(btnSave); bottom.add(btnCancel);

        add(form,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);

        btnCancel.addActionListener(e->dispose());
        btnSave.addActionListener(e->onSave());

        if(seed!=null) fill(seed);
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

    private void fill(CourseDTO c){
        tfId.setText(c.getCourseId()); 
        tfId.setEditable(false);
        tfName.setText(c.getCourseName());
        spCredits.setValue(c.getCredits());
        tfSemester.setText(c.getSemester());
    }

    private void onSave(){
        try{
            CourseDTO c = new CourseDTO();
            c.setCourseId(tfId.getText().trim());
            c.setCourseName(tfName.getText().trim());
            c.setCredits((Integer) spCredits.getValue());
            c.setSemester(tfSemester.getText().trim());

            AdminService admin = RMIConnector.admin();
            boolean ok = (seed==null) ? admin.addCourse(c) : admin.updateCourse(c);
            if(ok){ 
                confirmed=true; 
                UIUtils.info(this,"Saved successfully"); 
                dispose(); 
            }
            else UIUtils.error(this,"Save failed");
        }catch(Exception e){ 
            e.printStackTrace(); 
            UIUtils.error(this,e.getMessage()); 
        }
    }

    public boolean isConfirmed(){ return confirmed; }
}
