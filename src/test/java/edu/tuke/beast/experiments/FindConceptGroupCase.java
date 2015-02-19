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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
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
public class FindConceptGroupCase {

    public static String cortexPath = "/home/vrockai/cortex/viliam";
    public static Vector<Vector<String>> inputs = new Vector<Vector<String>>();
    public static Beast beast;
    public static Cortex cortex;
    public static SimilarityCortex sc;
    public static Lexicon<String> l;
    public static int xyz = 0;

    @BeforeClass
    public static void setUpClass() throws Exception {
        beast = new Beast(cortexPath);
        cortex = beast.getCortex();
        cortex.setTreshold(10f);
        sc = new SimilarityCortex(cortex);
        l = beast.getLexicon();
    }

    private Set<Vector<Integer>> vectorizeLex(Lexicon<String> l) {
        Set<Vector<Integer>> result = new HashSet<Vector<Integer>>();

        for (Integer key : l.getHashMap().keySet()) {
            Vector<Integer> v = new Vector<Integer>();
            v.add(key);
            result.add(v);
        }

        return result;
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

    private List<Vector<Integer>> sortMap(Map<Vector<Integer>, Double> map){
        List<Vector<Integer>> result = new ArrayList<Vector<Integer>>();

        Map<Vector<Integer>, Double> m = new HashMap<Vector<Integer>, Double>(map);

        do{

            Vector<Integer> v1 = m.keySet().iterator().next();
            double w_max = m.get(v1);

            for(Vector<Integer> v : m.keySet()){
                double w = m.get(v);
                if (w > w_max){
                    v1 = v;
                    w_max = w;
                }
            }

            result.add(v1);
            m.remove(v1);
        }while (m.size() > 0);

        return result;
    }

    @Test
    public void testFindContext() throws IOException, ClassNotFoundException, SizeLimitExceededException {
        String q = "yellow";

        Integer start = l.getIndex(new Token<String>(q));

        Vector<Integer> v_start = new Vector<Integer>();
        v_start.add(start);

        Set<Vector<Integer>> v_set = vectorizeLex(l);

        List<Vector<Integer>> q_set = new ArrayList<Vector<Integer>>();
        Set<Vector<Integer>> a_set = new HashSet<Vector<Integer>>();
        q_set.add(v_start);

        Map<Vector<Integer>, Double> rels = null;

        for (Vector<Integer> question : q_set) {
            if (!a_set.contains(question)) {
                //rels = sc.getRelatives(question, v_set, 0.4, 0.3, 0.2, 0.1);
                rels = sc.getRelativesInContext(question,SimilarityCortex.Strategy.WEIGHTED ,null, null, v_set, 0.4, 0.3, 0.2, 0.1);
                a_set.add(question);
                break;
            }
        }

        q_set = sortMap(rels);
        rels = getRels(q_set, a_set, v_set, rels);

        printRes(rels);

    }

    private void printRes(Map<Vector<Integer>, Double> res){
        for (Map.Entry<Vector<Integer>, Double> e : res.entrySet())        {
            System.out.println(l.getEntry(e.getKey().get(0))+" : "+e.getValue());
        }
    }

    private Map<Vector<Integer>, Double> getRels(List<Vector<Integer>> q_set, Set<Vector<Integer>> a_set, Set<Vector<Integer>> v_set, Map<Vector<Integer>, Double> semires) {

        System.out.println(xyz++ + " - " + semires.size());

        if (q_set.size() == 0) {
            return semires;
        }

        Iterator<Vector<Integer>> iter = q_set.iterator();

        if (!iter.hasNext()) {
            return semires;
        }

        boolean stop = true;
        
        for (Vector<Integer> q = iter.next(); iter.hasNext(); q = iter.next()) {
            System.out.println("!"+ q + " : " + l.getEntry(q.get(0)));
            if (!a_set.contains(q)) {
                System.out.println(q + " : " + l.getEntry(q.get(0)));
                Map<Vector<Integer>, Double> rels = sc.getRelativesInContext(q,SimilarityCortex.Strategy.WEIGHTED ,null, null, v_set, 0.4, 0.3, 0.2, 0.1);
                semires = unifyMaps(semires, rels);

                List<Vector<Integer>> list = sortMap(semires);

                a_set.add(q);
                q_set = list;
                q_set.removeAll(a_set);
                stop=false;
                break;
            }
        }

        if (!stop)
            return getRels(q_set, a_set, v_set, semires);
        else
            return semires;

    }

    private Map<Vector<Integer>, Double> unifyMaps(Map<Vector<Integer>, Double> m1, Map<Vector<Integer>, Double> m2) {
        Map<Vector<Integer>, Double> result = new HashMap<Vector<Integer>, Double>();

        for (Vector<Integer> key : m1.keySet()) {
            if (m2.containsKey(key)) {
                result.put(key, Math.min(m1.get(key) ,m2.get(key)) );

                if (key.get(0).equals(5492))
                    System.out.println(key + ": "+ m1.get(key) + ", " + m2.get(key) );
            }
        }

        return result;
    }

    private Map<Integer, Double> countConsensus(int t, ContextCortex.Strategy contextStr) {
        ContextCortex cc = new ContextCortex(cortex);
        Lexicon<String> l = beast.getLexicon();
        Vector<Integer> vt = new Vector<Integer>(1);
        vt.add(t);
        return cc.getRelatives(vt, l.getHashMap().keySet(), contextStr);
    }
}



