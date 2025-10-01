package client.ui.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntityTableModel<T> extends AbstractTableModel {
    protected final List<T> rows = new ArrayList<>();

    public void setData(List<T> data) {
        rows.clear();
        if (data != null) {
            rows.addAll(data);
        }
        fireTableDataChanged();
    }

    public T getRow(int index) {
        if (index >= 0 && index < rows.size()) {
            return rows.get(index);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }
}
