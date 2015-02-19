/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.experiments;

import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.cortex.SimilarityCortex;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.properties.PropertiesSingelton;
import edu.tuke.beast.wordnet.WordnetParser;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.dictionary.Dictionary;

/**
 *
 * @author blur
 */
public class HypernymTest extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();
    private static SimilarityCortex.Strategy strategy;
    private static Integer mayi = 0;
    private static Map<Set<Integer>, Set<Integer>> clusterCache = new HashMap<Set<Integer>, Set<Integer>>();

    public void testAvgDistance() throws IOException, ClassNotFoundException, SizeLimitExceededException, JWNLException {
        Beast beast = new Beast("idiot/cortex");
        double tresh = 1.;
        Cortex cortex = beast.getCortex();
        Lexicon<String> lexicon = beast.getLexicon();

        String wnPropPath = PropertiesSingelton.getInstance().getProperties().getProperty("wn.prop.path");
        System.out.println("Path to wordnet properties: " + wnPropPath);
        WordnetParser wp = new WordnetParser(wnPropPath);
        
        List<Synset> ls = wp.getSiblings(POS.NOUN, "january");
        
        for (Synset s: ls){
            System.out.println(s);
        }
       
    }
}
