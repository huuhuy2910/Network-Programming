package client.ui.model;

import common.dto.HocPhan;

public class HocPhanTableModel extends AbstractEntityTableModel<HocPhan> {
    public static final int ACTION_COLUMN_INDEX = 3;
    private static final String[] COLUMNS = {"Mã học phần", "Tên học phần", "Số tín chỉ", "Hành động"};

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
        HocPhan hp = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return hp.getMaHocPhan();
            case 1:
                return hp.getTenHocPhan();
            case 2:
                return hp.getSoTinChi();
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
