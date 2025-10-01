package client.ui.admin;

import client.network.ClientConnector;
import client.ui.components.ModernFormDialog;
import client.ui.components.RequiredLabel;
import client.ui.components.TableActionCell;
import client.ui.model.KhoaTableModel;
import client.util.DialogUtil;
import client.util.FormUIUtil;
import client.util.ValidationUtil;
import common.dto.Khoa;
import common.service.KhoaService;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class KhoaPanel extends AbstractManagementPanel<Khoa> {
    private KhoaService service;

    public KhoaPanel() {
        super(new KhoaTableModel());
        try {
            service = ClientConnector.getKhoaService();
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể kết nối tới KhoaService: " + e.getMessage());
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
    protected List<Khoa> loadData() throws Exception {
        return service != null ? service.getAll() : java.util.Collections.emptyList();
    }

    @Override
    protected void onAdd() {
        showForm(null);
    }

    @Override
    protected void onEdit(Khoa selected) {
        if (selected != null) {
            showForm(selected);
        }
    }

    @Override
    protected void onDelete(Khoa selected) {
        if (selected == null) {
            return;
        }
        if (!DialogUtil.confirm(this, "Bạn có chắc chắn muốn xóa khoa " + selected.getTenKhoa() + "?")) {
            return;
        }
        try {
            if (service.delete(selected.getMaKhoa())) {
                DialogUtil.showInfo(this, "Đã xóa thành công.");
                reloadData();
            } else {
                DialogUtil.showError(this, "Không thể xóa khoa.");
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

    private void showForm(Khoa khoa) {
        JTextField maField = FormUIUtil.createTextField();
        JTextField tenField = FormUIUtil.createTextField();

        if (khoa != null) {
            maField.setText(khoa.getMaKhoa());
            maField.setEnabled(false);
            tenField.setText(khoa.getTenKhoa());
        }

        ModernFormDialog dialog = new ModernFormDialog(
            SwingUtilities.getWindowAncestor(this),
            khoa == null ? "Thêm khoa" : "Cập nhật khoa",
            "Quản lý thông tin khoa"
        );
        dialog.addField(new RequiredLabel("🏛️ Mã khoa"), maField);
        dialog.addField(new RequiredLabel("🏛️ Tên khoa"), tenField);

        dialog.setSaveAction(khoa == null ? "Thêm mới" : "Lưu thay đổi", () -> {
            if (!ValidationUtil.isNotBlank(maField.getText()) || !ValidationUtil.isNotBlank(tenField.getText())) {
                DialogUtil.showError(dialog, "Vui lòng nhập đầy đủ các trường bắt buộc.");
                return;
            }
            Khoa newKhoa = khoa != null ? khoa : new Khoa();
            newKhoa.setMaKhoa(maField.getText().trim());
            newKhoa.setTenKhoa(tenField.getText().trim());

            try {
                boolean success = khoa == null ? service.insert(newKhoa) : service.update(newKhoa);
                if (success) {
                    DialogUtil.showInfo(dialog, "Lưu dữ liệu thành công.");
                    dialog.close();
                    reloadData();
                } else {
                    DialogUtil.showError(dialog, "Không thể lưu dữ liệu.");
                }
            } catch (Exception e) {
                DialogUtil.showError(dialog, e.getMessage());
            }
        });

        dialog.focusLater(khoa == null ? maField : tenField);
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

        TableActionCell<Khoa> actionCell = new TableActionCell<>(new TableActionCell.TableActionHandler<Khoa>() {
            @Override
            public void onView(Khoa item) {
                // View action disabled
            }

            @Override
            public void onEdit(Khoa item) {
                KhoaPanel.this.onEdit(item);
            }

            @Override
            public void onDelete(Khoa item) {
                KhoaPanel.this.onDelete(item);
            }
        }, rowIndex -> {
            int modelRow = tableRef.convertRowIndexToModel(rowIndex);
            return tableModel.getRow(modelRow);
        }, false, true, true);

        tableRef.getColumnModel().getColumn(KhoaTableModel.ACTION_COLUMN_INDEX).setCellRenderer(actionCell);
        tableRef.getColumnModel().getColumn(KhoaTableModel.ACTION_COLUMN_INDEX).setCellEditor(actionCell);
        tableRef.getColumnModel().getColumn(KhoaTableModel.ACTION_COLUMN_INDEX).setPreferredWidth(220);
        tableRef.getColumnModel().getColumn(KhoaTableModel.ACTION_COLUMN_INDEX).setMinWidth(200);
        tableRef.getColumnModel().getColumn(KhoaTableModel.ACTION_COLUMN_INDEX).setMaxWidth(280);
    }

}
