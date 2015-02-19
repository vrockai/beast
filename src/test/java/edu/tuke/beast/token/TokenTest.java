/*
 * TokenTest.java
 * JUnit based test
 *
 * Created on March 13, 2007, 11:29 PM
 */

package edu.tuke.beast.token;

import junit.framework.*;
import java.io.Serializable;

/**
 *
 * @author blur
 */
public class TokenTest extends TestCase {
    
    public TokenTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TokenTest.class);
        
        return suite;
    }

    /**
     * Test of getValue method, of class edu.tuke.beast.token.Token.
     */
    public void testGetValue() {
        
        Token<Integer> token = new Token<Integer>(10);
        
        assertEquals(new Integer(10), token.getValue());
        
        Integer value = 20;
        token.setValue(value);
        
        assertEquals(value, token.getValue());
    }

    /**
     * Test of setValue method, of class edu.tuke.beast.token.Token.
     */
    public void testSetValue() {
        
        Token<Integer> token = new Token<Integer>(10);
        
        assertEquals(new Integer(10), token.getValue());
        
        Integer value = 20;
        token.setValue(value);
        
        assertEquals(value, token.getValue());
    }

    /**
     * Test of equals method, of class edu.tuke.beast.token.Token.
     */
    public void testEquals() {
        
        assertTrue((new Token<String>("hello")).equals(new Token<String>("hello")));
        assertTrue((new Token<Integer>(101)).equals(new Token<Integer>(101)));
        assertTrue((new Token<Object>(null)).equals(new Token<Object>(null)));
        
    }

    /**
     * Test of hashCode method, of class edu.tuke.beast.token.Token.
     */
    public void testHashCode() {
        
        Token<Object> tNull = new Token<Object>(null);
        
        assertEquals(-1, tNull.hashCode());
        
        Token<String> tString = new Token<String>("Hello!");
        
        assertEquals("Hello!".hashCode(), tString.hashCode());
    }

    /**
     * Test of toString method, of class edu.tuke.beast.token.Token.
     */
    public void testToString() {
        
        Token<String> tString = new Token<String>("Hello!");
                
        assertEquals("Hello!", tString.toString());
        
        Token<Integer> tInteger = new Token<Integer>(101);
        
        assertEquals("101", tInteger.toString());
    }

    /**
     * Test of compareTo method, of class edu.tuke.beast.token.Token.
     */
    public void testCompareTo() {
        
       Token<String> tString1 = new Token<String>("Hello!");
       Token<String> tString2 = new Token<String>("Fellow!");
                
        assertEquals("Hello!".hashCode()-"Fellow!".hashCode(), tString1.compareTo(tString2));
    }

    
}
