/*
 * ExcitedIndexTest.java
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
public class ExcitedIndexTest extends TestCase {
    
    public ExcitedIndexTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ExcitedIndexTest.class);
        
        return suite;
    }

    /**
     * Test of getValue method, of class edu.tuke.beast.token.ExcitedIndex.
     */
    public void testGetValue() {
        
        ExcitedIndex<Integer> instance = new ExcitedIndex<Integer>();
        
        ExcitedIndex expResult = null;
        Integer result = instance.getValue();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStrength method, of class edu.tuke.beast.token.ExcitedIndex.
     */
    public void testGetStrength() {
        
        ExcitedIndex instance = new ExcitedIndex();
        
        Double expResult = null;
        Double result = instance.getStrength();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class edu.tuke.beast.token.ExcitedIndex.
     */
    public void testToString() {
        
        ExcitedIndex instance = new ExcitedIndex();
        
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compareTo method, of class edu.tuke.beast.token.ExcitedIndex.
     */
    public void testCompareTo() {
        
        ExcitedIndex o = null;
        ExcitedIndex instance = new ExcitedIndex();
        
        int expResult = 0;
        int result = instance.compareTo(o);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class edu.tuke.beast.token.ExcitedIndex.
     */
    public void testHashCode() {
        
        ExcitedIndex instance = new ExcitedIndex();
        
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class edu.tuke.beast.token.ExcitedIndex.
     */
    public void testEquals() {
        
        Object object = null;
        ExcitedIndex instance = new ExcitedIndex();
        
        boolean expResult = true;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
