package edu.tuke.beast.lexicon;

import edu.tuke.beast.input.BeastAnalyzer;
import edu.tuke.beast.input.Input;
import edu.tuke.beast.token.Token;

import java.io.*;
import java.util.*;
//import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;

/**
 * Trieda lexik?n sl??i na reprezent?ciu slovn?ka.
 */
public class Lexicon<E extends Comparable> implements Serializable {
    // ------------------------------ FIELDS ------------------------------
    private static final Logger logger = Logger.getRootLogger();
    private static final long serialVersionUID = 42L;    
    public static final Token NULL_TOKEN = new Token<String>(null);
    public static final int NULL_INDEX = Lexicon.NULL_INDEX;
    
    public long CORPUS_SIZE = 0;
    
    public Long indexCounter = 0l;
    private long occAll = -1;

    // ID - TOKEN pairs, the Fascicles then reference token through IDs
    public final Map<Integer, Token<E>> lexicon;

    // TOKEN - ID pairs as reverse lexikon for fast logic
    public final Map<Token<E>, Integer> lexiconInverse;

    // ID - occurence pairs.
    public final Map<Integer, Integer> occurences;

    private void countOccAll() {
        occAll = 0;
        for (Integer i : lexicon.keySet()) {
            occAll += occurences.get(i);
        }
    }

    public double getProb(int a) {
        if (occAll < 0) {
            countOccAll();
        }
        return occurences.get(a) / (double) occAll;
    }

    public int getSize(){
        return lexicon.size();
    }

    public Set<Integer> getLexiconSet(){
        return lexicon.keySet();
    }
    
    // -------------------------- STATIC METHODS --------------------------
    public static Lexicon<String> deSerialize(String filename) throws IOException {

        BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));

        //String line = (bfr.readLine()) == null ? "" : bfr.readLine();
        String line = bfr.readLine();
        Long indexcount = Long.decode(line);

        Map<Integer, Token<String>> lexicon = new HashMap<Integer, Token<String>>();
        Map<Token<String>, Integer> lexiconInverse = new HashMap<Token<String>, Integer>();
        Map<Integer, Integer> occurences = new HashMap<Integer, Integer>();

        while ((line = bfr.readLine()) != null) {
            String[] membs = line.split(",");
            lexicon.put(Integer.decode(membs[0]), new Token<String>(membs[1]));
            lexiconInverse.put(new Token<String>(membs[1]), Integer.decode(membs[0]));
            occurences.put(Integer.decode(membs[0]), Integer.decode(membs[2]));
        }
        @SuppressWarnings("unchecked")
        Lexicon<String> l = new Lexicon<String>(lexicon, lexiconInverse, occurences, indexcount);
        //NULL_INDEX = l.getIndex(NULL_TOKEN);

        bfr.close();

        logger.info("Lexicon deserialized: "+filename);

        return l;
    }

    // --------------------------- CONSTRUCTORS ---------------------------
    @SuppressWarnings("unchecked")
    public Lexicon() {
        lexicon = new TreeMap<Integer, Token<E>>();
        lexiconInverse = new TreeMap<Token<E>, Integer>();
        occurences = new TreeMap<Integer, Integer>();

        // lexicon is now initialised with the null token w/o occunces i.e.
        // lexicon.getOccurences(NULL_TOKEN) == 00
        addToken(NULL_TOKEN);

    //NULL_INDEX = getIndex(NULL_TOKEN);
    }

    public Lexicon(Map<Integer, Token<E>> l, Map<Token<E>, Integer> li, Map<Integer, Integer> oc, Long in) {
        lexicon = new HashMap<Integer, Token<E>>(l);
        lexiconInverse = new HashMap<Token<E>, Integer>(li);
        occurences = new HashMap<Integer, Integer>(oc);

        indexCounter = new Long(in);
    }

    // ------------------------ CANONICAL METHODS ------------------------
	/*
     * public Token<E> deLexiconize(Integer index) { return lexicon.get(index); }
     */
    // TODO nejako to nejde :)
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("i'm lexicon") ;
/*
        for (Map.Entry<Integer, Token<E>> integerTokenEntry : lexicon.entrySet()) {
            if (!integerTokenEntry.getKey().equals(lexiconInverse.get(NULL_TOKEN))) {
                out.append(integerTokenEntry.getKey()).append(',').append(integerTokenEntry.getValue()).append(',').append(getOccurences(integerTokenEntry.getValue())).append('\n');
            }
        }
*/
        return out.toString();
    }

    // -------------------------- OTHER METHODS --------------------------
    public void addToken(Token<E> token) {
        // check if tokenalready in
        // if there is an entry for this token already do nothing
        if ((token == null) || (lexiconInverse.containsKey(token))) {
        } else {
            Integer newIndex = (indexCounter++).intValue();
            lexicon.put(newIndex, token);
            lexiconInverse.put(token, newIndex);
            occurences.put(newIndex, 0);
        }
    }
    
    public void addOccurence(Token<E> token) throws Exception {

        if (lexiconInverse.containsKey(token)) {
            Integer key = lexiconInverse.get(token);
            occurences.put(key, (occurences.containsKey(key)) ? occurences.get(key) + 1 : 1);
        } else {
            throw new Exception("Token not in lexicon trying to add occurence.");
        }
    }

    private void setOccurences(Token<E> token, Integer value) throws Exception {

        if (lexiconInverse.containsKey(token)) {
            Integer key = lexiconInverse.get(token);
            occurences.put(key, value);
        } else {
            throw new Exception("Token not in lexicon trying to set occurence.");
        }
    }

    public boolean contains(E t) {
        return lexiconInverse.containsKey(new Token<E>(t));
    }
    
    public boolean contains(Token<E> t) {
        return lexiconInverse.containsKey(t);
    }

    public long getEntriesCount() {
        return lexicon.size();
    }

    public int getIndex(E token) {
        return getIndex(new Token<E>(token));
    }
    
    public int getIndex(Token<E> token) {
        // there is an entry for this token already, return the key for this
        // entry
        Integer i = lexiconInverse.get(token);
        return i == null ? NULL_INDEX : i;
    }

    public int getOccurences(int token) {
        return lexicon.containsKey(token) ? occurences.get(token) != null ? occurences.get(token) : 0 : 0;
    }

    public int getOccurences(Token<E> token) {// throws Exception {
        return lexiconInverse.containsKey(token) ? occurences.get(getIndex(token)) != null ? occurences.get(getIndex(token)) : 0 : 0;
    }

    public Map<Integer, Token<E>> getHashMap() {
        return lexicon;
    }

    public int[] lexiconizeWindow(Token<E>[] question) {
        int result[] = new int[question.length];
        for (int i = 0; i < question.length; i++) {
            result[i] = getIndex(question[i]);
        }
        return result;
    }

    public List<Integer> lexiconize(String input) throws IOException {
        List<Integer> result = new ArrayList<Integer>();

        //Token NULL_TOKEN = new Token(null);
        BeastAnalyzer ba = new BeastAnalyzer();
        TokenStream tokenStream = ba.tokenStream(input);
        org.apache.lucene.analysis.Token reusableToken = null;

        int counter = 1;

        for (org.apache.lucene.analysis.Token rawToken = tokenStream.next(reusableToken); rawToken != null; rawToken = tokenStream.next(reusableToken), ++counter) {

            Token<E> token = rawToken.term().equals(Input.NULL_TOKEN) ? NULL_TOKEN : new Token<String>(rawToken.term());

            if (!contains(token)) {
                result.add(NULL_INDEX);
            } else {
                result.add(getIndex(token));
            }
        }

        return result;
    }

    public Token<E>[] delexiconizeWindow(int[] question) {
        @SuppressWarnings("unchecked")
        Token<E> result[] = new Token[question.length];
        for (int i = 0; i < question.length; i++) {
            result[i] = getEntry(question[i]);
        }
        return result;
    }

    public void serialize(String fileName) throws IOException {

        OutputStream fos = new FileOutputStream(new File(fileName)+".lex");
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fos)));

        out.print(indexCounter + "\n");

        for (Map.Entry<Integer, Token<E>> integerTokenEntry : lexicon.entrySet()) {
            out.print(integerTokenEntry.getKey() + "," + integerTokenEntry.getValue() + ',' + occurences.get(integerTokenEntry.getKey()) + '\n');
        }

        out.close();

    // Old serialisation
		/*
     * FileOutputStream out = new FileOutputStream(fileName);
     * ObjectOutputStream s = new ObjectOutputStream(out);
     * s.writeObject(this); s.flush();
     */
    }

    public Token<E> getEntry(int indexToken) {
        if (indexToken == -1) {
            return null;
        }
        return lexicon.get(indexToken);
    }

    
    public Lexicon<E> top(int treshHold) throws Exception {

        Lexicon<E> newLexiconMap = new Lexicon<E>();
        newLexiconMap.addToken(NULL_TOKEN);

        Collection<Integer> topList;// = new ArrayList<Integer>();

        topList = getTopList(treshHold);

        for (Integer key : topList) {
            newLexiconMap.addToken(lexicon.get(key));
            newLexiconMap.setOccurences(lexicon.get(key), occurences.get(key));
        }

        return newLexiconMap;

    }

    public void resetOccurences(){
        for (Integer key: occurences.keySet())
            occurences.put(key, 0);
    }

    private Collection<Integer> getTopList( int treshHold) {

        class Record {

            private Integer key;
            private int val;

            public Integer getKey() {
                return key;
            }

            public int getVal() {
                return val;
            }

            public Record(Integer k, int v) {
                key = k;
                val = v;
            }
        }

        Comparator<Record> byValueComparator = new Comparator<Record>() {
            // defines the value sort order of Record

            @Override
            public int compare(Record o1, Record o2) {
                return (o2.getVal() - o1.getVal());
            }
        };

        HashMap<Integer, Record> tempMap = new HashMap<Integer, Record>();

        for (Map.Entry<Integer, Integer> integerIntegerEntry : occurences.entrySet()) {
            Integer key = integerIntegerEntry.getKey();            
            tempMap.put(key, new Record(key, integerIntegerEntry.getValue()));
        }

        ArrayList<Record> list = new ArrayList<Record>(tempMap.values()); // load
        // values
        Collections.sort(list, byValueComparator); // actual sort

        ArrayList<Integer> topList = new ArrayList<Integer>();

        for (int i = 0; i < list.size() && i < treshHold - 1; i++) {
            Integer key = list.get(i).getKey();
            topList.add(key);
        }
        return topList;
    }
}
