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
public interface Matrix {

    public int get(int i, int j);
    public void set(int i, int j, Integer k) throws SizeLimitExceededException;

    public int getSize();
    public Set<Integer> keySet();
    public Map<Integer, Integer> getRow(int i);
    public Map<Integer, Integer> getColumn(int j);


}
