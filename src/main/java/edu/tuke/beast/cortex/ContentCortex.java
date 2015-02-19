/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.cortex;

import edu.tuke.beast.association.Association;
import edu.tuke.beast.association.NewAssociation;
import edu.tuke.beast.lexicon.Lexicon;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author vrockai
 */
public class ContentCortex extends UpperCortex{
    
    private int window_size = 5;
    private int null_index = Lexicon.NULL_INDEX;

    public ContentCortex(Cortex c) {
        super(c);
    }

    public double getAssociationSimilarity(List<Integer> input1, List<Integer> input2) {
        double result = 0;

        Map<NewAssociation, Integer> m1 = getAssociations(input1);
        Map<NewAssociation, Integer> m2 = getAssociations(input2);

        result = getAssociationSimilarity(m1, m2);

        return result;
    }

    public double getAssociationSimilarity(Map<NewAssociation, Integer> m1, Map<NewAssociation, Integer> m2) {
        double result = 0;

        Set<NewAssociation> k1 = m1.keySet();
        Set<NewAssociation> k2 = m2.keySet();

        Set<NewAssociation> union = new HashSet<NewAssociation>(k1);
        union.addAll(k2);
        Set<NewAssociation> intersection = new HashSet<NewAssociation>(k1);
        intersection.retainAll(k2);

        if (intersection.isEmpty()) {
            return 0;
        }

        result = (double) intersection.size() / (double) union.size();

        return result;
    }

    public Map<NewAssociation, Integer> getAssociations(List<Integer> input) {

        Map<NewAssociation, Integer> result = new HashMap<NewAssociation, Integer>();

        // initialise token window
        // add 5x # after input
        int[] window = new int[window_size];


        for (Integer t : input) {

            // add token t at the end of the window
            System.arraycopy(window, 1, window, 0, window_size - 1);

            window[window_size - 1] = t;

            if (window[0] != null_index) {
                Set<NewAssociation> window_asoc = getAssociations(window);

                for (NewAssociation na : window_asoc) {
                    if (result.containsKey(na)) {
                        result.put(na, result.get(na) + 1);
                    } else {
                        result.put(na, 1);
                    }
                }
            }
        }

        return result;
    }

    public Map<NewAssociation, Integer> getContexts(List<Integer> input) {

        Map<NewAssociation, Integer> result = new HashMap<NewAssociation, Integer>();

        return result;
    }

    private Set<NewAssociation> getAssociations(int[] window) {
        Set<NewAssociation> result = new HashSet<NewAssociation>();

        int source = window[0];
        for (int i = 1; i < window_size; i++) {
            if (window[i] == null_index) {
                break;
            }
            Association<Integer> a = cortex.getAssociation(i - 1, source, window[i]);
            if (a != null) {
                result.add(new NewAssociation(source, window[i], i - 1, a.getStrength()));
            }
        }

        return result;
    }
}
