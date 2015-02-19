/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.experiments;

import edu.tuke.beast.Beast;
import java.io.IOException;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;

/**
 *
 * @author vrockai
 */
public class GetAsocCount extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();

    public void testMachTest() throws Exception {
        printForBeast("/home/vrockai/School/full_cortex/cortex");
        printForBeast("/home/vrockai/School/idiot_cortex/cortex");
        printForBeast("/home/vrockai/School/sk_cortex/cortex");
        printForBeast("/home/vrockai/School/cs_cortex/cortex");
    }
    
    public void printForBeast(String filename) throws Exception{
        Beast beast = new Beast(filename);
        System.out.println(filename);
        for (int i=0;i<4;i++){
            int asoc = beast.getRegion().fascicle[i].getAsocCount();
            System.out.println("F_"+i+" : "+asoc);
        }
        System.out.println("");
    }
    
  
}
