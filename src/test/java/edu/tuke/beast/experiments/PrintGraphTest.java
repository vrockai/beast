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
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author blur
 */
public class PrintGraphTest extends TestCase  {
    
    
     public void testSaveGraph() throws IOException, ClassNotFoundException, SizeLimitExceededException {
                
         
        String dataFileName = "/home/vrockai/workspace/beast/corsk121_lex.dat";//System.getProperty("data");
       // String graphFileName = System.getProperty("graph");
        String graphFileName = "/home/vrockai/corsk121_lex.png";
        
       File fdata = new File(dataFileName);
       //XYSeries dataSeries = getData2();
       XYSeries dataSeries = getData(fdata, 0, 1);
       
       Utils.saveGraph(dataSeries, graphFileName);
        
       
    }
     /*
     public void testSaveHisto() throws IOException, ClassNotFoundException, SizeLimitExceededException {
              
        String dataFileName = "/home/vrockai/coren3k_lex.dat";//System.getProperty("data");
       // String graphFileName = System.getProperty("graph");
        String graphFileName = "/home/vrockai/coren3k_lexH.png";
        
        File fdata = new File(dataFileName);
       XYSeries dataSeries = getData2();
       //XYSeries dataSeries = getData2(fdata, 0, 1);
       
       Utils.saveHistogram(getGlist(), graphFileName);
    }
     */
      private List<Double> getGlist(){
       List<Double> glist = new ArrayList<Double>();
       
       for (double d=0.; d<10.; d=d+0.1){
           glist.add(d);
       }
       
       return glist;
      }
     
     private double getError(double x, double y, double a, double b, double c){
         //double y2 = a*(1-Math.pow(b, (-1*c*x)));
         double y2 = a*(1-Math.pow(Math.E, (-1*b*Math.pow(Math.E,c*Math.log(x)))));
         return Math.abs(y - y2);
     }
     
    private XYSeries getData2() throws FileNotFoundException, IOException{
        XYSeries asocSeries = new XYSeries((double) 0);
        
        double a = 21046251.1800571870000000000000000000000000000000;
        double b = 0.0000038949836573076340000000000000000000;
        double c = 0.6468320176772836000000000000000000000000;

        System.out.println("3..%: "+0.003*a);
        
        double old = a*(1-Math.pow(Math.E, (-1*b*Math.pow(Math.E,c*Math.log(0)))));
        
        for(double  i = 1 ; i < 4140000000. ; i= i+10000000){
            double n = a*(1-Math.pow(Math.E, (-1*b*Math.pow(Math.E,c*Math.log(i)))));
            asocSeries.add(i, n );
            
            double r = n-old;
            
            
            
           if ( n > 0.997 * a)
                System.out.println("i: " + i+ ", r: "+r);
                
           old = n;
        }
        
        return asocSeries;
    }
    
    private XYSeries getData(File file, int index, int value) throws FileNotFoundException, IOException{
        XYSeries asocSeries = new XYSeries((double) 0);
        
        final BufferedReader bfr = new BufferedReader(new FileReader(file), 500);
        String s;
         while ((s = bfr.readLine()) != null) {
             String[] v = s.split(",");
             asocSeries.add(Double.valueOf(v[index].trim()),Double.valueOf(v[value].trim()));
         }
        
        return asocSeries;
    }
}
