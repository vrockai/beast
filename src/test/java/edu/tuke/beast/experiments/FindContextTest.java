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
public class FindContextTest {

    public static String cortexPath = "/home/blur/pride/cortex";
    public static Vector<Vector<String>> inputs = new Vector<Vector<String>>();

      public static  Beast beast;
     public static    Cortex cortex;
     public static    Lexicon<String> l;
    

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
    public void testFindContext() throws IOException, ClassNotFoundException, SizeLimitExceededException {

        String[] question = {"red", "green", "blue", "yellow", "pink"};
        String answer = "color";

        Integer answer_i = l.getIndex(new Token<String>(answer));

        //ContextCortex.Strategy context_str = ContextCortex.Strategy.SIG;
        //Cortex.ConsensusStrategy cortex_str = Cortex.ConsensusStrategy.MIN;

        for (ContextCortex.Strategy context_str : ContextCortex.Strategy.values()) {
            for (Cortex.ConsensusStrategy cortex_str : Cortex.ConsensusStrategy.values()) {

                Vector<Map<Integer, Double>> sets = new Vector<Map<Integer, Double>>();
                for (String word : question) {
                    word = word.trim();
                    sets.add(countConsensus(l.getIndex(new Token<String>(word)), context_str));
                }

                Cortex.setStrategy(cortex_str);
                Map<Integer, Double> result = cortex.maxMin(sets);

                Double answer_s = result.get(answer_i);
                int pos1 = 0;
                int pos2 = 0;
                if (answer_s == null){
                    pos1 = -1;
                    pos2 = -1;
                }
                else
                for (Double d : result.values()) {
                    pos1 += d > answer_s ? 1 : 0;
                    pos2 += d < answer_s ? 1 : 0;
                }

                System.out.println(pos1 + " | " + context_str + "," + cortex_str + " | " + pos2);
            }
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



