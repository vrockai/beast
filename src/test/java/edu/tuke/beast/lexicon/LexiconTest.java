/*
 * LexiconMapTest.java
 * JUnit based test
 *
 * Created on March 13, 2007, 11:29 PM
 */

package edu.tuke.beast.lexicon;

import junit.framework.*;
import edu.tuke.beast.token.Token;
import java.io.*;
import java.util.*;

/**
 *
 * @author blur
 */
public class LexiconTest extends TestCase {
    
    public LexiconTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LexiconTest.class);
        
        return suite;
    }

    /**
     * Test of deSerialize method, of class edu.tuke.beast.lexicon.LexiconMap.
     */
    public void testDeSerialize() throws Exception {
        
        String filename = "";
        
        Lexicon expResult = null;
        Lexicon result = Lexicon.deSerialize(filename);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addToken method, of class edu.tuke.beast.lexicon.LexiconMap.
     */
    public void testAddToken() {
        
        Token<String> token = new Token<String>("Hello world!");
        Lexicon<String> lex = new Lexicon<String>();
        
        assertTrue(lex.getHashMap().size() == 1);
        lex.addToken(token);
        assertTrue(lex.contains(token));
        
    }

    /**
     * Test of addOccurence method, of class edu.tuke.beast.lexicon.LexiconMap.
     */
    public void testAddOccurence() throws Exception {
        
        Token<String> token = new Token<String>("Hello world!");
        Lexicon<String> lex = new Lexicon<String>();
        
        lex.addToken(token);
        lex.addOccurence(token);
        
        assertTrue(lex.getOccurences(token) == 1);
    }

    /**
     * Test of contains method, of class edu.tuke.beast.lexicon.LexiconMap.
     */
    public void testContains() {
        Token<String> token = new Token<String>("Hello world!");
        Lexicon<String> lex = new Lexicon<String>();
        
        lex.addToken(token);
        
        assertTrue(lex.contains(token));
    }

    /**
     * Test of getEntriesCount method, of class edu.tuke.beast.lexicon.LexiconMap.
     */
    public void testGetEntriesCount() {
        
        Token<String> token = new Token<String>("Hello world!");
        Lexicon<String> lex = new Lexicon<String>();
        
        for (int i=0; i<10; i++){
            lex.addToken(new Token<String>(new String(""+i)));
        }
        
        assertTrue(lex.getEntriesCount() == 11);
    }

    /**
     * Test of getIndex method, of class edu.tuke.beast.lexicon.LexiconMap.
     */
    public void testGetIndex() {
        Token<String> t1 = new Token<String>("one");
        Token<String> t2 = new Token<String>("two");
        Token<String> t3 = new Token<String>("three");
        
        Token<String> token = new Token<String>("Hello world!");
        Lexicon<String> lex = new Lexicon<String>();
        int ni = Lexicon.NULL_INDEX;
        
        assertTrue(lex.getIndex(new Token(null)) == ni);
        
        lex.addToken(t1);
        
        assertTrue(lex.getIndex(new Token(null)) == ni);
        assertTrue(lex.getIndex(t1) == 2);
        
        lex.addToken(t2);
        
        assertTrue(lex.getIndex(new Token(null)) == ni);
        assertTrue(lex.getIndex(t1) == 2);
        assertTrue(lex.getIndex(t2) == 3);
                
        lex.addToken(t3);
        
        assertTrue(lex.getIndex(new Token(null)) == ni);
        assertTrue(lex.getIndex(t1) == 2);
        assertTrue(lex.getIndex(t2) == 3);
        assertTrue(lex.getIndex(t3) == 4);
    }

    /**
     * Test of getOccurences method, of class edu.tuke.beast.lexicon.LexiconMap.
     */
    public void testGetOccurences() throws Exception {
        
        Token<String> t1 = new Token<String>("one");
        Token<String> t2 = new Token<String>("two");
        Token<String> t3 = new Token<String>("three");
        
        Token<String> token = new Token<String>("Hello world!");
        Lexicon<String> lex = new Lexicon<String>();

        int ni = Lexicon.NULL_INDEX;

        assertTrue( lex.getOccurences(new Token(null)) == ni);
        assertTrue( lex.getOccurences(t1) == 0);
        
        lex.addToken(t1);
        lex.addToken(t2);
        lex.addToken(t3);
        
        assertTrue( lex.getOccurences(t1) == 0);
        
        lex.addOccurence(t1);
        
        lex.addOccurence(t2);
        lex.addOccurence(t2);
        
        lex.addOccurence(t3);
        lex.addOccurence(t3);
        lex.addOccurence(t3);
                
        assertTrue( lex.getOccurences(t1) == 1);
        assertTrue( lex.getOccurences(t2) == 2);
        assertTrue( lex.getOccurences(t3) == 3);
    }

    /**
     * Test of getSortedLexicon method, of class edu.tuke.beast.lexicon.LexiconMap.
     
    public void testGetSortedLexicon() {
       
        Lexicon<String> lex = new Lexicon<String>();
        
        Map<Integer, Token<String>> expResult = null;
        Map<Integer, Token<String>> result = lex.getLexicon();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    /**
     * Test of getHashMap method, of class edu.tuke.beast.lexicon.LexiconMap.
     */
    public void testGetHashMap() {
        
        Lexicon<String> lex = new Lexicon<String>();
        
        Map<Integer, Token<String>> expResult = null;
        Map<Integer, Token<String>> result = lex.getHashMap();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of lexiconizeWindow method, of class edu.tuke.beast.lexicon.LexiconMap.
     */
    public void testLexiconizeWindow() {
        
        Token<String>[] question = null;
        Lexicon<String> lex = new Lexicon<String>();
        
        int[] expResult = null;
        int[] result = lex.lexiconizeWindow(question);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delexiconizeWindow method, of class edu.tuke.beast.lexicon.LexiconMap.
     */
    public void testDelexiconizeWindow() {
        
        int[] question = null;
        Lexicon<String> lex = new Lexicon<String>();
        
        Token<String>[] expResult = null;
        Token<String>[] result = lex.delexiconizeWindow(question);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of serialize method, of class edu.tuke.beast.lexicon.LexiconMap.
     */
    public void testSerialize() throws Exception {
        
        String fileName = "";
        Lexicon<String> lex = new Lexicon<String>();
        
        lex.serialize(fileName);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEntry method, of class edu.tuke.beast.lexicon.LexiconMap.
     */
    public void testGetEntry() {
        int indexToken = 0;
        Lexicon<String> lex = new Lexicon<String>();
        
        Token<String> expResult = null;
        Token<String> result = lex.getEntry(indexToken);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of top method, of class edu.tuke.beast.lexicon.LexiconMap.
     */
    public void testTop() throws Exception {
        
        Token<String> token = new Token<String>("Hello world!");
        Lexicon<String> lex = new Lexicon<String>();
        
        for(int i=0;i<10;i++){
            lex.addToken(new Token<String>(new String("i"+i)));
            for(int j=0;j<i+10;j++)
                lex.addOccurence(new Token<String>(new String("i"+i)));
        }        
        
        fail("The test case is a prototype.");
    }
    
}
