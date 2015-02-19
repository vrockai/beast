/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.cortex;

import edu.tuke.beast.Region;
import java.util.HashMap;
import java.util.Map;

import static edu.tuke.beast.lexicon.Lexicon.NULL_INDEX;

/**
 * 
 * @author rockaiv
 */
public class SynonymCortex {

    public static enum SYNONYMS_STRATEGY {

        WEAK, STRONG
    }

    private Cortex cortex = null;

    public SynonymCortex(Cortex c) {
        this.cortex = c;
    }

    public Map<Integer,Double> getSynonyms(Integer t, SYNONYMS_STRATEGY strategy) {
        Map<Integer,Double> result = new HashMap<Integer,Double>();

        switch (strategy) {
            case WEAK:
                result = getSynonymsWeak(t);
                break;
            case STRONG:
                result = getSynonymsStrong(t);
                break;
            default:
                result = getSynonymsStrong(t);
                break;
        }

        return result;
    }

    public Map<Integer,Double> generalize(Integer t) {

        Map<Integer,Double> result = new HashMap<Integer,Double>();
        Map<Integer,Double> synonyms;

        synonyms = getSynonymsStrong(t);

        for (Map.Entry<Integer,Double> synonym : synonyms.entrySet()) {
            Map<Integer,Double> semiresult = getSynonymsStrong(synonym.getKey());
            result = cortex.maxMinSets(result, semiresult);
        }

        return result;

    }

    public Map<Integer,Double> getSynonymsStrong(Integer t) {

        Map<Integer,Double> result = new HashMap<Integer,Double>();
        Map<Integer,Double>  semiresult = new HashMap<Integer,Double>();

        Map<Integer,Double> [] subsets = new HashMap[4];

        final Region region = cortex.getRegion();

        subsets[0] = region.fascicle[1].getTargetTokens(t, false);
        subsets[1] = region.fascicle[0].getTargetTokens(t, false);
        subsets[2] = region.fascicle[0].getTargetTokens(t, true);
        subsets[3] = region.fascicle[1].getTargetTokens(t, true);

        int c = subsets[0].size();
        int size = c;

        double step = 100 / (double) size;
        double delta = 0;


        /*
         * [e0] [e1] QUESTION [e2] [e3]
         */

        for (Map.Entry<Integer,Double> e0 : subsets[0].entrySet()) {

            // percentage
            c--;
            if (delta > 1) {
                int tr = (int) Math.floor(delta);
                delta -= tr;
            }
            delta += step;

            // synonyms computing

            for (Map.Entry<Integer,Double> e1 : subsets[1].entrySet()) {
                if (cortex.hasAssociation(0, e0.getKey(), e1.getKey())) {
                    for (Map.Entry<Integer,Double> e2 : subsets[2].entrySet()) {
                        if ((cortex.hasAssociation(2, e0.getKey(), e2.getKey())) && (cortex.hasAssociation(1, e1.getKey(), e2.getKey()))) {
                            for (Map.Entry<Integer,Double> e3 : subsets[3].entrySet()) {
                                if ((cortex.hasAssociation(3, e0.getKey(), e3.getKey())) && (cortex.hasAssociation(2, e1.getKey(), e3.getKey())) && (cortex.hasAssociation(0, e2.getKey(), e3.getKey()))) {
                                    semiresult = region.getFascicle(1).getTargetTokens(e0.getKey(), true);
                                    semiresult = cortex.maxMinSets(semiresult, region.getFascicle(0).getTargetTokens(e1.getKey(), true));
                                    semiresult = cortex.maxMinSets(semiresult, region.getFascicle(0).getTargetTokens(e2.getKey(), false));
                                    semiresult = cortex.maxMinSets(semiresult, region.getFascicle(1).getTargetTokens(e3.getKey(), false));

                                    result = cortex.maxMinSets(result, semiresult);
                                }
                            }
                        }
                    }
                }
            }

        }
        System.out.println();
        return result;
    }

    public Map<Integer,Double> getSynonymsWeak(Integer t) {

        Map<Integer,Double> result = new HashMap<Integer,Double>();
        Map<Integer,Double> result_a;
        Map<Integer,Double> result_ai;
        Map<Integer,Double> result_b;
        Map<Integer,Double> result_bi;
        final Region region = cortex.getRegion();

        result_a = region.fascicle[0].getTargetTokens(t, true);
        result_ai = region.fascicle[0].getTargetTokens(t, false);
        result_b = region.fascicle[1].getTargetTokens(t, true);
        result_bi = region.fascicle[1].getTargetTokens(t, false);

        System.out.println(region.fascicle[0]);
        System.out.println(region.fascicle[1]);
        System.out.println(region.fascicle[2]);
        System.out.println(region.fascicle[3]);

        result = cortex.getTargetTokens(result_ai.keySet(), 0, true);
        result = cortex.maxMinSets(result, cortex.getTargetTokens(result_a.keySet(), 0, false));
        result = cortex.maxMinSets(result, cortex.getTargetTokens(result_bi.keySet(), 1, true));
        result = cortex.maxMinSets(result, cortex.getTargetTokens(result_b.keySet(), 1, false));

        return result;
    }

    public Map<Integer,Double> getSynonymsSlower2(Integer t) {

        final Region region = cortex.getRegion();

        Map<Integer,Double> result = new HashMap<Integer,Double>();
        Map<Integer,Double> result_a;
        Map<Integer,Double> result_ai;
        Map<Integer,Double> result_b;
        Map<Integer,Double> result_bi;

        result_a = region.fascicle[0].getTargetTokens(t, true);
        result_ai = region.fascicle[0].getTargetTokens(t, false);
        result_b = region.fascicle[1].getTargetTokens(t, true);
        result_bi = region.fascicle[1].getTargetTokens(t, false);

        int counter = 0;

        if ((result_a != null) && (result_ai != null) && (result_b != null) && (result_bi != null)) {
            for (Map.Entry<Integer,Double> et1 : result_bi.entrySet()) {
                for (Map.Entry<Integer,Double> et2 : result_ai.entrySet()) {
                    for (Map.Entry<Integer,Double> et3 : result_a.entrySet()) {
                        for (Map.Entry<Integer,Double> et4 : result_b.entrySet()) {
                            counter++;
                            Integer[] question = new Integer[5];

                            question[0] = et1.getKey();
                            question[1] = et2.getKey();
                            question[2] = NULL_INDEX;
                            question[3] = et3.getKey();
                            question[4] = et4.getKey();
                            Map<Integer,Double>[] resultSubset = cortex.getConsensusALLList(question);

                            if (resultSubset[2] != null) {
                                if (!resultSubset[2].isEmpty()) {
                                    result = cortex.maxMinSets(result, resultSubset[2]);
                                }
                            }

                        }
                    }
                }
            }
        }
        return result;
    }

    /*
    public Set<Token<Integer>> getPara(Integer t) {

    final Region region = cortex.getRegion();

    Set<Token<String>> result = new TreeSet<Token<String>>();
    SortedSet<ExcitedIndex<Integer>> semiresult1 = new TreeSet<ExcitedIndex<Integer>>();
    SortedSet<ExcitedIndex<Integer>> semiresult2 = new TreeSet<ExcitedIndex<Integer>>();
    SortedSet<ExcitedIndex<Integer>>[] subsets = new TreeSet[4];

    subsets[0] = region.fascicle[1].getTargetTokensOld(t, false);
    subsets[1] = region.fascicle[0].getTargetTokensOld(t, false);
    subsets[2] = region.fascicle[0].getTargetTokensOld(t, true);
    subsets[3] = region.fascicle[1].getTargetTokensOld(t, true);

    for (int i = 0; i < 4; i++) {
    System.out.println(i + " = " + subsets[i].size());
    }

    int c_d = (subsets[0].size() / 20) == 0 ? 1 : (subsets[0].size() / 20);
    int c = subsets[0].size();

    for (ExcitedIndex<Integer> e0 : subsets[0]) {
    c--;
    for (ExcitedIndex<Integer> e1 : subsets[1]) {
    if (cortex.hasAssociation(0, e0.getValue(), e1.getValue())) {
    for (ExcitedIndex<Integer> e2 : subsets[2]) {
    if (cortex.hasAssociation(2, e1.getValue(), e2.getValue())) {
    if (cortex.hasAssociation(3, e0.getValue(), e2.getValue())) {
    for (ExcitedIndex<Integer> e3 : subsets[3]) {
    if (cortex.hasAssociation(0, e2.getValue(), e3.getValue())) {
    if (cortex.hasAssociation(3, e1.getValue(), e3.getValue())) {

    semiresult1 = new TreeSet<ExcitedIndex<Integer>>();
    semiresult1 = region.getFascicle(1).getTargetTokensOld(e0.getValue(), true);
    semiresult1 = cortex.joinExcitedSetsOld(semiresult1, region.getFascicle(0).getTargetTokensOld(e1.getValue(), true), 0);
    semiresult1 = cortex.joinExcitedSetsOld(semiresult1, region.getFascicle(1).getTargetTokensOld(e2.getValue(), false), 0);
    semiresult1 = cortex.joinExcitedSetsOld(semiresult1, region.getFascicle(2).getTargetTokensOld(e3.getValue(), false), 0);

    semiresult2 = new TreeSet<ExcitedIndex<Integer>>();
    semiresult2 = region.getFascicle(2).getTargetTokensOld(e0.getValue(), true);
    semiresult2 = cortex.joinExcitedSetsOld(semiresult2, region.getFascicle(1).getTargetTokensOld(e1.getValue(), true), 0);
    semiresult2 = cortex.joinExcitedSetsOld(semiresult2, region.getFascicle(0).getTargetTokensOld(e2.getValue(), false), 0);
    semiresult2 = cortex.joinExcitedSetsOld(semiresult2, region.getFascicle(1).getTargetTokensOld(e3.getValue(), false), 0);

    for (ExcitedIndex<Integer> esr1 : semiresult1) {
    for (ExcitedIndex<Integer> esr2 : semiresult2) {
    if (cortex.hasAssociation(0, esr1.getValue(), esr2.getValue())) {
    result.add(new Token<String>((esr1.getValue()) + " " + (esr2.getValue())));
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    return result;
    }
     */
}
