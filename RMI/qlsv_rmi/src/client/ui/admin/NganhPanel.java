package client.ui.admin;

import client.network.ClientConnector;
import client.ui.components.ModernFormDialog;
import client.ui.components.RequiredLabel;
import client.ui.components.TableActionCell;
import client.ui.model.NganhTableModel;
import client.util.DialogUtil;
import client.util.FormUIUtil;
import client.util.ValidationUtil;
import common.dto.Khoa;
import common.dto.Nganh;
import common.service.KhoaService;
import common.service.NganhService;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class NganhPanel extends AbstractManagementPanel<Nganh> {
    private NganhService service;
    private KhoaService khoaService;

    public NganhPanel() {
        super(new NganhTableModel());
        try {
            service = ClientConnector.getNganhService();
            khoaService = ClientConnector.getKhoaService();
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
    protected List<Nganh> loadData() throws Exception {
        return service != null ? service.getAll() : java.util.Collections.emptyList();
    }

    @Override
    protected void onAdd() {
        showForm(null);
    }

    @Override
    protected void onEdit(Nganh selected) {
        if (selected != null) {
            showForm(selected);
        }
    }

    @Override
    protected void onDelete(Nganh selected) {
        if (selected == null) {
            return;
        }
        if (!DialogUtil.confirm(this, "Xóa ngành " + selected.getTenNganh() + "?")) {
            return;
        }
        try {
            if (service.delete(selected.getMaNganh())) {
                DialogUtil.showInfo(this, "Đã xóa thành công.");
                reloadData();
            } else {
                DialogUtil.showError(this, "Không thể xóa ngành.");
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

    private void showForm(Nganh nganh) {
        JTextField maField = FormUIUtil.createTextField();
        JTextField tenField = FormUIUtil.createTextField();

        JComboBox<Khoa> khoaCombo = FormUIUtil.createComboBox();
        try {
            List<Khoa> khoas = khoaService.getAll();
            khoaCombo.setModel(new DefaultComboBoxModel<>(khoas.toArray(new Khoa[0])));
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể tải danh sách khoa: " + e.getMessage());
        }

        if (nganh != null) {
            maField.setText(nganh.getMaNganh());
            maField.setEnabled(false);
            tenField.setText(nganh.getTenNganh());
            if (nganh.getKhoa() != null) {
                khoaCombo.setSelectedItem(nganh.getKhoa());
            }
        }

        ModernFormDialog dialog = new ModernFormDialog(
            SwingUtilities.getWindowAncestor(this),
            nganh == null ? "Thêm ngành" : "Cập nhật ngành",
            "Cập nhật thông tin ngành đào tạo"
        );
        dialog.addField(new RequiredLabel("📚 Mã ngành"), maField);
        dialog.addField(new RequiredLabel("📚 Tên ngành"), tenField);
        dialog.addField(new RequiredLabel("🏛️ Khoa"), khoaCombo);

        dialog.setSaveAction(nganh == null ? "Thêm mới" : "Lưu thay đổi", () -> {
            if (!ValidationUtil.isNotBlank(maField.getText()) || !ValidationUtil.isNotBlank(tenField.getText())) {
                DialogUtil.showError(dialog, "Vui lòng nhập đầy đủ thông tin.");
                return;
            }
            Khoa selectedKhoa = (Khoa) khoaCombo.getSelectedItem();
            if (selectedKhoa == null) {
                DialogUtil.showError(dialog, "Vui lòng chọn khoa.");
                return;
            }
            Nganh newNganh = nganh != null ? nganh : new Nganh();
            newNganh.setMaNganh(maField.getText().trim());
            newNganh.setTenNganh(tenField.getText().trim());
            newNganh.setKhoa(selectedKhoa);

            try {
                boolean success = nganh == null ? service.insert(newNganh) : service.update(newNganh);
                if (success) {
                    DialogUtil.showInfo(dialog, "Lưu thành công.");
                    dialog.close();
                    reloadData();
                } else {
                    DialogUtil.showError(dialog, "Không thể lưu dữ liệu.");
                }
            } catch (Exception e) {
                DialogUtil.showError(dialog, e.getMessage());
            }
        });

        dialog.focusLater(nganh == null ? maField : tenField);
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

        TableActionCell<Nganh> actionCell = new TableActionCell<>(new TableActionCell.TableActionHandler<Nganh>() {
            @Override
            public void onView(Nganh item) {
                // View action disabled
            }

            @Override
            public void onEdit(Nganh item) {
                NganhPanel.this.onEdit(item);
            }

            @Override
            public void onDelete(Nganh item) {
                NganhPanel.this.onDelete(item);
            }
        }, rowIndex -> {
            int modelRow = tableRef.convertRowIndexToModel(rowIndex);
            return tableModel.getRow(modelRow);
        }, false, true, true);

        tableRef.getColumnModel().getColumn(NganhTableModel.ACTION_COLUMN_INDEX).setCellRenderer(actionCell);
        tableRef.getColumnModel().getColumn(NganhTableModel.ACTION_COLUMN_INDEX).setCellEditor(actionCell);
        tableRef.getColumnModel().getColumn(NganhTableModel.ACTION_COLUMN_INDEX).setPreferredWidth(240);
        tableRef.getColumnModel().getColumn(NganhTableModel.ACTION_COLUMN_INDEX).setMinWidth(200);
        tableRef.getColumnModel().getColumn(NganhTableModel.ACTION_COLUMN_INDEX).setMaxWidth(300);
    }

}
