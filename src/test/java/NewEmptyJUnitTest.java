/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class NewEmptyJUnitTest {

    public NewEmptyJUnitTest() {
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

    @Test
    public void hello() {
    int a = 1;
    int b = 2;
    int c = 4;

    System.out.println((double)((a+b)/c));
    System.out.println((double)(a+b)/(c));
    System.out.println((a+b)/(double)(c));
    System.out.println((double)(a+b)/(double)(c));


    }

}