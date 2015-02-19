/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.experiments;

import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.token.Token;
import edu.tuke.beast.wikiparser.WikipediaParser;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import javax.naming.SizeLimitExceededException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author blur
 */
public class WikiCategoriesTest {

    public static String cortexPath = "/home/blur/pride/cortex2";
    public static Vector<Vector<String>> inputs = new Vector<Vector<String>>();

      public static  Beast beast;
     public static    Cortex cortex;
     public static    Lexicon<String> l;
    

    @BeforeClass
    public static void setUpClass() throws Exception {
         //beast = new Beast(cortexPath);
        // cortex = beast.getCortex();
        // cortex.setTreshold(1.5f);
        // l = beast.getLexicon();
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
    public void testFindContext() throws IOException, ClassNotFoundException, SizeLimitExceededException {

        String[] words = {"red", "green", "blue", "purple"};

        l = new Lexicon<String>();
        Map<String, Set<Integer>> categories = new TreeMap<String, Set<Integer>>();

        WikipediaParser htmlParse = new WikipediaParser(WikipediaParser.URL_WIKIPEDIA);
        for (String word : words) {
            htmlParse.setWord(word);
            htmlParse.parse();
/*
            System.out.println("");
            System.out.println(word+":");
            System.out.println(htmlParse.getCategories().keySet());
            System.out.println("");
*/
            categories.put(word, index(htmlParse.getCategories().keySet()));            
        }

        Set<Integer> inter = new HashSet<Integer>();
        inter.addAll(categories.get(words[0]));
        for (int i = 1; i < words.length; i++) {
            inter.retainAll(categories.get(words[i]));
        }

        /*  Integer c = l.getIndex(new Token<String>("Colors"));
        
        System.out.println("");
        System.out.println("");
        System.out.println(c);
        
        for (int i = 0; i < words.length; i++) {
            System.out.println(i+" : "+categories.get(words[i]).contains(c));
            System.out.println(categories.get(words[i]));

        }
*/
        for (Integer i : inter){
            System.out.println(l.getEntry(i).getValue());
        }

    }

    private Set<Integer> index(Set<String> set){
        Set<Integer> result = new HashSet<Integer>();

        if (set==null)
            return result;

        for(String s:set){
            //System.out.print(s+"\t");
            Token<String> t = new Token<String>(s.toLowerCase());
            l.addToken(t);
            result.add(l.getIndex(t));
        }

        return result;
    }
}



