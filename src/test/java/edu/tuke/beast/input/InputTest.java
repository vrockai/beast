/*
 * InputTest.java
 * JUnit based test
 *
 * Created on March 13, 2007, 11:29 PM
 */

package edu.tuke.beast.input;

import junit.framework.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.lucene.analysis.TokenStream;

/**
 *
 * @author blur
 */
public class InputTest extends TestCase {
    
    public InputTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(InputTest.class);
        
        return suite;
    }

    /**
     * Test of getTokenStream method, of class edu.tuke.beast.input.Input.
     */
    public void testGetTokenStream() throws Exception {
        
        Input instance = null;
        
        TokenStream expResult = null;
        TokenStream result = instance.getTokenStream();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
