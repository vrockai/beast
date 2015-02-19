/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.experiments;

import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.ClusterCortex;
import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.cortex.SimilarityCortex;
import edu.tuke.beast.fascicle.Fascicle;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.util.Utils;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;

/**
 *
 * @author blur
 */
public class NameClusterTest extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();

    public void testNaryRelatednessCluster() throws IOException, ClassNotFoundException, SizeLimitExceededException {

        String cortexString = System.getProperty("cortex");
        System.out.println("Cortex: " + cortexString);

        Beast beast = new Beast(cortexString);
        String[] input = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
        //String[] input = {"january", "february", "november", "december"};
        //String[] input = {"one","two","three","four","five"};
        Double[] treshes = {1.0, 2.0, 4.0, 8.0, 16.0, 32.0};
        double tresh = treshes[3];

        Cortex cortex = beast.getCortex();
        Lexicon<String> lexicon = beast.getLexicon();

        Set<Integer> fullset = new HashSet<Integer>();

        //Must be in lexicon
        for (String day : input) {
            assertTrue(lexicon.getIndex(day) != Lexicon.NULL_INDEX);
            fullset.add(lexicon.getIndex(day));
        }

        Integer iIs = lexicon.getIndex("is");
        Integer iA = lexicon.getIndex("a");
        Integer iMonth = lexicon.getIndex("month");

        Fascicle f0 = cortex.getRegion().getFascicle(0);
        Fascicle f1 = cortex.getRegion().getFascicle(1);
        Fascicle f2 = cortex.getRegion().getFascicle(2);
        Fascicle f3 = cortex.getRegion().getFascicle(3);

        Set<Set<Integer>> qsets = Utils.generateVariations(fullset);

        Map<Integer, Double> mapA = f0.getTargetTokens(iA, true);
        Map<Integer, Double> mapIs = f1.getTargetTokens(iIs, true);

    
        /*
              Map<Integer, Double> mapTf = new HashMap<Integer, Double>();
        for (Integer j : lexicon.getLexiconSet()) {
            mapTf.put(j, 0.);
        }
*/
        for (Set<Integer> qset : qsets) {

            Set<Integer> rSet = new HashSet<Integer>();
            //rSet.addAll(lexicon.getLexiconSet());
            rSet.retainAll(mapIs.keySet());

            for (Integer i : qset) {
                Map<Integer, Double> mapT = f2.getTargetTokens(i, true);
                rSet.retainAll(mapT.keySet());
                if (rSet.contains(iMonth)) {
                    System.out.println("TREFA");
                }
                /*
                Map<Integer, Double> mapT0 = f0.getTargetTokens(i, true);
                Map<Integer, Double> mapT1 = f1.getTargetTokens(i, true);
                Map<Integer, Double> mapT2 = f2.getTargetTokens(i, true);
                Map<Integer, Double> mapT3 = f3.getTargetTokens(i, true);
                
                for (Integer j : lexicon.getLexiconSet()) {
                if (mapT0.containsKey(j)) {
                if (mapT0.get(j) > mapTf.get(j)) {
                mapTf.put(j, mapT0.get(j));
                }
                }
                if (mapT1.containsKey(j)) {
                if (mapT1.get(j) > mapTf.get(j)) {
                mapTf.put(j, mapT1.get(j));
                }
                }
                if (mapT2.containsKey(j)) {
                if (mapT2.get(j) > mapTf.get(j)) {
                mapTf.put(j, mapT2.get(j));
                }
                }
                if (mapT3.containsKey(j)) {
                if (mapT3.get(j) > mapTf.get(j)) {
                mapTf.put(j, mapT3.get(j));
                }
                }
                }
                 * 
                 */
            }

            for (Integer i : rSet) {
                System.out.println(lexicon.getEntry(i));
            }
        }
    }
}
