/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.experiments;

import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.ContextCortex;
import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.cortex.SimilarityCortex;
import edu.tuke.beast.fascicle.Fascicle;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.token.Token;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
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
public class FascicleCheckCase {

    public static String cortexPath = "/home/vrockai/cortex/viliam";
    public static Vector<Vector<String>> inputs = new Vector<Vector<String>>();
    public static Beast beast;
    public static Cortex cortex;
    public static Lexicon<String> l;

    @BeforeClass
    public static void setUpClass() throws Exception {
        beast = new Beast(cortexPath);
        cortex = beast.getCortex();
        cortex.setTreshold(1.5f);
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
    public void testCheckFas() throws IOException, ClassNotFoundException, SizeLimitExceededException {

        for (int i=0;i<4;i++){
        Fascicle f = cortex.getRegion().getFascicle(i);

        double p1 = 0;
        double p2 = 0;

        int l1 = 0;
        int l2 = 0;
        
        for (Integer t : l.getHashMap().keySet()){
            p1 += f.getProb(t);
            p2 += f.getProb2(t);

            l1 += f.getSourceTokenLearningEvents(t);
            l2 += f.getTargetTokenLearningEvents(t);
        }

            System.out.println(i +" : " +p1 );
            System.out.println(i +" : " +p2 );

            System.out.println(f.getLearningEvents());
            System.out.println(i +" : " +l1 );
            System.out.println(i +" : " +l2 );

        }

    }

    private Map<Integer, Double> countConsensus(int t, ContextCortex.Strategy contextStr) {
        ContextCortex cc = new ContextCortex(cortex);
        Lexicon<String> l = beast.getLexicon();
        Vector<Integer> vt = new Vector<Integer>(1);
        vt.add(t);
        return cc.getRelatives(vt, l.getHashMap().keySet(), contextStr);
    }
}



