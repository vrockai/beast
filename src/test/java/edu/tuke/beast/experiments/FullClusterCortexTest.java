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
import edu.tuke.util.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;

/**
 *
 * @author viliam rockai
 * Na pocitanie presnosti a navratnosti pre vsetky mozne trojice zo suboru
 */
public class FullClusterCortexTest extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();
    
    public void testNaryRelatednessCluster() throws IOException, ClassNotFoundException, SizeLimitExceededException {

        String cortexString = System.getProperty("cortex");
        cortexString = "/home/vrockai/School/sk_cortex/cortex";
        System.out.println("Cortex: " + cortexString);

        Beast beast = new Beast(cortexString);
        //january, february, march, april, may, june, july, august, september, october, november, december
        //String[] input = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};        
        //Double[] treshes = {1.0, 2.0};
        //String[] input = {"srpen","září","říjen","listopad","prosinec","leden","únor","březen","duben","květen","červen","červenec"};
        String[] input = {"január","február","marec","apríl","máj","jún","júl","august","september","október","november","december"};
       // for (Double tresh : treshes) {
            double tresh = 2.;

            Cortex cortex = beast.getCortex();
            Lexicon<String> lexicon = beast.getLexicon();

            Set<Integer> fullset = new HashSet<Integer>();

            //Must be in lexicon
            for (String day : input) {
                assertTrue(lexicon.getIndex(day) != Lexicon.NULL_INDEX);
                fullset.add(lexicon.getIndex(day));
            }

            Set<Set<Integer>> qsets = Utils.generateVariations(fullset);

            cortex.setTreshold(tresh);
            ClusterCortex cc = new ClusterCortex(cortex);
            cc.setStrategy(SimilarityCortex.Strategy.INTERSECTION);
            Set<Integer> lset = lexicon.getLexiconSet();

            List<Double> glist = new ArrayList<Double>();
            List<Double> blist = new ArrayList<Double>();
            List<Double> mlist = new ArrayList<Double>();
            List<Double> plist = new ArrayList<Double>();
            List<Double> rlist = new ArrayList<Double>();

            int c = qsets.size();
            int k = 0;

            for (Set<Integer> qset : qsets) {
                System.out.println("to go: " + c--);
                Set<Integer> result = cc.getCluster(qset, lset, false);

                if (Utils.evaluateGood(result, fullset) > 11) {
                    k++;
                    for (Integer i : result) {
                        System.out.println(lexicon.getEntry(i).getValue());

                    }
                }
                double good = Double.valueOf(Utils.evaluateGood(result, fullset));
                double bad = Double.valueOf(Utils.evaluateBad(result, fullset));
                double missing = Double.valueOf(Utils.evaluateMissing(result, fullset));
                glist.add(good);
                blist.add(bad);
                mlist.add(missing);
                plist.add(good / (good + bad));
                rlist.add(good / (good + missing));
            }

            Utils.saveHistogram(glist, "target/monthsGood.png", "", "number of triplets", "good");
            Utils.saveHistogram(blist, "target/monthsBad.png", null, "number of triplets", "bad");
            Utils.saveHistogram(mlist, "target/monthsMissing.png", null, "number of triplets", "missing");
            Utils.saveHistogram(plist, "target/monthsPrecision.png", null, "number of triplets", "precision");
            Utils.saveHistogram(rlist, "target/monthsRecall.png", null, "number of triplets", "recall");
            
            double sum = 0;
            for (Double d : plist){
                sum+=d;
            }
            
            System.out.println("Average precision: " + sum/(double)(plist.size()));
            
            sum = 0;
            for (Double d : rlist){
                sum+=d;
            }
            
            System.out.println("Average recall: " + sum/(double)(rlist.size()));

    //    }
    }
}