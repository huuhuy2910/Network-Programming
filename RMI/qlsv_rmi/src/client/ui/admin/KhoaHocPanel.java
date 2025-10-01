package client.ui.admin;

import client.network.ClientConnector;
import client.ui.components.ModernFormDialog;
import client.ui.components.RequiredLabel;
import client.ui.components.TableActionCell;
import client.ui.model.KhoaHocTableModel;
import client.util.DialogUtil;
import client.util.FormUIUtil;
import client.util.ValidationUtil;
import common.dto.KhoaHoc;
import common.service.KhoaHocService;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class KhoaHocPanel extends AbstractManagementPanel<KhoaHoc> {

    private KhoaHocService service;

    public KhoaHocPanel() {
        super(new KhoaHocTableModel());
        try {
            service = ClientConnector.getKhoaHocService();
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể kết nối KhoaHocService: " + e.getMessage());
        }
        setupTableInteractions();
        reloadData();
    }

    @Override
    protected List<KhoaHoc> loadData() throws Exception {
        return service != null ? service.getAll() : java.util.Collections.emptyList();
    }

    @Override
    protected void onAdd() {
        showForm(null);
    }

    @Override
    protected void onEdit(KhoaHoc selected) {
        if (selected != null) {
            showForm(selected);
        }
    }

    @Override
    protected void onDelete(KhoaHoc selected) {
        if (selected == null) {
            return;
        }
        if (!DialogUtil.confirm(this, "Bạn chắc chắn muốn xóa khóa học " + selected.getTenKhoaHoc() + "?")) {
            return;
        }
        try {
            if (service.delete(selected.getMaKhoaHoc())) {
                DialogUtil.showInfo(this, "Đã xóa khóa học.");
                reloadData();
            } else {
                DialogUtil.showError(this, "Không thể xóa khóa học.");
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

    private void showForm(KhoaHoc item) {
        JTextField maField = FormUIUtil.createTextField();
        JTextField tenField = FormUIUtil.createTextField();

        if (item != null) {
            maField.setText(item.getMaKhoaHoc());
            maField.setEnabled(false);
            tenField.setText(item.getTenKhoaHoc());
        }

        ModernFormDialog dialog = new ModernFormDialog(
            SwingUtilities.getWindowAncestor(this),
            item == null ? "Thêm khóa học" : "Cập nhật khóa học",
            "Quản lý thông tin khóa học"
        );
        dialog.addField(new RequiredLabel("🎓 Mã khóa học"), maField);
        dialog.addField(new RequiredLabel("🎓 Tên khóa học"), tenField);

        dialog.setSaveAction(item == null ? "Thêm mới" : "Lưu thay đổi", () -> {
            if (!ValidationUtil.isNotBlank(maField.getText()) || !ValidationUtil.isNotBlank(tenField.getText())) {
                DialogUtil.showError(dialog, "Vui lòng nhập đầy đủ thông tin bắt buộc.");
                return;
            }
            KhoaHoc target = item != null ? item : new KhoaHoc();
            target.setMaKhoaHoc(maField.getText().trim());
            target.setTenKhoaHoc(tenField.getText().trim());
            try {
                boolean success = item == null ? service.insert(target) : service.update(target);
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

        dialog.focusLater(item == null ? maField : tenField);
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

        TableActionCell<KhoaHoc> actionCell = new TableActionCell<>(new TableActionCell.TableActionHandler<>() {
            @Override
            public void onView(KhoaHoc item) {
                // View action disabled
            }

            @Override
            public void onEdit(KhoaHoc item) {
                KhoaHocPanel.this.onEdit(item);
            }

            @Override
            public void onDelete(KhoaHoc item) {
                KhoaHocPanel.this.onDelete(item);
            }
        }, rowIndex -> {
            int modelRow = tableRef.convertRowIndexToModel(rowIndex);
            return tableModel.getRow(modelRow);
        }, false, true, true);

        tableRef.getColumnModel().getColumn(KhoaHocTableModel.ACTION_COLUMN_INDEX).setCellRenderer(actionCell);
        tableRef.getColumnModel().getColumn(KhoaHocTableModel.ACTION_COLUMN_INDEX).setCellEditor(actionCell);
        tableRef.getColumnModel().getColumn(KhoaHocTableModel.ACTION_COLUMN_INDEX).setPreferredWidth(220);
        tableRef.getColumnModel().getColumn(KhoaHocTableModel.ACTION_COLUMN_INDEX).setMinWidth(200);
        tableRef.getColumnModel().getColumn(KhoaHocTableModel.ACTION_COLUMN_INDEX).setMaxWidth(260);
    }
}
