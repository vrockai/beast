/*
 * FascicleMatrixTest.java
 * JUnit based test
 *
 * Created on March 13, 2007, 11:29 PM
 */
package edu.tuke.beast.fascicle;

import javax.naming.SizeLimitExceededException;
import junit.framework.*;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import edu.tuke.beast.association.Association;
import edu.tuke.beast.token.ExcitedIndex;
import java.util.HashMap;

/**
 *
 * @author blur
 */
public class FascicleTest extends TestCase {

    public static int SIZE = 100;

    public FascicleTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(FascicleTest.class);

        return suite;
    }

    /**
     * Test of getTreshold method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testGetSetTreshold() {
        
        Fascicle fascicle = new Fascicle("F", SIZE);

        // default fascicle treshold is 1
        assertEquals(1.0, fascicle.getTreshold());

        fascicle.setTreshold(10.15);
        assertEquals(10.15, fascicle.getTreshold());

    }

    /**
     * Test of setTreshold method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testInverseMatrix() throws SizeLimitExceededException {
        

        Integer source = null;
        Integer target = null;
        Fascicle fs = new Fascicle("Fs", SIZE);
        Fascicle fi = new Fascicle("Fi", SIZE);

        fs.store(0, 0);
        fi.store(0, 0);

        assertEquals(fs.getSignificance(0, 0), fi.getSignificance(0, 0));

        fs.store(1, 0);
        fi.store(0, 1);

        assertEquals(fs.getSignificance(1, 0), fi.getSignificance(0, 1));

        fs.store(1, 0);
        fi.store(0, 1);
        fs.store(1, 0);
        fi.store(0, 1);
        fs.store(1, 0);
        fi.store(0, 1);

        assertEquals(fs.getSignificance(0, 1), fi.getSignificance(1, 0));

        fs.store(2, 1);
        fi.store(1, 2);

        assertEquals(fs.getSignificance(2, 1), fi.getSignificance(1, 2));

        fs.store(3, 2);
        fi.store(2, 3);

        assertEquals(fs.getSignificance(3, 2), fi.getSignificance(2, 3));
    }

    public void testSetTreshold() {
        

        Fascicle fascicle = new Fascicle("F", SIZE);

        // default fascicle treshold is 1
        assertEquals(1.0, fascicle.getTreshold());

        fascicle.setTreshold(10.15);
        assertEquals(10.15, fascicle.getTreshold());
    }

    /**
     * Test of getSourceTokenLearningEvents method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testGetSourceTokenLearningEvents() throws SizeLimitExceededException {
        

        Fascicle f = new Fascicle("F", SIZE);
        f.store(1, 2);
        assertEquals(1, f.getSourceTokenLearningEvents(1));

        f.store(1, 2);
        assertEquals(2, f.getSourceTokenLearningEvents(1));

        f.store(1, null);
        assertEquals(2, f.getSourceTokenLearningEvents(1));

        f.store(null, 1);
        assertEquals(2, f.getSourceTokenLearningEvents(1));

        f.store(null, null);
        assertEquals(2, f.getSourceTokenLearningEvents(1));

    }

    public void testFinverse() throws SizeLimitExceededException {
        
        Fascicle F = new Fascicle("F", SIZE);
        Fascicle Fi = new Fascicle("Fi", SIZE);

        int[] w = {0, 1, 2, 3, 4};

        for (int i = 0; i < w.length - 1; i++) {
            for (int j = i + 1; j < w.length; j++) {
                F.store(w[i], w[j]);
                Fi.store(w[j], w[i]);
            }
        }

        
    }

    /**
     * Test of getTargetTokenLearningEvents method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testGetTargetTokenLearningEvents() throws SizeLimitExceededException {
        


        Fascicle f = new Fascicle("F", SIZE);
        f.store(2, 1);
        assertEquals(1, f.getTargetTokenLearningEvents(1));

        f.store(2, 1);
        assertEquals(2, f.getTargetTokenLearningEvents(1));

        f.store(null, 1);
        assertEquals(2, f.getTargetTokenLearningEvents(1));

        f.store(1, null);
        assertEquals(2, f.getTargetTokenLearningEvents(1));

        f.store(null, null);
        assertEquals(2, f.getTargetTokenLearningEvents(1));
    }

    /**
     * Test of store method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testStore() throws SizeLimitExceededException {
        

        Integer[] t = {1, 2, 3};

        Fascicle fascicle = new Fascicle("F", SIZE);

        fascicle.store(t[0], t[1]);
        assertEquals(1, fascicle.getLearningEvents());
        assertEquals(1.0, fascicle.getWeigth(t[0], t[1]));

        fascicle.store(t[1], t[1]);
        assertEquals(2, fascicle.getLearningEvents());
        assertEquals(1.0, fascicle.getWeigth(t[0], t[1]));

        fascicle.store(t[1], null);
        assertEquals(2, fascicle.getLearningEvents());
        assertEquals(1.0, fascicle.getWeigth(t[0], t[1]));

        fascicle.store(null, t[1]);
        assertEquals(2, fascicle.getLearningEvents());
        assertEquals(1.0, fascicle.getWeigth(t[0], t[1]));

        fascicle.store(null, null);
        assertEquals(2, fascicle.getLearningEvents());
        assertEquals(1.0, fascicle.getWeigth(t[0], t[1]));

        fascicle.store(t[0], t[2]);
        assertEquals(3, fascicle.getLearningEvents());
        assertEquals(0.5, fascicle.getWeigth(t[0], t[1]));

        fascicle.store(t[1], t[0]);
        assertEquals(4, fascicle.getLearningEvents());
        assertEquals(0.5, fascicle.getWeigth(t[0], t[1]));

//        fail("verify math");
    }

    /**
     * Test of getAssociations method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testGetAssociations() throws SizeLimitExceededException {
        

        Fascicle f = new Fascicle("F", SIZE);

        int[] t = {1, 2, 3, 4, 5};

        Map<Integer, Integer> expR1 = new TreeMap<Integer, Integer>();
        Map<Integer, Integer> expR2 = new TreeMap<Integer, Integer>();
        Map<Integer, Integer> expR3 = new TreeMap<Integer, Integer>();

        f.store(t[0], t[1]);
        expR1.put(t[1], 1);

        f.store(t[0], t[2]);
        expR1.put(t[2], 1);
        expR3.put(t[0], 1);

        f.store(t[0], t[3]);
        expR1.put(t[3], 1);

        f.store(t[1], t[4]);
        expR2.put(t[4], 1);

        
        
        Map<Integer, Integer> result = f.getAssociations(t[0], true);
        assertEquals(expR1, result);

        result = f.getAssociations(t[1], true);
        assertEquals(expR2, result);

        result = f.getAssociations(t[2], false);
        assertEquals(expR3, result);
    }

    /**
     * Test of hasAssociation method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testHasAssociation() throws SizeLimitExceededException {
        

        Fascicle f = new Fascicle("F", SIZE);

        int[] t = {14, 24, 34, 44, 54};

        assertEquals(false, f.isAssociation(t[0], t[1]));
        f.store(t[0], t[1]);
        
        assertEquals(true, f.isAssociation(t[0], t[1]));
        f.store(t[0], t[2]);
        
        assertEquals(true, f.isAssociation(t[0], t[2]));
        f.store(t[2], t[1]);
        assertEquals(true, f.isAssociation(t[2], t[1]));
        assertEquals(false, f.isAssociation(t[2], t[0]));
    }

    /**
     * Test of getAssociation method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testGetAssociation() throws SizeLimitExceededException {
        

        Integer idx1 = null;
        Integer idx2 = null;
        Fascicle f = new Fascicle("F", SIZE);
        Association<Integer> expResult = null;


        f.store(0, 1);

        
        expResult = new Association<Integer>(0, 1, 1);

        Association<Integer> result = f.getAssociation(0, 1);
        
        assertEquals(expResult, result);

    }

    /**
     * Test of getTargetTokens method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testGetTargetTokens() throws SizeLimitExceededException {
        Integer[] t = {0, 1, 2, 3, 4};

        Fascicle fascicle = new Fascicle("F", SIZE);

        for (int i = 0; i < t.length; i++) {
            fascicle.store(t[0], t[i]);
        }

        Map<Integer, Double> expResult = new HashMap<Integer, Double>();
        for (int i = 0; i < t.length; i++) {
        
            expResult.put(t[i], 0.2);
        }

        Map<Integer, Double> result = fascicle.getTargetTokens(t[0], true);
        
        assertEquals(expResult, result);

    }

    /**
     * Test of getName method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testGetSetName() {
        

        Fascicle f = new Fascicle("F", SIZE);

        assertEquals("F", f.getName());

        f.setName("F2");

        assertEquals("F2", f.getName());

    }

    /**
     * Test of getLearningEvents method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testGetLearningEvents() throws SizeLimitExceededException {
        

        Integer[] t = {1, 2, 3};

        Fascicle fascicle = new Fascicle("F", SIZE);

        fascicle.store(t[0], t[1]);
        assertEquals(1, fascicle.getLearningEvents());
        assertEquals(1., fascicle.getWeigth(t[0], t[1]));

        fascicle.store(t[1], t[1]);
        assertEquals(2, fascicle.getLearningEvents());
        assertEquals(1., fascicle.getWeigth(t[0], t[1]));
    }

    /**
     * Test of toString method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testToString() {
        
    }

    /**
     * Test of serialize method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testSerialize() throws Exception {
        

        Integer[] t = {1, 2, 3};

        Fascicle f1 = new Fascicle("F", SIZE);
        Fascicle f2 = null;

        String filename = "F.fas";

        f1.store(t[0], t[1]);
        f1.serialize(filename);
        f2 = Fascicle.deSerialize(filename);
        assertEquals(f1.getLearningEvents(), f2.getLearningEvents());
        assertEquals(f1.getSourceTokenLearningEvents(t[0]), f2.getSourceTokenLearningEvents(t[0]));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(f1.getWeigth(t[i], t[j]), f2.getWeigth(t[i], t[j]));
            }
        }

        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                f1.store(t[k], t[l]);
                f1.serialize(filename);
                f2 = Fascicle.deSerialize(filename);
                assertEquals(f1.getLearningEvents(), f2.getLearningEvents());
                for (int i = 0; i < 3; i++) {
                    assertEquals(f1.getSourceTokenLearningEvents(t[0]), f2.getSourceTokenLearningEvents(t[0]));
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        assertEquals(f1.getWeigth(t[i], t[j]), f2.getWeigth(t[i], t[j]));
                    }
                }
            }
        }

        f1.store(t[1], t[1]);
        f1.serialize(filename);
        f2 = Fascicle.deSerialize(filename);
        assertEquals(f1.getLearningEvents(), f2.getLearningEvents());
        for (int i = 0; i < 3; i++) {
            assertEquals(f1.getSourceTokenLearningEvents(t[0]), f2.getSourceTokenLearningEvents(t[0]));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(f1.getWeigth(t[i], t[j]), f2.getWeigth(t[i], t[j]));
            }
        }

        f1.store(t[0], t[2]);
        f1.serialize(filename);
        f2 = Fascicle.deSerialize(filename);
        
        assertEquals(f1.getLearningEvents(), f2.getLearningEvents());
        for (int i = 0; i < 3; i++) {
            assertEquals(f1.getSourceTokenLearningEvents(t[0]), f2.getSourceTokenLearningEvents(t[0]));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(f1.getWeigth(t[i], t[j]), f2.getWeigth(t[i], t[j]));
            }
        }

    }

    /**
     * Test of deSerialize method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    // TODO spravit lepsie

    public void testDeSerialize() throws Exception {


        Fascicle f1 = new Fascicle("F", SIZE);
        Fascicle f2 = null;
        Integer[] t = {1, 2, 3};

        String filename = "Fs.fas";

        f1.store(t[0], t[1]);

        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                f1.store(t[k], t[l]);
            }
        }

        f1.store(t[1], t[1]);


        f1.store(t[0], t[2]);
        f1.serialize(filename);
        f2 = Fascicle.deSerialize(filename);

        assertEquals(f1, f2);

    }

    /**
     * Test of getSignificance method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testGetSignificance() throws SizeLimitExceededException {
        Integer source = null;
        Integer target = null;
        Fascicle f = new Fascicle("F", SIZE);

        f.store(0, 0);
        assertEquals(1.0, f.getSignificance(0, 0));
        assertEquals(0.0, f.getSignificance(0, 1));
        f.store(1, 0);
        assertEquals(1.0, f.getSignificance(0, 0));
        f.store(1, 0);
        assertEquals(1.0, f.getSignificance(0, 0));
        f.store(0, 0);
        assertEquals(1.0, f.getSignificance(0, 0));
        f.store(0, 0);
        assertEquals(1.0, f.getSignificance(0, 0));

        double expResult = 1.0;
        double result = f.getSignificance(0, 0);
        assertEquals(expResult, result);

    }

    /**
     * Test of getWeigth method, of class edu.tuke.beast.fascicle.FascicleMatrix.
     */
    public void testGetWeigth() throws SizeLimitExceededException {
        Integer[] t = {1, 2, 3};

        Fascicle fascicle = new Fascicle("F", SIZE);

        fascicle.store(t[0], t[1]);
        assertEquals(1, fascicle.getLearningEvents());
        assertEquals(1.0, fascicle.getWeigth(t[0], t[1]));

        fascicle.store(t[1], t[1]);
        assertEquals(2, fascicle.getLearningEvents());
        assertEquals(1.0, fascicle.getWeigth(t[0], t[1]));

        fascicle.store(t[0], t[1]);
        assertEquals(3, fascicle.getLearningEvents());
        assertEquals(1.0, fascicle.getWeigth(t[0], t[1]));
    }
}
