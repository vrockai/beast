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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;

/**
 *
 * @author vrockai
 */
    public class DisambTest extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();

    public void testNaryRelatednessCluster() throws IOException, ClassNotFoundException, SizeLimitExceededException {

        String cortexString = System.getProperty("cortex");
        System.out.println("Cortex: " + cortexString);

        
        
                 String[] ct1 = {"may", "could", "might"};
        String[] cl1 = "# # # you".split(" ");
        String[] cr1 = "leave the table when".split(" ");

        String[] ct2 = {"may", "january", "february"};
        String[] cl2 = "the week in which".split(" ");
        String[] cr2 = "begins in one year".split(" ");
        /*
        
        String[] ct1 = {"march", "walk", "move"};
        String[] cl1 = "to a long distance".split(" ");
        String[] cr1 = "carrying full kit as".split(" ");

        String[] ct2 = {"march", "january", "february"};
        String[] cl2 = "the week in which".split(" ");
        String[] cr2 = "begins in one year".split(" ");
*/
        Beast beast = new Beast(cortexString);
        Cortex cortex = beast.getCortex();
        Lexicon<String> lexicon = beast.getLexicon();

        checkLexicon(ct1, lexicon);
//        checkLexicon(cl1, lexicon);
  //       checkLexicon(cr2, lexicon);

        checkLexicon(ct2, lexicon);
//        checkLexicon(cl2, lexicon);
 //       checkLexicon(cr2, lexicon);

        double tresh = 1.;

        List<Integer> lcl1 = getList(cl1, lexicon);
        List<Integer> lcr1 = getList(cr1, lexicon);
        List<Integer> lcl2 = getList(cl2, lexicon);
        List<Integer> lcr2 = getList(cr2, lexicon);

        double sc1 = 0.;
        double sc2 = 0.;
               
        for (Integer i : getList(ct1, lexicon)) {
            sc1 += countAsoc(i, lcl1, cortex, false);
            sc1 += countAsoc(i, lcr1, cortex, true);
        }
        for (Integer i : getList(ct2, lexicon)) {
            sc2 += countAsoc(i, lcl1, cortex, false);
            sc2 += countAsoc(i, lcr1, cortex, true);
        }
        System.out.println("sc1: " + sc1);
        System.out.println("sc2: " + sc2);
        System.out.println(sc1>sc2);

        sc1 = 0.;
        sc2 = 0.;
        
        for (Integer i : getList(ct1, lexicon)) {
            sc1 += countAsoc(i, lcl2, cortex, false);
            sc1 += countAsoc(i, lcr2, cortex, true);
        }
        for (Integer i : getList(ct2, lexicon)) {
            sc2 += countAsoc(i, lcl2, cortex, false);
            sc2 += countAsoc(i, lcr2, cortex, true);
        }
        System.out.println("sc1: " + sc1);
        System.out.println("sc2: " + sc2);
        System.out.println(sc2>sc1  );
    }

    public List<Integer> getList(String s[], Lexicon<String> l) {
        List<Integer> list = new ArrayList<Integer>();
        for (String term : s) {
            list.add(l.getIndex(term));
        }
        return list;
    }

    
    public double countAsoc(Integer term, List<Integer> context, Cortex c, boolean dir) {
        return countAsoc2(term, context, c, dir);
    }
    public double countAsoc1(Integer term, List<Integer> context, Cortex c, boolean dir) {
        
        double result = 0.;
        
        for (int i = 0; i < context.size(); i++) {            
            if (dir) {
                result += c.getRegion().getFascicle(i).isAssociation(term, context.get(i)) ? c.getRegion().getFascicle(i).getWeigth(term, context.get(i)) : 0;
            } else {
                result += c.getRegion().getFascicle(3-i).isAssociation(context.get(i), term) ? c.getRegion().getFascicle(i).getWeigth(context.get(i), term) : 0;
            }
        }

        return result/context.size();
    }

    public double countAsoc2(Integer term, List<Integer> context, Cortex c, boolean dir) {
        
        double result = 0.;
        
        for (int i = 0; i < context.size(); i++) {            
            if (dir) {
                result += c.getRegion().getFascicle(i).isAssociation(term, context.get(i)) ? 1 : 0;
            } else {
                result += c.getRegion().getFascicle(3-i).isAssociation(context.get(i), term) ? 1 : 0;
            }
        }

        return result;
    }
    
    public void checkLexicon(String[] s, Lexicon<String> l) {
        for (String term : s) {
            assertTrue(l.getIndex(term) != Lexicon.NULL_INDEX);
        }
    }
}
