package client.ui.admin;

import client.network.ClientConnector;
import client.ui.components.TableActionCell;
import client.ui.model.DiemTableModel;
import client.util.DialogUtil;
import client.util.UITheme;
import common.dto.Diem;
import common.dto.HocKy;
import common.dto.HocPhan;
import common.dto.NamHoc;
import common.dto.SinhVien;
import common.service.DiemService;
import common.service.HocKyService;
import common.service.HocPhanService;
import common.service.NamHocService;
import common.service.SinhVienService;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collections;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DiemPanel extends AbstractManagementPanel<Diem> {
    private DiemService service;
    private SinhVienService sinhVienService;
    private HocPhanService hocPhanService;
    private HocKyService hocKyService;
    private NamHocService namHocService;

    public DiemPanel() {
        super(new DiemTableModel());
        try {
            service = ClientConnector.getDiemService();
            sinhVienService = ClientConnector.getSinhVienService();
            hocPhanService = ClientConnector.getHocPhanService();
            hocKyService = ClientConnector.getHocKyService();
            namHocService = ClientConnector.getNamHocService();
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
    protected List<Diem> loadData() throws Exception {
        return service != null ? service.getAll() : Collections.emptyList();
    }

    @Override
    protected void onAdd() {
        showForm(null);
    }

    @Override
    protected void onEdit(Diem selected) {
        if (selected != null) {
            showForm(selected);
        }
    }

    @Override
    protected void onDelete(Diem selected) {
        if (selected == null) {
            return;
        }
        if (!DialogUtil.confirm(this, "Xóa điểm của sinh viên " + (selected.getSinhVien() != null ? selected.getSinhVien().getTenSv() : "") + "?")) {
            return;
        }
        try {
            if (service.delete(selected.getId())) {
                DialogUtil.showInfo(this, "Đã xóa thành công.");
                reloadData();
            } else {
                DialogUtil.showError(this, "Không thể xóa." );
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

    private void showForm(Diem diem) {
        JComboBox<SinhVien> svCombo = new JComboBox<>();
        JComboBox<HocPhan> hpCombo = new JComboBox<>();
        JComboBox<HocKy> hocKyCombo = new JComboBox<>();
        JComboBox<NamHoc> namHocCombo = new JComboBox<>();
        JTextField diemQtField = new JTextField();
        JTextField diemThiField = new JTextField();

        try {
            List<SinhVien> svs = sinhVienService.getAll();
            svCombo.setModel(new DefaultComboBoxModel<>(svs.toArray(new SinhVien[0])));
            List<HocPhan> hps = hocPhanService.getAll();
            hpCombo.setModel(new DefaultComboBoxModel<>(hps.toArray(new HocPhan[0])));
            List<HocKy> hocKys = hocKyService != null ? hocKyService.getAll() : java.util.Collections.emptyList();
            hocKyCombo.setModel(new DefaultComboBoxModel<>(hocKys.toArray(new HocKy[0])));
            List<NamHoc> namHocs = namHocService != null ? namHocService.getAll() : java.util.Collections.emptyList();
            namHocCombo.setModel(new DefaultComboBoxModel<>(namHocs.toArray(new NamHoc[0])));
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể tải dữ liệu: " + e.getMessage());
        }

        if (diem != null) {
            if (diem.getSinhVien() != null) {
                svCombo.setSelectedItem(diem.getSinhVien());
            }
            if (diem.getHocPhan() != null) {
                hpCombo.setSelectedItem(diem.getHocPhan());
            }
            diemQtField.setText(String.valueOf(diem.getDiemQuaTrinh()));
            diemThiField.setText(String.valueOf(diem.getDiemThi()));
            if (diem.getHocKy() != null) {
                hocKyCombo.setSelectedItem(diem.getHocKy());
            }
            if (diem.getNamHoc() != null) {
                namHocCombo.setSelectedItem(diem.getNamHoc());
            }
        }

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(UITheme.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("👤 Sinh viên"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(svCombo, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("📖 Học phần"), gbc);
        gbc.gridx = 1;
        panel.add(hpCombo, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("📊 Điểm quá trình"), gbc);
        gbc.gridx = 1;
        panel.add(diemQtField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("📈 Điểm thi"), gbc);
        gbc.gridx = 1;
        panel.add(diemThiField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("📅 Học kỳ"), gbc);
        gbc.gridx = 1;
        panel.add(hocKyCombo, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("📆 Năm học"), gbc);
        gbc.gridx = 1;
        panel.add(namHocCombo, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, diem == null ? "Thêm điểm" : "Cập nhật điểm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            SinhVien sinhVien = (SinhVien) svCombo.getSelectedItem();
            HocPhan hocPhan = (HocPhan) hpCombo.getSelectedItem();
            HocKy hocKy = (HocKy) hocKyCombo.getSelectedItem();
            NamHoc namHoc = (NamHoc) namHocCombo.getSelectedItem();
            if (sinhVien == null || hocPhan == null) {
                DialogUtil.showError(this, "Vui lòng chọn sinh viên và học phần.");
                return;
            }
            if (hocKy == null || namHoc == null) {
                DialogUtil.showError(this, "Vui lòng chọn học kỳ và năm học.");
                return;
            }
            double diemQt;
            double diemThi;
            try {
                diemQt = Double.parseDouble(diemQtField.getText().trim());
                diemThi = Double.parseDouble(diemThiField.getText().trim());
            } catch (NumberFormatException e) {
                DialogUtil.showError(this, "Điểm phải là số.");
                return;
            }

            Diem newDiem = diem != null ? diem : new Diem();
            newDiem.setSinhVien(sinhVien);
            newDiem.setHocPhan(hocPhan);
            newDiem.setDiemQuaTrinh(diemQt);
            newDiem.setDiemThi(diemThi);
            newDiem.recalculateTongKet();
            newDiem.setHocKy(hocKy);
            newDiem.setNamHoc(namHoc);

            try {
                boolean success = diem == null ? service.insert(newDiem) : service.update(newDiem);
                if (success) {
                    DialogUtil.showInfo(this, "Lưu thành công.");
                    reloadData();
                } else {
                    DialogUtil.showError(this, "Không thể lưu dữ liệu.");
                }
            } catch (Exception e) {
                DialogUtil.showError(this, e.getMessage());
            }
        }
    }

    private void setupTableInteractions() {
        JTable tableRef = this.table;
        tableRef.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tableRef.getSelectedRow() >= 0) {
                    int modelRow = tableRef.convertRowIndexToModel(tableRef.getSelectedRow());
                    showDetail(tableModel.getRow(modelRow));
                }
            }
        });

        TableActionCell<Diem> actionCell = new TableActionCell<>(new TableActionCell.TableActionHandler<Diem>() {
            @Override
            public void onView(Diem item) {
                showDetail(item);
            }

            @Override
            public void onEdit(Diem item) {
                DiemPanel.this.onEdit(item);
            }

            @Override
            public void onDelete(Diem item) {
                DiemPanel.this.onDelete(item);
            }
        }, rowIndex -> {
            int modelRow = tableRef.convertRowIndexToModel(rowIndex);
            return tableModel.getRow(modelRow);
        });

        tableRef.getColumnModel().getColumn(DiemTableModel.ACTION_COLUMN_INDEX).setCellRenderer(actionCell);
        tableRef.getColumnModel().getColumn(DiemTableModel.ACTION_COLUMN_INDEX).setCellEditor(actionCell);
        tableRef.getColumnModel().getColumn(DiemTableModel.ACTION_COLUMN_INDEX).setPreferredWidth(260);
        tableRef.getColumnModel().getColumn(DiemTableModel.ACTION_COLUMN_INDEX).setMinWidth(220);
        tableRef.getColumnModel().getColumn(DiemTableModel.ACTION_COLUMN_INDEX).setMaxWidth(320);
    }

    private void showDetail(Diem diem) {
        if (diem == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Sinh viên: ").append(diem.getSinhVien() != null ? diem.getSinhVien().getTenSv() : "-").append('\n');
        sb.append("Học phần: ").append(diem.getHocPhan() != null ? diem.getHocPhan().getTenHocPhan() : "-").append('\n');
        sb.append("Điểm quá trình: ").append(diem.getDiemQuaTrinh()).append('\n');
        sb.append("Điểm thi: ").append(diem.getDiemThi()).append('\n');
        sb.append("Điểm tổng kết: ").append(diem.getDiemTongKet()).append('\n');
        sb.append("Học kỳ: ").append(diem.getHocKy() != null ? diem.getHocKy().getTenHocKy() : "-").append('\n');
        sb.append("Năm học: ").append(diem.getNamHoc() != null ? diem.getNamHoc().getTenNamHoc() : "-").append('\n');
        DialogUtil.showInfo(this, sb.toString());
    }
}
