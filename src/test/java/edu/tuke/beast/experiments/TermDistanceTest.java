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
import edu.tuke.beast.properties.PropertiesSingelton;
import edu.tuke.util.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

/**
 *
 * @author blur
 */
public class TermDistanceTest extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();
    private static SimilarityCortex.Strategy strategy;
    private static Integer mayi = 0;
    private static Map<Set<Integer>, Set<Integer>> clusterCache = new HashMap<Set<Integer>, Set<Integer>>();

    public void testAvgDistance() throws IOException, ClassNotFoundException, SizeLimitExceededException, JWNLException {

        String cortexName = "/home/blur/full_cortex/cortex";
        String graphName = "full";

        Beast beast = new Beast(cortexName);

        double tresh = 2.;

        Cortex cortex = beast.getCortex();
        Lexicon<String> lexicon = beast.getLexicon();

        Set<Integer> qset = new HashSet<Integer>();

        Set<Set<Integer>> all_sets = Utils.generateVariations(qset);

        cortex.setTreshold(tresh);
        ClusterCortex cc = new ClusterCortex(cortex);
        cc.setStrategy(SimilarityCortex.Strategy.SIG);
        Set<Integer> lset = lexicon.getLexiconSet();

        String wnPropPath = PropertiesSingelton.getInstance().getProperties().getProperty("wn.prop.path");
        System.out.println("Path to wordnet properties: " + wnPropPath);
        JWNL.initialize(new FileInputStream(wnPropPath));

        int c = 0;
        int cs = 0;

        Set<Synset> ss = new HashSet<Synset>();
        Set<Integer> sset = new HashSet<Integer>();
        List<Double> avgList = new ArrayList<Double>();
        List<Double> ranList = new ArrayList<Double>();

        for (Integer i : lset) {
            String term = lexicon.getEntry(i).getValue();
            IndexWord indexWord = Dictionary.getInstance().getIndexWord(POS.NOUN, term);
            if (indexWord != null) {
                sset.add(i);;
                Synset[] set = indexWord.getSenses();

                for (Synset s : set) {
                    Set<Integer> others = new HashSet<Integer>();

                    for (Integer j : lset) {
                        if (!j.equals(i) && s.containsWord(lexicon.getEntry(j).getValue())) {
                            others.add(j);
                        }
                    }

                    if (!others.isEmpty()) {
                        if (others.size() > 3) {
                            ss.add(s);
                            cs++;
                        }
                    }

                }

                System.out.println(term + ": " + indexWord + " synset#: " + set.length);
                System.out.println("Synsets found: " + ss.size());

                c++;
            }
        }

        System.out.println("najdenych NOUN termov: " + c);
        System.out.println("najdenych synsetov: " + ss.size());

        for (Synset s : ss) {
            Set<Integer> iset = new HashSet<Integer>();
            for (Integer i : sset) {
                if (s.containsWord(lexicon.getEntry(i).getValue())) {
                    iset.add(i);
                }
            }
            Set<Set<Integer>> vset = Utils.generateVariations(iset);

            for (Set<Integer> xset : vset) {
                double clusterAvg = cc.avgDist(xset);
                avgList.add(clusterAvg);
            }

        }

        
        List<Integer> knownList = new ArrayList<Integer>(sset);
        for (int i = 0; i < avgList.size(); i++) {
            Random generator = new Random();
            int t1 = generator.nextInt(knownList.size() - 1);
            int t2 = generator.nextInt(knownList.size() - 1);
            do {
                t2 = generator.nextInt(knownList.size() - 1);
            } while (t1 == t2);

            int t3 = generator.nextInt(knownList.size() - 1);
            do {
                t3 = generator.nextInt(knownList.size() - 1);
            } while (t2 == t3);

            Set<Integer> iset = new HashSet<Integer>();
            iset.add(knownList.get(t1));
            iset.add(knownList.get(t2));
            iset.add(knownList.get(t3));
            ranList.add(cc.avgDist(iset));
        }

        System.out.println(cc.getRelatives(100, 100, 1., 1., 1., 1.));
        Set<Integer> testSet = new HashSet<Integer>();
        testSet.add(lexicon.getIndex("yellow"));
        testSet.add(lexicon.getIndex("red"));
        testSet.add(lexicon.getIndex("green"));
        System.out.println(cc.avgDist(testSet));

        Utils.saveHistogram(avgList, "real" + graphName + "Terms.png");
        Utils.saveHistogram(ranList, "random" + graphName + "Terms.pg");

    }
    /*
    public void saveHistogram(List<Double> dlist, String filename) {
    double[] value = new double[dlist.size()];
    
    for (int i = 0; i < dlist.size(); i++) {
    value[i] = dlist.get(i);
    }
    
    HistogramDataset      hds = new HistogramDataset();
    hds.addSeries("H1", value, 100, 0, 40);
    
    JFreeChart chart2 = ChartFactory.createHistogram("Word frequency", "frequency", "count", hds, PlotOrientation.VERTICAL, false, false, false);
    
    int width = 800;
    int height = 600;
    try {            
    ChartUtilities.saveChartAsPNG(new File(filename), chart2, width, height);
    } catch (IOException e) {
    }
    }
    
     */
}
