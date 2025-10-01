package client.ui.model;

import common.dto.Diem;

public class DiemTableModel extends AbstractEntityTableModel<Diem> {
    public static final int ACTION_COLUMN_INDEX = 8;
    private static final String[] COLUMNS = {"ID", "Sinh viên", "Học phần", "Điểm QT", "Điểm thi", "Điểm tổng", "Học kỳ", "Năm học", "Hành động"};

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Diem diem = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return diem.getId();
            case 1:
                return diem.getSinhVien() != null ? diem.getSinhVien().getTenSv() : null;
            case 2:
                return diem.getHocPhan() != null ? diem.getHocPhan().getTenHocPhan() : null;
            case 3:
                return diem.getDiemQuaTrinh();
            case 4:
                return diem.getDiemThi();
            case 5:
                return diem.getDiemTongKet();
            case 6:
                return diem.getHocKy() != null ? diem.getHocKy().getTenHocKy() : null;
            case 7:
                return diem.getNamHoc() != null ? diem.getNamHoc().getTenNamHoc() : null;
            case ACTION_COLUMN_INDEX:
                return "";
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == ACTION_COLUMN_INDEX;
    }
}
