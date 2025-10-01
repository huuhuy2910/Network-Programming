package client.ui.model;

import common.dto.Nganh;

public class NganhTableModel extends AbstractEntityTableModel<Nganh> {
    public static final int ACTION_COLUMN_INDEX = 3;
    private static final String[] COLUMNS = {"Mã ngành", "Tên ngành", "Khoa", "Hành động"};

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
        Nganh nganh = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return nganh.getMaNganh();
            case 1:
                return nganh.getTenNganh();
            case 2:
                return nganh.getKhoa() != null ? nganh.getKhoa().getTenKhoa() : null;
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
