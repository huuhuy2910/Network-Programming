package client.ui.admin;

import client.network.ClientConnector;
import client.ui.components.TableActionCell;
import client.ui.model.SinhVienTableModel;
import client.util.AcademicStandingUtil;
import client.util.DialogUtil;
import common.dto.HocKy;
import common.dto.Khoa;
import common.dto.KhoaHoc;
import common.dto.Lop;
import common.dto.NamHoc;
import common.dto.Nganh;
import common.dto.SinhVien;
import common.service.DiemService;
import common.service.HocKyService;
import common.service.HocPhanService;
import common.service.KhoaHocService;
import common.service.KhoaService;
import common.service.LopService;
import common.service.NamHocService;
import common.service.NganhService;
import common.service.SinhVienService;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SinhVienPanel extends AbstractManagementPanel<SinhVien> {
    private SinhVienService service;
    private LopService lopService;
    private DiemService diemService;
    private HocPhanService hocPhanService;
    private NganhService nganhService;
    private KhoaService khoaService;
    private KhoaHocService khoaHocService;
    private HocKyService hocKyService;
    private NamHocService namHocService;

    private JComboBox<Khoa> khoaFilter;
    private JComboBox<Nganh> nganhFilter;
    private JComboBox<Lop> lopFilter;

    private List<SinhVien> allSinhVien = new ArrayList<>();
    private List<Khoa> allKhoas = Collections.emptyList();
    private List<Nganh> allNganhs = Collections.emptyList();
    private List<Lop> allLops = Collections.emptyList();
    private List<KhoaHoc> allKhoaHocs = Collections.emptyList();
    private List<HocKy> allHocKys = Collections.emptyList();
    private List<NamHoc> allNamHocs = Collections.emptyList();
    private boolean updatingFilters = false;
    private String currentKeyword = "";
    private boolean academicStandingErrorShown = false;

    public SinhVienPanel() {
        super(new SinhVienTableModel());
        try {
            service = ClientConnector.getSinhVienService();
            lopService = ClientConnector.getLopService();
            diemService = ClientConnector.getDiemService();
            hocPhanService = ClientConnector.getHocPhanService();
            nganhService = ClientConnector.getNganhService();
            khoaService = ClientConnector.getKhoaService();
            khoaHocService = ClientConnector.getKhoaHocService();
            hocKyService = ClientConnector.getHocKyService();
            namHocService = ClientConnector.getNamHocService();
        } catch (Exception e) {
            DialogUtil.showError(this, "Lỗi kết nối: " + e.getMessage());
        }
        addTableInteractions();
        setupActionColumn();
        reloadData();
    }

    @Override
    public void reloadData() {
        loadFilterOptions();
        super.reloadData();
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
    protected javax.swing.JComponent buildFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        filterPanel.setOpaque(false);

        khoaFilter = createFilterCombo("Tất cả khoa");
        nganhFilter = createFilterCombo("Tất cả ngành");
        lopFilter = createFilterCombo("Tất cả lớp");

        filterPanel.add(new JLabel("Khoa"));
        filterPanel.add(khoaFilter);
        filterPanel.add(new JLabel("Ngành"));
        filterPanel.add(nganhFilter);
        filterPanel.add(new JLabel("Lớp"));
        filterPanel.add(lopFilter);

        khoaFilter.addActionListener(e -> onKhoaFilterChanged());
        nganhFilter.addActionListener(e -> onNganhFilterChanged());
        lopFilter.addActionListener(e -> onLopFilterChanged());

        return filterPanel;
    }

    private <T> JComboBox<T> createFilterCombo(String placeholder) {
        JComboBox<T> comboBox = new JComboBox<>();
        comboBox.setPreferredSize(new Dimension(180, 32));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    ((JLabel) component).setText(placeholder);
                }
                return component;
            }
        });
        return comboBox;
    }

    private void loadFilterOptions() {
        if (khoaFilter == null || nganhFilter == null || lopFilter == null) {
            return;
        }
        try {
            allKhoas = khoaService != null ? khoaService.getAll() : Collections.emptyList();
            allNganhs = nganhService != null ? nganhService.getAll() : Collections.emptyList();
            allLops = lopService != null ? lopService.getAll() : Collections.emptyList();
            allKhoaHocs = khoaHocService != null ? khoaHocService.getAll() : Collections.emptyList();
            allHocKys = hocKyService != null ? hocKyService.getAll() : Collections.emptyList();
            allNamHocs = namHocService != null ? namHocService.getAll() : Collections.emptyList();
        } catch (Exception e) {
            DialogUtil.showError(this, "Không thể tải bộ lọc: " + e.getMessage());
            allKhoas = Collections.emptyList();
            allNganhs = Collections.emptyList();
            allLops = Collections.emptyList();
            allKhoaHocs = Collections.emptyList();
            allHocKys = Collections.emptyList();
            allNamHocs = Collections.emptyList();
        }

        updatingFilters = true;
        setComboOptions(khoaFilter, allKhoas, (Khoa) khoaFilter.getSelectedItem());
        updatingFilters = false;
        onKhoaFilterChanged();
    }

    private void onKhoaFilterChanged() {
        if (updatingFilters) {
            return;
        }
        updatingFilters = true;
        Khoa selectedKhoa = (Khoa) (khoaFilter != null ? khoaFilter.getSelectedItem() : null);
        Nganh previous = nganhFilter != null ? (Nganh) nganhFilter.getSelectedItem() : null;

        List<Nganh> filtered = allNganhs.stream()
            .filter(ng -> selectedKhoa == null || (ng.getKhoa() != null && Objects.equals(ng.getKhoa().getMaKhoa(), selectedKhoa.getMaKhoa())))
            .collect(Collectors.toList());

        setComboOptions(nganhFilter, filtered, previous);
        updatingFilters = false;
        onNganhFilterChanged();
    }

    private void onNganhFilterChanged() {
        if (updatingFilters) {
            return;
        }
        updatingFilters = true;
        Nganh selectedNganh = nganhFilter != null ? (Nganh) nganhFilter.getSelectedItem() : null;
        Lop previous = lopFilter != null ? (Lop) lopFilter.getSelectedItem() : null;

        List<Lop> filtered = allLops.stream()
            .filter(lp -> selectedNganh == null || (lp.getNganh() != null && Objects.equals(lp.getNganh().getMaNganh(), selectedNganh.getMaNganh())))
            .collect(Collectors.toList());

        setComboOptions(lopFilter, filtered, previous);
        updatingFilters = false;
        refreshTableData();
    }

    private void onLopFilterChanged() {
        if (updatingFilters) {
            return;
        }
        refreshTableData();
    }

    private <T> void setComboOptions(JComboBox<T> comboBox, List<T> items, T previousSelection) {
        if (comboBox == null) {
            return;
        }
        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<>();
        model.addElement(null);
        for (T item : items) {
            model.addElement(item);
        }
        comboBox.setModel(model);
        if (previousSelection != null && items.contains(previousSelection)) {
            comboBox.setSelectedItem(previousSelection);
        } else {
            comboBox.setSelectedIndex(0);
        }
    }

    private void refreshTableData() {
        tableModel.setData(applyFilters(allSinhVien));
    }

    private List<SinhVien> applyFilters(List<SinhVien> source) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        Khoa selectedKhoa = khoaFilter != null ? (Khoa) khoaFilter.getSelectedItem() : null;
        Nganh selectedNganh = nganhFilter != null ? (Nganh) nganhFilter.getSelectedItem() : null;
        Lop selectedLop = lopFilter != null ? (Lop) lopFilter.getSelectedItem() : null;
        String keyword = currentKeyword != null ? currentKeyword : "";

        return source.stream()
            .filter(sv -> {
                Lop lop = sv.getLop();
                Khoa khoa = lop != null ? lop.getKhoa() : null;
                Nganh nganh = lop != null ? lop.getNganh() : null;

                boolean matchKhoa = selectedKhoa == null || (khoa != null && Objects.equals(khoa.getMaKhoa(), selectedKhoa.getMaKhoa()));
                boolean matchNganh = selectedNganh == null || (nganh != null && Objects.equals(nganh.getMaNganh(), selectedNganh.getMaNganh()));
                boolean matchLop = selectedLop == null || (lop != null && Objects.equals(lop.getMaLop(), selectedLop.getMaLop()));

                if (!matchKhoa || !matchNganh || !matchLop) {
                    return false;
                }

                if (keyword.isEmpty()) {
                    return true;
                }
                String lowerKeyword = keyword.toLowerCase();
                return (sv.getSvId() != null && sv.getSvId().toLowerCase().contains(lowerKeyword))
                    || (sv.getTenSv() != null && sv.getTenSv().toLowerCase().contains(lowerKeyword));
            })
            .collect(Collectors.toList());
    }

    private void addTableInteractions() {
        JTable tableRef = this.table;
        tableRef.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tableRef.getSelectedRow() >= 0) {
                    int viewRow = tableRef.getSelectedRow();
                    int modelRow = tableRef.convertRowIndexToModel(viewRow);
                    SinhVien selected = tableModel.getRow(modelRow);
                    showDetail(selected);
                }
            }
        });
    }

    private void setupActionColumn() {
        SinhVienTableModel model = (SinhVienTableModel) tableModel;
        JTable tableRef = this.table;
        TableActionCell.RowFetcher<SinhVien> fetcher = rowIndex -> {
            int modelRow = tableRef.convertRowIndexToModel(rowIndex);
            return model.getRow(modelRow);
        };
        TableActionCell<SinhVien> actionCell = new TableActionCell<>(new TableActionCell.TableActionHandler<SinhVien>() {
            @Override
            public void onView(SinhVien item) {
                showDetail(item);
            }

            @Override
            public void onEdit(SinhVien item) {
                SinhVienPanel.this.onEdit(item);
            }

            @Override
            public void onDelete(SinhVien item) {
                SinhVienPanel.this.onDelete(item);
            }
    }, fetcher);
        tableRef.getColumnModel().getColumn(SinhVienTableModel.ACTION_COLUMN_INDEX).setCellRenderer(actionCell);
        tableRef.getColumnModel().getColumn(SinhVienTableModel.ACTION_COLUMN_INDEX).setCellEditor(actionCell);
        tableRef.getColumnModel().getColumn(SinhVienTableModel.ACTION_COLUMN_INDEX).setPreferredWidth(260);
        tableRef.getColumnModel().getColumn(SinhVienTableModel.ACTION_COLUMN_INDEX).setMinWidth(220);
        tableRef.getColumnModel().getColumn(SinhVienTableModel.ACTION_COLUMN_INDEX).setMaxWidth(320);
    }

    @Override
    protected List<SinhVien> loadData() throws Exception {
        List<SinhVien> data = service != null ? service.getAll() : Collections.emptyList();
        // Sắp xếp theo mã sinh viên tăng dần
        data.sort((a, b) -> {
            if (a.getSvId() == null && b.getSvId() == null) return 0;
            if (a.getSvId() == null) return 1;
            if (b.getSvId() == null) return -1;
            return a.getSvId().compareToIgnoreCase(b.getSvId());
        });
        enrichAcademicStanding(data);
        allSinhVien = new ArrayList<>(data);
        return applyFilters(allSinhVien);
    }

    @Override
    protected void onAdd() {
        try {
            List<Lop> lopList = lopService != null ? lopService.getAll() : Collections.emptyList();
            if (lopList.isEmpty() || allKhoaHocs.isEmpty() || allHocKys.isEmpty() || allNamHocs.isEmpty()) {
                DialogUtil.showError(this, "Vui lòng cấu hình đầy đủ lớp, khóa học, học kỳ và năm học trước khi thêm sinh viên.");
                return;
            }
            SinhVienFormDialog dialog = new SinhVienFormDialog(javax.swing.SwingUtilities.getWindowAncestor(this), lopList,
                allKhoaHocs, allHocKys, allNamHocs, null);
            SinhVien created = dialog.showDialog();
            if (created != null) {
                if (service.insert(created)) {
                    DialogUtil.showInfo(this, "Thêm sinh viên thành công.");
                    reloadData();
                } else {
                    DialogUtil.showError(this, "Không thể thêm sinh viên.");
                }
            }
        } catch (Exception e) {
            DialogUtil.showError(this, e.getMessage());
        }
    }

    @Override
    protected void onEdit(SinhVien selected) {
        if (selected == null) {
            return;
        }
        try {
            List<Lop> lopList = lopService != null ? lopService.getAll() : Collections.emptyList();
            if (lopList.isEmpty() || allKhoaHocs.isEmpty() || allHocKys.isEmpty() || allNamHocs.isEmpty()) {
                DialogUtil.showError(this, "Thiếu dữ liệu tham chiếu để cập nhật sinh viên.");
                return;
            }
            SinhVienFormDialog dialog = new SinhVienFormDialog(javax.swing.SwingUtilities.getWindowAncestor(this), lopList,
                allKhoaHocs, allHocKys, allNamHocs, selected);
            SinhVien updated = dialog.showDialog();
            if (updated != null) {
                if (service.update(updated)) {
                    DialogUtil.showInfo(this, "Cập nhật sinh viên thành công.");
                    reloadData();
                } else {
                    DialogUtil.showError(this, "Không thể cập nhật sinh viên.");
                }
            }
        } catch (Exception e) {
            DialogUtil.showError(this, e.getMessage());
        }
    }

    @Override
    protected void onDelete(SinhVien selected) {
        if (selected == null) {
            return;
        }
        if (!DialogUtil.confirm(this, "Xóa sinh viên " + selected.getTenSv() + "?")) {
            return;
        }
        try {
            if (service.delete(selected.getSvId())) {
                DialogUtil.showInfo(this, "Đã xóa thành công.");
                reloadData();
            } else {
                DialogUtil.showError(this, "Không thể xóa sinh viên.");
            }
        } catch (Exception e) {
            DialogUtil.showError(this, e.getMessage());
        }
    }

    @Override
    protected void onSearch(String keyword) {
        currentKeyword = keyword != null ? keyword.trim().toLowerCase() : "";
        refreshTableData();
    }

    private void showDetail(SinhVien sinhVien) {
        if (sinhVien == null) {
            return;
        }
        try {
            SinhVienDetailDialog dialog = new SinhVienDetailDialog(
                javax.swing.SwingUtilities.getWindowAncestor(this),
                sinhVien,
                diemService,
                hocPhanService,
                hocKyService,
                namHocService
            );
            dialog.setVisible(true);
            reloadData();
        } catch (Exception e) {
            DialogUtil.showError(this, e.getMessage());
        }
    }

    private void enrichAcademicStanding(List<SinhVien> students) {
        if (students == null || students.isEmpty()) {
            return;
        }
        if (diemService == null) {
            students.forEach(sv -> {
                sv.setGpa(null);
                sv.setAcademicRank(null);
            });
            if (!academicStandingErrorShown) {
                DialogUtil.showError(this, "Không thể tính học lực do thiếu kết nối dịch vụ điểm.");
                academicStandingErrorShown = true;
            }
            return;
        }
        boolean hasError = false;
        String lastError = null;
        for (SinhVien sv : students) {
            if (sv == null || sv.getSvId() == null) {
                continue;
            }
            try {
                double gpa = diemService.calculateGpa(sv.getSvId());
                if (Double.isNaN(gpa)) {
                    sv.setGpa(null);
                    sv.setAcademicRank(null);
                } else {
                    sv.setGpa(gpa);
                    sv.setAcademicRank(AcademicStandingUtil.classifyByGpa(gpa));
                }
            } catch (Exception ex) {
                hasError = true;
                lastError = ex.getMessage();
                sv.setGpa(null);
                sv.setAcademicRank(null);
            }
        }
        if (hasError && !academicStandingErrorShown) {
            DialogUtil.showError(this, "Không thể tính học lực: " + lastError);
            academicStandingErrorShown = true;
        }
        if (!hasError) {
            academicStandingErrorShown = false;
        }
    }
}
