package ui;

import model.Charity;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class Table extends AbstractTableModel {

    public Table(String[] columnNames, Charity charity) {
        JTable table = new JTable();

        JScrollPane sp = new JScrollPane(table);
        table.setFillsViewportHeight(true);

    }

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        if (col < 2) {
            return false;
        } else {
            return true;
        }
    }

    public void setValueAt(Object value, int row, int col) {

    }
}
