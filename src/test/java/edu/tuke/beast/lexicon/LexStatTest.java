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
public class LexStatTest extends TestCase {
    
    public LexStatTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LexStatTest.class);
        
        return suite;
    }
    
    
    public void testLexStat(){
        LexStat ls = new LexStat();
        
        int t1 = 1;
        int t2 = 3;
        int t3 = 12;
        
        ls.addAsocValue(t1, 1);
        ls.addAsocValue(t1, 2);
        ls.addAsocValue(t1, 3);
        ls.addAsocValue(t1, 4);
        
        ls.addAsocValue(t2, 10);        
        ls.addAsocValue(t2, 30);
        ls.addAsocValue(t2, 40);
                
        ls.addAsocValue(t3, 21);        
        ls.addAsocValue(t3, 41);
        
        assertEquals(ls.getAsocData(t1).size(), 4);
        assertEquals(ls.getAsocData(t2).size(), 3);
        assertEquals(ls.getAsocData(t3).size(), 2);
        
        List<Integer> t1l = new ArrayList<Integer>();
        t1l.add(1);t1l.add(2);t1l.add(3);t1l.add(4);
        
        List<Integer> t2l = new ArrayList<Integer>();
        t2l.add(10);t2l.add(30);t2l.add(40);
        
        List<Integer> t3l = new ArrayList<Integer>();
        t3l.add(21);t3l.add(41);
        
        assertNotNull(ls.getAsocData(t1));
        assertNotNull(ls.getAsocData(t2));
        assertNotNull(ls.getAsocData(t3));
        
        assertEquals(ls.getAsocData(t1), t1l);
        assertEquals(ls.getAsocData(t2), t2l);
        assertEquals(ls.getAsocData(t3), t3l);
        
    }
            
    
}
