/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tuke.beast.cortex;

import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.token.ExcitedIndex;
import java.util.SortedSet;
import java.util.Vector;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;

/**
 *
 * @author rockaiv
 */
public class SimilarityCortexTest extends TestCase {
    
    public SimilarityCortexTest(String testName) {
           super(testName);
    }

    /**
     * Test of getSimilarity method, of class SimilarityCortex.
     */
    public void testGetWordSimilarity() throws SizeLimitExceededException {
        Integer t = null;
        Cortex c = new Cortex();
        
        SortedSet<ExcitedIndex<Integer>> expSs = null;
        SortedSet<ExcitedIndex<Integer>> result = null;
        
        Lexicon l = new Lexicon();
        
        Integer[] w1 = {11,22,33,44,55};
        Integer[] w2 = {11,22,66,44,55};
        Integer[] w3 = {77,88,66,99,12};
        SimilarityCortex scor = new SimilarityCortex(c);
        
        learnWindow(c,w1);
        /*
        assertEquals(scor.getSimilarity(33, 66, 0),0.);
        assertEquals(scor.getSimilarity(33, 66, 1),0.);
        assertEquals(scor.getSimilarity(33, 66, 2),0.);
        assertEquals(scor.getSimilarity(33, 66, 3),0.);
        
        learnWindow(c,w2);
        assertEquals(scor.getSimilarity(33, 66, 0),1.);
        assertEquals(scor.getSimilarity(33, 66, 1),1.);
        assertEquals(scor.getSimilarity(33, 66, 2),0.);
        assertEquals(scor.getSimilarity(33, 66, 3),0.);

        learnWindow(c,w3);
        assertEquals(scor.getSimilarity(33, 66, 0),0.5);
        assertEquals(scor.getSimilarity(33, 66, 1),0.5);
        assertEquals(scor.getSimilarity(33, 66, 2),0.);
        assertEquals(scor.getSimilarity(33, 66, 3),0.);
        */
    }

    public void testGetPhraseSimilarity() throws SizeLimitExceededException {
        Integer t = null;
        Cortex c = new Cortex();

        SortedSet<ExcitedIndex<Integer>> expSs = null;
        SortedSet<ExcitedIndex<Integer>> result = null;

        Lexicon l = new Lexicon();

        Integer[] w1 = {11,22,33,44,55};
        Integer[] w2 = {11,22,66,44,55};
        Integer[] w3 = {77,88,66,99,12};
        SimilarityCortex scor = new SimilarityCortex(c);

        Vector<Integer> v1 = new Vector<Integer>(); v1.add(33);
        Vector<Integer> v2 = new Vector<Integer>(); v2.add(66);

        learnWindow(c,w1);
/*
        assertEquals(scor.getSimilarityInter(v1, v2, 0),0.);
        assertEquals(scor.getSimilarityInter(v1, v2, 1),0.);
        assertEquals(scor.getSimilarityInter(v1, v2, 2),0.);
        assertEquals(scor.getSimilarityInter(v1, v2, 3),0.);

        learnWindow(c,w2);
        assertEquals(scor.getSimilarityInter(v1, v2, 0),1.);
        assertEquals(scor.getSimilarityInter(v1, v2, 0),1.);
        assertEquals(scor.getSimilarityInter(v1, v2, 1),1.);
        assertEquals(scor.getSimilarityInter(v1, v2, 2),0.);
        assertEquals(scor.getSimilarityInter(v1, v2, 3),0.);

        learnWindow(c,w3);
        assertEquals(scor.getSimilarityInter(v1, v2, 0),0.5);
        assertEquals(scor.getSimilarityInter(v1, v2, 1),0.5);
        assertEquals(scor.getSimilarityInter(v1, v2, 2),0.);
        assertEquals(scor.getSimilarityInter(v1, v2, 3),0.);
*/
    }

    public void learnWindow(Cortex c, Integer[] w) throws SizeLimitExceededException{
        
        for (int i=0;i<w.length-1; i++)
            for (int j=i+1;j<w.length; j++)
            c.getRegion().getFascicle(j-i-1).store(w[i],w[j]);
    }
}
