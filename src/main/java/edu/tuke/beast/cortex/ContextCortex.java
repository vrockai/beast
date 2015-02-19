/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.cortex;

import edu.tuke.beast.fascicle.Fascicle;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

/**
 *
 * @author vrockai
 */
public class ContextCortex extends UpperCortex {

    public static enum Strategy {

        SUM, SIG, WEI,WEIINV, WEIAVG, WEIMIN, WEIMAX, EXP1, EXP2, EXP3
    }

    public ContextCortex(Cortex c) {
        super(c);
    }

    public double getContextStrength(Vector<Integer> phrase, Vector<Integer> context, Strategy strategy) {

        double result = 0d;

        switch (strategy) {
            case SUM:
                result = getContextStrengthSUM(phrase, context);
                break;
            case SIG:
                result = getContextStrengthSIG(phrase, context);
                break;
            case WEI:
                result = getContextStrengthWEI(phrase, context);
                break;
            case WEIINV:
                result = getContextStrengthWEIINV(phrase, context);
                break;
            case WEIAVG:
                result = getContextStrengthWEIAVG(phrase, context);
                break;
            case WEIMIN:
                result = getContextStrengthWEIMIN(phrase, context);
                break;
            case WEIMAX:
                result = getContextStrengthWEIMAX(phrase, context);
                break;

            case EXP1:
                result = getContextStrengthEXP1(phrase, context);
                break;
            case EXP2:
                result = getContextStrengthEXP2(phrase, context);
                break;
            case EXP3:
                result = getContextStrengthEXP3(phrase, context);
                break;
            default:
                result = 0d;
                break;
        }

        return result;
    }

    public double getContextStrength(Integer t1, Integer t2, Strategy strategy) {

        double result = 0d;


        int size = cortex.getRegion().getSize();
        double step = 0.5d/(double)size;

        for (int j = 0; j < size; j++) {
            Fascicle f = cortex.getRegion().getFascicle(j);
            result += f.isAssociation(t1, t2) ? step : 0;
            result += f.isAssociation(t2, t1) ? step : 0;
        }

        
        return result;
    }
    
   private double getContextStrengthSUM(Vector<Integer> phrase, Vector<Integer> context) {

        double result = 0;
        Integer t1 = phrase.get(0);
        Integer t2 = context.get(0);
        int size = cortex.getRegion().getSize();
        double step = 1d/(double)size;

        for (int j = 0; j < size; j++) {
            Fascicle f = cortex.getRegion().getFascicle(j);
            result += f.isAssociation(t1, t2) ? step : 0;
            result += f.isAssociation(t2, t1) ? step : 0;
        }

        return result;
    }

   private double getContextStrengthSIG(Vector<Integer> phrase, Vector<Integer> context) {

        double result = 0;
        Integer t1 = phrase.get(0);
        Integer t2 = context.get(0);

        //int j=2;
        
        for (int j = 0; j < cortex.getRegion().getSize(); j++) {
            Fascicle f = cortex.getRegion().getFascicle(j);
            result += f.isAssociation(t1, t2) ? f.getSignificance(t1, t2) : 0;
            result += f.isAssociation(t2, t1) ? f.getSignificance(t2, t1) : 0;
        }

        return result/8;
    }

   private double getContextStrengthWEIINV(Vector<Integer> phrase, Vector<Integer> context) {


       boolean onlyone = true;
        double result = 1;
        Integer t1 = phrase.get(0);
        Integer t2 = context.get(0);

        for (int j = 0; j < cortex.getRegion().getSize(); j++) {
            Fascicle f = cortex.getRegion().getFascicle(j);
                double w1 = f.isAssociation(t1, t2) ? f.getWeigthInv(t1, t2) : 1;
                double w2 = f.isAssociation(t2, t1) ? f.getWeigth(t2, t1) : 1;

                if(w1 != 1 || w2 != 1)
                    onlyone = false;

                result *= w1;
                result *= w2;
        }

        if ( (onlyone) && (result == 1) )
            result = 0;

        return result;
    }

   private double getContextStrengthWEI(Vector<Integer> phrase, Vector<Integer> context) {


       boolean onlyone = true;
        double result = 1;
        Integer t1 = phrase.get(0);
        Integer t2 = context.get(0);

        for (int j = 0; j < cortex.getRegion().getSize(); j++) {
            Fascicle f = cortex.getRegion().getFascicle(j);
                double w1 = f.isAssociation(t1, t2) ? f.getWeigth(t1, t2) : 1;
                double w2 = f.isAssociation(t2, t1) ? f.getWeigthInv(t2, t1) : 1;

                if(w1 != 1 || w2 != 1)
                    onlyone = false;

                result *= w1;
                result *= w2;
        }

        if ( (onlyone) && (result == 1) )
            result = 0;

        return result;
    }
   
    private double getContextStrengthWEIAVG(Vector<Integer> phrase, Vector<Integer> context) {

        double result = 0;
        Integer t1 = phrase.get(0);
        Integer t2 = context.get(0);

        for (int j = 0; j < cortex.getRegion().getSize(); j++) {
            Fascicle f = cortex.getRegion().getFascicle(j);
            
                result += f.isAssociation(t1, t2) ? f.getWeigth(t1, t2) : 0;
                result += f.isAssociation(t2, t1) ? f.getWeigthInv(t2, t1) : 0;
        }

        return result/cortex.getRegion().getSize()/2.;
    }

    private  double getContextStrengthWEIMIN(Vector<Integer> phrase, Vector<Integer> context) {

        boolean was = false;
        double result = 0;
        Integer t1 = phrase.get(0);
        Integer t2 = context.get(0);

        for (int j = 0; j < cortex.getRegion().getSize(); j++) {
            Fascicle f = cortex.getRegion().getFascicle(j);
            if( f.isAssociation(t1, t2)){             
                double wei = f.getWeigth(t1, t2);
                if (!was){
                    result = wei;
                    was = true;
                }

                if (wei < result){
                    result = wei;
                }
            }
        }

        return result;
    }

    private  double getContextStrengthWEIMAX(Vector<Integer> phrase, Vector<Integer> context) {

        double result = 0;
        Integer t1 = phrase.get(0);
        Integer t2 = context.get(0);

        for (int j = 0; j < cortex.getRegion().getSize(); j++) {
            Fascicle f = cortex.getRegion().getFascicle(j);
            if( f.isAssociation(t1, t2)){
                double wei = f.getWeigth(t1, t2);
                if (wei > result)
                    result = wei;
            }
        }

        return result;
    }



   private double getContextStrengthEXP1(Vector<Integer> phrase, Vector<Integer> context) {

        int t = phrase.get(0);
        int i = context.get(0);

        double s = 0;
        double s2 = 0;
        double s1 = 0;
        double wei = 0;

        for (int j = 0; j < 4; j++) {
            double wei_t = 0;
            Fascicle f = cortex.getRegion().getFascicle(j);

            double coe = 1d;// / (double) (j + 1);

            s1 = f.getSignificance(t, i);
            s2 = f.getSignificance(i, t);

            if (s1 > cortex.getTreshold()) {
                wei_t += coe * f.getWeigth(t, i);// * f.getProb2(i);
            }

            if (s2 > cortex.getTreshold()) {
                wei_t += coe * f.getWeigthInv(i, t);// * f.getProb(i);
            }

            wei += wei_t;// (f.getSourceTokenLearningEvents(i)+f.getTargetTokenLearningEvents(i));
        }

        return wei;
    }

  private  double getContextStrengthEXP2(Vector<Integer> phrase, Vector<Integer> context) {

       int t = phrase.get(0);
        int i = context.get(0);

         double s = 0;
        double s2 = 0;
        double s1 = 0;
        double wei = 0;

        for (int j = 0; j < 4; j++) {
            double wei_t = 0;
            Fascicle f = cortex.getRegion().getFascicle(j);

            double coe = 1d;// / (double) (j + 1);

            s1 = f.getSignificance(t, i);
            s2 = f.getSignificance(i, t);

            if (s1 > cortex.getTreshold()) {
                wei_t += coe * f.getWeigth(t, i);// * f.getProb2(i);
            }

            if (s2 > cortex.getTreshold()) {
                wei_t += coe * f.getWeigth(i, t);// * f.getProb(i);
            }

            wei += wei_t/ (f.getSourceTokenLearningEvents(i)+f.getTargetTokenLearningEvents(i));
        }

        return wei;
    }

   private double getContextStrengthEXP3(Vector<Integer> phrase, Vector<Integer> context) {

        int t = phrase.get(0);
        int i = context.get(0);

         double s = 0;
        double s2 = 0;
        double s1 = 0;
        double wei = 0;

        for (int j = 0; j < 4; j++) {
            double wei_t = 0;
            Fascicle f = cortex.getRegion().getFascicle(j);

            double coe = 1d;// / (double) (j + 1);

            s1 = f.getSignificance(t, i);
            s2 = f.getSignificance(i, t);

            if (s1 > cortex.getTreshold()) {
                wei_t += coe * f.getWeigth(t, i) / f.getProb2(i);
            }

            if (s2 > cortex.getTreshold()) {
                wei_t += coe * f.getWeigth(i, t) / f.getProb(i);
            }

            wei += wei_t / (f.getSourceTokenLearningEvents(i)+f.getTargetTokenLearningEvents(i));
        }

        return wei;
    }

//red,green,blue,purple,brown
    public double getRelatives(int t, int i) {

        double s = 0;
        double s2 = 0;
        double s1 = 0;
        double wei = 0;

        for (int j = 0; j < 4; j++) {
            double wei_t = 0;
            Fascicle f = cortex.getRegion().getFascicle(j);

            double coe = 1d;// / (double) (j + 1);

            s1 = f.getSignificance(t, i);
            s2 = f.getSignificance(i, t);

            if (s1 > cortex.getTreshold()) {
                wei_t += coe * f.getWeigth(t, i);// * f.getProb2(i);
            }

            if (s2 > cortex.getTreshold()) {
                wei_t += coe * f.getWeigth(i, t);// * f.getProb(i);
            }

            wei += wei_t;// (f.getSourceTokenLearningEvents(i)+f.getTargetTokenLearningEvents(i));
        }

        return wei;

    }

    public Map<Integer, Double> getRelatives(int t, Set<Integer> set) {

        Map<Integer, Double> result = new HashMap<Integer, Double>();

        for (Integer i : set) {
            double wei = getRelatives(t, i);
            if (wei > 0) {
                result.put(i, wei);
            }
        }

        return result;
    }

    public Map<Integer, Double> getRelatives(Vector<Integer> t, Set<Integer> set, Strategy str) {

        Map<Integer, Double> result = new HashMap<Integer, Double>();

        for (Integer i : set) {
            Vector<Integer> vi = (new Vector<Integer>(1));
            vi.add(i);
            double wei = getContextStrength(t, vi , str);
            if (wei > 0) {
                result.put(i, wei);
            }
        }

        return result;
    }

    public Set<Integer> getFriends(int t1, int t2, int i) {
        Fascicle f = cortex.getRegion().getFascicle(i);

        Map<Integer, Double> t1s1 = f.getTargetTokens(t1, true);
        Map<Integer, Double> t2s1 = f.getTargetTokens(t2, true);

        Set<Integer> t1f = t1s1.keySet();
        Set<Integer> t2f = t2s1.keySet();

        t1f.retainAll(t2f);

        return t1f;
    }

    public Map<Integer, Double> getRelativesBool(int t1, int t2, Set<Integer> set, int[] wi) {
        Map<Integer, Double> result = new HashMap<Integer, Double>();
        Set<Integer> f0 = getFriends(t1, t2, 0);
        Set<Integer> f1 = getFriends(t1, t2, 1);
        Set<Integer> f2 = getFriends(t1, t2, 2);
        Set<Integer> f3 = getFriends(t1, t2, 3);

        for (Integer i : set) {
            double s = 0;
            double wei = 0;
            Fascicle f = cortex.getRegion().getFascicle(0);

            for (Integer j : f0) {
                if (f.isAssociation(i, j)) {
                    wei++;
                }
            }
            for (Integer j : f1) {
                if (f.isAssociation(i, j)) {
                    wei += 0.5;
                }
            }
            for (Integer j : f2) {
                if (f.isAssociation(i, j)) {
                    wei += 0.25;
                }
            }
            for (Integer j : f3) {
                if (f.isAssociation(i, j)) {
                    wei += 0.125;
                }
            }

            if (wei > 0) {
                result.put(i, wei);
            }
        }

        return result;
    }

    public Map<Integer, Double> getRelativesBool2(int t1, int t2, Set<Integer> set, int[] wi) {

        Map<Integer, Double> result = new HashMap<Integer, Double>();
        Set<Integer> ff[] = new TreeSet[4];

        for (int i = 0; i < 4; i++) {
            ff[i] = getFriends(t1, t2, i);
        }

        int c = set.size();

        for (Integer i : set) {
            double wei = hladanie2(ff, i);
            if (wei > 0) {
                result.put(i, wei);
            }

            System.out.println(c-- + " w");
        }

        return result;
    }

   
    public double hladanie(int t1, int t2, int d) {

        double p = 0;

        Fascicle[] f = new Fascicle[4];
        for (int i = 0; i < 4; i++) {
            f[i] = cortex.getRegion().getFascicle(i);
        }

        Set<Integer> f1 = getFriends(t1, t2, 0);
        for (int i : f1) {
            p += f[0].getSignificance(i, d);
        }

        Set<Integer> f2 = getFriends(t1, t2, 1);
        for (int i : f2) {
            p += f[1].getSignificance(i, d);
        }

        Set<Integer> f3 = getFriends(t1, t2, 2);
        for (int i : f3) {
            p += f[2].getSignificance(i, d);
        }

        Set<Integer> f4 = getFriends(t1, t2, 3);
        for (int i : f4) {
            p += f[3].getSignificance(i, d);
        }

        return p;
    }

    public double hladanie2(Set<Integer>[] ff, int d) {

        double p = 0;

        Fascicle[] f = new Fascicle[4];
        for (int i = 0; i < 4; i++) {
            f[i] = cortex.getRegion().getFascicle(i);
        }

        for (int i : ff[0]) {
            p += f[0].getSignificance(d, i);
        }

        for (int i : ff[1]) {
            p += f[1].getSignificance(d, i);
        }

        for (int i : ff[2]) {
            p += f[2].getSignificance(d, i);
        }

        for (int i : ff[3]) {
            p += f[3].getSignificance(d, i);
        }

        return p;
    }
}
