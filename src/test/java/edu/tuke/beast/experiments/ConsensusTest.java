/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.experiments;

import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.ContextCortex;
import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.cortex.SimilarityCortex;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.token.Token;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import javax.naming.SizeLimitExceededException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author blur
 */
public class ConsensusTest {

    public static String cortexPath = "/home/blur/workspace/beast_maven/idiot/cortex";
    public static Vector<Vector<String>> inputs = new Vector<Vector<String>>();
    public static Beast beast;
    public static Cortex cortex;
    public static Lexicon<String> l;

    @BeforeClass
    public static void setUpClass() throws Exception {
        beast = new Beast(cortexPath);
        cortex = beast.getCortex();
        cortex.setTreshold(0.7f);
        l = beast.getLexicon();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void testConsensus() throws IOException, ClassNotFoundException, SizeLimitExceededException {
        System.out.println("zacinam test");
        Cortex c = beast.getCortex();
        
        Vector<Integer> phrase1 = new Vector<Integer>();
        Integer i_w1 = l.getIndex(new Token<String>("yellow"));
        Integer i_w2 = l.getIndex(new Token<String>("is"));
        Integer i_w3 = l.getIndex(new Token<String>("a"));
        Integer i_w4 = l.getIndex(new Token<String>("#"));
        
        Vector<Integer> phrase2 = new Vector<Integer>();
        Integer j_w1 = l.getIndex(new Token<String>("green"));
        Integer j_w2 = l.getIndex(new Token<String>("is"));
        Integer j_w3 = l.getIndex(new Token<String>("a"));
        Integer j_w4 = l.getIndex(new Token<String>("#"));
        
        Vector<Integer> phrase3 = new Vector<Integer>();
        Integer k_w1 = l.getIndex(new Token<String>("white"));
        Integer k_w2 = l.getIndex(new Token<String>("is"));
        Integer k_w3 = l.getIndex(new Token<String>("a"));
        Integer k_w4 = l.getIndex(new Token<String>("#"));
        
        phrase1.add(i_w1);
        phrase1.add(i_w2);
        phrase1.add(i_w3);
        phrase1.add(i_w4);
        
        phrase2.add(j_w1);
        phrase2.add(j_w2);
        phrase2.add(j_w3);
        phrase2.add(j_w4);
        
        phrase3.add(k_w1);
        phrase3.add(k_w2);
        phrase3.add(k_w3);
        phrase3.add(k_w4);
        
         System.out.println(phrase1);
         System.out.println(phrase2);
         
         Vector<Map<Integer, Double>> m1 = c.getConsensusSets(phrase1);
         Vector<Map<Integer, Double>> m2 = c.getConsensusSets(phrase2);
         Vector<Map<Integer, Double>> m3 = c.getConsensusSets(phrase3);
         
         Map<Integer, Double> map1 = m1.get(3);
         Map<Integer, Double> map2 = m2.get(3);
         Map<Integer, Double> map3 = m3.get(3);
         
         for (Integer i : map1.keySet())
             for (Integer j : map2.keySet())
                for (Integer k : map3.keySet()){
                 if (i.equals(j) && i.equals(k)){
                     //System.out.println(l.getEntry(i)+ " : "+Math.max(map3.get(k),Math.max(map1.get(i), map2.get(j))));
                     System.out.println(l.getEntry(i)+ " : "+map3.get(k) * map1.get(i) * map2.get(j));
                 }
             }
         
        //Integer i_color = l.getIndex(new Token<String>("colour"));
        /*
        Vector<Map<Integer, Double>> m = c.getConsensusSets(phrase);
        Double w = m.get(3).get(i_color);
        
        Double s1 = c.getRegion().fascicle[0].getSignificance(i_w1, i_color);
        Double s2 = c.getRegion().fascicle[1].getSignificance(i_w2, i_color);
        Double s3 = c.getRegion().fascicle[2].getSignificance(i_w3, i_color);
                
        System.out.println("Color("+i_color+"): " + w);
        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);
        System.out.println("s3: " + s3);
        
        System.out.println(m);
         * 
         */
    }
    
}
