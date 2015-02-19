/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tuke.beast.cortex;

import edu.tuke.beast.Beast.LearnStrategy;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.token.ExcitedIndex;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;

/**
 *
 * @author rockaiv
 */
public class SynonymCortexTest extends TestCase {
    
    public SynonymCortexTest(String testName) {
        super(testName);
    }

    public void testGetSynonyms() throws Exception {
        
        Integer t = null;
        Cortex c = new Cortex();
        
        Map<Integer,Double> expSs = null;
        Map<Integer,Double> result = null;
        Lexicon l = new Lexicon();
        
        Integer[] w1 = {11,22,33,44,55};
        Integer[] w2 = {11,22,66,44,55};
        
        SynonymCortex scor = new SynonymCortex(c);
        
        learnWindow(c,w1);

        expSs = new HashMap<Integer,Double>();
        result = scor.getSynonymsStrong(33);
        expSs.put(33,1.);
        assertEquals(expSs, result);
        
        learnWindow(c,w2);
        
        expSs = new HashMap<Integer,Double>();
        result = scor.getSynonymsStrong(33);
        expSs.put(33,0.5);
        expSs.put(66,0.5);
        assertEquals(expSs, result);
    }
    
    public void learnWindow(Cortex c, Integer[] w) throws SizeLimitExceededException{
        
        for (int i=0;i<w.length-1; i++)
            for (int j=i+1;j<w.length; j++)
            c.getRegion().getFascicle(j-i-1).store(w[i],w[j]);
    }

}
