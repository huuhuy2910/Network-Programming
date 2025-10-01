package client.ui.components;

import client.util.UITheme;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

public class TableActionCell<T> extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    public interface TableActionHandler<T> {
        void onView(T item);
        void onEdit(T item);
        void onDelete(T item);
    }

    public interface RowFetcher<T> {
        T getRow(int rowIndex);
    }

    private final JPanel rendererPanel;
    private final JPanel editorPanel;
    private final JButton detailButton;
    private final JButton editButton;
    private final JButton deleteButton;
    private final boolean showView;
    private final boolean showEdit;
    private final boolean showDelete;
    private final TableActionHandler<T> handler;
    private final RowFetcher<T> fetcher;
    private T currentItem;
    private JTable currentTable;
    private int currentViewRow = -1;

    public TableActionCell(TableActionHandler<T> handler, RowFetcher<T> fetcher) {
        this(handler, fetcher, true, true, true);
    }

    public TableActionCell(TableActionHandler<T> handler, RowFetcher<T> fetcher,
                           boolean showView, boolean showEdit, boolean showDelete) {
        this.handler = handler;
        this.fetcher = fetcher;
        this.showView = showView;
        this.showEdit = showEdit;
        this.showDelete = showDelete;
        this.rendererPanel = createPanel();
        this.editorPanel = createPanel();
        this.detailButton = showView ? createButton("🔎 Chi tiết", UITheme.PRIMARY_DARK) : null;
        this.editButton = showEdit ? createButton("✏️ Sửa", UITheme.ACCENT) : null;
        this.deleteButton = showDelete ? createButton("🗑️ Xóa", UITheme.DANGER) : null;
        addButtons(rendererPanel, false);
        addButtons(editorPanel, true);
    }

    private JPanel createPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        panel.setOpaque(true);
        panel.setBorder(new EmptyBorder(0, 8, 0, 8));
        return panel;
    }

    private JButton createButton(String text, java.awt.Color background) {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(java.awt.Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(UITheme.smallFont().deriveFont(Font.BOLD));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setBorder(new EmptyBorder(6, 12, 6, 12));
        return button;
    }

    private void addButtons(JPanel panel, boolean withActions) {
        panel.removeAll();
        if (showView && detailButton != null) {
            JButton detail = new JButton(detailButton.getText());
            copyButtonStyle(detailButton, detail);
            if (withActions) {
                detail.addActionListener(e -> {
                    T item = resolveCurrentItem();
                    if (handler != null && item != null) {
                        handler.onView(item);
                    }
                    fireEditingStopped();
                });
            }
            panel.add(detail);
        }

        if (showEdit && editButton != null) {
            JButton edit = new JButton(editButton.getText());
            copyButtonStyle(editButton, edit);
            if (withActions) {
                edit.addActionListener(e -> {
                    T item = resolveCurrentItem();
                    if (handler != null && item != null) {
                        handler.onEdit(item);
                    }
                    fireEditingStopped();
                });
            }
            panel.add(edit);
        }

        if (showDelete && deleteButton != null) {
            JButton delete = new JButton(deleteButton.getText());
            copyButtonStyle(deleteButton, delete);
            if (withActions) {
                delete.addActionListener(e -> {
                    T item = resolveCurrentItem();
                    if (handler != null && item != null) {
                        handler.onDelete(item);
                    }
                    fireEditingStopped();
                });
            }
            panel.add(delete);
        }
    }

    private void copyButtonStyle(JButton source, JButton target) {
        target.setBackground(source.getBackground());
        target.setForeground(source.getForeground());
        target.setFont(source.getFont());
        target.setFocusPainted(false);
        target.setBorder(new EmptyBorder(6, 12, 6, 12));
    }

    private void updatePanel(JPanel panel, JTable table, boolean isSelected) {
        panel.setBackground(isSelected ? UITheme.PRIMARY.brighter() : UITheme.CARD_BACKGROUND);
        panel.setForeground(UITheme.TEXT_PRIMARY);
    }

    @Override
    public Object getCellEditorValue() {
        return currentItem;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        currentItem = fetcher != null ? fetcher.getRow(row) : null;
        updatePanel(rendererPanel, table, isSelected);
        return rendererPanel;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentItem = fetcher != null ? fetcher.getRow(row) : null;
        currentTable = table;
        currentViewRow = row;
        updatePanel(editorPanel, table, true);
        return editorPanel;
    }

    private T resolveCurrentItem() {
        if (fetcher != null && currentTable != null && currentViewRow >= 0) {
            return fetcher.getRow(currentViewRow);
        }
        return currentItem;
    }
}
