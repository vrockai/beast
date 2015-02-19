/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author vrockai
 */
public class Utils {

        private static int WIDTH = 800;
        private static int HEIGHT = 580;
    
    public static SortedMap<Integer, Double> sortHashMap(Map<Integer, Double> input) {
        Map<Integer, Double> tempMap = new HashMap<Integer, Double>();
        for (Integer wsState : input.keySet()) {
            tempMap.put(wsState, input.get(wsState));
        }

        List<Integer> mapKeys = new ArrayList<Integer>(tempMap.keySet());
        List<Double> mapValues = new ArrayList<Double>(tempMap.values());
        SortedMap<Integer, Double> sortedMap = new TreeMap<Integer, Double>();
        SortedSet<Double> sortedSet = new TreeSet<Double>(mapValues);
        Object[] sortedArray = sortedSet.toArray();
        int size = sortedArray.length;
        for (int i = 0; i < size; i++) {
            sortedMap.put(mapKeys.get(mapValues.indexOf(sortedArray[i])),
                    (Double) sortedArray[i]);
        }
        return sortedMap;
    }

    public static SortedSet<Map.Entry<Integer,Double>> sortMapByValue(Map<Integer,Double> map){
        SortedSet<Map.Entry<Integer,Double>> sortedSet = new TreeSet<Map.Entry<Integer,Double>>(new Utils.ComparatorSetValue());
        sortedSet.addAll(map.entrySet());
        return sortedSet;            
    }

    public static void saveGraph(XYSeries dataSeries, String graphFileName) {
       
        JFreeChart sigChart = ChartFactory.createXYLineChart("Energy", // Title
                "tokens read", // x-axis Label
                "energy (sum of significance)", // y-axis Label
                new XYSeriesCollection(dataSeries), // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                false, // Show Legend
                false, // Use tooltips
                false // Configure chart to generate URLs?
                );
       
       
        Plot plot = sigChart.getPlot();
        plot.setBackgroundPaint(Color.white);
        
        // Sirka ciary
        sigChart.getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(1.2f));
        
        // Font nadpisu
        //sigChart.getTitle().setFont(new Font("Ubuntu", Font.PLAIN, 52));
        
        // Farba gridu
        ((XYPlot)sigChart.getPlot()).setRangeGridlinePaint(Color.gray);
        ((XYPlot)sigChart.getPlot()).setDomainGridlinePaint(Color.gray);
        
        // Font na osiach - popis
        //((XYPlot)sigChart.getPlot()).getDomainAxis().setLabelFont(new Font("Ubuntu", Font.PLAIN, 32));
        //((XYPlot)sigChart.getPlot()).getRangeAxis().setLabelFont(new Font("Ubuntu", Font.PLAIN, 32));
        
        // Font na osiach - cisla        
        //((XYPlot)sigChart.getPlot()).getDomainAxis().setTickLabelFont(new Font("Ubuntu", Font.PLAIN, 22));
        //((XYPlot)sigChart.getPlot()).getRangeAxis().setTickLabelFont(new Font("Ubuntu", Font.PLAIN, 22));
        
        int width = WIDTH;
        int height = HEIGHT;
        try {            
            ChartUtilities.saveChartAsPNG(new File(graphFileName), sigChart, width, height);
        } catch (IOException e) {
            System.err.print(e);
        }
        
    }
    
    @Deprecated
    public static void saveHistogram(List<Double> dlist, String filename) {
        double[] value = new double[dlist.size()];
    
        String title = "";
        String x = "";
        String y = "";
        
        title = title == null ? "" : title;
        
        double max = 0;
        double sum = 0;
        for (int i = 0; i < dlist.size(); i++) {            
            value[i] = dlist.get(i);
            sum += value[i];
            if (value[i]>max)
                max = value[i];
        }
        
        System.out.println("avg distance: "+filename+": "+sum/dlist.size());
        
        HistogramDataset hds = new HistogramDataset();
        hds.addSeries("H1", value, 100, 0, max);
        
        JFreeChart sigChart = ChartFactory.createHistogram(title,
                y, 
                x, 
                hds, 
                PlotOrientation.VERTICAL, 
                false, // Show Legend
                false, // Use tooltips
                false); // Configure chart to generate URLs?
        
         Plot plot = sigChart.getPlot();
        plot.setBackgroundPaint(Color.white);
        
        // Sirka ciary
        //sigChart.getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(3));
        
        XYBarRenderer renderer = (XYBarRenderer)((XYPlot)sigChart.getPlot()).getRenderer();
        renderer.setShadowVisible(false);
        renderer.setBarPainter( new org.jfree.chart.renderer.xy.StandardXYBarPainter() );
        renderer.setMargin( .3 );
        renderer.setSeriesPaint( 0, Color.red );
        
        // Font nadpisu
        sigChart.getTitle().setFont(new Font("Ubuntu", Font.PLAIN, 52));
        
        // Farba gridu
        ((XYPlot)sigChart.getPlot()).setRangeGridlinePaint(Color.gray);
        ((XYPlot)sigChart.getPlot()).setDomainGridlinePaint(Color.gray);
        
        // Font na osiach - popis
        ((XYPlot)sigChart.getPlot()).getDomainAxis().setLabelFont(new Font("Ubuntu", Font.PLAIN, 32));
        ((XYPlot)sigChart.getPlot()).getRangeAxis().setLabelFont(new Font("Ubuntu", Font.PLAIN, 32));
        
        // Font na osiach - cisla        
        ((XYPlot)sigChart.getPlot()).getDomainAxis().setTickLabelFont(new Font("Ubuntu", Font.PLAIN, 22));
        ((XYPlot)sigChart.getPlot()).getRangeAxis().setTickLabelFont(new Font("Ubuntu", Font.PLAIN, 22));
        
        int width = WIDTH;
        int height = HEIGHT;
        try {            
            ChartUtilities.saveChartAsPNG(new File(filename), sigChart, width, height);
        } catch (IOException e) {
        }
    }
    
    public static void saveHistogram(List<Double> dlist, String filename, String title, String x, String y) {
        double[] value = new double[dlist.size()];
    
        title = title == null ? "" : title;
        
        double max = 0;
        double sum = 0;
        for (int i = 0; i < dlist.size(); i++) {            
            value[i] = dlist.get(i);
            sum += value[i];
            if (value[i]>max)
                max = value[i];
        }
        
        System.out.println("avg distance: "+filename+": "+sum/dlist.size());
        
        HistogramDataset hds = new HistogramDataset();
        hds.addSeries("H1", value, 100, 0, max);
        
        JFreeChart sigChart = ChartFactory.createHistogram(title,
                y, 
                x, 
                hds, 
                PlotOrientation.VERTICAL, 
                false, // Show Legend
                false, // Use tooltips
                false); // Configure chart to generate URLs?
        
         Plot plot = sigChart.getPlot();
        plot.setBackgroundPaint(Color.white);
        
        // Sirka ciary
        //sigChart.getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(3));
        
        XYBarRenderer renderer = (XYBarRenderer)((XYPlot)sigChart.getPlot()).getRenderer();
        renderer.setShadowVisible(false);
        renderer.setBarPainter( new org.jfree.chart.renderer.xy.StandardXYBarPainter() );
        renderer.setMargin( .3 );
        renderer.setSeriesPaint( 0, Color.red );
        
        // Font nadpisu
        sigChart.getTitle().setFont(new Font("Ubuntu", Font.PLAIN, 52));
        
        // Farba gridu
        ((XYPlot)sigChart.getPlot()).setRangeGridlinePaint(Color.gray);
        ((XYPlot)sigChart.getPlot()).setDomainGridlinePaint(Color.gray);
        
        // Font na osiach - popis
        ((XYPlot)sigChart.getPlot()).getDomainAxis().setLabelFont(new Font("Ubuntu", Font.PLAIN, 32));
        ((XYPlot)sigChart.getPlot()).getRangeAxis().setLabelFont(new Font("Ubuntu", Font.PLAIN, 32));
        
        // Font na osiach - cisla        
        ((XYPlot)sigChart.getPlot()).getDomainAxis().setTickLabelFont(new Font("Ubuntu", Font.PLAIN, 22));
        ((XYPlot)sigChart.getPlot()).getRangeAxis().setTickLabelFont(new Font("Ubuntu", Font.PLAIN, 22));
        
        int width = WIDTH;
        int height = HEIGHT;
        try {            
            ChartUtilities.saveChartAsPNG(new File(filename), sigChart, width, height);
        } catch (IOException e) {
        }
    }

    

  
    public static class ComparatorSetValue implements Comparator {

        public ComparatorSetValue(){
            
        }
        
        public int compare(Object o1, Object o2) {
            return -1 * ((Comparable) ((Map.Entry<Integer, Double>) (o1)).getValue()).compareTo(((Map.Entry<Integer, Double>) (o2)).getValue());
        }
    };
    
    public static Set<Set<Integer>> generateVariations(Set<Integer> set) {
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
    
    public static Set<Set<Integer>> generate2Variations(Set<Integer> set) {
        Set<Set<Integer>> result = new HashSet<Set<Integer>>();

        List<Integer> list = new ArrayList<Integer>(set);

        int size = list.size();

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                    Set<Integer> triplet = new HashSet<Integer>();
                    triplet.add(list.get(i));
                    triplet.add(list.get(j));
                    result.add(triplet);
                }
            }
        
        return result;
    }
    
    
    public static Set<Integer> _pick3(Set<Integer> set) {
        assert set.size() > 2;
        
        Set<Integer> r = new HashSet<Integer>();
        
        int c = 3;
        for (Integer i : set){
            if (c == 0)
                break;
            r.add(i);
            c--;            
        }
        
        
        return r;
    }
    
    public static Set<Set<Integer>> pick3(Set<Integer> set) {
        assert set.size() > 3;

        Set<Set<Integer>> result = new HashSet<Set<Integer>>();
        Set<Integer> r = new HashSet<Integer>();
        
        int c = 3;
        for (Integer i : set){
            if (c == 0)
                break;
            r.add(i);
            c--;            
        }
        
        result.add(r);
        
        return result;
    }
    
    public static int evaluateGood(Set<Integer> returned, Set<Integer> full){
        Set<Integer> r = new HashSet<Integer>(full);
        r.retainAll(returned);        
        return r.size();
    }
    
    public static int evaluateBad(Set<Integer> returned, Set<Integer> full){
        Set<Integer> r = new HashSet<Integer>(returned);
        r.removeAll(full);        
        return r.size();
    }
    
    public static int evaluateMissing(Set<Integer> returned, Set<Integer> full){
        Set<Integer> r = new HashSet<Integer>(full);
        r.retainAll(returned);        
        return full.size() - r.size();
    }
    
   
    /*
    public static Set<Set<Integer>> generateVariations(Set<Integer> set) {
        Set<Set<Integer>> result = new HashSet<Set<Integer>>();

        int size = set.size();
        
        List<Integer> list = new ArrayList<Integer>(set);

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
     * 
     */
}
