/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.wordnet;

import edu.tuke.beast.lexicon.Lexicon;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerTarget;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.data.list.PointerTargetTree;
import net.didion.jwnl.data.list.PointerTargetTreeNode;
import net.didion.jwnl.data.list.PointerTargetTreeNodeList;
import net.didion.jwnl.dictionary.Dictionary;

/**
 *
 * @author blur
 */
public class WordnetParser {

    Dictionary dict;
    PointerUtils pu = PointerUtils.getInstance();

    public Dictionary getDict() {
        return dict;
    }
    Lexicon<String> lexicon;

    public boolean isSingle(String term) throws JWNLException{        
        boolean adj = isSingle(term,POS.ADJECTIVE);
        boolean adv = isSingle(term,POS.ADVERB);
        boolean nou = isSingle(term,POS.NOUN);
        boolean ver = isSingle(term,POS.VERB);
        System.out.println(adj+","+adv+","+nou+","+ver+",");
        return  adj && adv && nou && ver;
    }
    
    public boolean isSingle(String term, POS pos) throws JWNLException{
        Synset[] sss = getSynsets(term,pos);
            if ((sss == null)){ return true;}
            
                if (sss.length > 1){
                    
                return false;
            }
            return true;
    }
    
    public WordnetParser(String configPath) throws JWNLException, FileNotFoundException {
        JWNL.initialize(new FileInputStream(configPath));
        dict = Dictionary.getInstance();
    }

    public WordnetParser(String configPath, Lexicon<String> lexicon) throws JWNLException, FileNotFoundException {
        JWNL.initialize(new FileInputStream(configPath));
        this.lexicon = lexicon;
        dict = Dictionary.getInstance();
    }

    public Synset[] getSynsets(Integer t) throws JWNLException {
        if (t == null) {
            return new Synset[0];
        }
        return getSynsets(lexicon.getEntry(t).getValue());
    }

    public Synset[] getSynsets(String t) throws JWNLException {

        IndexWord iw = dict.getIndexWord(POS.NOUN, t);

        if (iw == null) {
            return null;
        }

        Synset[] set = iw.getSenses();

        return set;
    }
    
    public Synset[] getSynsets(String t, POS pos) throws JWNLException {

        IndexWord iw = dict.getIndexWord(pos, t);

        if (iw == null) {
            return null;
        }

        Synset[] set = iw.getSenses();

        return set;
    }

    public List<Word> getHypernyms(Integer t) throws JWNLException {
        return getHypernyms(lexicon.getEntry(t).getValue());
    }

    public List<Word> getHypernyms(String t) throws JWNLException {

        IndexWord iw = dict.getIndexWord(POS.NOUN, t);
        Synset[] set = iw.getSenses();
        List<Word> result = new ArrayList<Word>();

        for (Synset ss : set) {
            PointerTargetTree ptt = pu.getHypernymTree(ss);
            PointerTargetTreeNode pttn = ptt.getRootNode();
            result = getListFromNode(pttn, result);
        }

        return result;
    }

    public Synset[] getHypernyms(Synset s) throws JWNLException {

        //System.out.println(s + " -> ");

        PointerTarget[] pt = s.getTargets(PointerType.HYPERNYM);

        Synset[] ssr = new Synset[pt.length];

        int c = 0;
        for (PointerTarget p : pt) {
            ssr[c++] = (Synset) p;
        }
        return ssr;
    }

    protected List<Synset> getHypernymTree(Synset s, List<Synset> list) throws JWNLException {

        // System.out.println(s + " -> ");

        Synset[] ss = getHypernyms(s);
        //System.out.println(ss.length);

        for (Synset syns : ss) {
            list.add(syns);
            getHypernymTree(syns, list);
        }

        return list;
    }

    public List<Synset> getHypernymTree(Synset s) throws JWNLException {

        List<Synset> result = new ArrayList<Synset>();
        result.add(s);
        // System.out.println(s + " -> ");

        Synset[] ss = getHypernyms(s);
        //System.out.println(ss.length);

        for (Synset syns : ss) {

            getHypernymTree(syns, result);
        }

        return result;

    }
    /*
    public List<Word> getHypernymTree(String t) throws JWNLException {
    
    System.out.println(t+" -> ");
    
    
    Synset[] synsets = dict.getIndexWord(POS.NOUN, t).getSenses();
    List<Word> s = getHypernyms(t);
    
    for (Word w : s) {
    
    System.out.println(w.getLemma() + " " + w.getSynset());
    
    if (w.getSynset().getOffset() != 1740) {
    getHypernymTree(w.getLemma());
    }
    
    
    }
    
    return null;
    }
     */

    private Set<Word> listToSet(List<Word> list) {

        Set<Word> hset = new HashSet<Word>(list);
        for (Word w : list) {
            hset.add(w);
        }
        return hset;
    }

    public Set<Word> getNearestCommonHypernyms(String[] strings) throws JWNLException {

        if (strings.length == 0) {
            return null;
        }

        Set<Word> result = getCommonHypernyms(strings);

        return result;
    }

    private Integer getDepth(PointerTargetTreeNode pttn, Word hypernym, Integer depth) throws JWNLException {

        depth++;

        PointerTargetTreeNodeList pttnl = pttn.getChildTreeList();

        for (int i = 0; i < pttnl.size(); i++) {
            PointerTargetTreeNode node = (PointerTargetTreeNode) pttnl.get(i);
            System.out.println(depth + " --- " + node);
            //System.out.println("");

            Synset s = node.getSynset();

            for (Word w : s.getWords()) {
                System.out.println(w.getLemma());
                if (w.equals(hypernym)) {
                    return depth;
                }
            }
            System.out.println("");
            depth = getDepth(node, hypernym, depth);
        }

        return --depth;
    }

    private Integer getHypernymDepth(String token, Word hypernym) throws JWNLException {

        IndexWord iw = dict.getIndexWord(POS.NOUN, token);
        Synset[] set = iw.getSenses();
        //List<Word> result = new ArrayList<Word>();

        Integer depth = 0;
        Integer d = -1;

        for (Synset ss : set) {

            ss.containsWord(hypernym.getLemma());

            PointerTargetTree ptt = pu.getHypernymTree(ss);
            PointerTargetTreeNode pttn = ptt.getRootNode();

            depth = getDepth(pttn, hypernym, depth);

            if (d == -1) {
                d = depth;
            }

            d = d < depth ? d : depth;
        }

        return d;
    }

    public Synset getMostCommonParent(Synset s1, Synset s2) throws JWNLException {
        Synset result = null;

        List<Synset> l1 = getHypernymTree(s1);
        List<Synset> l2 = getHypernymTree(s2);

        for (Synset sl1 : l1) {
            for (Synset sl2 : l2) {
                if (sl1.equals(sl2)) {
                    return sl1;
                }
            }
        }

        return result;
    }

    public Set<Word> getCommonHypernyms(String[] strings) throws JWNLException {

        if (strings.length == 0) {
            return null;
        }

        Set<Word> result = listToSet(getHypernyms(strings[0]));

        for (int i = 1; i < strings.length; i++) {
            List<Word> hlist = getHypernyms(strings[i]);
            Set<Word> hset = listToSet(hlist);

            result.retainAll(hset);
        }

        return result;

    }

    private List<Word> getListFromNode(PointerTargetTreeNode pttn, List<Word> list) {

        PointerTargetTreeNodeList pttnl = pttn.getChildTreeList();

        for (int i = 0; i < pttnl.size(); i++) {
            PointerTargetTreeNode node = (PointerTargetTreeNode) pttnl.get(i);

            list = getListFromNode(node, list);

            Synset s = node.getSynset();

            for (Word w : s.getWords()) {
                list.add(w);
            }
        }

        return list;
    }

    public Lexicon<String> getLexicon() {
        return lexicon;
    }

    public void setLexicon(Lexicon<String> lexicon) {
        this.lexicon = lexicon;
    }

    public Synset getCommonParent(Synset s1, Synset s2) throws JWNLException {
        Synset result = null;

        PointerTargetTree t1 = pu.getHypernymTree(s1);
        PointerTargetTree t2 = pu.getHypernymTree(s2);

        System.out.println(t1);
        System.out.println(t1.getRootNode().getSynset().toString());
        System.out.println(t1.getRootNode().getParent().getSynset().toString());
        System.out.println("");
        System.out.println(t2);

        return result;
    }
    
    public List<PointerTargetNodeList> getHypernymPointerTargetNodeList(POS pos,String term) throws JWNLException {
        
        List<PointerTargetNodeList> result = new ArrayList<PointerTargetNodeList>();

        IndexWord indexWord = dict.getIndexWord(pos, term);
        Synset[] sl = indexWord.getSenses();
        
        for (Synset s : sl) {
            PointerTargetNodeList ptnl = PointerUtils.getInstance().getDirectHypernyms(s);
            result.add(ptnl);
        }
        
        return result;
    }

    public List<Synset> getSiblings(POS pos,String term) throws JWNLException {
        List<Synset> result = new ArrayList<Synset>();

        IndexWord indexWord = dict.getIndexWord(pos, term);
        
        if (indexWord!=null){
        Synset[] sl = indexWord.getSenses();        
        for (Synset s : sl) {
            PointerTargetNodeList ptnl = PointerUtils.getInstance().getCoordinateTerms(s);//PointerUtils.getInstance().getDirectHypernyms(s);
            
            Iterator i = ptnl.iterator();
	    
            while (i.hasNext()) {
                PointerTargetNode kuku = (PointerTargetNode) i.next();
                result.add(kuku.getSynset());
            }
            /*
            Iterator i = ptnl.iterator();
	    
            while (i.hasNext()) {
                PointerTargetNode ptn = (PointerTargetNode) i.next();
                Synset dhs = ptn.getSynset();
                PointerTargetNodeList hypoSets = PointerUtils.getInstance().getDirectHyponyms(dhs);
                Iterator j = hypoSets.iterator();
                while (j.hasNext()) {
                    PointerTargetNode kuku = (PointerTargetNode) j.next();
                    result.add(kuku.getSynset());
                };
            };
             * 
             */
        }
        }
        return result;
    }

    public static void main(String[] args) throws JWNLException, FileNotFoundException {
        String configpath = "/home/workspace/beast_maven/file_properties.xml";
        WordnetParser wp = new WordnetParser(configpath);

//        System.out.println(wp.getDict().getSynsetAt(POS.NOUN, 0));

        String[] words = {"yellow", "red", "ugh"};

        Synset[] synsets1 = wp.dict.getIndexWord(POS.NOUN, words[0]).getSenses();
        Synset[] synsets2 = wp.dict.getIndexWord(POS.NOUN, words[1]).getSenses();
        System.out.println("");
        List<Synset> l1 = wp.getHypernymTree(synsets1[0]);
        System.out.println("");
        System.out.println("");
        List<Synset> l2 = wp.getHypernymTree(synsets2[0]);
        /*
        for (Synset s : l1) {
        System.out.println(s);
        }
        
        for (Synset s : l2) {
        System.out.println(s);
        }
         */
        
        System.out.println(wp.getMostCommonParent(synsets1[0], synsets2[0]));

        /*
        Synset[] s1 = wp.getSynsets(words[0]);
        Synset[] s2 = wp.getSynsets(words[1]);
        
        wp.getCommonParent(s1[0], s2[0]);
        
        /*
        PointerUtils pu = PointerUtils.getInstance();
        
        for (String word : words) {
        System.out.println(word);
        Synset[] ss = wp.getSynsets(word);
        if (ss !=null)
        for (Synset s : ss) {
        System.out.println("-----");
        PointerTargetTree ptt = pu.getHypernymTree(s);
        
        //PointerTarget pt = ptt.
        ptt.findAll(s);
        System.out.println(ptt.getRootNode().getSynset().getGloss());
        for(Object o : ptt.toList()){
        PointerTargetNodeList ptnl = (PointerTargetNodeList)o;
        //System.out.println(ptnl);
        }
        
        System.out.println();
        System.out.println(s.getKey() + ", ");
        System.out.println("-----");
        }
        }
        
        
        /*
        wp.getHypernyms("yellow");
        
        Set<Word> set = wp.getCommonHypernyms(new String[]{"yellow", "blue", "orange", "brown", "pink"});
        
        Word color = null;
        
        for(Word w: set){
        System.out.println(w.getLemma());
        if (w.getLemma().equals("color"))
        color = w;
        }
        
        System.out.println("");
        System.out.println(color.getLemma());
        System.out.println("");
         */
        //System.out.println(wp.getHypernymDepth("yellow", color));
        //System.out.println(wp.getHypernymDepth("green", color));
        //System.out.println(wp.getHypernymDepth("blue", color));
        //System.out.println(wp.getHypernymDepth("red", color));

    }
}
