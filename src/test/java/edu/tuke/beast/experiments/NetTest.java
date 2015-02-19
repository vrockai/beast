/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.experiments;

import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.cortex.SimilarityCortex;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.token.Token;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import javax.naming.SizeLimitExceededException;
import org.junit.Test;

/**
 * Named entity tagging test
 * @author blur
 */
public class NetTest {

    public static String cortexPath = "/home/blur/workspace/beast_maven/idiot/cortex";

    @Test
    public void testNet() throws IOException, ClassNotFoundException, SizeLimitExceededException {

        Comparator comp = new Comparator() {

            public int compare(Object o1, Object o2) {
                return -1 * ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        };

        Beast beast = new Beast(cortexPath);
        Cortex cortex = beast.getCortex();
        Lexicon<String> l = beast.getLexicon();

        List<String> input = new ArrayList<String>();

        input.add("red");
        input.add("green");
        input.add("blue");

        List<Map<Vector<Integer>, Double>> mapList = new ArrayList<Map<Vector<Integer>, Double>>();
        SimilarityCortex sc = new SimilarityCortex(cortex);

        Set<Vector<Integer>> lex_words = new HashSet<Vector<Integer>>();
        for (Integer word : l.getHashMap().keySet()) {

            Vector<Integer> v = new Vector<Integer>();
            v.add(word);

            if (cortex.isNgramConsensus(v) == v.size()) {
                lex_words.add(v);
            }
        }

        // is = input string
        for (String is : input) {
            // ii = input index
            Integer ii = l.getIndex(new Token<String>(is));
            // iv = input vector
            Vector<Integer> iv = new Vector<Integer>();
            iv.add(ii);
            mapList.add(sc.getRelatives(iv, lex_words, 1., 1., 1., 1.));
        }

        System.out.println(mapList.get(0));
        System.out.println(mapList.get(0).keySet().size());


        TreeSet<Map.Entry<Integer, Double>> sr0 = new TreeSet<Map.Entry<Integer, Double>>(comp);
        sr0.addAll(simplifyMap(mapList.get(0)).entrySet());

        for (int k = 0; k < 30; k++) {
            Map.Entry<Integer, Double> e = sr0.pollFirst();
            System.out.println(l.getEntry(e.getKey()).getValue() + " = " + e.getValue());
        }

    }

    private Map<Integer, Double> simplifyMap(Map<Vector<Integer>, Double> map) {
        Map<Integer, Double> result = new HashMap<Integer, Double>();

        for (Map.Entry<Vector<Integer>, Double> e : map.entrySet()) {
            result.put(e.getKey().get(0), e.getValue());
        }

        return result;
    }
}
