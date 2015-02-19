/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.experiments;

import edu.tuke.util.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author blur
 */
public class FindFuncTest extends TestCase  {
    
    
     public void testSaveGraph() throws IOException, ClassNotFoundException, SizeLimitExceededException {
                
        String dataFileName = System.getProperty("data");
        
        File fdata = new File(dataFileName);
       SortedMap<Double, Double> dataSeries = getData(fdata,0,1);
       
       double ma = 21086124.21344253;
       double mb = 0.000004057243566377979;
       double mc = 0.644538965183839;
                    
       for (int i =0 ; i< 100; i++){
           System.out.println(i);
           List<Double> rlist = find(ma, mb, mc, dataSeries,(i+1));
           ma = rlist.get(0);
           mb = rlist.get(1);
           mc = rlist.get(2);
       }
       
    }
     
     List<Double> find(double ma, double mb, double mc, SortedMap<Double, Double> dataSeries,double i){
         List<Double> rlist = new ArrayList<Double>();
       double emin = 0;
       for (Double k : dataSeries.keySet()){
                       emin += getError(k, dataSeries.get(k), ma, mb, mc);
       }
       
       double s1 = 2*i;
       double s2 = 100*i;
       
       double s = dataSeries.size();
       for (double a = ma - ma/s1; a < ma + ma/s1; a = a + ma/s2){
           for (double b = mb - mb/s1; b < mb + mb/s1; b = b + mb/s2){
               for (double c = mc - mc/s1; c < mc + mc/s1; c = c + mc/s2){
                   
                   double e = 0;
                   
                   for (Double k : dataSeries.keySet()){
                       e += getError(k, dataSeries.get(k), a, b, c);                       
                   }
                   
                   if (e < emin){
                       emin = e;
                       System.out.println("emin: "+e/s);
                       ma = a;
                       mb = b;
                       mc = c;
                       System.out.format("a: %-20.40f%n", ma);
                       System.out.format("b: %-20.40f%n", mb);
                       System.out.format("c: %-20.40f%n", mc);
                   }
               }
           }
       }  
       rlist.add(ma);
       rlist.add(mb);
       rlist.add(mc);
       return rlist;
     }
    
     private double getError(double x, double y, double a, double b, double c){
         //double y2 = a*(1-Math.pow(b, (-1*c*x)));
         double y2 = a*(1-Math.pow(Math.E, (-1*b*Math.pow(Math.E,c*Math.log(x)))));
         return Math.abs(y - y2);
     }
     
    private SortedMap<Double, Double> getData(File file, int index, int value) throws FileNotFoundException, IOException{
        SortedMap<Double,Double> asocSeries = new TreeMap<Double,Double>();
        
        final BufferedReader bfr = new BufferedReader(new FileReader(file), 500);
        String s;
         while ((s = bfr.readLine()) != null) {
             String[] v = s.split(";");
             asocSeries.put(Double.valueOf(v[index].trim()),Double.valueOf(v[value].trim()));
         }
        
        return asocSeries;
    }
}
