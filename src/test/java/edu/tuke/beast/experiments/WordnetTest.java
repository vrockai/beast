/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tuke.beast.experiments;


import edu.tuke.beast.properties.PropertiesSingelton;
import edu.tuke.beast.wordnet.WordnetParser;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.data.list.PointerTargetTree;
import net.didion.jwnl.dictionary.Dictionary;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author blur
 */
public class WordnetTest {

    public WordnetTest() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testHello() throws JWNLException, FileNotFoundException {
        
        String wnPropPath = PropertiesSingelton.getInstance().getProperties().getProperty("wn.prop.path");
        System.out.println("Path to wordnet properties: " + wnPropPath);
        WordnetParser wp = new WordnetParser(wnPropPath);
        
        String word = "islander";
        
        System.out.println(word+ " "+wp.isSingle(word));
        for (Synset s : wp.getSiblings(POS.NOUN, word)){
            System.out.println(s);
        }

    }
}

