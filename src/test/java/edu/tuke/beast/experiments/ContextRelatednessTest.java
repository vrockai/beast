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
public class ContextRelatednessTest {

    public static String cortexPath = "/home/blur/1gbcortex/112/vilko";
    public static Vector<Vector<String>> inputs = new Vector<Vector<String>>();

    public ContextRelatednessTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Vector<String> v;

        v = new Vector<String>();
        v.add("star");
        v.add("fame");
        v.add("sky");
        inputs.add(v);

        v = new Vector<String>();
        v.add("music");
        v.add("music");
        v.add("art");
        inputs.add(v);

        v = new Vector<String>();
        v.add("apple");
        v.add("food");
        v.add("plant");
        inputs.add(v);

        v = new Vector<String>();
        v.add("blue");
        v.add("color");
        v.add("sad");
        inputs.add(v);
/*
        v = new Vector<String>();
        v.add("head");
        v.add("body");
        v.add("kingdom");

        inputs.add(v);
*/
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
    public void testContextRelatedness() throws IOException, ClassNotFoundException, SizeLimitExceededException {

        Beast beast = new Beast(cortexPath);
        Cortex cortex = beast.getCortex();
        Lexicon<String> l = beast.getLexicon();

        Comparator comp = new Comparator() {

            public int compare(Object o1, Object o2) {
                return -1 * ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        };

        Set<Vector<Integer>> lex_words = new HashSet<Vector<Integer>>();
        for (Integer word : l.getHashMap().keySet()) {
            Vector<Integer> v = new Vector<Integer>();
            v.add(word);

            if (cortex.isNgramConsensus(v) == v.size()) {
                lex_words.add(v);
            }
        }

        SimilarityCortex.Strategy similarityStr = SimilarityCortex.Strategy.INTERSECTION;
        //ContextCortex.Strategy contextStr = ContextCortex.Strategy.SUM;

        ContextCortex.Strategy[] strategies = {ContextCortex.Strategy.SUM};
        SimilarityCortex sc = new SimilarityCortex(cortex);

        double[] w = {1, 0.25, 0.125, 0.065};
        for (ContextCortex.Strategy contextStr : strategies) {
            System.out.println("contextStr: " + contextStr);
            for (Vector<String> line : inputs) {

                Vector<Integer> phrase = new Vector<Integer>();
                phrase.add(l.getIndex(new Token<String>(line.get(0))));

                Vector<Integer> context0 = new Vector<Integer>();
                context0.add(Lexicon.NULL_INDEX);

                Vector<Integer> context1 = new Vector<Integer>();
                context1.add(l.getIndex(new Token<String>(line.get(1))));

                Vector<Integer> context2 = new Vector<Integer>();
                context2.add(l.getIndex(new Token<String>(line.get(2))));

                Map<Vector<Integer>, Double> result_c0 = sc.getRelativesInContext(phrase, similarityStr, context0, contextStr, lex_words, w[0], w[1], w[2], w[3]);
                Map<Vector<Integer>, Double> result_c1 = sc.getRelativesInContext(phrase, similarityStr, context1, contextStr, lex_words, w[0], w[1], w[2], w[3]);
                Map<Vector<Integer>, Double> result_c2 = sc.getRelativesInContext(phrase, similarityStr, context2, contextStr, lex_words, w[0], w[1], w[2], w[3]);

                TreeSet<Map.Entry<Vector<Integer>, Double>> sr0 = new TreeSet<Map.Entry<Vector<Integer>, Double>>(comp);
                sr0.addAll(result_c0.entrySet());

                TreeSet<Map.Entry<Vector<Integer>, Double>> sr1 = new TreeSet<Map.Entry<Vector<Integer>, Double>>(comp);
                sr1.addAll(result_c1.entrySet());

                TreeSet<Map.Entry<Vector<Integer>, Double>> sr2 = new TreeSet<Map.Entry<Vector<Integer>, Double>>(comp);
                sr2.addAll(result_c2.entrySet());

                System.out.println("******");
                System.out.println(line.get(0) + " : -");
                System.out.println();

                printIterator(sr0.iterator(), 15, l);

                System.out.println(line.get(0) + " : " + line.get(1));
                System.out.println();

                printIterator(sr1.iterator(), 15, l);

                System.out.println(line.get(0) + " : " + line.get(2));
                System.out.println();

                printIterator(sr2.iterator(), 15, l);
                System.out.println("******");

            }
            System.out.println("*&*&*%*%*^$^&$*^%(&%^&^%*&%&^%*&%");
        }

    }


    public void printIterator(Iterator<Map.Entry<Vector<Integer>, Double>> iter, int size, Lexicon<String> l) {
        for (int i = 0; i < size; i++) {
            Map.Entry<Vector<Integer>, Double> cur;
            if (iter.hasNext()) {
                cur = iter.next();
                System.out.println(l.getEntry(cur.getKey().get(0)) + " , " + cur.getValue());
            }
        }
        System.out.println("----");
    }
}



