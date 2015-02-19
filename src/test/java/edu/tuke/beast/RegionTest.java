/*
 * RegionTest.java
 * JUnit based test
 *
 * Created on March 13, 2007, 11:29 PM
 */

package edu.tuke.beast;

import edu.tuke.beast.fascicle.Fascicle;
import junit.framework.*;
import java.io.*;

/**
 *
 * @author blur
 */
public class RegionTest extends TestCase {
    
    public RegionTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(RegionTest.class);
        
        return suite;
    }

    /**
     * Test of getFascicle method, of class edu.tuke.beast.Region.
     */
    public void testGetFascicle() {
        
        int i = 0;
        Region instance = new Region();
        
        Fascicle expResult = null;
        Fascicle result = instance.getFascicle(i);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTreshold method, of class edu.tuke.beast.Region.
     */
    public void testSetTreshold() {
        
        float treshold = 0.0F;
        Region instance = new Region();
        
        instance.setTreshold(treshold);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of serialize method, of class edu.tuke.beast.Region.
     */
    public void testSerialize() throws Exception {
        
        String fileName = "";
        Region instance = new Region();
        
        instance.serialize(fileName);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deSerialize method, of class edu.tuke.beast.Region.
     */
    public void testDeSerialize() throws Exception {
        
        String filename = "";
        
        Region expResult = null;
        Region result = null;
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class edu.tuke.beast.Region.
     */
    public void testToString() {
        
        Region instance = new Region();
        
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
