/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.cortex;

import edu.tuke.beast.Region;
import edu.tuke.beast.fascicle.Fascicle;
import edu.tuke.beast.fascicle.storage.CommonMatrixDouble;
import edu.tuke.beast.lexicon.Lexicon;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.SizeLimitExceededException;

/**
 *
 * @author rockaiv
 */
public class SimilarityCortex extends UpperCortex {

    //Map<Integer, Map<Integer, Double>> cache = new HashMap<Integer, Map<Integer, Double>>();
    private CommonMatrixDouble cache;

    public static enum Strategy {

        INTERSECTION, WEIGHTED, SIG, WEI2
    }
    private double gamma = 1.0;

    public double getEta() {
        return gamma;
    }

    public void setGamma(Double gamma) {

        if (gamma == null) {
            return;
        }

        this.gamma = gamma;

        logger.debug("Gamma set to :" + this.gamma);
    }
    private int status = 0;
    private Strategy strategy = Strategy.INTERSECTION;

    public Strategy getStrategy() {
        return strategy;
    }

    public void setTreshold(double treshold) {
        cache = new CommonMatrixDouble(cortex.getRegion().getFascicle(0).getMatrixSize());
        cortex.setTreshold(treshold);
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
        System.out.println("Strategy set " + strategy);
    }

    public SimilarityCortex(Cortex c) {
        super(c);
        cache = new CommonMatrixDouble(c.getRegion().getFascicle(0).getMatrixSize());
    }

    private double getSimilarityMultiInter(Set<Integer> phrase_a, Integer phrase_b, int fascInd, boolean dir) {


        double ss = phrase_a.size();
        Fascicle f = cortex.getRegion().getFascicle(fascInd);

        Set<Integer> ac = new HashSet<Integer>(f.getTargetTokens(phrase_a.iterator().next(), dir).keySet());

        for (Integer a : phrase_a) {
            Set<Integer> a1 = f.getTargetTokens(a, dir).keySet();
            ac.retainAll(a1);
        }

        Set<Integer> b1 = f.getTargetTokens(phrase_b, dir).keySet();

        Set<Integer> intersection = new HashSet<Integer>(ac);
        Set<Integer> union = new HashSet<Integer>(ac);

        union.addAll(b1);
        intersection.retainAll(b1);

        if (union.isEmpty() || intersection.isEmpty()) {
            return 0;
        }

        if (strategy == Strategy.WEIGHTED) {

            if (intersection.isEmpty()) {
                return 0;
            }

            Set<Integer> aset = new HashSet();
            aset.addAll(phrase_a);

            Integer t2 = phrase_b;

            double z1 = 0;
            double z2 = 0;

            for (Integer in : intersection) {
                if (dir) {
                    double sz1 = 0;
                    sz1 += f.getWeigth(aset, in);
                    z1 += f.getWeigth(t2, in) + (sz1 / ss);
                } else {
                    double sz1 = 0;
                    sz1 += f.getWeigthInv(in, aset);
                    z1 += f.getWeigthInv(in, t2) + (sz1 / ss);
                }
            }

            for (Integer in : union) {
                if (dir) {
                    double sz2 = 0;
                    sz2 += f.getWeigth(aset, in);
                    z2 += f.getWeigth(t2, in) + (sz2 / ss);
                } else {
                    double sz2 = 0;
                    sz2 += f.getWeigthInv(in, aset);
                    z2 += f.getWeigthInv(in, t2) + (sz2 / ss);
                }
            }
            //System.out.println(t11+", "+t12+", "+t13+", "+t2 + " | "+z1+", "+z2+"|");
            if (z2 == 0) {
                return 0;
            }

            //System.out.println("z1: "+z1+", z2: "+z2+", z: "+z1 / z2);
            return z1 / z2;


        }

        return (double) intersection.size() / (double) union.size();

    }

    private double getSimilarityInter(Vector<Integer> phrase_a, Vector<Integer> phrase_b, int fascInd, boolean dir) {

        Fascicle f = cortex.getRegion().getFascicle(fascInd);

        //Set<Integer> a1 = f.getAssociations(phrase_a.get(0), dir).keySet();
        //Set<Integer> b1 = f.getAssociations(phrase_b.get(0), dir).keySet();

        Set<Integer> a1 = f.getTargetTokens(phrase_a.get(0), dir).keySet();
        Set<Integer> b1 = f.getTargetTokens(phrase_b.get(0), dir).keySet();

        Set<Integer> intersection = new HashSet<Integer>(a1);
        Set<Integer> union = new HashSet<Integer>(a1);

        union.addAll(b1);
        intersection.retainAll(b1);

        if (union.isEmpty()) {
            return 0;
        }

        if (strategy == Strategy.SIG) {
            Integer t1 = phrase_a.get(0);
            Integer t2 = phrase_b.get(0);

            double z1 = 0;
            double z2 = 0;

            for (Integer in : intersection) {
                if (dir) {
                    z1 += f.getSignificance(t1, in);
                    z1 += f.getSignificance(t2, in);
                } else {
                    z1 += f.getSignificance(in, t1);
                    z1 += f.getSignificance(in, t2);
                }

            }
            for (Integer in : union) {
                if (dir) {
                    z2 += f.getSignificance(t1, in);
                    z2 += f.getSignificance(t2, in);
                } else {
                    z2 += f.getSignificance(in, t1);
                    z2 += f.getSignificance(in, t2);
                }
            }

            return z1 / z2;
        }

        if (strategy == Strategy.WEI2) {
            Integer t1 = phrase_a.get(0);
            Integer t2 = phrase_b.get(0);

            double z1 = 0;
            double z2 = 0;

            for (Integer in : intersection) {
                if (dir) {
                    z1 += f.getWeigthInv(t1, in);
                    z1 += f.getWeigthInv(t2, in);
                } else {
                    z1 += f.getWeigth(in, t1);
                    z1 += f.getWeigth(in, t2);
                }

            }
            for (Integer in : union) {
                if (dir) {
                    z2 += f.getWeigthInv(t1, in);
                    z2 += f.getWeigthInv(t2, in);
                } else {
                    z2 += f.getWeigth(in, t1);
                    z2 += f.getWeigth(in, t2);
                }
            }

            return z1 / z2;
        }

        if (strategy == Strategy.WEIGHTED) {
            Integer t1 = phrase_a.get(0);
            Integer t2 = phrase_b.get(0);

            double z1 = 0;
            double z2 = 0;

            for (Integer in : intersection) {
                if (dir) {
                    z1 += f.getWeigth(t1, in);
                    z1 += f.getWeigth(t2, in);
                } else {
                    z1 += f.getWeigthInv(in, t1);
                    z1 += f.getWeigthInv(in, t2);
                }

            }
            for (Integer in : union) {
                if (dir) {
                    z2 += f.getWeigth(t1, in);
                    z2 += f.getWeigth(t2, in);
                } else {
                    z2 += f.getWeigthInv(in, t1);
                    z2 += f.getWeigthInv(in, t2);
                }
            }

            if (z2 == 0) {
                return 0;
            }

            return z1 / z2;
        }

        return ((double) intersection.size() / (double) union.size());
    }

    private double getSimilarityInter(Integer phrase_a, Integer phrase_b, int fascInd, boolean dir) {

        Fascicle f = cortex.getRegion().getFascicle(fascInd);

        Set<Integer> a1 = f.getTargetTokens(phrase_a, dir).keySet();
        Set<Integer> b1 = f.getTargetTokens(phrase_b, dir).keySet();

        Set<Integer> intersection = new HashSet<Integer>(a1);
        Set<Integer> union = new HashSet<Integer>(a1);

        union.addAll(b1);
        intersection.retainAll(b1);

        if (union.isEmpty()) {
            return 0;
        }

        if (strategy == Strategy.SIG) {
            Integer t1 = phrase_a;
            Integer t2 = phrase_b;

            double z1 = 0;
            double z2 = 0;

            for (Integer in : intersection) {
                if (dir) {
                    z1 += f.getSignificance(t1, in);
                    z1 += f.getSignificance(t2, in);
                } else {
                    z1 += f.getSignificance(in, t1);
                    z1 += f.getSignificance(in, t2);
                }

            }
            for (Integer in : union) {
                if (dir) {
                    z2 += f.getSignificance(t1, in);
                    z2 += f.getSignificance(t2, in);
                } else {
                    z2 += f.getSignificance(in, t1);
                    z2 += f.getSignificance(in, t2);
                }
            }

            return z1 / z2;
        }

        if (strategy == Strategy.WEI2) {
            Integer t1 = phrase_a;
            Integer t2 = phrase_b;

            double z1 = 0;
            double z2 = 0;

            for (Integer in : intersection) {
                if (dir) {
                    z1 += f.getWeigthInv(t1, in);
                    z1 += f.getWeigthInv(t2, in);
                } else {
                    z1 += f.getWeigth(in, t1);
                    z1 += f.getWeigth(in, t2);
                }

            }
            for (Integer in : union) {
                if (dir) {
                    z2 += f.getWeigthInv(t1, in);
                    z2 += f.getWeigthInv(t2, in);
                } else {
                    z2 += f.getWeigth(in, t1);
                    z2 += f.getWeigth(in, t2);
                }
            }

            return z1 / z2;
        }

        if (strategy == Strategy.WEIGHTED) {
            Integer t1 = phrase_a;
            Integer t2 = phrase_b;

            double z1 = 0;
            double z2 = 0;

            for (Integer in : intersection) {
                if (dir) {
                    z1 += f.getWeigth(t1, in);
                    z1 += f.getWeigth(t2, in);
                } else {
                    z1 += f.getWeigthInv(in, t1);
                    z1 += f.getWeigthInv(in, t2);
                }

            }
            for (Integer in : union) {
                if (dir) {
                    z2 += f.getWeigth(t1, in);
                    z2 += f.getWeigth(t2, in);
                } else {
                    z2 += f.getWeigthInv(in, t1);
                    z2 += f.getWeigthInv(in, t2);
                }
            }

            if (z2 == 0) {
                return 0;
            }

            return z1 / z2;
        }

        return ((double) intersection.size() / (double) union.size());
    }

    private Set<Integer> getEnviroment(Vector<Integer> phrase, int distance, boolean direction) {

        Region region = cortex.getRegion();
        Set<Integer> result = new HashSet<Integer>();
        final int size = phrase.size();

        if (cortex.isNgramConsensus(phrase) != size) {
            return result;
        }

        int d = 4 - distance;
        int d2 = Math.min(size - 1, d);

        if (!direction) {
            for (int i = 0; i < d2; i++) {
                Fascicle fascicle = region.getFascicle(i + distance);

                Set<Integer> ss = fascicle.getNeighbourghs(phrase.get(i), direction);
                if (i == 0) {
                    result = ss;
                } else {
                    result.retainAll(ss);
                }
            }
        }

        if (direction) {
            for (int i = size - 1; i >= size - 1 - d2; i--) {
                Fascicle fascicle = region.getFascicle(size - 1 - i + distance);

                Set<Integer> ss = fascicle.getNeighbourghs(phrase.get(i), direction);
                if (i == size - 1) {
                    result = ss;
                } else {
                    result.retainAll(ss);
                }
            }
        }
        return result;
    }

    public Map<Integer, Double> getRelatives(Integer phrase, Set<Integer> set, double w1, double w2, double w3, double w4) {
        Vector<Integer> pv = new Vector<Integer>();
        Set<Vector<Integer>> lset = new HashSet<Vector<Integer>>();

        pv.add(phrase);

        for (Integer t : set) {
            Vector<Integer> v = new Vector<Integer>();
            v.add(t);
            lset.add(v);
        }

        Map<Vector<Integer>, Double> rvm = getRelatives(pv, lset, w1, w2, w3, w4);
        Map<Integer, Double> rm = new HashMap<Integer, Double>();

        for (Map.Entry<Vector<Integer>, Double> e : rvm.entrySet()) {
            rm.put(e.getKey().firstElement(), e.getValue());
        }

        return rm;
    }

    public double getRelativesMulti(Set<Integer> phrase, Integer set, double w1, double w2, double w3, double w4) {


        double wsum = w1 + w2 + w3 + w4;
        double sd = 0;
        double si = 0;
        for (int i = 0; i < 4; i++) {
            sd += getSimilarityMultiInter(phrase, set, i, true);
            si += getSimilarityMultiInter(phrase, set, i, false);
        }

        return (sd + si) / wsum;
    }

    public Map<Integer, Double> getRelativesMulti(Set<Integer> phrase, Set<Integer> set, double w1, double w2, double w3, double w4) {

        Map<Integer, Double> semiMap = new HashMap<Integer, Double>();

        for (Integer t : set) {
            double sd = 0;
            double si = 0;
            for (int i = 0; i < 4; i++) {
                sd += getSimilarityMultiInter(phrase, t, i, true);
                si += getSimilarityMultiInter(phrase, t, i, false);
            }
            semiMap.put(t, sd + si);
        }

        return semiMap;
    }

    public double getDistance(Integer t1, Integer t2, double w1, double w2, double w3, double w4) {

        double precache = cache.get(t1, t2);
        if (precache != -1.) {
            return precache;
        }

        precache = cache.get(t2, t1);
        if (precache != -1.) {
            return precache;
        }

        /*
         if (cache.containsKey(t1)) {
         if (cache.get(t1).containsKey(t2)) {
         return cache.get(t1).get(t2);
         }
         }
         if (cache.containsKey(t2)) {
         if (cache.get(t2).containsKey(t1)) {
         return cache.get(t2).get(t1);
         }
         }
         */
        //Map<Integer, Double> semiMap = new HashMap<Integer, Double>();
        //cache.put(t1, semiMap);

        Vector<Integer> phrase1 = new Vector<Integer>();
        Vector<Integer> phrase2 = new Vector<Integer>();
        phrase1.add(t1);
        phrase2.add(t2);

        int g = 0;

        if (w1 != 0) {
            g += w1;
        }
        if (w2 != 0) {
            g += w2;
        }
        if (w3 != 0) {
            g += w3;
        }
        if (w4 != 0) {
            g += w4;
        }


        double s1 = w1 > 0 ? w1 * (1 - getSimilarityInter(phrase1, phrase2, 0, true)) : 0;
        s1 += w1 > 0 ? w1 * (1 - getSimilarityInter(phrase1, phrase2, 0, false)) : 0;
        double s2 = w2 > 0 ? w2 * (1 - getSimilarityInter(phrase1, phrase2, 1, true)) : 0;
        s2 += w2 > 0 ? w2 * (1 - getSimilarityInter(phrase1, phrase2, 1, false)) : 0;
        double s3 = w3 > 0 ? w3 * (1 - getSimilarityInter(phrase1, phrase2, 2, true)) : 0;
        s3 += w3 > 0 ? w3 * (1 - getSimilarityInter(phrase1, phrase2, 2, false)) : 0;
        double s4 = w4 > 0 ? w4 * (1 - getSimilarityInter(phrase1, phrase2, 3, true)) : 0;
        s4 += w4 > 0 ? w4 * (1 - getSimilarityInter(phrase1, phrase2, 3, false)) : 0;

        double s = (s1 + s2 + s3 + s4) / (2 * g);
        try {
            //cache.get(t1).put(t2, s);
            cache.set(t1, t2, s);
        } catch (SizeLimitExceededException ex) {
            Logger.getLogger(SimilarityCortex.class.getName()).log(Level.SEVERE, null, ex);
        }

        return s;
    }

    public double getRelatives(Integer t1, Integer t2, double w1, double w2, double w3, double w4) {

        double precache = cache.get(t1, t2);
        if (precache != -1.) {
            return precache;
        }

        precache = cache.get(t2, t1);
        if (precache != -1.) {
            return precache;
        }
        /*
         if (cache.containsKey(t1)) {
         if (cache.get(t1).containsKey(t2)) {
         return cache.get(t1).get(t2);
         }
         } else {
         Map<Integer, Double> semiMap = new HashMap<Integer, Double>();
         cache.put(t1, semiMap);
         }
         if (cache.containsKey(t2)) {
         if (cache.get(t2).containsKey(t1)) {
         return cache.get(t2).get(t1);
         }
         }
         */

        double g = w1 + w2 + w3 + w4;

        double s1 = w1 > 0 ? w1 * getSimilarityInter(t1, t2, 0, true) : 0;
        s1 += w1 > 0 ? w1 * getSimilarityInter(t1, t2, 0, false) : 0;
        double s2 = w2 > 0 ? w2 * getSimilarityInter(t1, t2, 1, true) : 0;
        s2 += w2 > 0 ? w2 * getSimilarityInter(t1, t2, 1, false) : 0;
        double s3 = w3 > 0 ? w3 * getSimilarityInter(t1, t2, 2, true) : 0;
        s3 += w3 > 0 ? w3 * getSimilarityInter(t1, t2, 2, false) : 0;
        double s4 = w4 > 0 ? w4 * getSimilarityInter(t1, t2, 3, true) : 0;
        s4 += w4 > 0 ? w4 * getSimilarityInter(t1, t2, 3, false) : 0;

        double s = (s1 + s2 + s3 + s4) / (2 * g);
        try {
            cache.set(t1, t2, s);
        } catch (SizeLimitExceededException ex) {
            Logger.getLogger(SimilarityCortex.class.getName()).log(Level.SEVERE, null, ex);
        }

        return s;
    }

    public Map<Vector<Integer>, Double> getRelatives(Vector<Integer> phrase, Set<Vector<Integer>> set, double w1, double w2, double w3, double w4) {
        logger.debug("Starting geting relatives in cortex");
        Map<Vector<Integer>, Double> result = new HashMap<Vector<Integer>, Double>();

        this.status = 0;
        int size = set.size();
        int c = size;
        double step = 100 / (double) size;
        double delta = 0;

        this.status++;

        for (Vector<Integer> t2 : set) {

            // percentage
            c--;
            if (delta > 1) {
                delta -= computeProgress(delta);
            }
            delta += step;
            // computing synonyms
            double s1 = w1 > 0 ? w1 * getSimilarityInter(phrase, t2, 0, true) : 0;
            s1 += w1 > 0 ? w1 * getSimilarityInter(phrase, t2, 0, false) : 0;
            double s2 = w2 > 0 ? w2 * getSimilarityInter(phrase, t2, 1, true) : 0;
            s2 += w2 > 0 ? w2 * getSimilarityInter(phrase, t2, 1, false) : 0;
            double s3 = w3 > 0 ? w3 * getSimilarityInter(phrase, t2, 2, true) : 0;
            s3 += w3 > 0 ? w3 * getSimilarityInter(phrase, t2, 2, false) : 0;
            double s4 = w4 > 0 ? w4 * getSimilarityInter(phrase, t2, 3, true) : 0;
            s4 += w4 > 0 ? w4 * getSimilarityInter(phrase, t2, 3, false) : 0;

            double s = (s1 + s2 + s3 + s4) / 2;
            if (s > 0) {
                result.put(t2, s);
            }
        }
        logger.debug("Stoping geting relatives in cortex");

        return result;
    }

    public Map<Vector<Integer>, Double> getRelativesInContext(Vector<Integer> phrase, Strategy relativesStrategy, Vector<Integer> context, ContextCortex.Strategy contextStrategy, Set<Vector<Integer>> lex, double w1, double w2, double w3, double w4) {

        logger.debug("Starting geting relatives in cortex");
        logger.debug("REGION: " + cortex.getRegion());

        Map<Vector<Integer>, Double> result = new HashMap<Vector<Integer>, Double>();

        ContextCortex conCor = new ContextCortex(cortex);

        boolean isContextFound = true;
        if (context != null) {
            for (Integer w : context) {
                isContextFound = !w.equals(Lexicon.NULL_INDEX);
            }
        } else {
            isContextFound = false;
        }

        logger.debug("isContextFound" + isContextFound);
        this.status = 0;
        int size = lex.size();
        int c = size;
        double step = 100 / (double) size;
        double delta = 0;

        this.status++;

        for (Vector<Integer> t2 : lex) {

            // percentage
            c--;
            if (delta > 1) {
                delta -= computeProgress(delta);
            }
            delta += step;
            // computing synonyms
            double s1 = w1 > 0 ? w1 * getSimilarityInter(phrase, t2, 0, true) : 0;
            s1 += w1 > 0 ? w1 * getSimilarityInter(phrase, t2, 0, false) : 0;
            double s2 = w2 > 0 ? w2 * getSimilarityInter(phrase, t2, 1, true) : 0;
            s2 += w2 > 0 ? w2 * getSimilarityInter(phrase, t2, 1, false) : 0;
            double s3 = w3 > 0 ? w3 * getSimilarityInter(phrase, t2, 2, true) : 0;
            s3 += w3 > 0 ? w3 * getSimilarityInter(phrase, t2, 2, false) : 0;
            double s4 = w4 > 0 ? w4 * getSimilarityInter(phrase, t2, 3, true) : 0;
            s4 += w4 > 0 ? w4 * getSimilarityInter(phrase, t2, 3, false) : 0;

            double s = (s1 + s2 + s3 + s4) / 2;
            double c_str = isContextFound ? gamma * conCor.getContextStrength(t2, context, contextStrategy) : 1.;
            s *= c_str;

            logger.trace(t2 + "," + s1 + ',' + s2 + ',' + s3 + ',' + s4);
            if (s > 0) {
                result.put(t2, s);
            }
        }
        logger.debug("Stoping geting relatives in cortex");
        return result;
    }

    public Map<Integer, Double> getRelativesInContext(Integer phrase, Integer t2, Strategy relativesStrategy, Integer context, ContextCortex.Strategy contextStrategy, double w1, double w2, double w3, double w4) {

        logger.debug("Starting geting relatives in cortex");
        logger.debug("REGION: " + cortex.getRegion());

        Map<Integer, Double> result = new HashMap<Integer, Double>();

        ContextCortex conCor = new ContextCortex(cortex);

        boolean isContextFound = true;

        logger.debug("isContextFound" + isContextFound);

        // computing synonyms
        double s1 = w1 > 0 ? w1 * getSimilarityInter(phrase, t2, 0, true) : 0;
        s1 += w1 > 0 ? w1 * getSimilarityInter(phrase, t2, 0, false) : 0;
        double s2 = w2 > 0 ? w2 * getSimilarityInter(phrase, t2, 1, true) : 0;
        s2 += w2 > 0 ? w2 * getSimilarityInter(phrase, t2, 1, false) : 0;
        double s3 = w3 > 0 ? w3 * getSimilarityInter(phrase, t2, 2, true) : 0;
        s3 += w3 > 0 ? w3 * getSimilarityInter(phrase, t2, 2, false) : 0;
        double s4 = w4 > 0 ? w4 * getSimilarityInter(phrase, t2, 3, true) : 0;
        s4 += w4 > 0 ? w4 * getSimilarityInter(phrase, t2, 3, false) : 0;

        double s = (s1 + s2 + s3 + s4) / 2;
        double c_str = isContextFound ? gamma * conCor.getContextStrength(t2, context, contextStrategy) : 1.;
        s *= c_str;

        logger.trace(t2 + "," + s1 + ',' + s2 + ',' + s3 + ',' + s4);
        if (s > 0) {
            result.put(t2, s);
        }

        logger.debug("Stoping geting relatives in cortex");
        return result;
    }

    private int computeProgress(double delta) {
        int tr = (int) Math.floor(delta);
        for (int k = 0; k < tr; k++) {
            this.status++;
            if (propertyChangeListener != null) {
                PropertyChangeEvent pce = new PropertyChangeEvent(this, "status", this.status - 1, this.status);
                propertyChangeListener.propertyChange(pce);
            }
        }
        return tr;
    }
}
