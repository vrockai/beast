/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast;

import java.util.ArrayList;
import java.util.List;
import edu.tuke.beast.lexicon.LexStat;
import java.util.Set;
import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.fascicle.Fascicle;
import edu.tuke.beast.input.Input;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.properties.PropertiesSingelton;
import edu.tuke.beast.token.Token;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Vector;

import javax.naming.SizeLimitExceededException;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.jfree.data.xy.XYSeries;
import static edu.tuke.beast.lexicon.Lexicon.NULL_TOKEN;

/**
 *
 * @author vrockai
 */
public class Beast implements Serializable {

    public static enum LearnStrategy {

        STRICT, FREE
    }
    public static final Logger logger = Logger.getRootLogger();
    private static final long serialVersionUID = 5848194047435785954L;
    private Cortex cortex;
    private XYSeries lexSeries = new XYSeries(0);
    private XYSeries lexSeries2 = new XYSeries(0);
    private XYSeries asocSeries = new XYSeries((double) 0);
    private XYSeries sigSeries = new XYSeries((double) 0);
    private boolean isLexiconLog = false;
    private boolean isCortexLog = false;
    private boolean measure = true;
    private boolean stop = false;
    private int step_l = 10000;

    public int getStep_l() {
        return step_l;
    }

    public void setStep_l(int step_l) {
        this.step_l = step_l;
    }

    public boolean isMeasure() {
        return measure;
    }

    public void setMeasure(boolean measure) {
        this.measure = measure;
    }
    public static PropertyChangeListener propertyChangeListener;
    private final Token[] tokenWindow = {NULL_TOKEN, NULL_TOKEN, NULL_TOKEN, NULL_TOKEN, NULL_TOKEN};
    private int numberOfValidWindowEvents = 0;
    int step = 100000;

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
    private Lexicon<String> lexicon;
    private Region region;
    LearnStrategy strategy = LearnStrategy.STRICT;

    public Cortex getCortex() {
        return cortex;
    }

    public XYSeries getAsocSeries() {
        return asocSeries;
    }

    public XYSeries getLexSeries() {
        return lexSeries;
    }

    public XYSeries getSigSeries() {
        return sigSeries;
    }

    public void setAsocSeries(XYSeries asocSeries) {
        this.asocSeries = asocSeries;
    }

    public void setLexSeries(XYSeries lexSeries) {
        this.lexSeries = lexSeries;
    }

    public void setLexSeries2(XYSeries lexSeries) {
        this.lexSeries2 = lexSeries;
    }

    public void setSigSeries(XYSeries sigSeries) {
        this.sigSeries = sigSeries;
    }

    public LearnStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(LearnStrategy strategy) {
        this.strategy = strategy;
    }

    public static PropertyChangeListener getPropertyChangeListener() {
        return propertyChangeListener;
    }

    public static void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        Beast.propertyChangeListener = propertyChangeListener;
    }

    public Beast() {
        this.region = new Region();
        this.lexicon = new Lexicon<String>();
        this.cortex = new Cortex(region);

        logger.debug("PROPERTY" + PropertiesSingelton.getInstance().getProperties().getProperty("wn.prop.path"));
    }

    public Beast(Lexicon<String> l, Region r) {
        this.lexicon = l;
        this.region = r;
        this.cortex = new Cortex(region);
    }

    public Beast(String path) throws IOException, ClassNotFoundException, SizeLimitExceededException {

        logger.info("Creating new beast: " + path);

        if (propertyChangeListener != null) {
            Region.addPropertyChangeListener(propertyChangeListener);
        }
        Region r = new Region();
        this.region = r.deSerialize(path + ".reg");
        this.lexicon = Lexicon.deSerialize(path + ".lex");
        this.cortex = new Cortex(region);
    }

    public Beast(Region r) {
        this.region = r;
        this.lexicon = new Lexicon<String>();
        //  this.cortex = new Cortex(region);
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
        this.cortex = new Cortex(region);
    }

    public Lexicon<String> getLexicon() {
        return lexicon;
    }

    public void setLexicon(Lexicon<String> lexicon) {
        this.lexicon = lexicon;
    }

    public void stop() {
        stop = true;
    }

    public Lexicon<String> buildLexicon(TokenStream tokenStream, int size) throws Exception {
        stop = false;
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("target/buildlex.log")));
        lexSeries.clear();
        lexSeries2.clear();

        long time = System.currentTimeMillis();
        logger.debug("start learn time" + time);
        lexicon = new Lexicon<String>();
        int counter = 0;
        int tokenNum = 0;
        long lastEntries = -1;
        org.apache.lucene.analysis.Token reusableToken = new org.apache.lucene.analysis.Token();

        for (org.apache.lucene.analysis.Token rawToken = tokenStream.next(reusableToken); rawToken != null; rawToken = tokenStream.next(reusableToken), ++counter) {

            if (stop) {
                break;
            }

            Token<String> token = (rawToken.term().equals(Input.NULL_TOKEN)) ? Lexicon.NULL_TOKEN : new Token<String>(rawToken.term());

            if (token != Lexicon.NULL_TOKEN) {
                lexicon.addToken(token);
                lexicon.addOccurence(token);
            }

            if (counter % step == 0) {                
                lexSeries.add(counter, lexicon.getEntriesCount());
                lexSeries.fireSeriesChanged();
                lexSeries2.add(counter, lexicon.getEntriesCount() - lastEntries);
                lexSeries2.fireSeriesChanged();
                lastEntries = lexicon.getEntriesCount();
            }
            
            tokenNum++;
        }

        out.close();
        lexicon = lexicon.top(size);
        time = System.currentTimeMillis() - time;
        logger.info("Building lexicon is finished. it took " + (time / 1000) + 's');

        lexicon.CORPUS_SIZE = tokenNum;
        
        return lexicon;

    }

    public Vector<Double> getCoverage(TokenStream tokenStream, Lexicon<String> lex) throws Exception {

        Vector<Double> result = new Vector<Double>();

        int known = 0;
        int windows = 0;

        stop = false;

        long time = System.currentTimeMillis();
        logger.debug("start learn time" + time);

        int counter = 0;
        org.apache.lucene.analysis.Token reusableToken = new org.apache.lucene.analysis.Token();
        for (org.apache.lucene.analysis.Token rawToken = tokenStream.next(reusableToken); rawToken != null; rawToken = tokenStream.next(reusableToken), ++counter) {
            if (stop) {
                break;
            }
            Token<String> token = (rawToken.term() == null) ? NULL_TOKEN : new Token<String>(rawToken.term());

            shiftWindow(token);

            int[] lwin = lex.lexiconizeWindow(tokenWindow);

            boolean validWindow = true;
            for (int k : lwin) {
                if (k == Lexicon.NULL_INDEX) {
                    validWindow = false;
                }
            }

            if ((lex.getIndex(token) != Lexicon.NULL_INDEX)) {
                known++;
                if (validWindow) {
                    windows++;
                }
            }
        }

        result.add(counter == 0 ? 0 : (double) known / (double) counter);
        result.add((double) windows);
        return result;

    }

    public Region learn(TokenStream tokenStream, Lexicon<String> lexicon, LearnStrategy strategy, boolean rebuildLex) throws Exception {

        stop = false;
        logger.debug("Lex size:" + lexicon.getSize());

        Set<Integer> lexSet = lexicon.getLexiconSet();

        Fascicle.SigStrategy ss = cortex.getRegion().getFascicle(0).getStrategy();
        
        double tresh = cortex.getTreshold();
        
        cortex = new Cortex(lexicon.getSize());
        cortex.setTreshold(tresh);
        cortex.getRegion().setFascicleStrategy(ss);

        if (rebuildLex) {
            lexicon.resetOccurences();
        }

        PrintWriter outDat = new PrintWriter(new BufferedWriter(new FileWriter("target/buildlea.dat")));
        sigSeries.clear();
        asocSeries.clear();
        long time = System.currentTimeMillis();

        int counter = 0;
        org.apache.lucene.analysis.Token reusableToken = new org.apache.lucene.analysis.Token();
        for (org.apache.lucene.analysis.Token rawToken = tokenStream.next(reusableToken); rawToken != null; rawToken = tokenStream.next(reusableToken), ++counter) {
            if (stop) {
                break;
            }
            Token<String> token = (rawToken.term() == null) ? NULL_TOKEN : new Token<String>(rawToken.term());
            
            shiftWindow(token);

            int[] lwin = lexicon.lexiconizeWindow(tokenWindow);
/*
            for(int i : lwin){
                System.out.print(i+ " ");
            }
            System.out.print("\n");
*/
            if (rebuildLex) {
                if (lwin[lwin.length - 1] != Lexicon.NULL_INDEX) {
                    lexicon.addOccurence(token);
                }
            }

            cortex.presentLearningWindow(lwin, strategy);

            if (tokenWindow[tokenWindow.length - 1] == NULL_TOKEN) {
                numberOfValidWindowEvents++;
            }

            if (counter % step == 0) {

                logger.trace("step fired");

                List<Double> v = new ArrayList<Double>();
                if (measure) {
                    List<Double> cor_fea = cortex.getCortexFeatures(lexicon);

                    //Vector<Double> cor_fea = cortex.getCortexFeatures(t1,t2);
                    v.add(cor_fea.get(0));
                    v.add(cor_fea.get(1));
                    outDat.println(counter+";"+cor_fea.get(0)+";"+cor_fea.get(1));
                } else {
                    v.add(1d);
                    v.add(2d);
                }

                asocSeries.add(counter, v.get(0));
                sigSeries.add(counter, v.get(1));
                asocSeries.fireSeriesChanged();
                sigSeries.fireSeriesChanged();
            }

        }
        outDat.close();
        time = System.currentTimeMillis() - time;
        logger.info("Building fascicles is finished. it took " + (time / 1000) + 's');
        region = cortex.getRegion();
        //logger.debug("REGION: " + region.toString());

        //System.out.println(cortex.getLexiconStatistics());

        return region;
    }

    private void shiftWindow(Token<String> newToken) {
        System.arraycopy(tokenWindow, 1, tokenWindow, 0, tokenWindow.length - 1);
        tokenWindow[tokenWindow.length - 1] = newToken;
    }

    public void serialize(String fileName) throws IOException {
        logger.info("serializing regions...");
        region.serialize(fileName + ".reg");
        logger.info("serializing lexicon...");
        lexicon.serialize(fileName + ".lex");
    }

    public Beast deSerialize(String fileName) throws Exception {
        logger.info("deserializing cortex...");

        if (propertyChangeListener != null) {
            logger.debug("adding prop change listener to region...");
            Region.addPropertyChangeListener(propertyChangeListener);
        }

        region = region.deSerialize(fileName + ".reg");
        lexicon = lexicon.deSerialize(fileName + ".lex");
        logger.info("deserializing lexicon...");

        return new Beast(lexicon, region);
    }
}
