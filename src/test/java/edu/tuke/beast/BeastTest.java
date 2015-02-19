/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tuke.beast;

import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.input.BeastAnalyzer;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.token.Token;
import java.io.Reader;
import java.io.StringReader;
import org.apache.lucene.analysis.TokenStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vrockai
 */
public class BeastTest {

    public BeastTest() {
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

    /**
     * Test of buildLexicon method, of class Beast.
     */
    @Test
    public void testBuildLexicon() throws Exception {
        String input_str1 = "a 1 b c d \n \n e . f g h ja som viliam. ja som viliam, som, pekny.";
        BeastAnalyzer ba = new BeastAnalyzer();
        TokenStream ts= ba.tokenStream(input_str1);

        int size = 10;

        Beast beast = new Beast();

        Lexicon<String> lexicon = beast.buildLexicon(ts,size);

        int ni = Lexicon.NULL_INDEX;

        assertEquals("Null index has wrong value.",ni,lexicon.getIndex(new Token<String>(null)));

        assertEquals( "Wrong learned lexicon size.", 10, lexicon.getHashMap().size() );

        assertEquals("Null token should was counted.",0,lexicon.getOccurences(new Token<String>(null)));

        assertNotSame(ni,lexicon.getIndex(new Token<String>("a")));
        //assertNotSame(lexicon.getIndex(new Token<String>("1")),0);
        assertNotSame(ni,lexicon.getIndex(new Token<String>("b")));
        assertNotSame(ni,lexicon.getIndex(new Token<String>("c")));
        assertNotSame(ni,lexicon.getIndex(new Token<String>("d")));
        assertNotSame(ni,lexicon.getIndex(new Token<String>("e")));
        assertNotSame(ni,lexicon.getIndex(new Token<String>("f")));
        //assertNotSame(lexicon.getIndex(new Token<String>("g")),0);
        //assertNotSame(lexicon.getIndex(new Token<String>("h")),0);
        assertNotSame(ni,lexicon.getIndex(new Token<String>("ja")));
        assertNotSame(ni,lexicon.getIndex(new Token<String>("som")));
        assertNotSame(ni,lexicon.getIndex(new Token<String>("viliam")));
        //assertNotSame(lexicon.getIndex(new Token<String>("pekny")),0);

        assertEquals(1,lexicon.getOccurences(new Token<String>("a")));
        assertEquals(0,lexicon.getOccurences(new Token<String>("1")));
        assertEquals(1,lexicon.getOccurences(new Token<String>("b")));
        assertEquals(1,lexicon.getOccurences(new Token<String>("c")));
        assertEquals(1,lexicon.getOccurences(new Token<String>("d")));
        assertEquals(1,lexicon.getOccurences(new Token<String>("e")));
        assertEquals(1,lexicon.getOccurences(new Token<String>("f")));
        assertEquals(0,lexicon.getOccurences(new Token<String>("g")));
        assertEquals(0,lexicon.getOccurences(new Token<String>("h")));
        assertEquals(2,lexicon.getOccurences(new Token<String>("ja")));
        assertEquals(3,lexicon.getOccurences(new Token<String>("som")));
        assertEquals(2,lexicon.getOccurences(new Token<String>("viliam")));
        assertEquals(0,lexicon.getOccurences(new Token<String>("pekny")));
    }

    /**
     * Test of learn method, of class Beast.
     */
    @Test
    public void testLearn() throws Exception {
        String input_str1 = "a 1 b c d \n \n e . f g h ja som viliam. ja som viliam, som, pekny.";
        BeastAnalyzer ba = new BeastAnalyzer();
        TokenStream ts= ba.tokenStream(input_str1);

        int size = 100;

        Beast beast = new Beast();
        beast.setStep_l(5);
        Lexicon<String> lexicon = beast.buildLexicon(ts,size);

        ba = new BeastAnalyzer();
        ts= ba.tokenStream(input_str1);
        Region region = beast.learn(ts, lexicon, Beast.LearnStrategy.STRICT, false);

        int t1 = lexicon.getIndex(new Token<String>("ja"));
        int t2 = lexicon.getIndex(new Token<String>("som"));

        System.out.println("r:"+region.getFascicle(0).getSignificance(t1, t2));
    }

}