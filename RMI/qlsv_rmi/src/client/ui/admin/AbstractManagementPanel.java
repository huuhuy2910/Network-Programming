package client.ui.admin;

import client.ui.model.AbstractEntityTableModel;
import client.util.DialogUtil;
import client.util.UITheme;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

public abstract class AbstractManagementPanel<T> extends JPanel {

    protected final AbstractEntityTableModel<T> tableModel;
    protected final JTable table;
    private final JTextField searchField;

    protected AbstractManagementPanel(AbstractEntityTableModel<T> tableModel) {
        this.tableModel = tableModel;
        this.table = new JTable(tableModel);
        this.searchField = new JTextField();
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(16, 16));
        setBorder(new EmptyBorder(16, 16, 16, 16));
        setBackground(UITheme.BACKGROUND);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(UITheme.BACKGROUND);
        toolBar.setBorder(new EmptyBorder(0, 0, 0, 0));

    JButton addBtn = styledButton("➕ Thêm", UITheme.PRIMARY);
        addBtn.addActionListener(e -> onAdd());
        toolBar.add(addBtn);

    JButton refreshBtn = styledButton("🔄 Làm mới", UITheme.PRIMARY_DARK);
        refreshBtn.addActionListener(e -> reloadData());
        toolBar.add(refreshBtn);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        searchField.setPreferredSize(new Dimension(220, 30));
        searchField.setFont(UITheme.bodyFont());
        searchField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(UITheme.PRIMARY_DARK, 1, true),
                new EmptyBorder(4, 10, 4, 10)));
        searchPanel.add(searchField);
    JButton searchBtn = styledButton("🔍 Tìm kiếm", UITheme.PRIMARY_DARK);
        searchBtn.addActionListener(e -> onSearch(searchField.getText().trim()));
        searchPanel.add(searchBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(toolBar, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        JComponent filterPanel = buildFilterPanel();
        if (filterPanel != null) {
            JPanel northWrapper = new JPanel(new BorderLayout(0, 8));
            northWrapper.setOpaque(false);
            northWrapper.add(topPanel, BorderLayout.NORTH);
            northWrapper.add(filterPanel, BorderLayout.SOUTH);
            add(northWrapper, BorderLayout.NORTH);
        } else {
            add(topPanel, BorderLayout.NORTH);
        }

        table.setRowHeight(36);
        table.setFillsViewportHeight(true);
        table.setFont(UITheme.bodyFont());
        table.setSelectionBackground(UITheme.PRIMARY.brighter());
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(0xE5E9F2));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new java.awt.Dimension(0, 1));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(UITheme.subHeaderFont());
        table.getTableHeader().setForeground(UITheme.TEXT_PRIMARY);
        table.getTableHeader().setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UITheme.CARD_BACKGROUND);
        add(scrollPane, BorderLayout.CENTER);
    }

    protected boolean enableEditAction() {
        return true;
    }

    protected boolean enableDeleteAction() {
        return true;
    }

    protected JComponent buildFilterPanel() {
        return null;
    }

    private JButton styledButton(String text, java.awt.Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(UITheme.buttonFont());
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        return button;
    }

    protected T getSelectedRow() {
        int viewIndex = table.getSelectedRow();
        if (viewIndex < 0) {
            DialogUtil.showError(this, "Vui lòng chọn một dòng trong bảng.");
            return null;
        }
        int modelIndex = table.convertRowIndexToModel(viewIndex);
        return tableModel.getRow(modelIndex);
    }

    public void reloadData() {
        SwingUtilities.invokeLater(() -> {
            try {
                tableModel.setData(loadData());
            } catch (Exception e) {
                e.printStackTrace();
                DialogUtil.showError(this, "Không thể tải dữ liệu: " + e.getMessage());
            }
        });
    }

    protected abstract java.util.List<T> loadData() throws Exception;

    protected abstract void onAdd();

    protected abstract void onEdit(T selected);

    protected abstract void onDelete(T selected);

    protected abstract void onSearch(String keyword);
}
