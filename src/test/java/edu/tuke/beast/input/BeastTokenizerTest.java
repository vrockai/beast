/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.input;

import java.beans.PropertyChangeListener;
import java.io.Reader;
import java.io.StringReader;
import org.apache.lucene.analysis.Token;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vrockai
 */
public class BeastTokenizerTest {

    public BeastTokenizerTest() {
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
     * Test of next method, of class BeastTokenizer.
     */
    @Test
    public void testNext() throws Exception {

        String input_str1 = "a 1 b c d \n \n e . f g h ja som viliam. ja som viliam, som, pekny.";
        String[] expected = {"a", "#", "b", "c", "d","e", "#", "f", "g", "h", "ja", "som", "viliam", "#", "ja", "som", "viliam","#", "som","#", "pekny","#"};

        Reader reader = new StringReader(input_str1);

        BeastTokenizer instance = new BeastTokenizer(reader);

        Token token = new Token();

       for(String exp : expected){
        token = instance.next(token);
        assertEquals(exp, token.term());
        }
    }

    @Test
    public void testNext_advanced() throws Exception {

        String input_str1 = "i'm vEry nice b.y";
        String[] expected = {"i'm","vEry","nice","b","#","y"};
        Reader reader = new StringReader(input_str1);

        BeastTokenizer instance = new BeastTokenizer(reader);

        Token token = new Token();

        for(String exp : expected){
        token = instance.next(token);
        assertEquals(exp, token.term());
        }
        

    }
}