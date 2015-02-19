package edu.tuke.beast.lexicon.test;

/**
 * Created by IntelliJ IDEA.
 * User: blur
 * Date: Nov 2, 2005
 * Time: 10:21:14 PM
 * To change this template use File | Settings | File Templates.
 */


import junit.framework.*;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.token.Token;

public class LexiconTest extends TestCase {


	public void testLexiconStorage() throws Exception{
		
		Lexicon<String> lexicon = new Lexicon<String>();
		
		Token<String> t1 = new Token<String>("mama");
		Token<String> t2 = new Token<String>("ma");
		Token<String> t3 = new Token<String>("malu");
		Token<String> t4 = new Token<String>("rozkosnu");
		Token<String> t5 = new Token<String>("emu");
		
		lexicon.addToken(t1);
		lexicon.addOccurence(t1);
		
		lexicon.addToken(t2);
		lexicon.addOccurence(t2);
		lexicon.addOccurence(t2);
		lexicon.addOccurence(t2);
		
		lexicon.addToken(t3);
		lexicon.addOccurence(t3);
		
		lexicon.addToken(t4);
		lexicon.addOccurence(t4);
		lexicon.addOccurence(t4);
		
		lexicon.addToken(t5);		
		lexicon.addOccurence(t5);
		
		// trying to add "bad" inputs
		lexicon.addToken(null);
		lexicon.addToken(t5);
		
		assertTrue(lexicon.getOccurences(t1) == 1);
		assertTrue(lexicon.getOccurences(t2) == 3);
		assertTrue(lexicon.getOccurences(t3) == 1);
		assertTrue(lexicon.getOccurences(t4) == 2);
		assertTrue(lexicon.getOccurences(t5) == 1);
		
	}

}