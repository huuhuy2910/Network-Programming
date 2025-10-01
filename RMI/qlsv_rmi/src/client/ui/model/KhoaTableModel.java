package client.ui.model;

import common.dto.Khoa;

public class KhoaTableModel extends AbstractEntityTableModel<Khoa> {
    public static final int ACTION_COLUMN_INDEX = 2;
    private static final String[] COLUMNS = {"Mã khoa", "Tên khoa", "Hành động"};

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
        Khoa khoa = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return khoa.getMaKhoa();
            case 1:
                return khoa.getTenKhoa();
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
