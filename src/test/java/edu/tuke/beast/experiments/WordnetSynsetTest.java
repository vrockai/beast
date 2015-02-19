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
import edu.tuke.beast.token.Token;
import edu.tuke.beast.wordnet.WordnetParser;
import edu.tuke.util.Utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;

/**
 *
 * @author blur
 */
public class WordnetSynsetTest extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();
    private static SimilarityCortex.Strategy strategy;
    private static Integer mayi = 0;
    private static Map<Set<Integer>, Set<Integer>> clusterCache = new HashMap<Set<Integer>, Set<Integer>>();

    public void testAvgDistance() throws IOException, ClassNotFoundException, SizeLimitExceededException, JWNLException {
        //Beast beast = new Beast("2g_cortex/cortex");
        Beast beast = new Beast("/home/blur/full_cortex/cortex");

        double tresh = 2.;

        Cortex cortex = beast.getCortex();
        Lexicon<String> lexicon = beast.getLexicon();

        cortex.setTreshold(tresh);
        ClusterCortex cc = new ClusterCortex(cortex);
        cc.setStrategy(SimilarityCortex.Strategy.INTERSECTION);
        Set<Integer> lset = lexicon.getLexiconSet();

        String wnPropPath = PropertiesSingelton.getInstance().getProperties().getProperty("wn.prop.path");
        System.out.println("Path to wordnet properties: " + wnPropPath);
        WordnetParser wp = new WordnetParser(wnPropPath);
        
        Set<Set<Integer>> relevantSiblingList = new HashSet<Set<Integer>>();

        for (Integer i : lset) {
            String term = lexicon.getEntry(i).getValue();

            
            if (wp.isSingle(term)){
                continue;
            }
            
            List<Synset> sl = wp.getSiblings(POS.NOUN, term);

            Set<Integer> siblings = new HashSet<Integer>();

            for (Synset synset : sl) {
                //siblings.addAll(getSynsetSet(synset, lexicon));
                siblings.addAll(getUniqueSynsetSet(synset, lexicon, wp));
            }
            
            relevantSiblingList.add(siblings);
        }
        
        List<Double> pList = new ArrayList<Double>();
        List<Double> rList = new ArrayList<Double>();

        int c = relevantSiblingList.size();
        System.out.println("To do: " + (c));
        for (Set<Integer> synsetIndexes : relevantSiblingList) {
            if (synsetIndexes.size() < 4)
                continue;
            
            Set<Integer> iset = Utils._pick3(synsetIndexes);
            for (Integer op : iset){
                System.out.print(lexicon.getEntry(op)+" ");
            }
            System.out.println("");
            
            for (Integer op : synsetIndexes){
                System.out.print(lexicon.getEntry(op)+" ");
            }
            System.out.println("");
            
            Set<Integer> cluster = cc.getCluster(iset, lset, false);
            
            
            for (Integer op : cluster){
                System.out.print(lexicon.getEntry(op)+" ");
            }
            System.out.println("");
            //cluster.removeAll(iset);
            

                double good = Double.valueOf(Utils.evaluateGood(cluster, synsetIndexes));
                double bad = Double.valueOf(Utils.evaluateBad(cluster, synsetIndexes));
                double missing = Double.valueOf(Utils.evaluateMissing(cluster, synsetIndexes));
                pList.add(good / (good + bad));
                rList.add(good / (good + missing));
            
            System.out.println("p: "+(good / (good + bad)) + ", r:"+(good / (good + missing)) + " ("+(--c)+")");
                    Utils.saveHistogram(pList, "precision.png");
        Utils.saveHistogram(rList, "recall.png");
        }
        


    }

    public Set<Integer> getSynsetSet(Synset synset, Lexicon<String> lexicon) {
        Set<Integer> result = new HashSet<Integer>();

        for (Word word : synset.getWords()) {
            if (lexicon.contains(word.getLemma().toLowerCase())) {
                result.add(lexicon.getIndex(word.getLemma().toLowerCase()));
            }
        }

        return result;
    }

    public Set<Integer> getSynsetSet2(Synset synset, Lexicon<String> lexicon) {
        Set<Integer> result = new HashSet<Integer>();

        for (Word word : synset.getWords()) {
            System.out.println(word.getLemma() + ": " + lexicon.contains(word.getLemma().toLowerCase()));
            if (lexicon.contains(word.getLemma())) {
                result.add(lexicon.getIndex(word.getLemma()));
            }
        }

        return result;
    }

    public int evaluateResult2(Set<Integer> returned, Set<Integer> complete) {

        Set<Integer> comp1 = new HashSet<Integer>();
        comp1.addAll(complete);
        comp1.removeAll(returned);

        Set<Integer> comp2 = new HashSet<Integer>();
        comp2.addAll(returned);
        comp2.removeAll(complete);

        Set<Integer> comp3 = new HashSet<Integer>();
        comp3.addAll(comp1);
        comp3.addAll(comp2);

        System.out.println("returned: " + returned);
        System.out.println("complete: " + complete);
        System.out.println("comp: " + comp2);

        return comp2.size();

    }

    public int evaluateResultGood(Set<Integer> returned, Set<Integer> complete) {

        Set<Integer> comp1 = new HashSet<Integer>();
        comp1.addAll(complete);
        comp1.removeAll(returned);

        Set<Integer> comp2 = new HashSet<Integer>();
        comp2.addAll(returned);
        comp2.removeAll(complete);

        Set<Integer> comp3 = new HashSet<Integer>();
        comp3.addAll(comp1);
        comp3.addAll(comp2);

        System.out.println("returned: " + returned);
        System.out.println("complete: " + complete);
        System.out.println("comp: " + comp2);

        return returned.size() - comp2.size();

    }

    public double evaluateResult(Set<Integer> returned, Set<Integer> complete) {

        Set<Integer> comp = new HashSet<Integer>();

        comp.addAll(complete);
        comp.removeAll(returned);
        double x = (double) comp.size() / (double) complete.size();
        double y = Math.abs(complete.size() - returned.size()) + 1;
        return x / y;
    }

    private Collection<? extends Integer> getUniqueSynsetSet(Synset synset, Lexicon<String> lexicon, WordnetParser wp) throws JWNLException {
        Set<Integer> result = new HashSet<Integer>();

        for (Word word : synset.getWords()) {
            String term = word.getLemma().toLowerCase();
            if (lexicon.contains(term)) {
                if (wp.isSingle(term)){
                    result.add(lexicon.getIndex(word.getLemma().toLowerCase()));
                }
            }
        }

        return result;
    }
}
