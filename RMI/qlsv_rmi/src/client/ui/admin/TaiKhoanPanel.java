package client.ui.admin;

import client.network.ClientConnector;
import client.ui.components.ModernFormDialog;
import client.ui.components.TableActionCell;
import client.ui.model.TaiKhoanTableModel;
import client.util.DialogUtil;
import client.util.FormUIUtil;
import client.util.UITheme;
import client.util.ValidationUtil;
import common.dto.SinhVien;
import common.dto.TaiKhoan;
import common.service.SinhVienService;
import common.service.TaiKhoanService;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.util.Collections;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class TaiKhoanPanel extends AbstractManagementPanel<TaiKhoan> {
    private TaiKhoanService service;
    private SinhVienService sinhVienService;

    public TaiKhoanPanel() {
        super(new TaiKhoanTableModel());
        try {
            service = ClientConnector.getTaiKhoanService();
            sinhVienService = ClientConnector.getSinhVienService();
        } catch (Exception e) {
            DialogUtil.showError(this, "Lỗi kết nối: " + e.getMessage());
        }
        setupTableInteractions();
        reloadData();
    }

    @Override
    protected boolean enableEditAction() {
        return false;
    }

    @Override
    protected boolean enableDeleteAction() {
        return false;
    }

    @Override
    protected List<TaiKhoan> loadData() throws Exception {
        return service != null ? service.getAll() : Collections.emptyList();
    }

    @Override
    protected void onAdd() {
        showForm(null);
    }

    @Override
    protected void onEdit(TaiKhoan selected) {
        if (selected != null) {
            showForm(selected);
        }
    }

    @Override
    protected void onDelete(TaiKhoan selected) {
        if (selected == null) {
            return;
        }
        if (!DialogUtil.confirm(this, "Xóa tài khoản " + selected.getUsername() + "?")) {
            return;
        }
        try {
            if (service.delete(selected.getUsername())) {
                DialogUtil.showInfo(this, "Đã xóa thành công.");
                reloadData();
            } else {
                DialogUtil.showError(this, "Không thể xóa tài khoản.");
            }
        } catch (Exception e) {
            DialogUtil.showError(this, e.getMessage());
        }
    }

    @Override
    protected void onSearch(String keyword) {
        try {
            if (keyword == null || keyword.isEmpty()) {
                reloadData();
            } else {
                tableModel.setData(service.search(keyword));
            }
        } catch (Exception e) {
            DialogUtil.showError(this, e.getMessage());
        }
    }

    private void showForm(TaiKhoan taiKhoan) {
        JTextField usernameField = FormUIUtil.createTextField();
        JPasswordField passwordField = new JPasswordField();
        FormUIUtil.styleTextComponent(passwordField);
        JComboBox<String> roleCombo = FormUIUtil.createComboBox();
        roleCombo.setModel(new DefaultComboBoxModel<>(new String[]{TaiKhoan.ROLE_ADMIN, TaiKhoan.ROLE_SINHVIEN}));
        JComboBox<SinhVien> sinhVienCombo = FormUIUtil.createComboBox();
        JComboBox<String> statusCombo = FormUIUtil.createComboBox();
        statusCombo.setModel(new DefaultComboBoxModel<>(new String[]{"Hoạt động", "Khoá"}));
        JLabel displayNameLabel = new JLabel("-");
        displayNameLabel.setFont(UITheme.bodyFont());
        displayNameLabel.setForeground(UITheme.TEXT_PRIMARY);

        try {
            List<SinhVien> sinhVienList = sinhVienService.getAll();
            sinhVienCombo.setModel(new DefaultComboBoxModel<>(sinhVienList.toArray(new SinhVien[0])));
            sinhVienCombo.insertItemAt(null, 0);
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể tải danh sách sinh viên: " + e.getMessage());
        }

        if (taiKhoan != null) {
            usernameField.setText(taiKhoan.getUsername());
            usernameField.setEnabled(false);
            roleCombo.setSelectedItem(taiKhoan.getRole());
            statusCombo.setSelectedItem(taiKhoan.getStatus() != null ? taiKhoan.getStatus() : "Hoạt động");
            if (taiKhoan.getSvId() != null) {
                for (int i = 0; i < sinhVienCombo.getItemCount(); i++) {
                    SinhVien sv = sinhVienCombo.getItemAt(i);
                    if (sv != null && taiKhoan.getSvId().equals(sv.getSvId())) {
                        sinhVienCombo.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
                sinhVienCombo.setSelectedIndex(0);
            }
        } else {
            sinhVienCombo.setSelectedIndex(0);
        }

        roleCombo.addActionListener(e -> {
            boolean isStudent = TaiKhoan.ROLE_SINHVIEN.equals(roleCombo.getSelectedItem());
            sinhVienCombo.setEnabled(isStudent);
            updateDisplayNameLabel(displayNameLabel, usernameField.getText().trim(), (SinhVien) sinhVienCombo.getSelectedItem(), (String) roleCombo.getSelectedItem());
        });
        sinhVienCombo.addActionListener(e -> updateDisplayNameLabel(displayNameLabel, usernameField.getText().trim(), (SinhVien) sinhVienCombo.getSelectedItem(), (String) roleCombo.getSelectedItem()));
        sinhVienCombo.setEnabled(roleCombo.getSelectedItem() == TaiKhoan.ROLE_SINHVIEN);
        updateDisplayNameLabel(displayNameLabel, usernameField.getText().trim(), (SinhVien) sinhVienCombo.getSelectedItem(), (String) roleCombo.getSelectedItem());

        ModernFormDialog dialog = new ModernFormDialog(
            SwingUtilities.getWindowAncestor(this),
            taiKhoan == null ? "Thêm tài khoản" : "Cập nhật tài khoản",
            "Quản lý thông tin tài khoản"
        );
        dialog.addField(new JLabel("👤 Username"), usernameField);
        dialog.addField(new JLabel(taiKhoan == null ? "🔒 Password" : "🔒 Password (để trống nếu không đổi)"), passwordField);
        dialog.addField(new JLabel("🎭 Role"), roleCombo);
        dialog.addField(new JLabel("📝 Tên hiển thị"), displayNameLabel);
        dialog.addField(new JLabel("🎓 Sinh viên"), sinhVienCombo);
        dialog.addField(new JLabel("📊 Trạng thái"), statusCombo);

        dialog.setSaveAction(taiKhoan == null ? "Thêm mới" : "Lưu thay đổi", () -> {
            String username = usernameField.getText().trim();
            if (!ValidationUtil.isNotBlank(username)) {
                DialogUtil.showError(dialog, "Username là bắt buộc.");
                return;
            }
            String password = new String(passwordField.getPassword());
            if (taiKhoan == null && !ValidationUtil.isNotBlank(password)) {
                DialogUtil.showError(dialog, "Password là bắt buộc.");
                return;
            }
            String role = (String) roleCombo.getSelectedItem();
            SinhVien selectedSv = (SinhVien) sinhVienCombo.getSelectedItem();
            if (TaiKhoan.ROLE_SINHVIEN.equals(role) && selectedSv == null) {
                DialogUtil.showError(dialog, "Vui lòng chọn sinh viên cho tài khoản sinh viên.");
                return;
            }
            String status = (String) statusCombo.getSelectedItem();
            if (taiKhoan == null) {
                TaiKhoan newTk = new TaiKhoan();
                newTk.setUsername(username);
                newTk.setPassword(password);
                newTk.setRole(role);
                newTk.setSvId(selectedSv != null ? selectedSv.getSvId() : null);
                newTk.setDisplayName(resolveDisplayName(username, selectedSv, role));
                newTk.setStatus(status);
                newTk.setCreatedAt(new Date());
                try {
                    if (service.insert(newTk)) {
                        DialogUtil.showInfo(dialog, "Thêm tài khoản thành công.");
                        dialog.close();
                        reloadData();
                    } else {
                        DialogUtil.showError(dialog, "Không thể thêm tài khoản.");
                    }
                } catch (Exception e) {
                    DialogUtil.showError(dialog, e.getMessage());
                }
            } else {
                taiKhoan.setRole(role);
                taiKhoan.setSvId(selectedSv != null ? selectedSv.getSvId() : null);
                taiKhoan.setDisplayName(resolveDisplayName(username, selectedSv, role));
                taiKhoan.setStatus(status);
                try {
                    boolean updated = service.update(taiKhoan);
                    if (ValidationUtil.isNotBlank(password)) {
                        updated = service.resetPassword(taiKhoan.getUsername(), password) && updated;
                    }
                    if (updated) {
                        DialogUtil.showInfo(dialog, "Cập nhật thành công.");
                        dialog.close();
                        reloadData();
                    } else {
                        DialogUtil.showError(dialog, "Không thể cập nhật tài khoản.");
                    }
                } catch (Exception e) {
                    DialogUtil.showError(dialog, e.getMessage());
                }
            }
        });

        dialog.focusLater(taiKhoan == null ? usernameField : passwordField);
        dialog.showDialog();
    }

    private void setupTableInteractions() {
        JTable tableRef = this.table;
        tableRef.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tableRef.getSelectedRow() >= 0) {
                    int modelRow = tableRef.convertRowIndexToModel(tableRef.getSelectedRow());
                    onEdit(tableModel.getRow(modelRow));
                }
            }
        });

        TableActionCell<TaiKhoan> actionCell = new TableActionCell<TaiKhoan>(new TableActionCell.TableActionHandler<TaiKhoan>() {
            @Override
            public void onView(TaiKhoan item) {
                // View action disabled
            }

            @Override
            public void onEdit(TaiKhoan item) {
                TaiKhoanPanel.this.onEdit(item);
            }

            @Override
            public void onDelete(TaiKhoan item) {
                TaiKhoanPanel.this.onDelete(item);
            }
        }, rowIndex -> {
            int modelRow = tableRef.convertRowIndexToModel(rowIndex);
            return tableModel.getRow(modelRow);
        }, false, true, true);

        tableRef.getColumnModel().getColumn(TaiKhoanTableModel.ACTION_COLUMN_INDEX).setCellRenderer(actionCell);
        tableRef.getColumnModel().getColumn(TaiKhoanTableModel.ACTION_COLUMN_INDEX).setCellEditor(actionCell);
        tableRef.getColumnModel().getColumn(TaiKhoanTableModel.ACTION_COLUMN_INDEX).setPreferredWidth(260);
        tableRef.getColumnModel().getColumn(TaiKhoanTableModel.ACTION_COLUMN_INDEX).setMinWidth(220);
        tableRef.getColumnModel().getColumn(TaiKhoanTableModel.ACTION_COLUMN_INDEX).setMaxWidth(320);
    }

    private void updateDisplayNameLabel(JLabel label, String username, SinhVien sinhVien, String role) {
        if (label == null) {
            return;
        }
        label.setText(resolveDisplayName(username, sinhVien, role));
    }

    private String resolveDisplayName(String username, SinhVien sinhVien, String role) {
        if (TaiKhoan.ROLE_SINHVIEN.equals(role) && sinhVien != null) {
            String name = sinhVien.getTenSv();
            if (name != null && !name.isBlank()) {
                return name;
            }
        }
        return (username != null && !username.isBlank()) ? username : "-";
    }

}
