package client.ui.model;

import common.dto.Lop;

public class LopTableModel extends AbstractEntityTableModel<Lop> {
    public static final int ACTION_COLUMN_INDEX = 4;
    private static final String[] COLUMNS = {"Mã lớp", "Tên lớp", "Ngành", "Khoa", "Hành động"};

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
        Lop lop = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return lop.getMaLop();
            case 1:
                return lop.getTenLop();
            case 2:
                return lop.getNganh() != null ? lop.getNganh().getTenNganh() : null;
            case 3:
                return lop.getKhoa() != null ? lop.getKhoa().getTenKhoa() : null;
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
