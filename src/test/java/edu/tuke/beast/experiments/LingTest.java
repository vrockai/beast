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
public class LingTest extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();

    public void testNaryRelatednessCluster() throws IOException, ClassNotFoundException, SizeLimitExceededException {

        String cortexString = System.getProperty("cortex");
        System.out.println("Cortex: " + cortexString);
        Beast beast = new Beast(cortexString);

        //String nounString = "mesto";
        //String adjString = "pekný";
        //String verbString = "písať";
        String[] nounString = { "king", "animal", "chair" };
        String[] adjString = {"yellow","effective","nice"};
        String[] verbString = {"walk","execute", "build"};

        String[][] inputs = {
         //   {"meno","vec","kráľ","ľuďom","poriadok",
         //    "biely","červená","kráľovský","zaujímavé","mladý",
         //    "narodil","zomrel","písať","byť","pracovať"},

             {"thing","king","pen","paper","water",
             "fast","slow","smooth","quiet","young",
             "drink","eat","investigate","be","produce"}
        };
        
        double tresh = 5.;

        Cortex cortex = beast.getCortex();
        Lexicon<String> lexicon = beast.getLexicon();

        Set<Integer> sNoun = new HashSet<Integer>();
        Set<Integer> sAdj = new HashSet<Integer>();
        Set<Integer> sVerb = new HashSet<Integer>();

        for (String s : nounString){
            sNoun.add(lexicon.getIndex(s));
        }

        for (String s : adjString){
            sAdj.add(lexicon.getIndex(s));
        }

        for (String s : verbString){
            sVerb.add(lexicon.getIndex(s));
        }

        Set<Integer> aset = new HashSet<Integer>();

        //Must be in lexicon
        for (String[] input : inputs) {
            for (String day : input) {
                System.out.println(day);
                assertTrue(lexicon.getIndex(day) != Lexicon.NULL_INDEX);
                aset.add(lexicon.getIndex(day));
            }
        }

        //Integer cont = lexicon.getIndex(context);
        // assertTrue(cont != Lexicon.NULL_INDEX);

        cortex.setTreshold(tresh);
        SimilarityCortex cc = new SimilarityCortex(cortex);
        cc.setStrategy(SimilarityCortex.Strategy.INTERSECTION);
        
        for(Integer i : aset){
            
            double dNoun = cc.getRelativesMulti(sNoun, i, 0.25, 0.25, 0.25, 0.25);
            double dAdj  = cc.getRelativesMulti(sAdj, i, 0.25, 0.25, 0.25, 0.25);
            double dVerb = cc.getRelativesMulti(sVerb, i, 0.25, 0.25, 0.25, 0.25);

            System.out.println("n: "+dNoun+", a: "+dAdj + ", v: "+dVerb );

            double max = Math.max(dNoun, Math.max(dAdj, dVerb));

            if (dNoun == max){
                System.out.println(lexicon.getEntry(i)+": Noun");
            }
            else if(dAdj == max)
            {
                System.out.println(lexicon.getEntry(i)+": Adj");
            }
            else
            {
                System.out.println(lexicon.getEntry(i)+": Verb");
            }
        }


    }
}
