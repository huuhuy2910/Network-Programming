package client.ui.model;

import common.dto.KhoaHoc;

public class KhoaHocTableModel extends AbstractEntityTableModel<KhoaHoc> {
    public static final int ACTION_COLUMN_INDEX = 2;
    private static final String[] COLUMNS = {"Mã khóa học", "Tên khóa học", "Hành động"};

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
        KhoaHoc item = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return item.getMaKhoaHoc();
            case 1:
                return item.getTenKhoaHoc();
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
