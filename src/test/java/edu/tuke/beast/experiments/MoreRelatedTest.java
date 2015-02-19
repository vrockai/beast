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
public class MoreRelatedTest extends TestCase {

    private static SimilarityCortex.Strategy strategy;

    public void testNaryRelatedness() throws IOException, ClassNotFoundException, SizeLimitExceededException {
        Beast beast = new Beast("/home/vrockai/cortex/cortex");
        String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
        String[] months = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
        String[] seasons = {"summer", "winter", "autumn", "spring"};

        List<Integer> correctAnswers;

        double[] tres = {1., 4., 16.};

        double[][] wei = {{1., 0., 0., 0.}, {1., 0.5, 0.25, 0.125}, {1., 1., 1., 1.}, {1., 0.5, 0.25, 0.125}};

        for (int i = 0; i < 4; i++) {
            //wei[2][i] = Math.random();
            wei[3][i] = Math.random();
        }

        for (int i = 0; i < 4; i++) {
            //System.out.println(wei[2][i]);
            System.out.println(wei[3][i]);
        }

        strategy = SimilarityCortex.Strategy.INTERSECTION;

        for (Double t : tres) {
            for (int i = 0; i < wei.length; i++) {
                String fPfx = "INT-T" + t;
                for (int j = 0; j < 4; j++) {
                    fPfx += "w" + j + "-" + wei[i][j];
                }

                correctAnswers = getCorrectAnswers(beast, seasons, t, wei[i],true);
                saveDat("seasonsM" + fPfx + ".dat", correctAnswers);

                correctAnswers = getCorrectAnswers(beast, days, t, wei[i],true);
                saveDat("daysM" + fPfx + ".dat", correctAnswers);

                correctAnswers = getCorrectAnswers(beast, months, t, wei[i],true);
                saveDat("monthsM" + fPfx + ".dat", correctAnswers);
                
                correctAnswers = getCorrectAnswers(beast, seasons, t, wei[i],false);
                saveDat("seasonsA" + fPfx + ".dat", correctAnswers);

                correctAnswers = getCorrectAnswers(beast, days, t, wei[i],false);
                saveDat("daysA" + fPfx + ".dat", correctAnswers);

                correctAnswers = getCorrectAnswers(beast, months, t, wei[i],false);
                saveDat("monthsA" + fPfx + ".dat", correctAnswers);
            }
        }

        strategy = SimilarityCortex.Strategy.WEIGHTED;

        for (Double t : tres) {
            for (int i = 0; i < wei.length; i++) {
                String fPfx = "WEI-T" + t;
                for (int j = 0; j < 4; j++) {
                    fPfx += "w" + j + "-" + wei[i][j];
                }

                correctAnswers = getCorrectAnswers(beast, seasons, t, wei[i],true);
                saveDat("seasonsM" + fPfx + ".dat", correctAnswers);

                correctAnswers = getCorrectAnswers(beast, days, t, wei[i],true);
                saveDat("daysM" + fPfx + ".dat", correctAnswers);

                correctAnswers = getCorrectAnswers(beast, months, t, wei[i],true);
                saveDat("monthsM" + fPfx + ".dat", correctAnswers);
                
                correctAnswers = getCorrectAnswers(beast, seasons, t, wei[i],false);
                saveDat("seasonsA" + fPfx + ".dat", correctAnswers);

                correctAnswers = getCorrectAnswers(beast, days, t, wei[i],false);
                saveDat("daysA" + fPfx + ".dat", correctAnswers);

                correctAnswers = getCorrectAnswers(beast, months, t, wei[i],false);
                saveDat("monthsA" + fPfx + ".dat", correctAnswers);
            }
        }
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
            Logger.getLogger(MoreRelatedTest.class.getName()).log(Level.SEVERE, null, ex);
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
            } else 
            // priemer
            {
                for (Integer i : fm.keySet()) {
                    double w = fm.get(i) != null ? fm.get(i) : 0;
                    for (int j = 1; j < rml.size(); j++) {
                        w += rml.get(j).get(i) != null ? rml.get(j).get(i) : 0;
                    }
                    if (w > 0) {
                        result.put(i, w/daySubset.size());
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
