/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tuke.beast.fascicle.storage;

import java.util.Map;
import java.util.Set;
import javax.naming.SizeLimitExceededException;

/**
 *
 * @author vrockai
 */
public abstract class DBMatrix implements Matrix {

    public int get(int i, int j) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void set(int i, int j, Integer k) throws SizeLimitExceededException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<Integer, Integer> getRow(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<Integer, Integer> getColumn(int j) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
