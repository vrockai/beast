/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.experiments;

import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.cortex.SynonymCortex;
import edu.tuke.beast.lexicon.Lexicon;
import java.io.IOException;
import java.util.Map;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;

/**
 *
 * @author blur
 */
public class SynonymsTest extends TestCase {
    
    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();

    public void testNaryRelatednessCluster() throws IOException, ClassNotFoundException, SizeLimitExceededException {
        
        String cortexString = "/home/vrockai/full_cortex/cortex";
        Beast beast = new Beast(cortexString);
        
        Cortex c = beast.getCortex();
        
        SynonymCortex sc = new SynonymCortex(c);
        
        Map<Integer, Double> syns = sc.getSynonyms(beast.getLexicon().getIndex("january"), SynonymCortex.SYNONYMS_STRATEGY.STRONG);
        
        for(Map.Entry<Integer, Double> me : syns.entrySet()){
            System.out.println(beast.getLexicon().getEntry(me.getKey())+": "+me.getValue());
        }
    }  
}
