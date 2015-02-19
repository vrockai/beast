/*
 * ExcitedTokenTest.java
 * JUnit based test
 *
 * Created on March 13, 2007, 11:29 PM
 */

package edu.tuke.beast.token;

import junit.framework.*;

/**
 *
 * @author blur
 */
public class ExcitedTokenTest extends TestCase {
    
    public ExcitedTokenTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ExcitedTokenTest.class);
        
        return suite;
    }

    /**
     * Test of getStrength method, of class edu.tuke.beast.token.ExcitedToken.
     */
    public void testGetStrength() {
        
        ExcitedToken instance = null;
        
        float expResult = 0.0F;
        float result = instance.getStrength();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStrength method, of class edu.tuke.beast.token.ExcitedToken.
     */
    public void testSetStrength() {
        
        float strength = 0.0F;
        ExcitedToken instance = null;
        
        instance.setStrength(strength);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class edu.tuke.beast.token.ExcitedToken.
     */
    public void testToString() {
        
        ExcitedToken instance = null;
        
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compareTo method, of class edu.tuke.beast.token.ExcitedToken.
     */
    public void testCompareTo() {
        
        Token o = null;
        ExcitedToken instance = null;
        
        int expResult = 0;
        int result = instance.compareTo(o);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class edu.tuke.beast.token.ExcitedToken.
     */
    public void testHashCode() {
        
        ExcitedToken instance = null;
        
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class edu.tuke.beast.token.ExcitedToken.
     */
    public void testEquals() {
        
        Object object = null;
        ExcitedToken instance = null;
        
        boolean expResult = true;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clone method, of class edu.tuke.beast.token.ExcitedToken.
     */
    public void testClone() throws Exception {
        
        ExcitedToken instance = null;
        
        Object expResult = null;
        Object result = instance.clone();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
