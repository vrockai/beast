/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tuke.beast;

import edu.tuke.beast.input.BeastAnalyzer;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.token.Token;
import org.apache.lucene.analysis.TokenStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author vrockai
 */
public class LearnPerfTest {

    public LearnPerfTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLearn() throws Exception {
        String input_str1 = "a 1 b c d \n \n e . f g h ja som viliam. ja som viliam, som, pekny.";
        BeastAnalyzer ba = new BeastAnalyzer();
        TokenStream ts= ba.tokenStream(input_str1);

        int size = 2000;

        Beast instance = new Beast();
        Lexicon result = new Lexicon();

        for (int i=0;i<size;i++){
            Token<String> token = new Token<String>(i+"");
            result.addToken(token);
            result.addOccurence(token);
        }
        
        ba = new BeastAnalyzer();
        ts= ba.tokenStream(input_str1);
        instance.setStep(5);
        instance.learn(ts,result, Beast.LearnStrategy.STRICT, false);
    }

}