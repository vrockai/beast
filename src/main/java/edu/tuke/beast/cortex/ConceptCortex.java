/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.cortex;

import edu.tuke.beast.fascicle.Fascicle;
import edu.tuke.util.BeastMath;
import java.beans.PropertyChangeListener;
import java.util.Vector;

/**
 *
 * @author vrockai
 */
public class ConceptCortex extends UpperCortex{

    private double treshold = 0.2;

    public double getTreshold() {
        return treshold;
    }

    public void setTreshold(double treshold) {
        this.treshold = treshold;
    }

    int status = 0;

    public ConceptCortex(Cortex c) {
     super(c);
    }

    public boolean isConcept(int a, int b) {
        return isConcept(a, b, 0);
    }

    public boolean isConcept(int a, int b, int i) {

        return getConceptStrenght(a, b, i) > treshold;

    }

    public double getConceptStrenght(int a, int b) {
        return getConceptStrenght(a, b, 0);
    }

    public double getConceptStrenght(int a, int b, int i) {

        double res = 0;
        double res2 = 0;

        Fascicle f = cortex.getRegion().getFascicle(i);

        double pab = f.getProb(a, b);
        double pa = f.getProb(a);
        double pb = f.getProb2(b);

        res = BeastMath.mutInf(pab, pa, pb);
        res2 = (BeastMath.inf(pa) + BeastMath.inf(pb)) / 2;

        double result = res2 == 0 ? 0 : res / res2;


        return result;
    }

    public int getStrongest(Vector<Integer> sentence) {

        int size = sentence.size();
        int result = -1;

        if (size < 1) {
            return -1;
        }

        for (int i = 0; i < size - 1; i++) {
        }

        return result;
    }

    public String getSentence(Vector<Integer> input) {

        StringBuilder sb = new StringBuilder();

        int s = input.size();

        if (s > 1) {
            Fascicle f = cortex.getRegion().getFascicle(0);

            for (int i = 0; i < s - 1; i++) {
                Integer t1 = input.get(i);
                Integer t2 = input.get(i + 1);
                
                sb.append(t1);
                sb.append(f.getWeigth(t1, t2) > f.getWeigth(t2, t1) ? " -> " : " <- ");
            }
            sb.append(input.get(s - 1));
        }

        return sb.toString();
    }
}
