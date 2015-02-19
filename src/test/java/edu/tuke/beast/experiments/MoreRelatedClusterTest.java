/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.experiments;

import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.cortex.SimilarityCortex;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.util.Utils;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;

/**
 *
 * @author vrockai
 */
public class MoreRelatedClusterTest extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();
    private static SimilarityCortex.Strategy strategy;
    private static Integer mayi = 0;
    private static Map<Set<Integer>,Set<Integer>> clusterCache = new HashMap<Set<Integer>,Set<Integer>>();

    public void testNaryRelatednessCluster() throws IOException, ClassNotFoundException, SizeLimitExceededException {
        Beast beast = new Beast("idiot/cortex");
        String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
        String[] months = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
        String[] seasons = {"summer", "winter", "autumn", "spring"};

        String[] completeS = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
        String[] input = {"january", "february", "march"};
        double tresh = 4.;

        Cortex cortex = beast.getCortex();
        Lexicon<String> lexicon = beast.getLexicon();

        Set<Integer> complete = new HashSet<Integer>();
        
        //Must be in lexicon
        for (String day : input) {
            assertTrue(lexicon.getIndex(day) != Lexicon.NULL_INDEX);
            complete.add(lexicon.getIndex(day));
        }

        cortex.setTreshold(tresh);
        SimilarityCortex sc = new SimilarityCortex(cortex);
        Set<Integer> lset = lexicon.getLexiconSet();
        
        Set<Set<Integer>> isetSet = generateVariations(complete);
        List<Integer> resultC = new ArrayList<Integer>();
        int c = 0;
        for (Set<Integer> iset : isetSet) {
            System.out.println(isetSet.size() - (c++));
            
            for (Integer i : iset)
                System.out.println("S: "+ lexicon.getEntry(i).getValue());
            
            Set<Integer> result = getCluster(iset, lset, sc);

            for (Integer i : result) {
                System.out.println(lexicon.getEntry(i).getValue());
            }
            
            resultC.add(evaluate(result, complete));
            
            
            for (Integer i : result){
                System.out.println(i+": "+lexicon.getEntry(i).getValue());
            }
        }
        
        double sum = 0;
        for(Integer i : resultC){
            sum += i;
            System.out.println(i);
        }
        
        log.info(sum/resultC.size());
    }

    private Set<Integer> getCluster(Set<Integer> iset, Set<Integer> lset, SimilarityCortex sc) {  
        
        log.info("getCluster "+iset);
        Set<Integer> result = new HashSet<Integer>();

        Set<Set<Integer>> visited = new HashSet<Set<Integer>>();
        Set<Set<Integer>> to_visit = new HashSet<Set<Integer>>();

        Set<Integer> cluster = generateCluster(iset, sc, lset);
        
        System.out.println("CS: " + cluster.size());
        
        
        result.addAll(cluster);
        
        /*
        to_visit.addAll(Utils.generateVariations(cluster));
        do {
            if (!to_visit.iterator().hasNext())
                break;
            iset = to_visit.iterator().next();
            cluster = generateCluster(iset, sc, lset);
            visited.add(iset);
            result.addAll(cluster);
            to_visit.addAll(Utils.generateVariations(cluster));
            to_visit.removeAll(visited);
            log.info("To finish: " + to_visit.size());
        } while (to_visit.size() > 0);
        
         
         */
        
        return result;

    }

    private Integer evaluate(Set<Integer> result, Set<Integer> correct) {
        System.out.println("Rs:" + result.size());
        System.out.println("Cs:" + correct.size());

        int x = 1;
        
        Set<Integer> xxx = new HashSet<Integer>(result);
        xxx.removeAll(correct);
        if (xxx.size()> 0){
               System.out.println("POZOR!");
               x = -1;
        }
        
        Set<Integer> compl = new HashSet<Integer>(correct);
        compl.removeAll(result);


        return x*(correct.size() - compl.size());
    }
    private static Map<Integer, Map<Integer, Double>> cache = new HashMap<Integer, Map<Integer, Double>>();

    private double relate(Integer i1, Integer i2, SimilarityCortex sc) {

        if (!cache.containsKey(i1)) {
            Map<Integer, Double> imap = new HashMap<Integer, Double>();
            cache.put(i1, imap);
        }
        if (!cache.containsKey(i2)) {
            Map<Integer, Double> imap = new HashMap<Integer, Double>();
            cache.put(i2, imap);
        }
        if (!cache.get(i1).containsKey(i2)) {
            double r = sc.getRelatives(i1, i2, 1., 1., 1., 1.);
            cache.get(i1).put(i2, r);
            cache.get(i2).put(i1, r);
        }

        return cache.get(i1).get(i2);
    }

    private Set<Integer> generateCluster(Set<Integer> iset, SimilarityCortex sc, Set<Integer> lexicon) {
        
        double param = 1.;
        
        log.info("generateCluster "+iset);
        
        
        if (clusterCache.containsKey(iset)){
            return clusterCache.get(iset);
        }
        
        //System.out.println("RODINA: " + iset);

        assertEquals(iset.size(), 3);
        Iterator<Integer> iter = iset.iterator();
        Integer i1 = iter.next();
        Integer i2 = iter.next();
        Integer i3 = iter.next();

        //System.out.println(i1 + ", " + i2 + ", " + i3);

        Set<Integer> result = new HashSet<Integer>();
        /*
        double c1 = 1. / sc.getRelatives(i1, i2, 1., 1., 1., 1.);
        double c2 = 1. / sc.getRelatives(i1, i3, 1., 1., 1., 1.);
        double c3 = 1. / sc.getRelatives(i2, i3, 1., 1., 1., 1.);
         */
        double c1 = 1. / relate(i1, i2, sc);
        double c2 = 1. / relate(i1, i3, sc);
        double c3 = 1. / relate(i2, i3, sc);
        int c = 0;

        for (Integer i : lexicon) {
            //log.info("To S finish: " + (lexicon.size() - (c++)));
            /*
            double k1 = 1. / sc.getRelatives(i, i1, 1., 1., 1., 1.);
            double k2 = 1. / sc.getRelatives(i, i2, 1., 1., 1., 1.);
            double k3 = 1. / sc.getRelatives(i, i3, 1., 1., 1., 1.);
             */
            double k1 = 1. / relate(i, i1, sc);
            double k2 = 1. / relate(i, i2, sc);
            double k3 = 1. / relate(i, i3, sc);

            if (i.equals(mayi)) {
                //System.out.println(iset);
                //System.out.println((k1 + k2 + k3) + " > " + (c1 + c2 + c3));
            }
            if ((param*(k1 + k2 + k3)) <= ((c1 + c2 + c3))) {
                result.add(i);
                //System.out.println((k1 + k2 + k3) + " - " + (c1 + c2 + c3));
            }

        }

        clusterCache.put(iset, result);
        
        return result;
    }

    private void saveDat(String filename, List<Integer> iList) {

        try {

            FileOutputStream fis = new FileOutputStream(filename);
            PrintStream ps = new PrintStream(fis);

            for (Integer i : iList) {
                ps.println(i);
            }

            ps.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MoreRelatedClusterTest.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    private List<Integer> getCorrectAnswers(Beast beast, String[] input, double tresh, double[] wei, boolean meth) {
        //Entity to be checked
        Cortex cortex = beast.getCortex();
        Lexicon<String> lexicon = beast.getLexicon();

        List<Integer> dayList = new ArrayList<Integer>();
        //Must be in lexicon
        for (String day : input) {
            assertTrue(lexicon.getIndex(day) != Lexicon.NULL_INDEX);
            dayList.add(lexicon.getIndex(day));
        }

        cortex.setTreshold(tresh);
        SimilarityCortex sc = new SimilarityCortex(cortex);

        sc.setStrategy(strategy);

        Set<Integer> lexset = lexicon.getHashMap().keySet();
        Map<Integer, Map<Integer, Double>> cache = new HashMap<Integer, Map<Integer, Double>>();

        List<List<Integer>> daySubsets = generateVariations(dayList);
        List<Integer> correctAnswers = new ArrayList<Integer>();
        // pocitanie podobnosti

        for (List<Integer> daySubset : daySubsets) {

            List<Map<Integer, Double>> rml = new ArrayList<Map<Integer, Double>>();

            for (int i = 0; i < daySubset.size(); i++) {
                Integer token = daySubset.get(i);
                if (!cache.containsKey(token)) {
                    Map<Integer, Double> relatives = sc.getRelatives(token, lexset, wei[0], wei[1], wei[2], wei[3]);
                    cache.put(token, relatives);
                }

                rml.add(cache.get(token));
            }

            Map<Integer, Double> result = new HashMap<Integer, Double>();
            Map<Integer, Double> fm = rml.get(0);

            // nasobenie
            if (meth) {
                for (Integer i : fm.keySet()) {
                    double w = fm.get(i) != null ? fm.get(i) : 0;
                    for (int j = 1; j < rml.size(); j++) {
                        w *= rml.get(j).get(i) != null ? rml.get(j).get(i) : 0;
                    }
                    if (w > 0) {
                        result.put(i, w);
                    }
                }
            } else // priemer
            {
                for (Integer i : fm.keySet()) {
                    double w = fm.get(i) != null ? fm.get(i) : 0;
                    for (int j = 1; j < rml.size(); j++) {
                        w += rml.get(j).get(i) != null ? rml.get(j).get(i) : 0;
                    }
                    if (w > 0) {
                        result.put(i, w / daySubset.size());
                    }
                }
            }


            SortedSet<Map.Entry<Integer, Double>> sorted_map = Utils.sortMapByValue(result);

            int correct = 0;
            int counter = dayList.size();

            for (Map.Entry<Integer, Double> entry : sorted_map) {
                System.out.println(lexicon.getEntry(entry.getKey()) + ": " + entry.getValue());
                if (dayList.contains(entry.getKey())) {
                    correct++;
                }
                if (--counter <= 0) {
                    break;
                }
            }

            correctAnswers.add(correct);
        }

        return correctAnswers;


    }

    private Set<Set<Integer>> generateVariations(Set<Integer> set) {
        Set<Set<Integer>> result = new HashSet<Set<Integer>>();

        List<Integer> list = new ArrayList<Integer>(set);

        int size = list.size();

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                for (int k = j + 1; k < size; k++) {
                    Set<Integer> triplet = new HashSet<Integer>();
                    triplet.add(list.get(i));
                    triplet.add(list.get(j));
                    triplet.add(list.get(k));
                    result.add(triplet);
                }
            }
        }


        return result;
    }

    private List<List<Integer>> generateVariations(List<Integer> list) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();

        int size = list.size();

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                for (int k = j + 1; k < size; k++) {
                    List<Integer> triplet = new ArrayList<Integer>();
                    triplet.add(list.get(i));
                    triplet.add(list.get(j));
                    triplet.add(list.get(k));
                    result.add(triplet);
                }
            }
        }


        return result;
    }

    private List<Integer> pick3(List<Integer> list) {
        List<Integer> result = new ArrayList<Integer>();

        for (int i = 0; i < 3; i++) {
            result.add(list.get(i));
        }

        return result;
    }
    /*
    class ValueComparator implements Comparator {
    
    Map base;
    public ValueComparator(Map base) {
    this.base = base;
    }
    
    public int compare(Object a, Object b) {
    
    if((Double)base.get(a) < (Double)base.get(b)) {
    return 1;
    } else if(((Double)base.get(a)).equals((Double)base.get(b))) {
    return 0;
    } else {
    return -1;
    }
    }
     * 
     */
}
