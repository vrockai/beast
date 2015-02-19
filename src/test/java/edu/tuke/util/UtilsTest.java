/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.util;


import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author vrockai
 */
public class UtilsTest extends TestCase {
 
    @Test
    public void testSortedMap(){
               
        Map<Integer,Double> map = new HashMap<Integer,Double>();
        map.put(1,0.5);
        map.put(2,0.25);
        map.put(3,1.);
        map.put(4,0.125);
        map.put(5,1.2);
        
        SortedSet<Map.Entry<Integer,Double>> sortedSet = new TreeSet<Map.Entry<Integer,Double>>(new Utils.ComparatorSetValue());
        sortedSet.addAll(map.entrySet());
        
        Iterator<Map.Entry<Integer,Double>> ei = sortedSet.iterator();
                
        Entry<Integer,Double> e = ei.next();
        assertEquals(Integer.valueOf(5), Integer.valueOf(e.getKey()));        
        e = ei.next();
        assertEquals(Integer.valueOf(3), Integer.valueOf(e.getKey()));
        e = ei.next();
        assertEquals(Integer.valueOf(1), Integer.valueOf(e.getKey()));
        e = ei.next();
        assertEquals(Integer.valueOf(2), Integer.valueOf(e.getKey()));
        e = ei.next();
        assertEquals(Integer.valueOf(4), Integer.valueOf(e.getKey()));
         
    }
    
    
    
}
