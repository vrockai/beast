/*
 * CortexTest.java
 * JUnit based test
 *
 * Created on March 20, 2007, 10:15 PM
 */

package edu.tuke.beast.cortex;

import javax.naming.SizeLimitExceededException;
import junit.framework.*;
import edu.tuke.beast.association.Association;

import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.token.*;
import java.util.*;

/**
 *
 * @author blur
 */
public class CortexTest extends TestCase {

    public CortexTest(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(CortexTest.class);
        
        return suite;
    }
   
    /**
     * Test of presentLearningWindow method, of class edu.tuke.beast.Cortex.
     */
    public void testPresentLearningWindow() throws SizeLimitExceededException {
        
        Cortex cor = null;
        Map<Integer, Integer> res = new TreeMap<Integer, Integer>();
        Map<Integer, Integer> expRes = new TreeMap<Integer, Integer>();
        Map<Integer, Integer> empty = new TreeMap<Integer, Integer>();
        //Lexicon l = new Lexicon<String>();
        
        Integer a = new Integer(1);
        Integer b = new Integer(2);
        Integer c = new Integer(3);
        Integer d = new Integer(4);
        Integer e = new Integer(5);
        Integer N = Lexicon.NULL_INDEX;
        
        
        //  a b c d e
        //  we learn associations 3(a e), 2(b e), 1(c e), 0(d e)
        cor = new Cortex();
        
        int[] w1 = {a,b,c,d,e};
        cor.presentLearningWindowStrict(w1);
        res = cor.getRegion().getFascicle(3).getAssociations(a,true);
        expRes = new TreeMap<Integer, Integer>();
        expRes.put(e,1);
        assertEquals(expRes, res);
        
        res = cor.getRegion().getFascicle(2).getAssociations(b,true);
        expRes = new TreeMap<Integer, Integer>();
        expRes.put(e,1);
        assertEquals(expRes, res);
        
        res = cor.getRegion().getFascicle(1).getAssociations(c,true);
        expRes = new TreeMap<Integer, Integer>();
        expRes.put(e,1);
        assertEquals(expRes, res);
        
        res = cor.getRegion().getFascicle(0).getAssociations(d,true);
        expRes = new TreeMap<Integer, Integer>();
        expRes.put(e,1);
        assertEquals(expRes, res);
        
        res = cor.getRegion().getFascicle(0).getAssociations(e,true);
        expRes = new TreeMap<Integer, Integer>();
        //expRes.put(l.getIndex(e),(short)1);
        assertEquals(expRes, res);
        
        //  N a b c d
        //  ucime (a d) (b d) (c d)
        cor = new Cortex();
        
        int[] w2 = {N,a,b,c,d};
        cor.presentLearningWindowStrict(w2);
        
        res = cor.getRegion().getFascicle(2).getAssociations(a,true);
        expRes = new TreeMap<Integer, Integer>();
        expRes.put(d,1);
        assertEquals(expRes, res);
        
        res = cor.getRegion().getFascicle(1).getAssociations(b,true);
        expRes = new TreeMap<Integer, Integer>();
        expRes.put(d,1);
        assertEquals(expRes, res);
        
        res = cor.getRegion().getFascicle(0).getAssociations(c,true);
        expRes = new TreeMap<Integer, Integer>();
        expRes.put(d,1);
        assertEquals(expRes, res);
        
        //  N N b d N
        cor = new Cortex();
        
        int[] w3 = {N,N,b,d,N};
        cor.presentLearningWindowStrict(w3);
        
        res = cor.getRegion().getFascicle(0).getAssociations(b,true);
        expRes = new TreeMap<Integer, Integer>();
        expRes.put(d,1);
        assertEquals(empty, res);
        
        //  a N N a b
        //  ucime (a b)
        cor = new Cortex();
        
        int[] w4 = {a,N,N,a,b};
        cor.presentLearningWindowStrict(w4);
        
        res = cor.getRegion().getFascicle(0).getAssociations(a,true);
        expRes = new TreeMap<Integer, Integer>();
        expRes.put(d,1);
        assertEquals(expRes, res);
        
        //  N N a b c
        //  ucime (a c) (b c)
        cor = new Cortex();
        
        int[] w5 = {N,N,a,b,c};
        cor.presentLearningWindowStrict(w5);
        
        res = cor.getRegion().getFascicle(1).getAssociations(a,true);
        expRes = new TreeMap<Integer, Integer>();
        expRes.put(c,1);
        assertEquals(expRes, res);
        
        res = cor.getRegion().getFascicle(0).getAssociations(b,true);
        expRes = new TreeMap<Integer, Integer>();
        expRes.put(c,1);
        assertEquals(expRes, res);
        
        Token<String>[] tokens = null;
        Cortex instance = new Cortex();
        
//        instance.presentLearningWindow(tokens);
        
        // TODO review the generated test code and remove the default call to fail.
    }
    
    /**
     * Test of getLearningEvents method, of class edu.tuke.beast.Cortex.
     */
    public void testGetLearningEvents() {
        
        Cortex instance = new Cortex();
        
        int expResult = 0;
        int result = instance.getLearningEvents();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of getSynonyms method, of class edu.tuke.beast.Cortex.
     */
   
    
    /**
     * Test of getSynonymsSlower2 method, of class edu.tuke.beast.Cortex.
     */
    public void learnWindow(Cortex c, Integer[] w) throws SizeLimitExceededException{
        
        
        for (int i=0;i<w.length-1; i++)
            for (int j=i+1;j<w.length; j++)
            c.getRegion().getFascicle(j-i-1).store(w[i],w[j]);
    }
    
      
    /**
     * Test of hasConsensus method, of class edu.tuke.beast.Cortex.
     */
    public void testHasConsensus() throws Exception {
        
        Cortex cor = new Cortex();
        TreeMap<Integer, Integer> res = new TreeMap<Integer, Integer>();
        TreeMap<Integer, Integer> expRes = new TreeMap<Integer, Integer>();
        //Lexicon l = new Lexicon();
        
        
        Integer a = new Integer("a");
        Integer b = new Integer("b");
        Integer c = new Integer("c");
        Integer d = new Integer("d");
        Integer e = new Integer("e");
        Integer N = Lexicon.NULL_INDEX;
        
        int[] w1 = {N,N,N,N,a};
        int[] w2 = {N,N,N,a,b};
        int[] w3 = {N,N,a,b,c};
        int[] w4 = {N,a,b,c,d};
        int[] w5 = {a,b,c,d,e};
        
        cor.presentLearningWindowStrict(w1);
        cor.presentLearningWindowStrict(w2);
        cor.presentLearningWindowStrict(w3);
        cor.presentLearningWindowStrict(w4);
        cor.presentLearningWindowStrict(w5);
        
        Integer[] i1 = {a,b,c,d,e};
        Integer[] i2 = {a,b,c,d,Lexicon.NULL_INDEX};
        Integer[] i3 = {a,b,c,Lexicon.NULL_INDEX,Lexicon.NULL_INDEX};
        Integer[] i4 = {a,b,Lexicon.NULL_INDEX,Lexicon.NULL_INDEX,Lexicon.NULL_INDEX};
        Integer[] i5 = {a,Lexicon.NULL_INDEX,Lexicon.NULL_INDEX,Lexicon.NULL_INDEX,Lexicon.NULL_INDEX};
        
        Integer[] i6 = {Lexicon.NULL_INDEX,b,c,d,e};
        Integer[] i7 = {b,a,d,e,c};
        
       
        assertTrue(cor.isNgramConsensus(toVector(i1)) == 5);
        assertTrue(cor.isNgramConsensus(toVector(i2)) == 4);
        assertTrue(cor.isNgramConsensus(toVector(i3)) == 3);
        assertTrue(cor.isNgramConsensus(toVector(i4)) == 2);
        assertTrue(cor.isNgramConsensus(toVector(i5)) == 1);
        assertTrue(cor.isNgramConsensus(toVector(i6)) == 0);
        assertTrue(cor.isNgramConsensus(toVector(i7)) == 1);
        
        
    }

    private Vector<Integer> toVector(Integer[] arr){
        Vector<Integer> v = new Vector<Integer>();
        for (int i: arr)
            v.add(i);

        return v;
    }

    /**
     * Test of unExciteTokens method, of class edu.tuke.beast.Cortex.
     */
    public void testUnExciteTokens() {
        
        SortedSet<ExcitedIndex<Integer>> tokens = null;
        Cortex instance = new Cortex();
        SortedSet<ExcitedIndex<Integer>> etset = new TreeSet<ExcitedIndex<Integer>>();
        SortedSet<Integer> tset = new TreeSet<Integer>();
        etset.add(new ExcitedIndex<Integer>(1,0.2f));
        etset.add(new ExcitedIndex<Integer>(2,0.3f));
        etset.add(new ExcitedIndex<Integer>(3,0.4f));
        etset.add(new ExcitedIndex<Integer>(4,0.5f));
        etset.add(new ExcitedIndex<Integer>(5,0.6f));
        
        tset.add(new Integer(1));
        tset.add(new Integer(2));
        tset.add(new Integer(3));
        tset.add(new Integer(4));
        tset.add(new Integer(5));
        
        SortedSet<Integer> expResult = null;
        SortedSet<Integer> result = instance.unexcite(etset);
        assertEquals(tset, result);
        
    }
    
    /**
     * Test of getConsensusALL method, of class edu.tuke.beast.Cortex.
     
    public void testGetConsensusALL() throws Exception {
        
        Integer t = null;
        Cortex instance = new Cortex();
        
        Integer[] result = {33};
                
        Integer[] w1 = {11,22,33,44,55};
        Integer[] w2 = {11,22,66,44,55};
        Integer[] q = {11,22,Lexicon.NULL_INDEX,44,55};
        
        learnWindow(instance,w1);
                        
        Integer[] expResult = {null,null,33,null,null};
        result = instance.getConsensusALL(q);

        for (int i=0;i<result.length;i++)
            assertEquals(expResult[i], result[i]);
    }
     * */
    
    /**
     * Test of getConsensusALLList method, of class edu.tuke.beast.Cortex.
     */
    public void testGetConsensusALLList() throws Exception {
        
        Cortex instance = new Cortex();
        
        Map<Integer,Double>[] result = null;
        Lexicon l = new Lexicon();
        
        Integer[] w1 = {11,22,33,44,55};
        Integer[] w2 = {11,22,66,44,55};
        Integer[] q = {11,22,Lexicon.NULL_INDEX,44,55};
        
        learnWindow(instance,w1);
                        
        Map<Integer,Double>[] expResult = null;
        result = instance.getConsensusALLList(q);
        
        assertTrue(Arrays.equals(result,expResult));
        
        for (int i=0;i<result.length;i++)
            assertEquals(expResult[i], result[i]);
    }
      
    /**
     * Test of hasAssociation method, of class edu.tuke.beast.Cortex.
     */
    public void testHasAssociation() throws SizeLimitExceededException {
        Cortex instance = new Cortex();
                
        Lexicon l = new Lexicon();
        
        Integer[] w1 = {11,22,33,44,55};
        Integer[] w2 = {11,22,66,44,55};
        Integer[] q = {11,22,Lexicon.NULL_INDEX,44,55};
        
        learnWindow(instance,w1);
        
        boolean result = instance.hasAssociation(0, 11, 22);
        assertEquals(true, result);
        result = instance.hasAssociation(0, 22, 33);
        assertEquals(true, result);
        result = instance.hasAssociation(0, 44, 55);
        assertEquals(true, result);
        result = instance.hasAssociation(1, 11, 33);
        assertEquals(true, result);
        result = instance.hasAssociation(1, 22, 44);
        assertEquals(true, result);
        result = instance.hasAssociation(1, 33, 55);
        assertEquals(true, result);
        result = instance.hasAssociation(2, 11, 44);
        assertEquals(true, result);
        result = instance.hasAssociation(2, 22, 55);
        assertEquals(true, result);
        result = instance.hasAssociation(3, 11, 55);
        assertEquals(true, result);
        
        result = instance.hasAssociation(0, 11, 33);
        assertEquals(false, result);
        result = instance.hasAssociation(1, 22, 55);
        assertEquals(false, result);
        result = instance.hasAssociation(2, 11, 33);
        assertEquals(false, result);
        result = instance.hasAssociation(3, 33, 55);
        assertEquals(false, result);
        
}
    
    /**
     * Test of getAssociation method, of class edu.tuke.beast.Cortex.
     */
    public void testGetAssociation() {
        
        int i = 0;
        int source = 0;
        int target = 0;
        Cortex instance = new Cortex();
        
        Association<Integer> expResult = null;
        Association<Integer> result = instance.getAssociation(i, source, target);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of getIntegerAssociation method, of class edu.tuke.beast.Cortex.
     */
    public void testGetIntegerAssociation() {
        
        int i = 0;
        int source = 0;
        int target = 0;
        Cortex instance = new Cortex();
        
        Association<Integer> expResult = null;
       // Association<Integer> result = instance.getIntegerAssociation(i, source, target);
        //assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of learnCompleteWindow method, of class edu.tuke.beast.Cortex.
     
    public void testLearnCompleteWindow() throws SizeLimitExceededException {
        
        Token<String>[] window = null;
        Cortex instance = new Cortex();
        
        instance.learnCompleteWindow(window);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     * */
    
    /**
     * Test of getAnswers method, of class edu.tuke.beast.Cortex.
     */
    public void testGetAnswers() throws Exception {
        
        Integer[] question = null;
        Cortex instance = new Cortex();
        
        Map<Integer,Double> expResult = null;
        Map<Integer,Double> result = instance.getAnswers(question);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of getSameALL method, of class edu.tuke.beast.Cortex.
     */
    public void testGetSameALL() throws Exception {
        
        Set<ExcitedIndex<Integer>> tokensetA = null;
        Set<ExcitedIndex<Integer>> tokensetB = null;
        Cortex instance = new Cortex();
        
        SortedSet<ExcitedIndex<Integer>> expResult = null;
        SortedSet<ExcitedIndex<Integer>> result = instance.getSameALL(tokensetA, tokensetB,0);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of initWindow method, of class edu.tuke.beast.Cortex.
     */
    public void testInitWindow() {
        
        Cortex instance = new Cortex();
        
        Vector<Integer> expResult = null;
        Vector<Integer> result = instance.initWindow();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of windowToString method, of class edu.tuke.beast.Cortex.
     */
    public void testWindowToString() {
        
        String[] inputWindow = null;
        Cortex instance = new Cortex();
                
        
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of sliceWindow method, of class edu.tuke.beast.Cortex.
     */
    public void testSliceWindow() {
        Vector<Integer> inputWindow = null;
        Cortex instance = new Cortex();
        
        Vector<Integer> expResult = null;
        Vector<Integer> result = instance.sliceWindow(inputWindow);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of lexiconizeWindow method, of class edu.tuke.beast.Cortex.
     
    public void testLexiconizeWindow() {
        
        Token<String>[] inputWindow = null;
        Cortex instance = new Cortex();
        
        Integer[] expResult = null;
        Integer[] result = instance.lexiconizeWindow(inputWindow);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     * */
    
    /**
     * Test of serialize method, of class edu.tuke.beast.Cortex.
     */
    public void testSerialize() throws Exception {
        String fileName = "";
        Cortex instance = new Cortex();
        
        instance.serialize(fileName);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of deSerialize method, of class edu.tuke.beast.Cortex.
     */
    public void testDeSerialize() throws Exception {
        String fileName = "";
        
        Cortex expResult = null;
       // Cortex result = Cortex.deSerialize(fileName);
        assertEquals(expResult, null);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of unExciteIndex method, of class edu.tuke.beast.Cortex.
     */
    public void testUnExciteIndex() {
        
        SortedSet<ExcitedIndex<Integer>> source = null;
        Cortex instance = new Cortex();
        
        SortedSet<Integer> expResult = null;
        SortedSet<Integer> result = instance.unexcite(source);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of getSimilarity method, of class edu.tuke.beast.Cortex.
     */
    
}
