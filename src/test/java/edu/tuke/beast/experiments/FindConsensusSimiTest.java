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
public class FindConsensusSimiTest {

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

    private Vector<Integer> getPhrase(String[] q){
        Vector<Integer> v = new Vector<Integer>();

        for(String s:q)
            v.add(l.getIndex(new Token<String>(s)));

        return v;
    }

    private Set<Vector<Integer>> getLexWords(){
             Set<Vector<Integer>> lex_words = new HashSet<Vector<Integer>>();
        for (Integer word : l.getHashMap().keySet()) {

            Vector<Integer> v = new Vector<Integer>();
            v.add(word);

            if (cortex.isNgramConsensus(v) == v.size()) {
                lex_words.add(v);
            }
        }

        return lex_words;
    }

    private Map<Integer,Double> simplifyMap(Map<Vector<Integer>, Double> map){
        Map<Integer,Double> result = new HashMap<Integer,Double>();

        for (Map.Entry<Vector<Integer>, Double> e : map.entrySet()){
            result.put(e.getKey().get(0),e.getValue());
        }

        return result;
    }

    @Test
    public void testFindContext() throws IOException, ClassNotFoundException, SizeLimitExceededException {
        double[] w = { 1.0, 0.5, 0.25, 0.05 };
        String[] question = {"he", "is", "a", "#"};
        String answer = "student";

        Comparator comp = new Comparator() {

            public int compare(Object o1, Object o2) {
                return -1 * ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        };


        Vector<Integer> simi = new Vector<Integer>();
        simi.add(l.getIndex(new Token<String>(answer)));

        Integer answer_i = l.getIndex(new Token<String>(answer));

        SimilarityCortex sc = new SimilarityCortex(cortex);
        SimilarityCortex.Strategy similarityStr = SimilarityCortex.Strategy.INTERSECTION;

        Map<Vector<Integer>, Double> simiwords = sc.getRelativesInContext(simi, similarityStr, null, null, getLexWords(), w[0], w[1], w[2], w[3]);
        Cortex.setStrategy(Cortex.ConsensusStrategy.AVERAGE);
        Vector<Map<Integer, Double>> consensuswords = cortex.getConsensusSets(getPhrase(question));

        Vector<Map<Integer, Double>> sets = new Vector<Map<Integer, Double>>();

        sets.add(simplifyMap(simiwords));
        sets.add(consensuswords.get(question.length-1));

        Cortex.setStrategy(Cortex.ConsensusStrategy.MAX);
        Map<Integer, Double> res = cortex.maxMin(sets);

        TreeSet<Map.Entry<Integer, Double>> sr0 = new TreeSet<Map.Entry<Integer, Double>>(comp);
                sr0.addAll(res.entrySet());

        for(Map.Entry<Integer, Double> e: sr0){
            System.out.println(l.getEntry(e.getKey())+"\t"+ e.getValue());
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



