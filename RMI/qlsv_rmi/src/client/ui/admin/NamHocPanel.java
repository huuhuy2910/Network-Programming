package client.ui.admin;

import client.network.ClientConnector;
import client.ui.components.ModernFormDialog;
import client.ui.components.RequiredLabel;
import client.ui.components.TableActionCell;
import client.ui.model.NamHocTableModel;
import client.util.DialogUtil;
import client.util.FormUIUtil;
import client.util.ValidationUtil;
import common.dto.NamHoc;
import common.service.NamHocService;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class NamHocPanel extends AbstractManagementPanel<NamHoc> {

    private NamHocService service;

    public NamHocPanel() {
        super(new NamHocTableModel());
        try {
            service = ClientConnector.getNamHocService();
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể kết nối NamHocService: " + e.getMessage());
        }
        setupTableInteractions();
        reloadData();
    }

    @Override
    protected List<NamHoc> loadData() throws Exception {
        return service != null ? service.getAll() : java.util.Collections.emptyList();
    }

    @Override
    protected void onAdd() {
        showForm(null);
    }

    @Override
    protected void onEdit(NamHoc selected) {
        if (selected != null) {
            showForm(selected);
        }
    }

    @Override
    protected void onDelete(NamHoc selected) {
        if (selected == null) {
            return;
        }
        if (!DialogUtil.confirm(this, "Bạn chắc chắn muốn xóa năm học " + selected.getTenNamHoc() + "?")) {
            return;
        }
        try {
            if (service.delete(selected.getMaNamHoc())) {
                DialogUtil.showInfo(this, "Đã xóa năm học.");
                reloadData();
            } else {
                DialogUtil.showError(this, "Không thể xóa năm học.");
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

    private void showForm(NamHoc item) {
        JTextField maField = FormUIUtil.createTextField();
        JTextField tenField = FormUIUtil.createTextField();

        if (item != null) {
            maField.setText(item.getMaNamHoc());
            maField.setEnabled(false);
            tenField.setText(item.getTenNamHoc());
        }

        ModernFormDialog dialog = new ModernFormDialog(
            SwingUtilities.getWindowAncestor(this),
            item == null ? "Thêm năm học" : "Cập nhật năm học",
            "Quản lý thông tin năm học"
        );
        dialog.addField(new RequiredLabel("📆 Mã năm học"), maField);
        dialog.addField(new RequiredLabel("📆 Tên năm học"), tenField);

        dialog.setSaveAction(item == null ? "Thêm mới" : "Lưu thay đổi", () -> {
            if (!ValidationUtil.isNotBlank(maField.getText()) || !ValidationUtil.isNotBlank(tenField.getText())) {
                DialogUtil.showError(dialog, "Vui lòng nhập đầy đủ thông tin bắt buộc.");
                return;
            }
            NamHoc target = item != null ? item : new NamHoc();
            target.setMaNamHoc(maField.getText().trim());
            target.setTenNamHoc(tenField.getText().trim());
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

        TableActionCell<NamHoc> actionCell = new TableActionCell<>(new TableActionCell.TableActionHandler<>() {
            @Override
            public void onView(NamHoc item) {
                // View action disabled
            }

            @Override
            public void onEdit(NamHoc item) {
                NamHocPanel.this.onEdit(item);
            }

            @Override
            public void onDelete(NamHoc item) {
                NamHocPanel.this.onDelete(item);
            }
        }, rowIndex -> {
            int modelRow = tableRef.convertRowIndexToModel(rowIndex);
            return tableModel.getRow(modelRow);
        }, false, true, true);

        tableRef.getColumnModel().getColumn(NamHocTableModel.ACTION_COLUMN_INDEX).setCellRenderer(actionCell);
        tableRef.getColumnModel().getColumn(NamHocTableModel.ACTION_COLUMN_INDEX).setCellEditor(actionCell);
        tableRef.getColumnModel().getColumn(NamHocTableModel.ACTION_COLUMN_INDEX).setPreferredWidth(220);
        tableRef.getColumnModel().getColumn(NamHocTableModel.ACTION_COLUMN_INDEX).setMinWidth(200);
        tableRef.getColumnModel().getColumn(NamHocTableModel.ACTION_COLUMN_INDEX).setMaxWidth(260);
    }
}
