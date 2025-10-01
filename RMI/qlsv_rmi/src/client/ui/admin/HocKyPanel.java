package client.ui.admin;

import client.network.ClientConnector;
import client.ui.components.ModernFormDialog;
import client.ui.components.RequiredLabel;
import client.ui.components.TableActionCell;
import client.ui.model.HocKyTableModel;
import client.util.DialogUtil;
import client.util.FormUIUtil;
import client.util.ValidationUtil;
import common.dto.HocKy;
import common.service.HocKyService;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class HocKyPanel extends AbstractManagementPanel<HocKy> {

    private HocKyService service;

    public HocKyPanel() {
        super(new HocKyTableModel());
        try {
            service = ClientConnector.getHocKyService();
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể kết nối HocKyService: " + e.getMessage());
        }
        setupTableInteractions();
        reloadData();
    }

    @Override
    protected List<HocKy> loadData() throws Exception {
        return service != null ? service.getAll() : java.util.Collections.emptyList();
    }

    @Override
    protected void onAdd() {
        showForm(null);
    }

    @Override
    protected void onEdit(HocKy selected) {
        if (selected != null) {
            showForm(selected);
        }
    }

    @Override
    protected void onDelete(HocKy selected) {
        if (selected == null) {
            return;
        }
        if (!DialogUtil.confirm(this, "Bạn chắc chắn muốn xóa học kỳ " + selected.getTenHocKy() + "?")) {
            return;
        }
        try {
            if (service.delete(selected.getMaHocKy())) {
                DialogUtil.showInfo(this, "Đã xóa học kỳ.");
                reloadData();
            } else {
                DialogUtil.showError(this, "Không thể xóa học kỳ.");
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

    private void showForm(HocKy item) {
        JTextField maField = FormUIUtil.createTextField();
        JTextField tenField = FormUIUtil.createTextField();

        if (item != null) {
            maField.setText(item.getMaHocKy());
            maField.setEnabled(false);
            tenField.setText(item.getTenHocKy());
        }

        ModernFormDialog dialog = new ModernFormDialog(
            SwingUtilities.getWindowAncestor(this),
            item == null ? "Thêm học kỳ" : "Cập nhật học kỳ",
            "Quản lý thông tin học kỳ"
        );
        dialog.addField(new RequiredLabel("📅 Mã học kỳ"), maField);
        dialog.addField(new RequiredLabel("📅 Tên học kỳ"), tenField);

        dialog.setSaveAction(item == null ? "Thêm mới" : "Lưu thay đổi", () -> {
            if (!ValidationUtil.isNotBlank(maField.getText()) || !ValidationUtil.isNotBlank(tenField.getText())) {
                DialogUtil.showError(dialog, "Vui lòng nhập đầy đủ thông tin bắt buộc.");
                return;
            }
            HocKy target = item != null ? item : new HocKy();
            target.setMaHocKy(maField.getText().trim());
            target.setTenHocKy(tenField.getText().trim());
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

        TableActionCell<HocKy> actionCell = new TableActionCell<>(new TableActionCell.TableActionHandler<>() {
            @Override
            public void onView(HocKy item) {
                // View action disabled
            }

            @Override
            public void onEdit(HocKy item) {
                HocKyPanel.this.onEdit(item);
            }

            @Override
            public void onDelete(HocKy item) {
                HocKyPanel.this.onDelete(item);
            }
        }, rowIndex -> {
            int modelRow = tableRef.convertRowIndexToModel(rowIndex);
            return tableModel.getRow(modelRow);
        }, false, true, true);

        tableRef.getColumnModel().getColumn(HocKyTableModel.ACTION_COLUMN_INDEX).setCellRenderer(actionCell);
        tableRef.getColumnModel().getColumn(HocKyTableModel.ACTION_COLUMN_INDEX).setCellEditor(actionCell);
        tableRef.getColumnModel().getColumn(HocKyTableModel.ACTION_COLUMN_INDEX).setPreferredWidth(220);
        tableRef.getColumnModel().getColumn(HocKyTableModel.ACTION_COLUMN_INDEX).setMinWidth(200);
        tableRef.getColumnModel().getColumn(HocKyTableModel.ACTION_COLUMN_INDEX).setMaxWidth(260);
    }
}
