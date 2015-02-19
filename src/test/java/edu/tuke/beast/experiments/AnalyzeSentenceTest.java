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
public class AnalyzeSentenceTest {

    public static String cortexPath = "/home/blur/pride/cortex";
    public static Vector<Vector<String>> inputs = new Vector<Vector<String>>();
    public static Beast beast;
    public static Cortex cortex;
    public static Lexicon<String> l;

    public Vector<Vector<Map<Integer,Double>>> results;

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

    private Vector<Vector<Map<Integer,Double>>> prepareResults(int[] tokens){
       Vector<Vector<Map<Integer,Double>>> results = new Vector<Vector<Map<Integer,Double>>>();

       return results;
    }



    @Test
    public void testFindContext() throws IOException, ClassNotFoundException, SizeLimitExceededException {
        
        String sentence = "robert is a king";

        int[] tokens = lexiconizeSentence(sentence);

        compute(tokens);
        System.out.println("");

        Fascicle.setInvWei(true);
        compute(tokens);
    }

    private void compute(int[] tokens){
        results = new Vector<Vector<Map<Integer,Double>>>();

        for(int i=0;i<tokens.length;i++){
            Map<Integer,Double> map1 = new HashMap<Integer,Double>();
            Map<Integer,Double> map2 = new HashMap<Integer,Double>();
            Vector<Map<Integer,Double>> v = new Vector<Map<Integer,Double>>();
            v.add(map1); v.add(map2);
            results.add(v);
            System.out.print("("+i+ ", "+ tokens[i]+") ");
        }

        System.out.println("");

        for(int i=0;i<tokens.length;i++){
         //   System.out.println("");
         //   System.out.println(i);
         //   System.out.println("S");
            for(int j = 1; j < i+4 && i+j < tokens.length; j++){
                int t1 = tokens[i];
                int t2 = tokens[i+j];

                //System.out.println(i+","+ (i+j) +": "+ (j-1)+"");
                Fascicle f = cortex.getRegion().getFascicle(j-1);

                double w = getWeight(f, t1, t2);

                Map<Integer,Double> m = results.get(i).get(0);
                m.put(t2,w);
            }
          //  System.out.println("I");
            for(int j = 1; j < i+4 && i-j >= 0; j++) {
                int t1 = tokens[i];
                int t2 = tokens[i-j];

               // System.out.println((i-j) +","+ i +": "+ (j-1)+"");
                Fascicle f = cortex.getRegion().getFascicle(j-1);

                double w = getWeight(f, t1, t2);

                Map<Integer,Double> m = results.get(i).get(1);
                m.put(t2,w);
            }

        }

        //System.out.println(results);

        analyzeResult();
    }

    private double getWeight(Fascicle f, int t1, int t2){
        //double w = f.getSignificance(t1, t2);
        //double w = f.getWeigth(t1, t2);
        double w = f.getWeigth(t1, t2) - f.getWeigthInv(t1, t2);

        return w;
    }

    private int[] lexiconizeSentence(String sentence) {
        String[] words = sentence.split(" ");

        int[] result = new int[words.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = l.getIndex(new Token<String>(words[i]));
        }

        return result;
    }

    private void analyzeResult(){

        for(int i=0;i<results.size();i++){
            Vector<Map<Integer,Double>> v = results.get(i);

            int t1=-1;
            int t2=-1;
            double w1 = 0.;
            double w2 = 0.;
            Map<Integer,Double> map1 = v.get(0);
            Map<Integer,Double> map2 = v.get(1);

            for(Map.Entry<Integer,Double> e : map1.entrySet()){
                if (Math.abs(e.getValue()) > w1){// && e.getValue() > 0){
                    t1 = e.getKey();
                    w1 = e.getValue();
                }
            }

            for(Map.Entry<Integer,Double> e : map2.entrySet()){
                if (Math.abs(e.getValue()) > w2){// && e.getValue() > 0){
                    t2 = e.getKey();
                    w2 = e.getValue();
                }
            }

            System.out.println(i+": "+(w1-w2) +" -: ("+ t1 + " :" + w1 +") ("+ t2 + " :" + w2 +")");

        }

    }
}



