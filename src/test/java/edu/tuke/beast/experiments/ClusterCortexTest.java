/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.experiments;

import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.ClusterCortex;
import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.cortex.SimilarityCortex;
import edu.tuke.beast.lexicon.Lexicon;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;

/**
 *
 * @author vrockai
 */
public class ClusterCortexTest extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();

    public void testNaryRelatednessCluster() throws IOException, ClassNotFoundException, SizeLimitExceededException {

        String cortexString = System.getProperty("cortex");
        System.out.println("Cortex: " + cortexString);
        cortexString = "/home/vrockai/School/sk_cortex/cortex";
        Beast beast = new Beast(cortexString);
        //Beast beast = new Beast("idiot/cortex");
        //Beast beast = new Beast("/home/vrockai/sl_cortex/cortex");
        //Beast beast = new Beast("sk_corpus/corpus");

        //String[] input = {"florence", "rome", "berlin"};
        //String[] input = {"január", "február", "máj"};
        //String[] input = {"three", "fifty", "twenty"};
        String[][] inputs = {
            // CS
            //{"červenec","březen","duben"},
            //{"jeden","dva","tři"},
            //{"berlín","bratislava","praha"},
            //{"červená","modrá","zelená"},
            //{"kytara","klavír","bicí"},
            //{"anglicky","česky","rusky"}

            // SK
            {"január","február","marec"},
            //{"jeden","dva","tri"},
            //{"bratislava","košice","prešov"},
            //{"červená","modrá","zelená"},
            //{"lev","pes","mačka"},
            //{"anglický","slovenský","nemecký"}
            //{"january", "february", "may"},
            //{"one", "two", "three"},
        //    {"red", "green", "blue"}//,
        //{"english", "french", "german"},
         //   {"coffee", "beer", "wine"}
         //   {"srpen","září","říjen"}
         //{"peter","john","oliver"}
        //{"water", "beer", "wine"},
        //{"monday", "tuesday", "wednesday"}
        //{"paris", "london", "rome"}
        //    {"zem", "mars", "saturn"}
        };
        //String[] input = {"islander", "asian", "scottish"};
        // String context = "native";
        //String[] input = new String[3];
        /*
        String inputString = System.getProperty("input");
        int c = 0;
        for (String s : inputString.split(",")){
        input[c++] = s;
        }
         */
        double tresh = 2.;

        Cortex cortex = beast.getCortex();
        Lexicon<String> lexicon = beast.getLexicon();

        Set<Set<Integer>> aset = new HashSet<Set<Integer>>();

        //Must be in lexicon
        for (String[] input : inputs) {
            Set<Integer> qset = new HashSet<Integer>();
            for (String day : input) {
                System.out.println(day);
                assertTrue(lexicon.getIndex(day) != Lexicon.NULL_INDEX);
                qset.add(lexicon.getIndex(day));
            }
            aset.add(qset);
        }

        //Integer cont = lexicon.getIndex(context);
        // assertTrue(cont != Lexicon.NULL_INDEX);

        cortex.setTreshold(tresh);
        ClusterCortex cc = new ClusterCortex(cortex);

        cc.setStrategy(SimilarityCortex.Strategy.INTERSECTION);
        Set<Integer> lset = lexicon.getLexiconSet();

       // for (double gamma = 1.0; gamma < 1.5; gamma = gamma + 0.01) {
        double gamma = 1.0;
            cc.setGamma(gamma);
            for (Set<Integer> qset : aset) {
                System.out.println("gamma:" + gamma);

                Set<Integer> result = cc.getCluster(qset, lset, false);

                for (Integer i : result) {
                    System.out.print(lexicon.getEntry(i).getValue() + ", ");

                }
                
                System.out.println("size: " + result.size());
            }
        //}
    }
}
