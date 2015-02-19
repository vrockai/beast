/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.cortex;

import edu.tuke.util.Utils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author vrockai
 */
public class ClusterCortex extends SimilarityCortex {

    private double eta = 1.0;
    
    public void setEta(double g){
        eta = g;
    }
    
    public double getEta(){
        return eta;
    }
    
//    private Map<Set<Integer>,Set<Integer>> clusterCache = new HashMap<Set<Integer>,Set<Integer>>();
    public ClusterCortex(Cortex c) {
        super(c);
    }

    public Set<Integer> getCluster(Set<Integer> iset, Set<Integer> lset, boolean iterative) {

        logger.info("getCluster " + iset);
        Set<Integer> result = new HashSet<Integer>();

        Set<Set<Integer>> visited = new HashSet<Set<Integer>>();
        Set<Set<Integer>> to_visit = new HashSet<Set<Integer>>();

        Set<Integer> cluster = generateCluster(iset, lset);

        double avgs = avgDist2(iset);

        System.out.println("CS: " + cluster.size());
        System.out.println("CSconf: " + avgs);

        result.addAll(cluster);

        if (iterative) {
            to_visit.addAll(Utils.generateVariations(cluster));
            do {
                iset = to_visit.iterator().next();
                logger.info("To finish: " + to_visit.size());
                if (avgDist(iset) > 2) {
                    visited.add(iset);
                    to_visit.removeAll(visited);
                    continue;
                }

                cluster = generateCluster(iset, lset);
                visited.add(iset);
                result.addAll(cluster);
                to_visit.addAll(Utils.generateVariations(cluster));
                to_visit.removeAll(visited);

            } while (to_visit.size() > 0);
        }

        return result;

    }

    public Set<Integer> getCluster(Set<Integer> iset, Set<Integer> lset, Integer context, boolean iterative) {

        logger.info("getCluster " + iset);
        Set<Integer> result = new HashSet<Integer>();

        Set<Set<Integer>> visited = new HashSet<Set<Integer>>();
        Set<Set<Integer>> to_visit = new HashSet<Set<Integer>>();

        Set<Integer> cluster = generateCluster(iset, context, lset);

        double avgs = avgDist2(iset);

        System.out.println("CS: " + cluster.size());
        System.out.println("CSconf: " + avgs);

        result.addAll(cluster);

        if (iterative) {
            to_visit.addAll(Utils.generateVariations(cluster));
            do {
                iset = to_visit.iterator().next();
                logger.info("To finish: " + to_visit.size());
                if (avgDist(iset) > 2) {
                    visited.add(iset);
                    to_visit.removeAll(visited);
                    continue;
                }

                cluster = generateCluster(iset, lset);
                visited.add(iset);
                result.addAll(cluster);
                to_visit.addAll(Utils.generateVariations(cluster));
                to_visit.removeAll(visited);

            } while (to_visit.size() > 0);
        }

        return result;

    }
    
    private Set<Integer> generateCluster(Set<Integer> iset, Set<Integer> lexicon) {

        double param = 1.;

        logger.info("generateCluster " + iset);
        /*
        if (clusterCache.containsKey(iset)){
        return clusterCache.get(iset);
        }
         */
        assert (iset.size() == 3);
        Iterator<Integer> iter = iset.iterator();
        Integer i1 = iter.next();
        Integer i2 = iter.next();
        Integer i3 = iter.next();

        Set<Integer> result = new HashSet<Integer>();
        double c1 = 1.0 - getRelatives(i1, i2, 1., 1., 1., 1.);
        double c2 = 1.0 - getRelatives(i2, i3, 1., 1., 1., 1.);
        double c3 = 1.0 - getRelatives(i1, i3, 1., 1., 1., 1.);
        for (Integer i : lexicon) {

            double k1 = 1.0 - getRelatives(i, i1, 1., 1., 1., 1.);
            double k2 = 1.0 - getRelatives(i, i2, 1., 1., 1., 1.);
            double k3 = 1.0 - getRelatives(i, i3, 1., 1., 1., 1.);

            if ((k1 + k2 + k3) <= (c1 + c2 + c3)*eta) {
                //if (Math.min(k1, Math.min(k2,k3)) <=  ((c1+c2+c3)/3)) {
                //if ( ((k1+k2+k3)/3) <=  Math.min(c1, Math.min(c2,c3))) {
                //if (Math.min(k1, Math.min(k2,k3)) <= Math.min(c1, Math.min(c2,c3))) {
                //if (Math.max(k1, Math.max(k2,k3)) <= ((c1+c2+c3)/3)) {
                //    System.out.println(k1+k2+k3);
                //if ((param * (k1 + k2 + k3)) <= ((c1 + c2 + c3))) {
                result.add(i);
            }
        }
        /*
        clusterCache.put(iset, result);
         */
        logger.info("cluster finished");
        return result;
    }
    
     private Set<Integer> generateCluster(Set<Integer> iset,Integer context, Set<Integer> lexicon) {
        ContextCortex conCor = new ContextCortex(cortex);
        double param = 1.;

        logger.info("generateCluster " + iset);
        /*
        if (clusterCache.containsKey(iset)){
        return clusterCache.get(iset);
        }
         */
        assert (iset.size() == 3);
        Iterator<Integer> iter = iset.iterator();
        Integer i1 = iter.next();
        Integer i2 = iter.next();
        Integer i3 = iter.next();

        Set<Integer> result = new HashSet<Integer>();
        double c1 = 1.0 - (getRelatives(i1, i2, 1., 1., 1., 1.)*conCor.getContextStrength( i1,context, ContextCortex.Strategy.SUM));
        double c2 = 1.0 - (getRelatives(i2, i3, 1., 1., 1., 1.)*conCor.getContextStrength( i2,context, ContextCortex.Strategy.SUM));;
        double c3 = 1.0 - (getRelatives(i3, i1, 1., 1., 1., 1.)*conCor.getContextStrength( i3,context, ContextCortex.Strategy.SUM));;
        for (Integer i : lexicon) {

            double k1 = 1.0 - (getRelatives(i, i1, 1., 1., 1., 1.)*conCor.getContextStrength(i,context, ContextCortex.Strategy.SUM));;
            double k2 = 1.0 - (getRelatives(i, i2, 1., 1., 1., 1.)*conCor.getContextStrength(i,context, ContextCortex.Strategy.SUM));;
            double k3 = 1.0 - (getRelatives(i, i3, 1., 1., 1., 1.)*conCor.getContextStrength(i,context, ContextCortex.Strategy.SUM));;

            if ((k1 + k2 + k3) <= (c1 + c2 + c3)) {
                //if (Math.min(k1, Math.min(k2,k3)) <=  ((c1+c2+c3)/3)) {
                //if ( ((k1+k2+k3)/3) <=  Math.min(c1, Math.min(c2,c3))) {
                //if (Math.min(k1, Math.min(k2,k3)) <= Math.min(c1, Math.min(c2,c3))) {
                //if (Math.max(k1, Math.max(k2,k3)) <= ((c1+c2+c3)/3)) {
                //    System.out.println(k1+k2+k3);
                //if ((param * (k1 + k2 + k3)) <= ((c1 + c2 + c3))) {
                result.add(i);
            }
        }
        /*
        clusterCache.put(iset, result);
         */
        return result;
    }
    // method used to evaluate cluster (aby sme zahodili sum)

    public double avgDist(Set<Integer> set) {

        Iterator<Integer> iter = set.iterator();
        Integer i1 = iter.next();
        Integer i2 = iter.next();
        Integer i3 = iter.next();

        double c1 = 1. - getRelatives(i1, i2, 1., 1., 1., 1.);
        double c2 = 1. - getRelatives(i2, i3, 1., 1., 1., 1.);
        double c3 = 1. - getRelatives(i1, i3, 1., 1., 1., 1.);

        double s1 = getRelatives(i1, i2, 1., 1., 1., 1.);
        double s2 = getRelatives(i2, i3, 1., 1., 1., 1.);
        double s3 = getRelatives(i1, i3, 1., 1., 1., 1.);

        double c12 = Math.abs(c1 - c2);
        double c13 = Math.abs(c1 - c3);
        double c23 = Math.abs(c2 - c3);

        //return Math.max(s1, Math.max(s2,s3)) - Math.min(s1, Math.min(s2,s3));
        //return s1*s2*s3;
        //return (c1+c2+c3/3) - Math.min(c1, Math.min(c2,c3));
        //return Math.min(s1, Math.min(s2,s3));
        //return (c1 * c2 * c3);
        return (c1 + c2 + c3) / 3.;
    }

    public double avgDistAll(Set<Integer> set) {

        Set<Set<Integer>> vset = Utils.generate2Variations(set);
        
        double sum = 0;
        
        for(Set<Integer> iset : vset){
        Iterator<Integer> iter = set.iterator();
        Integer i1 = iter.next();
        Integer i2 = iter.next();
        
        sum += 1. - getRelatives(i1, i2, 1., 1., 1., 1.);
        
        }
      
     
        return sum / (double)vset.size();
    }
    
    public double avgDist2(Set<Integer> set) {

        Iterator<Integer> iter = set.iterator();
        Integer i1 = iter.next();
        Integer i2 = iter.next();
        Integer i3 = iter.next();

        double c1 = 1. - getRelatives(i1, i2, 1., 1., 1., 1.);
        double c2 = 1. - getRelatives(i2, i3, 1., 1., 1., 1.);
        double c3 = 1. - getRelatives(i1, i3, 1., 1., 1., 1.);

        double s1 = getRelatives(i1, i2, 1., 1., 1., 1.);
        double s2 = getRelatives(i2, i3, 1., 1., 1., 1.);
        double s3 = getRelatives(i1, i3, 1., 1., 1., 1.);

        double c12 = Math.abs(c1 - c2);
        double c13 = Math.abs(c1 - c3);
        double c23 = Math.abs(c2 - c3);

        //return Math.max(s1, Math.max(s2,s3)) - Math.min(s1, Math.min(s2,s3));
        //return s1*s2*s3;
        //return (c1+c2+c3/3) - Math.min(c1, Math.min(c2,c3));
        //return Math.min(s1, Math.min(s2,s3));
        //return (c1 * c2 * c3);
        return (c12 + c13 + c23) / 3.;
    }
}
