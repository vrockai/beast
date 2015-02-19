/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.visu.tablemodel;

import java.util.Collections;
import java.util.Vector;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author blur
 */
public class JCommonDefaultModel extends DefaultTableModel {

    private final Class[] classes;

    public void sort(int i, boolean b){
        Vector v = getDataVector();
        Collections.sort(v , new ColumnSorter(i,b));
        fireTableStructureChanged();
    }

    public JCommonDefaultModel(Vector<String> columnNames, Class[] columnsClasses) {
        super(columnNames, 0);
        this.classes = columnsClasses;
    }

    public JCommonDefaultModel(Vector<String> columnNames, Class[] columnsClasses, int i, boolean b) {
        super(columnNames, columnNames.size());
        this.classes = columnsClasses;

        sort(i,b);
    }

    public Class getColumnClass(int columnIndex) {
        return classes[columnIndex];
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
