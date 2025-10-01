package client.ui.model;

import common.dto.HocKy;

public class HocKyTableModel extends AbstractEntityTableModel<HocKy> {
    public static final int ACTION_COLUMN_INDEX = 2;
    private static final String[] COLUMNS = {"Mã học kỳ", "Tên học kỳ", "Hành động"};

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
        HocKy item = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return item.getMaHocKy();
            case 1:
                return item.getTenHocKy();
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
