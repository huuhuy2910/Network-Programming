package client.ui.model;

import common.dto.TaiKhoan;

import java.text.SimpleDateFormat;

public class TaiKhoanTableModel extends AbstractEntityTableModel<TaiKhoan> {
    public static final int ACTION_COLUMN_INDEX = 6;
    private static final String[] COLUMNS = {"Username", "Tên hiển thị", "Role", "SV ID", "Ngày tạo", "Trạng thái", "Hành động"};
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

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
        TaiKhoan tk = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return tk.getUsername();
            case 1:
                return tk.getDisplayName();
            case 2:
                return tk.getRole();
            case 3:
                return tk.getSvId();
            case 4:
                return tk.getCreatedAt() != null ? DATE_FORMAT.format(tk.getCreatedAt()) : "";
            case 5:
                return tk.getStatus() != null ? tk.getStatus() : "-";
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
