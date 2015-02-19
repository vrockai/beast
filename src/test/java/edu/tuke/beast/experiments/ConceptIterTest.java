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
 * @author vrockai
 */
public class ConceptIterTest extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();

    public void testNaryRelatednessCluster() throws IOException, ClassNotFoundException, SizeLimitExceededException {

        String cortexString = System.getProperty("cortex");
        System.out.println("Cortex: " + cortexString);
        //cortexString = "/home/blur/full_cortex/cortex";
        Beast beast = new Beast(cortexString);

        String[] input = {"január", "február", "okt", "marec"};
        //String[] input = {"january", "february", "may", "march"};

        double tresh = 1.5;

        Cortex cortex = beast.getCortex();
        Lexicon<String> lexicon = beast.getLexicon();

        //Must be in lexicon

        Set<Integer> qset = new HashSet<Integer>();
        for (String day : input) {
            System.out.println(day);
            assertTrue(lexicon.getIndex(day) != Lexicon.NULL_INDEX);
            qset.add(lexicon.getIndex(day));
        }

        
        

        cortex.setTreshold(tresh);
        ClusterCortex cc = new ClusterCortex(cortex);

        cc.setStrategy(SimilarityCortex.Strategy.INTERSECTION);

        double gamma = 1.0;
        cc.setGamma(gamma);
        
        boolean first = true;
        
        do {
            Set<Set<Integer>> aset = Utils.generateVariations(qset);
            Map<String, Integer> rrr = getIterMap(aset, lexicon, cc);
            Set<Integer> newnew = new HashSet<Integer>();
            for (String s : rrr.keySet()){
                Integer i = rrr.get(s);
                
                if (i > aset.size()/2){
                    newnew.add(lexicon.getIndex(s));
                }
            }
            
            Set<Integer> newnews = new HashSet<Integer>();
            newnews.addAll(newnew);
            newnews.removeAll(qset);
            
            qset.addAll(newnew);
            
            if(first){
            System.out.println("qset: "+qset);
            System.out.println("newnews size: "+newnews.size());
            first = false;
            }
            
            if(newnews.isEmpty()){
            System.out.println("qset: "+qset);
            System.out.println("newnews size: "+newnews.size());
            
            break;
            }
        }while(true);
        

    }
   
   private Map<String, Integer> getIterMap(Set<Set<Integer>> aset, Lexicon<String> lexicon, ClusterCortex cc){
        Map<String, Integer> rmap = new HashMap<String, Integer>();
        Set<Integer> lset = lexicon.getLexiconSet();
        double gamma = 1.0;
        for (Set<Integer> iset : aset) {
            //System.out.println("gamma:" + gamma);

            Set<Integer> result = cc.getCluster(iset, lset, false);

            for (Integer i : result) {
                String val = lexicon.getEntry(i).getValue();
              //  System.out.print(val + ", ");
                
                if(!rmap.containsKey(val)){
                    rmap.put(val, 1);
                } else {
                    rmap.put(val, rmap.get(val)+1);
                }
                
            }

            //System.out.println("size: " + result.size());
            //
            //S/ystem.out.println(rmap);
            
        }
        return rmap;
    }
}
