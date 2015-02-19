/*
 * CommonMatrix.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.tuke.beast.fascicle.storage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import javax.naming.SizeLimitExceededException;

/**
 * 
 * @author rockaiv
 */
public class CommonMatrixDouble implements Serializable {

    private int sets = 0;
    private static final long serialVersionUID = 412341334L;
    private final int size;
    private final double[][] array;
    private int m_i = 0;
    private final Map<Integer, Integer> indexMap;
    private final Map<Integer, Integer> indexMapi;

    /**
     * Creates a new instance of CommonMatrix
     * @param s
     */
    public CommonMatrixDouble(int s) {
        this.size = s;
        this.array = new double[s][s];
        this.indexMap = new HashMap<Integer, Integer>();
        this.indexMapi = new HashMap<Integer, Integer>();
        
        for (int i = 0 ; i < size; i++){
            for (int j = 0 ; j < size; j++){
                array[i][j] = -1.;
            }
        }
    }

    public Set<Integer> keySet() {
        return indexMap.keySet();
    }

    private void indexToken(Integer t) throws SizeLimitExceededException {
        if (!indexMap.containsKey(t)) {
            if (m_i > size) {
                throw new SizeLimitExceededException();
            }
            int ind = m_i++;
            indexMap.put(t, ind);
            indexMapi.put(ind, t);
        }
    }

    public double get(int i, int j) {
        
        Integer k = indexMap.get(i);
        Integer l = indexMap.get(j);

        if ( k == null || l == null) {
            return -1.;
        }
        return array[k][l];
    }

    public void set(int i, int j, Double v) throws SizeLimitExceededException {
        indexToken(i);
        indexToken(j);
        
        int full = (size*size/2)-(++sets);
        if (full%100000 == 0)
            System.out.println("Full: "+full);
        
        
        array[indexMap.get(i)][indexMap.get(j)] = v;
    }

    public Map<Integer, Double> getRow(int i) {
        Map<Integer, Double> result = new Hashtable<Integer, Double>();

        Integer iI  =indexMap.get(i) ;
        if ( iI == null) {
            return new HashMap<Integer, Double>();
        }

        double[] row = array[iI];

        for (int j = 0; j < row.length; j++) {
            if (row[j] > 0) {
                Integer jI = indexMapi.get(j) ;
                if (jI != null) {
                    result.put(jI, row[j]);
                }
            }
        }

        return result;
    }

    public Map<Integer, Double> getColumn(int j) {
        Map<Integer, Double> result = new Hashtable<Integer, Double>();

        Integer column = indexMap.get(j);

        if (column == null) {
            return result;
        }

        for (int i = 0; i < size; i++) {
            if (array[i][column] > 0) {

                result.put(indexMapi.get(i), array[i][column]);
            }
        }

        return result;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("\nmatrix:\n");
        sb.append(indexMap).append("\n   ");
        for (Integer i : indexMap.keySet()) {
            sb.append(i).append(' ');
        }
        sb.append("\n-------\n");
        for (Integer i : indexMap.keySet()) {
            sb.append(i).append(" |");
            for (Integer j : indexMap.keySet()) {
                sb.append(get(i, j)).append(' ');
            }

            sb.append('\n');
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CommonMatrixDouble)) {
            return false;
        }
        CommonMatrixDouble cm = (CommonMatrixDouble) object;

        if (this.size != cm.size) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (get(i, j) != cm.get(i, j)) {
                    return false;
                }
            }
        }

        return true;

    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash += 97 * hash + this.size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                hash += 97 * hash + this.array[i][j];
            }
        }
        hash += 97 * hash + (this.indexMap != null ? this.indexMap.hashCode() : 0);
        return hash;
    }
}
