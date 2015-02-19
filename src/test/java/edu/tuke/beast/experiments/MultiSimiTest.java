/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.experiments;

import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.cortex.SimilarityCortex;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.util.Utils;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;

/**
 *
 * @author vrockai
 */
public class MultiSimiTest extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();

    public void testMultiSimiTest() throws IOException, ClassNotFoundException, SizeLimitExceededException {
        Beast beast = new Beast("/home/blur/full_cortex/cortex");

        String[] input = {"january", "february", "march"};
        double tresh = 2.;

        Cortex cortex = beast.getCortex();
        Lexicon<String> lexicon = beast.getLexicon();
        
        Set<Integer> complete = new HashSet<Integer>();

        //Must be in lexicon
        for (String day : input) {
            assertTrue(lexicon.getIndex(day) != Lexicon.NULL_INDEX);
            complete.add(lexicon.getIndex(day));
        }
        
        cortex.setTreshold(tresh);
        SimilarityCortex sc = new SimilarityCortex(cortex);
        Set<Integer> lset = lexicon.getLexiconSet();
        Set<Integer> phrase = new HashSet<Integer>();
        
        for (Integer t : complete) {
            phrase.add(t);
        }
        
        sc.setStrategy(SimilarityCortex.Strategy.INTERSECTION);
        Map<Integer, Double> result = sc.getRelativesMulti(phrase, lset, 1., 1., 1., 1.);
        
        Comparator<Map.Entry<Integer, Double>> comp = new CompByValue();
        
        SortedSet<Map.Entry<Integer, Double>> sr0 = new TreeSet<Map.Entry<Integer, Double>>(comp);
        sr0.addAll(result.entrySet());
                
        int c = 30;
        for (Map.Entry<Integer, Double> me : sr0) {
            System.out.println(lexicon.getEntry(me.getKey()) + ": " + me.getValue());
            c--;
        
            if (c < 0) {
                break;
            }        
        }        
        
    }
    
    class CompByValue implements Comparator<Map.Entry<Integer,Double>> {
    public int compare(Map.Entry<Integer,Double> e1, Map.Entry<Integer,Double> e2) {
        if (e1.getValue() < e2.getValue()){
            return 1;
        } else if (e1.getValue() == e2.getValue()) {
            return 0;
        } else {
            return -1;
        }
    }
}
}
