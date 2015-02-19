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
public class LexiconCoverageCorrectTest extends TestCase {

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();

    public void testNaryRelatednessCluster() throws IOException, ClassNotFoundException, SizeLimitExceededException, Exception {

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("corsk121_lex.dat")));
        
        String file = "/home/vrockai/School/diz_experiment/korpus/corsk121/";
        //String file = "/home/blur/test.txt";

        int sizeMax = 21000;

        Beast beast = new Beast();

        Input input = new Input(new File(file));

        Lexicon<String> lexFull = beast.buildLexicon(input.getTokenStream(), sizeMax);

        System.out.println("Size 1: " + lexFull.getSize());

        for (int i = 1; i < lexFull.getSize(); i = i + 250) {
            input = new Input(new File(file));
            Lexicon<String> lexFullTest = new Lexicon<String>(lexFull.lexicon, lexFull.lexiconInverse, lexFull.occurences, lexFull.indexCounter);
            lexFullTest = lexFullTest.top(i);
            System.out.println("Size " + i + ": " + lexFullTest.getSize());
            beast.setLexicon(lexFullTest);
            Vector<Double> v = beast.getCoverage(input.getTokenStream(), lexFullTest);
            System.out.println(i + "; " + v.get(0) + ", " + v.get(1));
            out.println(i + ", " + v.get(0) + ", " + v.get(1));
        }
        
        out.close();
    }
}
