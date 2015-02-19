/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.experiments.thesis;

import edu.tuke.beast.experiments.*;
import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.ClusterCortex;
import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.cortex.SimilarityCortex;
import edu.tuke.beast.input.Input;
import edu.tuke.beast.lexicon.Lexicon;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.naming.SizeLimitExceededException;
import junit.framework.TestCase;

/**
 *
 * @author vrockai
 */
public class LexiconCoverageTest extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();

    public void testLexiconCoverageTest() throws IOException, ClassNotFoundException, SizeLimitExceededException, Exception {

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("coren3k_lex.dat")));
        
        String file = "/home/vrockai/School/diz_experiment/korpus/coren3k/";
        //String file = "/home/blur/test.txt";

        int sizeMax = 21000;

        Beast beast = new Beast();

        Input input = new Input(new File(file));

        Lexicon<String> lexFull = beast.buildLexicon(input.getTokenStream(), sizeMax);

        long tokenNum = lexFull.CORPUS_SIZE;
        
        System.out.println("Size 1: " + lexFull.getSize());

        for (int i = 1; i < lexFull.getSize(); i += 50) {            
            
            Lexicon<String> lexFullTest = new Lexicon<String>(lexFull.lexicon, lexFull.lexiconInverse, lexFull.occurences, lexFull.indexCounter);
            lexFullTest = lexFullTest.top(i);
            
            long occurences = 0;
            
            for (Integer token : lexFullTest.getLexiconSet()){
                occurences += lexFullTest.getOccurences(token);
            }
            
            System.out.println("Size " + i + ": " + (double)occurences/(double)tokenNum);
            out.println(i + ", " + (double)occurences/(double)tokenNum);
        }
        
        out.close();
    }
}
