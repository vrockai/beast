/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.visu.tablemodel;

import java.io.FileNotFoundException;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
//import rita.wordnet.RiWordnet;

/**
 *
 * @author blur
 */
public class WordnetTabelModel extends AbstractTableModel {

    private static final long serialVersionUID = 1406210936976040395L;
    private final String[] columnNames;
    private final Object[][] data;
    private final Class[] classes = {Integer.class, String.class, Double.class, Double.class, Double.class};

    WordnetTabelModel(Vector<String> columns, Vector data) {


        columnNames = new String[columns.size()];
        int s1 = (data).size();
        int s2 = ((Vector) (data.get(0))).size();
        this.data = new Object[s1][s2];

        for (int i = 0; i < this.columnNames.length; i++) {
            this.columnNames[i] = columns.get(i);
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

    public static void main(String[] args) throws FileNotFoundException {
        
    }
}
