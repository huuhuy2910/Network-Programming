package client.ui.model;

import common.dto.NamHoc;

public class NamHocTableModel extends AbstractEntityTableModel<NamHoc> {
    public static final int ACTION_COLUMN_INDEX = 2;
    private static final String[] COLUMNS = {"Mã năm học", "Tên năm học", "Hành động"};

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
        NamHoc item = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return item.getMaNamHoc();
            case 1:
                return item.getTenNamHoc();
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
