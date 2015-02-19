package edu.tuke.visu.tablemodel;


import java.io.Serializable;
import java.util.Comparator;
import java.util.Vector;

public class ColumnSorter implements Comparator, Serializable {
        final int colIndex;
        final boolean ascending;

        private static final long serialVersionUID = 189698384798265L;

        public ColumnSorter(int colIndex, boolean ascending) {
            this.colIndex = colIndex;
            this.ascending = ascending;
        }
    @Override
        public int compare(Object a, Object b) {
            Vector v1 = (Vector)a;
            Vector v2 = (Vector)b;
            Object o1 = v1.get(colIndex);
            Object o2 = v2.get(colIndex);

            // Treat empty strains like nulls
            if (o1 instanceof String && ((String)o1).length() == 0) {
                o1 = null;
            }
            if (o2 instanceof String && ((String)o2).length() == 0) {
                o2 = null;
            }

            // Sort nulls so they appear last, regardless
            // of sort order
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) {
                return 1;
            } else if (o2 == null) {
                return -1;
            } else if (o1 instanceof Comparable) {
                return ascending ? ((Comparable) o1).compareTo(o2) : ((Comparable) o2).compareTo(o1);
            } else {
                return ascending ? o1.toString().compareTo(o2.toString()) : o2.toString().compareTo(o1.toString());
            }
        }
    }
