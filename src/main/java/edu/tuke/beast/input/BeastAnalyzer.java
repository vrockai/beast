package edu.tuke.beast.input;

import java.beans.PropertyChangeListener;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;

import java.io.Reader;
import java.io.StringReader;

public class BeastAnalyzer extends Analyzer {

    private PropertyChangeListener propertyChangeListener;

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.propertyChangeListener = pcl;
    }

    public TokenStream tokenStream(String text) {
        Reader r = new StringReader(text);
        BeastTokenizer result1 = new BeastTokenizer(r);
        return tokenStream(result1);
    }

    public TokenStream tokenStream(Reader[] readers) {
        BeastTokenizer result1 = new BeastTokenizer(readers);
        return tokenStream(result1);
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        BeastTokenizer result1 = new BeastTokenizer(reader);

        return tokenStream(result1);
    }

    private TokenStream tokenStream(BeastTokenizer result1) {
        result1.addPropertyChangeListener(propertyChangeListener);
        TokenStream result = new StandardFilter(result1);
        result = new LowerCaseFilter(result);
        return result;
    }
}
