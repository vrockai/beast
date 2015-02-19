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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
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
public class GenerateSimMatrixTest {

    public static String cortexPath = "/home/blur/pride/cortex2";
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

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/home/blur/sim2.dat")));

        SimilarityCortex sc = new SimilarityCortex(cortex);

        Set<Integer> keys = l.getHashMap().keySet();
        Integer[] k = keys.toArray(new Integer[keys.size()]);

        String delimiter = "\t";

        //int len = k.length;
        int len = 1000;

        //out.print("\t");
        for(int i = 1;i< len; i++){
            out.print(l.getEntry(k[i]).getValue() + (i < len-1 ? delimiter : ""));
        }

        out.println();
        double max=0;
        int count=0;

        long l0 = 0;
        long l1 = 0;

        for(int i = 1;i< len; i++){
            l0 = System.currentTimeMillis();
            System.out.println(i + " : "+ (l1-l0) + " ms");
            out.print(l.getEntry(k[i]).getValue());// + "\t");
            for(int j = 1; j < i; j++){
                double d = 0;//sc.getRelatives(k[i], k[j], 1., 0.5, 0.25, 0.125);
                max+=d;
                count++;
                out.print((j < i ? delimiter : "") + d   );
            }
            for(int j = i; j < len; j++){
                double d = 0.;
                out.print((j < len ? delimiter : "") + d);
            }
            if (i < len -1)
                out.println();
            l1 = System.currentTimeMillis();
        }

        out.close();

        System.out.println(max+"/"+count);
    }

    private Map<Integer, Double> countConsensus(int t, ContextCortex.Strategy contextStr) {
        ContextCortex cc = new ContextCortex(cortex);
        Lexicon<String> l = beast.getLexicon();
        Vector<Integer> vt = new Vector<Integer>(1);
        vt.add(t);
        return cc.getRelatives(vt, l.getHashMap().keySet(), contextStr);
    }

}



