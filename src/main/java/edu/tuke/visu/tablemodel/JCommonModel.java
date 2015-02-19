/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.visu.tablemodel;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author blur
 */
public class JCommonModel extends AbstractTableModel {

    private static final long serialVersionUID = 2964928340396380868L;
    private final String[] columnNames;
    private final Object[][] data;
    private Class[] classes = {Integer.class, String.class, Double.class};//, Double.class, Double.class};

    public JCommonModel(Vector data, Vector<String> columnNames, Class[] classes) {
        this(data, columnNames);
        this.classes = classes;
    }

    JCommonModel(Vector data, Vector<String> columnNames) {
        this.columnNames = new String[columnNames.size()];
        int s1 = (data).size();
        int s2 = ((Vector) (data.get(0))).size();
        this.data = new Object[s1][s2];

        for (int i = 0; i < this.columnNames.length; i++) {
            this.columnNames[i] = columnNames.get(i);
        }

        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data[0].length; j++) {
                this.data[i][j] = ((Vector) (data.get(i))).get(j);
            }
        }
    }


    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    @Override
    public Class getColumnClass(int c) {
        return classes[c];
    }

    public static class DoubleRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 326492131234480868L;

        public DoubleRenderer() {
            super();
        }

        @Override
        public void setValue(Object value) {

            setText((value == null) ? "" : value.toString());
        }
    }
}
