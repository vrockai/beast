/*
 * AssociationTest.java
 * JUnit based test
 *
 * Created on March 13, 2007, 11:29 PM
 */

package edu.tuke.beast.association;

import edu.tuke.beast.token.Token;
import junit.framework.*;
import java.io.Serializable;

/**
 *
 * @author blur
 */
public class AssociationTest extends TestCase {
    
    public AssociationTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AssociationTest.class);
        
        return suite;
    }

    /**
     * Test of getSourceToken method, of class edu.tuke.beast.association.Association.
     */
    public void testGetSourceToken() {
        
        Association<Token> instance = new Association<Token>();
        
        Token expResult = null;
        Token result = instance.getSourceToken();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSourceToken method, of class edu.tuke.beast.association.Association.
     */
    public void testSetSourceToken() {
        
        Token sourceToken = null;
        Association<Token> instance = new Association<Token>();
        
        instance.setSourceToken(sourceToken);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTargetToken method, of class edu.tuke.beast.association.Association.
     */
    public void testGetTargetToken() {
        
        Association<Token> instance = new Association<Token>();
        
        Token expResult = null;
        Token result = instance.getTargetToken();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTargetToken method, of class edu.tuke.beast.association.Association.
     */
    public void testSetTargetToken() {
        
        Token targetToken = null;
        Association<Token> instance = new Association<Token>();
        
        instance.setTargetToken(targetToken);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStrength method, of class edu.tuke.beast.association.Association.
     */
    public void testGetStrength() {
        
        Association<Token> instance = new Association<Token>();
        
        double expResult = 0;
        double result = instance.getStrength();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStrength method, of class edu.tuke.beast.association.Association.
     */
    public void testSetStrength() {
        
        long strength = 0L;
        Association instance = new Association();
        
        instance.setStrength(strength);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
