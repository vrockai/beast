/*
 * ExcitedTokenStrengthComparatorTest.java
 * JUnit based test
 *
 * Created on March 13, 2007, 11:29 PM
 */

package edu.tuke.beast.token;

import junit.framework.*;
import java.util.Comparator;

/**
 *
 * @author blur
 */
public class ExcitedTokenStrengthComparatorTest extends TestCase {
    
    public ExcitedTokenStrengthComparatorTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ExcitedTokenStrengthComparatorTest.class);
        
        return suite;
    }

    /**
     * Test of compare method, of class edu.tuke.beast.token.ExcitedTokenStrengthComparator.
     */
    public void testCompare() {
        
        ExcitedToken t1 = null;
        ExcitedToken t2 = null;
        ExcitedTokenStrengthComparator instance = new ExcitedTokenStrengthComparator();
        
        int expResult = 0;
        int result = instance.compare(t1, t2);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
