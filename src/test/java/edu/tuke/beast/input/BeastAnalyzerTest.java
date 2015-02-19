/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tuke.beast.input;

import java.beans.PropertyChangeListener;
import java.io.Reader;
import org.apache.lucene.analysis.TokenStream;
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
public class BeastAnalyzerTest {

    public BeastAnalyzerTest() {
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
     * Test of addPropertyChangeListener method, of class BeastAnalyzer.
     */
    @Test
    public void testAddPropertyChangeListener() {
        System.out.println("addPropertyChangeListener");
        PropertyChangeListener pcl = null;
        BeastAnalyzer instance = new BeastAnalyzer();
        instance.addPropertyChangeListener(pcl);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tokenStream method, of class BeastAnalyzer.
     */
    @Test
    public void testTokenStream_String() {
        System.out.println("tokenStream");
        String text = "";
        BeastAnalyzer instance = new BeastAnalyzer();
        TokenStream expResult = null;
        TokenStream result = instance.tokenStream(text);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tokenStream method, of class BeastAnalyzer.
     */
    @Test
    public void testTokenStream_ReaderArr() {
        System.out.println("tokenStream");
        Reader[] readers = null;
        BeastAnalyzer instance = new BeastAnalyzer();
        TokenStream expResult = null;
        TokenStream result = instance.tokenStream(readers);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tokenStream method, of class BeastAnalyzer.
     */
    @Test
    public void testTokenStream_String_Reader() {
        System.out.println("tokenStream");
        String fieldName = "";
        Reader reader = null;
        BeastAnalyzer instance = new BeastAnalyzer();
        TokenStream expResult = null;
        TokenStream result = instance.tokenStream(fieldName, reader);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}