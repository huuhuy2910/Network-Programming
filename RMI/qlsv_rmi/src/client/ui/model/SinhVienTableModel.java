package client.ui.model;

import common.dto.SinhVien;

public class SinhVienTableModel extends AbstractEntityTableModel<SinhVien> {
    public static final int ACTION_COLUMN_INDEX = 6;
    private static final String[] COLUMNS = {"Mã SV", "Tên SV", "Lớp", "Khóa học", "Học lực", "Trạng thái", "Hành động"};

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
        SinhVien sv = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return sv.getSvId();
            case 1:
                return sv.getTenSv();
            case 2:
                return sv.getLop() != null ? sv.getLop().getTenLop() : null;
            case 3:
                return sv.getKhoaHoc() != null ? sv.getKhoaHoc().getTenKhoaHoc() : null;
            case 4:
                return sv.getAcademicRank() != null ? sv.getAcademicRank() : "-";
            case 5:
                return sv.getStatus();
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
