package edu.tuke.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;

public class TmpClass {

    public static void main(String s[]) throws MalformedURLException {
        String path = "/home/blur/Projects/Beast/WordNet-3.0/dict";
        URL url = new URL("file", null, path);
/*
        IDictionary dict = new Dictionary(url);
        dict.open();

        String wordstr = "yellow";
        IIndexWord idxWord = dict.getIndexWord(wordstr, POS.ADJECTIVE);
        IWordID wordID = idxWord.getWordIDs().get(0); // 1st meaning
        IWord word = dict.getWord(wordID);
        List<IWordID> synset = word.getRelatedWords();

        for (IWordID iword : synset) {
            System.out.println(iword.getLemma());
        }*/
    }
}
