package client.ui.admin;

import client.network.ClientConnector;
import client.ui.components.ModernFormDialog;
import client.ui.components.RequiredLabel;
import client.ui.components.TableActionCell;
import client.ui.model.LopTableModel;
import client.util.DialogUtil;
import client.util.FormUIUtil;
import client.util.UITheme;
import client.util.ValidationUtil;
import common.dto.Khoa;
import common.dto.Lop;
import common.dto.Nganh;
import common.service.LopService;
import common.service.NganhService;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class LopPanel extends AbstractManagementPanel<Lop> {
    private LopService service;
    private NganhService nganhService;

    public LopPanel() {
        super(new LopTableModel());
        try {
            service = ClientConnector.getLopService();
            nganhService = ClientConnector.getNganhService();
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
    protected List<Lop> loadData() throws Exception {
        return service != null ? service.getAll() : java.util.Collections.emptyList();
    }

    @Override
    protected void onAdd() {
        showForm(null);
    }

    @Override
    protected void onEdit(Lop selected) {
        if (selected != null) {
            showForm(selected);
        }
    }

    @Override
    protected void onDelete(Lop selected) {
        if (selected == null) {
            return;
        }
        if (!DialogUtil.confirm(this, "Xóa lớp " + selected.getTenLop() + "?")) {
            return;
        }
        try {
            if (service.delete(selected.getMaLop())) {
                DialogUtil.showInfo(this, "Đã xóa thành công.");
                reloadData();
            } else {
                DialogUtil.showError(this, "Không thể xóa lớp.");
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

    private void showForm(Lop lop) {
        JTextField maField = FormUIUtil.createTextField();
        JTextField tenField = FormUIUtil.createTextField();
        JComboBox<Nganh> nganhCombo = FormUIUtil.createComboBox();
        JLabel khoaLabel = new JLabel();
        khoaLabel.setFont(UITheme.bodyFont());
        khoaLabel.setForeground(UITheme.TEXT_PRIMARY);

        try {
            List<Nganh> nganhs = nganhService.getAll();
            nganhCombo.setModel(new DefaultComboBoxModel<>(nganhs.toArray(new Nganh[0])));
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể tải danh sách ngành: " + e.getMessage());
        }

        nganhCombo.addActionListener(e -> {
            Nganh selectedNganh = (Nganh) nganhCombo.getSelectedItem();
            Khoa khoa = selectedNganh != null ? selectedNganh.getKhoa() : null;
            khoaLabel.setText(khoa != null ? khoa.getTenKhoa() : "");
        });

        if (lop != null) {
            maField.setText(lop.getMaLop());
            maField.setEnabled(false);
            tenField.setText(lop.getTenLop());
            if (lop.getNganh() != null) {
                nganhCombo.setSelectedItem(lop.getNganh());
            }
            if (lop.getKhoa() != null) {
                khoaLabel.setText(lop.getKhoa().getTenKhoa());
            }
        } else if (nganhCombo.getItemCount() > 0) {
            Nganh first = nganhCombo.getItemAt(0);
            khoaLabel.setText(first != null && first.getKhoa() != null ? first.getKhoa().getTenKhoa() : "");
        }

        ModernFormDialog dialog = new ModernFormDialog(
            SwingUtilities.getWindowAncestor(this),
            lop == null ? "Thêm lớp" : "Cập nhật lớp",
            "Quản lý thông tin lớp học"
        );
        dialog.addField(new RequiredLabel("🏫 Mã lớp"), maField);
        dialog.addField(new RequiredLabel("🏫 Tên lớp"), tenField);
        dialog.addField(new RequiredLabel("📚 Ngành"), nganhCombo);
        dialog.addField(new JLabel("🏛️ Khoa"), khoaLabel);

        dialog.setSaveAction(lop == null ? "Thêm mới" : "Lưu thay đổi", () -> {
            if (!ValidationUtil.isNotBlank(maField.getText()) || !ValidationUtil.isNotBlank(tenField.getText())) {
                DialogUtil.showError(dialog, "Mã lớp và tên lớp là bắt buộc.");
                return;
            }
            Nganh selectedNganh = (Nganh) nganhCombo.getSelectedItem();
            if (selectedNganh == null) {
                DialogUtil.showError(dialog, "Vui lòng chọn ngành.");
                return;
            }
            Lop newLop = lop != null ? lop : new Lop();
            newLop.setMaLop(maField.getText().trim());
            newLop.setTenLop(tenField.getText().trim());
            newLop.setNganh(selectedNganh);
            newLop.setKhoa(selectedNganh.getKhoa());

            try {
                boolean success = lop == null ? service.insert(newLop) : service.update(newLop);
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

        dialog.focusLater(lop == null ? maField : tenField);
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

        TableActionCell<Lop> actionCell = new TableActionCell<>(new TableActionCell.TableActionHandler<Lop>() {
            @Override
            public void onView(Lop item) {
                // View action disabled
            }

            @Override
            public void onEdit(Lop item) {
                LopPanel.this.onEdit(item);
            }

            @Override
            public void onDelete(Lop item) {
                LopPanel.this.onDelete(item);
            }
        }, rowIndex -> {
            int modelRow = tableRef.convertRowIndexToModel(rowIndex);
            return tableModel.getRow(modelRow);
        }, false, true, true);

        tableRef.getColumnModel().getColumn(LopTableModel.ACTION_COLUMN_INDEX).setCellRenderer(actionCell);
        tableRef.getColumnModel().getColumn(LopTableModel.ACTION_COLUMN_INDEX).setCellEditor(actionCell);
        tableRef.getColumnModel().getColumn(LopTableModel.ACTION_COLUMN_INDEX).setPreferredWidth(260);
        tableRef.getColumnModel().getColumn(LopTableModel.ACTION_COLUMN_INDEX).setMinWidth(220);
        tableRef.getColumnModel().getColumn(LopTableModel.ACTION_COLUMN_INDEX).setMaxWidth(320);
    }
}
