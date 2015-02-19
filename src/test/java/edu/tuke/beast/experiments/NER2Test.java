/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.experiments;

import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.ClusterCortex;
import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.cortex.SimilarityCortex;
import edu.tuke.beast.lexicon.Lexicon;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;

/**
 *
 * @author vrockai
 */
public class NER2Test extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();

    public void testNameCluster() throws IOException, ClassNotFoundException, SizeLimitExceededException {

        String cortexString = "/home/blur/full_cortex/cortex";
        Beast beast = new Beast(cortexString);

        String[] input = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};////
        //String[] input = {"monday", "friday", "saturday", "sunday"};
        //String[] input = {"red", "green", "blue", "yellow","pink"};
       
        double tresh = 1.0;

        Cortex cortex = beast.getCortex();
        cortex.setTreshold(tresh);
        Lexicon<String> lexicon = beast.getLexicon();

        Set<Integer> qset = new HashSet<Integer>();

        //Must be in lexicon
        for (String day : input) {
            assertTrue(lexicon.getIndex(day) != Lexicon.NULL_INDEX);
            qset.add(lexicon.getIndex(day));
        }
        
        SimilarityCortex sc = new SimilarityCortex(cortex);
        sc.setStrategy(SimilarityCortex.Strategy.WEIGHTED);
        
        Map<Integer,Double> mp = sc.getRelativesMulti(qset, lexicon.getLexiconSet(), 1., 1., 1., 1.);
       Comparator<Map.Entry<Integer, Double>> comp = new CompByValue();
        
        SortedSet<Map.Entry<Integer, Double>> sr0 = new TreeSet<Map.Entry<Integer, Double>>(comp);
        sr0.addAll(mp.entrySet());
        int c=30;
        for( Map.Entry<Integer, Double> me : sr0){
            if (--c < 0)
                break;
            System.out.println(lexicon.getEntry(me.getKey()) + ": "+me.getValue());
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
