/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tuke.beast.cortex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author vrockai
 *
 * For computing similar words in context
 */
public class RelationshipCortex extends UpperCortex {

    final SimilarityCortex sc;
    final ContextCortex cc;

    public RelationshipCortex(Cortex c) {
        super(c);

        sc = new SimilarityCortex(cortex);
        cc = new ContextCortex(cortex);
    }


    /**
     *
     * @param input - input word
     * @param context - input context
     * @param lexicon - lexicon to scan
     * @return List of Vectors of this format (int word, double similarity, double context, double computationalResults...)
     */
    /*
    public List<Vector<Number>> getRelationship(Integer input, Integer context, Set<Integer> lexicon){
        List<Vector<Number>> result = new ArrayList<Vector<Number>>();

        double context_treshold = cc.getRelatives(input, context);

        double sim;
        double con;
        double cr1, cr2, cr3;

        for (Integer candidate : lexicon){
            Vector<Number> row = new Vector<Number>();
            sim = 0;//sc.getSimilarity(input, candidate, 0);
            con = cc.getRelatives(input, candidate);
            cr1 = sim/con;
            cr2 = con - context_treshold;
            cr3 = con/sim;

            row.add(sim);
            row.add(con);
            row.add(cr1);
            row.add(cr2);
            row.add(cr3);
            result.add(row);
        }

        return result;
    }
*/
}
