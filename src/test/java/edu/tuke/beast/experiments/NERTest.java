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
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;

/**
 *
 * @author vrockai
 */
public class NERTest extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();

    public void testNameCluster() throws IOException, ClassNotFoundException, SizeLimitExceededException {

        String cortexString = "/home/blur/full_cortex/cortex";
        Beast beast = new Beast(cortexString);

        String[] input = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};////
        //String[] input = {"monday", "friday", "saturday", "sunday"};
        //String[] input = {"red", "green", "blue", "yellow","pink"};
       
        double tresh = 1.0;

        Cortex cortex = beast.getCortex();
        cortex.setTreshold(tresh);
        Lexicon<String> lexicon = beast.getLexicon();

        Set<Integer> qset = new HashSet<Integer>();

        //Must be in lexicon
        for (String day : input) {
            assertTrue(lexicon.getIndex(day) != Lexicon.NULL_INDEX);
            qset.add(lexicon.getIndex(day));
        }

        System.out.println("tt2");
        foo4(qset, cortex, lexicon, true);
        System.out.println("ft2");
        foo4(qset, cortex, lexicon, false);

        System.out.println("tt");
        foo(qset, cortex, lexicon, true, true);
        System.out.println("ft");
        foo(qset, cortex, lexicon, false, true);

        System.out.println("tf");
        foo(qset, cortex, lexicon, true, false);
        System.out.println("ff");
        foo(qset, cortex, lexicon, false, false);
        
        System.out.println("tt3");
        foo3(qset, cortex, lexicon, true, true);
        System.out.println("ft3");
        foo3(qset, cortex, lexicon, false, true);

        System.out.println("tf3");
        foo3(qset, cortex, lexicon, true, false);
        System.out.println("ff3");
        foo3(qset, cortex, lexicon, false, false);

        System.out.println(cortex.getRegion().getFascicle(0).getWeigth(lexicon.getIndex("january"), lexicon.getIndex("the")));
    }

    public void foo(Set<Integer> qset, Cortex cortex, Lexicon<String> lexicon, boolean wdir, boolean mod) {
        for (int i = 0; i < 4; i++) {
            double aminW = -1;
            int aminT = -1;
            for (Integer t : lexicon.getLexiconSet()) {

                if (t == Lexicon.NULL_INDEX) {
                    continue;
                }

                double minW = -1, semW = 1;
                int minT = 0;

                for (Integer m : qset) {
                    if (!cortex.getRegion().getFascicle(i).isAssociation(m, t)) {
                        semW = 0;
                        minW = -1;
                        continue;
                    }
                    double w = 0;
                    if (mod) {
                        w = wdir ? cortex.getRegion().getFascicle(i).getWeigth(m, t) : cortex.getRegion().getFascicle(i).getWeigth(t, m);
                    } else {
                        w = wdir ? cortex.getRegion().getFascicle(i).getWeigthInv(m, t) : cortex.getRegion().getFascicle(i).getWeigthInv(t, m);
                    }
                    semW += w;



                    if (minW < 0) {
                        minW = w;
                        minT = t;
                    } else {
                        if (w < minW) {
                            minW = w;
                            minT = t;
                        }
                    }

                }

                //minW = semW/qset.size();

                if (minW > aminW) {
                    //System.out.println(lexicon.getEntry(t)+" - "+minW);
                    aminW = minW;
                    aminT = t;
                }

            }

            System.out.println(i + ": " + lexicon.getEntry(aminT) + ": " + aminW);
        }
    }

    public void foo2(Set<Integer> qset, Cortex cortex, Lexicon<String> lexicon, boolean wdir) {
        for (int i = 0; i < 4; i++) {
            double aminW = -1;
            int aminT = -1;
            for (Integer t : lexicon.getLexiconSet()) {

                if (t == Lexicon.NULL_INDEX) {
                    continue;
                }

                double minW = -1, semW = 1;
                int minT = 0;

                for (Integer m : qset) {
                    if (!cortex.getRegion().getFascicle(i).isAssociation(m, t)) {
                        semW = 0;
                        minW = -1;
                        continue;
                    }
                    double w = 0;

                    w = wdir ? cortex.getRegion().getFascicle(i).getSignificance(m, t) : cortex.getRegion().getFascicle(i).getSignificance(t, m);

                    semW += w;


                    /*
                    if (minW < 0) {
                    minW = w;
                    minT = t;
                    } else {
                    if (w < minW) {
                    minW = w;
                    minT = t;
                    }
                    }
                     */
                }

                minW = semW;

                if (minW > aminW) {
                    //System.out.println(lexicon.getEntry(t)+" - "+minW);
                    aminW = minW;
                    aminT = t;
                }

            }

            System.out.println(i + ": " + lexicon.getEntry(aminT) + ": " + aminW);
        }
    }

    public void foo3(Set<Integer> qset, Cortex cortex, Lexicon<String> lexicon, boolean wdir, boolean mod) {
        int tmax = -1;
        double wmax = 0;
        for (Integer t : lexicon.getLexiconSet()) {
            double w = 1;
            for (int i = 0; i < 4; i++) {

                if (t == Lexicon.NULL_INDEX) {
                    continue;
                }

                for (Integer m : qset) {
                    if (!cortex.getRegion().getFascicle(i).isAssociation(m, t)) {
                        w=0;   
                        continue;
                    }
                   
                    if (mod) {
                        w *= wdir ? cortex.getRegion().getFascicle(i).getWeigth(m, t) : cortex.getRegion().getFascicle(i).getWeigth(t, m);
                    } else {
                        w *= wdir ? cortex.getRegion().getFascicle(i).getWeigthInv(m, t) : cortex.getRegion().getFascicle(i).getWeigthInv(t, m);
                    }

                }

                if(w> wmax){
                    wmax=w;
                    tmax=t;
                }
            }

            
        }
        System.out.println(lexicon.getEntry(tmax) + ": " + wmax);
    }
    
    
    public void foo4(Set<Integer> qset, Cortex cortex, Lexicon<String> lexicon, boolean wdir) {
        int tmax = -1;
        double wmax = 0;
        for (Integer t : lexicon.getLexiconSet()) {
            
            
             if (t == Lexicon.NULL_INDEX || qset.contains(t)) {
                    continue;
                }
            
             double w = 0;
             
            for (int i = 0; i < 4; i++) {
                for (Integer m : qset) {
                    if (!cortex.getRegion().getFascicle(i).isAssociation(m, t)) {
                        w=0;   
                        continue;
                    }
                        w += wdir ? cortex.getRegion().getFascicle(i).getSignificance(m, t) : cortex.getRegion().getFascicle(i).getSignificance(t, m);
                }

            }
            
            
                if(w> wmax){
                    wmax=w;
                    tmax=t;
                }
        }
        System.out.println(lexicon.getEntry(tmax) + ": " + wmax);
    }
}
