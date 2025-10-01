package client.ui.admin;

import client.network.ClientConnector;
import client.ui.components.ModernFormDialog;
import client.ui.components.RequiredLabel;
import client.ui.components.TableActionCell;
import client.ui.model.HocPhanTableModel;
import client.util.DialogUtil;
import client.util.FormUIUtil;
import client.util.ValidationUtil;
import common.dto.HocPhan;
import common.service.HocPhanService;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class HocPhanPanel extends AbstractManagementPanel<HocPhan> {
    private HocPhanService service;

    public HocPhanPanel() {
        super(new HocPhanTableModel());
        try {
            service = ClientConnector.getHocPhanService();
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể kết nối HocPhanService: " + e.getMessage());
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
    protected List<HocPhan> loadData() throws Exception {
        return service != null ? service.getAll() : java.util.Collections.emptyList();
    }

    @Override
    protected void onAdd() {
        showForm(null);
    }

    @Override
    protected void onEdit(HocPhan selected) {
        if (selected != null) {
            showForm(selected);
        }
    }

    @Override
    protected void onDelete(HocPhan selected) {
        if (selected == null) {
            return;
        }
        if (!DialogUtil.confirm(this, "Xóa học phần " + selected.getTenHocPhan() + "?")) {
            return;
        }
        try {
            if (service.delete(selected.getMaHocPhan())) {
                DialogUtil.showInfo(this, "Đã xóa thành công.");
                reloadData();
            } else {
                DialogUtil.showError(this, "Không thể xóa học phần.");
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

    private void showForm(HocPhan hocPhan) {
        JTextField maField = FormUIUtil.createTextField();
        JTextField tenField = FormUIUtil.createTextField();
        JTextField tinChiField = FormUIUtil.createTextField();

        if (hocPhan != null) {
            maField.setText(hocPhan.getMaHocPhan());
            maField.setEnabled(false);
            tenField.setText(hocPhan.getTenHocPhan());
            tinChiField.setText(String.valueOf(hocPhan.getSoTinChi()));
        }

        ModernFormDialog dialog = new ModernFormDialog(
            SwingUtilities.getWindowAncestor(this),
            hocPhan == null ? "Thêm học phần" : "Cập nhật học phần",
            "Quản lý thông tin học phần"
        );
        dialog.addField(new RequiredLabel("📖 Mã học phần"), maField);
        dialog.addField(new RequiredLabel("📖 Tên học phần"), tenField);
        dialog.addField(new RequiredLabel("🔢 Số tín chỉ"), tinChiField);

        dialog.setSaveAction(hocPhan == null ? "Thêm mới" : "Lưu thay đổi", () -> {
            if (!ValidationUtil.isNotBlank(maField.getText()) || !ValidationUtil.isNotBlank(tenField.getText())
                || !ValidationUtil.isNotBlank(tinChiField.getText())) {
                DialogUtil.showError(dialog, "Vui lòng nhập đầy đủ thông tin bắt buộc.");
                return;
            }
            int soTinChi;
            try {
                soTinChi = Integer.parseInt(tinChiField.getText().trim());
            } catch (NumberFormatException e) {
                DialogUtil.showError(dialog, "Số tín chỉ phải là số.");
                return;
            }

            HocPhan newHp = hocPhan != null ? hocPhan : new HocPhan();
            newHp.setMaHocPhan(maField.getText().trim());
            newHp.setTenHocPhan(tenField.getText().trim());
            newHp.setSoTinChi(soTinChi);

            try {
                boolean success = hocPhan == null ? service.insert(newHp) : service.update(newHp);
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

        dialog.focusLater(hocPhan == null ? maField : tenField);
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

        TableActionCell<HocPhan> actionCell = new TableActionCell<>(new TableActionCell.TableActionHandler<HocPhan>() {
            @Override
            public void onView(HocPhan item) {
                // View action disabled
            }

            @Override
            public void onEdit(HocPhan item) {
                HocPhanPanel.this.onEdit(item);
            }

            @Override
            public void onDelete(HocPhan item) {
                HocPhanPanel.this.onDelete(item);
            }
        }, rowIndex -> {
            int modelRow = tableRef.convertRowIndexToModel(rowIndex);
            return tableModel.getRow(modelRow);
        }, false, true, true);

        tableRef.getColumnModel().getColumn(HocPhanTableModel.ACTION_COLUMN_INDEX).setCellRenderer(actionCell);
        tableRef.getColumnModel().getColumn(HocPhanTableModel.ACTION_COLUMN_INDEX).setCellEditor(actionCell);
        tableRef.getColumnModel().getColumn(HocPhanTableModel.ACTION_COLUMN_INDEX).setPreferredWidth(260);
        tableRef.getColumnModel().getColumn(HocPhanTableModel.ACTION_COLUMN_INDEX).setMinWidth(220);
        tableRef.getColumnModel().getColumn(HocPhanTableModel.ACTION_COLUMN_INDEX).setMaxWidth(320);
    }
}
