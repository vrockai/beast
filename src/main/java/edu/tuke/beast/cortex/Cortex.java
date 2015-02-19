package edu.tuke.beast.cortex;

import edu.tuke.beast.*;
import edu.tuke.beast.association.Association;
import edu.tuke.beast.fascicle.Fascicle;
import edu.tuke.beast.lexicon.LexStat;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.token.*;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.*;

import static edu.tuke.beast.lexicon.Lexicon.NULL_INDEX;

import javax.naming.SizeLimitExceededException;
import org.apache.log4j.Logger;

// TODO check the security, cortex uses NULL_INDEX witch is relevant only when new LexiconMap is created
public class Cortex {

    public static Logger logger = Logger.getRootLogger();
    private static final int WINDOW_SIZE = 5;
    private double treshold = 1;
    private static PropertyChangeListener propertyChangeListener;
    private static ConsensusStrategy strategy = ConsensusStrategy.MIN;
    private Region region;
    private LexStat lexiconStatistics = new LexStat();

    public LexStat getLexiconStatistics() {
        return lexiconStatistics;
    }

    public void setLexiconStatistics(LexStat lexiconStatistics) {
        this.lexiconStatistics = lexiconStatistics;
    }

    public static enum ConsensusStrategy {

        MIN, AVERAGE, MAX, STR1, STR2, EXP
    }

    public static ConsensusStrategy getStrategy() {
        return strategy;
    }

    public static void setStrategy(ConsensusStrategy strategy) {
        Cortex.strategy = strategy;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region r) {
        region = r;
    }

    public static void addPropertyChangeListener(PropertyChangeListener pcl) {
        Cortex.propertyChangeListener = pcl;
    }

    public Vector<Integer> initWindow() {

        int nulli = Lexicon.NULL_INDEX;
        Vector<Integer> question = new Vector<Integer>();
        for (int i = 0; i < WINDOW_SIZE; i++) {
            question.add(nulli);
        }
        return question;
    }

    public int getAsocCount(Integer t, Set<Integer> ls) {
        int asocCount = 0;

        for (Fascicle f : region.fascicle) {
            asocCount += f.getAsocCount(t, ls);
        }

        return asocCount;
    }

    public List<Double> getCortexFeatures(Lexicon<String> lexicon) {
        List<Double> result = new ArrayList<Double>();

        int asoc_s = 0;
        double wei_s = 0;
        
        for (Integer tok : lexicon.getLexiconSet()) {
            
            int asoc = 0;
            double wei = 0;
        
            for (Fascicle f : region.fascicle) {
                Vector<Double> v = f.getTokenFeatures(tok);
                asoc += v.get(0);
                wei += v.get(1);                
            }
            
            lexiconStatistics.addAsocValue(tok, asoc);
            asoc_s += asoc;
            wei_s += wei;
            
        }

        result.add((double) asoc_s);
        result.add(wei_s);
        
        return result;
    }

    public Vector<Double> getCortexFeatures(Integer t1, Integer t2) {
        Vector<Double> result = new Vector<Double>();

        double asoc = 0, sig = 0;
        Fascicle f = region.fascicle[0];
        //for (Fascicle f : region.fascicle) {
        asoc += f.getSignificance(t1, t2);
        sig += f.getWeigth(t1, t2);
        //}

        result.add(asoc);
        result.add(sig);
        return result;
    }

    public double getTreshold() {
        return treshold;
    }

    public void setTreshold(double treshold) {
        this.treshold = treshold;
        region.setTreshold(treshold);
    }
    private int learningEvents = 0; // number of processed association storages

    public Cortex() {
        region = new Region();
    }

    public Cortex(Region reg) {
        this.region = reg;
    }

    public Cortex(int size) {
        this.region = new Region(size);
    }

    public boolean presentLearningWindow(int[] tokens, Beast.LearnStrategy str) throws SizeLimitExceededException {
        switch (str) {
            case STRICT:
                return presentLearningWindowStrict(tokens);
            case FREE:
                return presentLearningWindowFree(tokens);
            default:
                return presentLearningWindowStrict(tokens);
        }
    }

    public boolean presentLearningWindowStrict(int[] tokens) throws SizeLimitExceededException {


        // ucime len ak najpravejsi token v okne nie je NULL
        // ucime asociacie z kazdeho source tokenu takeho ze medzi tymto source
        // tokenom a target tokenom sa nenachazdza ziaden NULL token (mame tzv
        // admissibke string)
        // priklady:
        // a b c d e admisible string je cele okno a b c d e, ucime teda
        // asociacie (a e), (b e), (c e), (d e)
        // N a b c d admissible string je a b c d, ucime (a d) (b d) (c d)
        // N N b d N nemame admissible string
        // a N N a b admissible string je (a b) ucime (a b)
        // N N a b c (a b c), ucime (a c) (b c)

        Integer targetToken = tokens[WINDOW_SIZE - 1];
        // ak ho nenajde v slovniku, povazuje ho za nulltoken
        if (targetToken < 0) {
            targetToken = NULL_INDEX;
        }

        if (targetToken.equals(NULL_INDEX)) {
            return false;

        } else {

            for (int i = WINDOW_SIZE - 2; i >= 0; i--) {
                Integer sourceToken = tokens[i]; // ideme od konca

                // ak ho nenajde v slovniku, je to pre nas tnull token

                if (sourceToken < 0) {
                    sourceToken = NULL_INDEX;
                }

                if (sourceToken.equals(NULL_INDEX)) {
                    return false; // skoncil admissible string (natrafili sme na null
                    // token)

                } else {
                    region.getFascicle(3 - i).store(sourceToken, targetToken);                    
                }
                learningEvents++;
            }
        }

        return true;
    }

    
    
    public boolean presentLearningWindowFree(int[] tokens) throws SizeLimitExceededException {

        Integer targetToken = tokens[WINDOW_SIZE - 1];

        if (targetToken < 0) {
            targetToken = NULL_INDEX;
        }

        if (targetToken.equals(NULL_INDEX)) {
            return false;

        } else {

            for (int i = WINDOW_SIZE - 2; i >= 0; i--) {
                Integer sourceToken = tokens[i];

                if (sourceToken.equals(NULL_INDEX) || targetToken.equals(NULL_INDEX)) {
                    continue;
                }

                region.getFascicle(3 - i).store(sourceToken, targetToken);

                learningEvents++;
            }
        }

        return true;
    }

    public int getLearningEvents() {
        return learningEvents;
    }
    /*
    @Deprecated
    private SortedSet<ExcitedIndex<Integer>> honeALL(SortedSet<ExcitedIndex<Integer>>... factSets) {
    
    SortedSet<ExcitedIndex<Integer>> answers = new TreeSet<ExcitedIndex<Integer>>(new ExcitedIndexStrengthComparator());
    
    if ((factSets != null) && (factSets.length > 1)) {
    
    if (factSets[0] == null) {
    return null;
    }
    
    answers.addAll(factSets[0]);
    
    for (int i = 1; i < factSets.length; i++) {
    SortedSet<ExcitedIndex<Integer>> factSet = factSets[i];
    answers = joinExcitedSetsOld(answers, factSet, 0);
    if (answers == null) {
    return null;
    }
    }
    
    if (answers.size() == 0) {
    return null;
    }
    return answers;
    
    } else if (factSets.length == 1) {
    return factSets[0];
    } else {
    return null;
    }
    }
     */

    public Map<Integer, Double> maxMin(Vector<Map<Integer, Double>> factSets) {

        Map<Integer, Double> answers = new HashMap<Integer, Double>();

        if (factSets.size() > 1) {

            answers = new HashMap<Integer, Double>(factSets.get(0));

            for (int i = 1; i < factSets.size(); i++) {
                Map<Integer, Double> factSet = factSets.get(i);
                answers = maxMinSets(answers, factSet);
            }
        }

        return answers;

    }

    public Map<Integer, Double> maxAvg(Vector<Map<Integer, Double>> factSets) {

        Map<Integer, Double> answers = new HashMap<Integer, Double>();

        if (factSets.size() > 1) {
            
            Set<Integer> allSet = new HashSet<Integer>();
            
            for (Map<Integer, Double> v: factSets){
                allSet.addAll(v.keySet());
            }
            
            for(Integer i : allSet){
                double sum = 0;
                for (Map<Integer, Double> v: factSets){
                    sum += v.get(i) != null ? v.get(i) : 0;
                }
                answers.put(i, sum/(double)factSets.size());
            }
            
        }

        return answers;

    }

    
    public Map<Integer, Double> maxMinSets(Map<Integer, Double> exctitedSet1, Map<Integer, Double> exctitedSet2) {

        Map<Integer, Double> result = new HashMap<Integer, Double>();

        if (exctitedSet1.isEmpty()) {
            return result;
        }

        if (exctitedSet2.isEmpty()) {
            return result;
        }

        Set<Integer> tokenSet1 = exctitedSet1.keySet();
        Set<Integer> tokenSet2 = exctitedSet2.keySet();
        Set<Integer> tokenSet = new HashSet<Integer>();

        tokenSet.addAll(tokenSet1);
        tokenSet.retainAll(tokenSet2);

        for (Integer token : tokenSet) {

            double w1 = exctitedSet1.get(token);
            double w2 = exctitedSet2.get(token);

            double w = getStrenght(w1, w2, strategy);
            result.put(token, w);
        }

        return result;
    }
    /*
    @Deprecated
    public SortedSet<ExcitedIndex<Integer>> maxMinSetsOld(Set<ExcitedIndex<Integer>> exctitedSet1, Set<ExcitedIndex<Integer>> exctitedSet2, int mode) {
    
    SortedSet<ExcitedIndex<Integer>> result = new TreeSet<ExcitedIndex<Integer>>(new ExcitedIndexStrengthComparator());
    
    if (exctitedSet1.size() == 0) {
    return result;
    }
    
    if (exctitedSet2.size() == 0) {
    return result;
    }
    
    Set<Integer> tokenSet1 = unexcite(exctitedSet1);
    Set<Integer> tokenSet2 = unexcite(exctitedSet2);
    Set<Integer> tokenSet = new TreeSet<Integer>();
    
    tokenSet.addAll(tokenSet1);
    tokenSet.retainAll(tokenSet2);
    
    for (Integer token : tokenSet) {
    
    double weight = 0;
    
    for (ExcitedIndex<Integer> et : exctitedSet1) {
    if (et.getValue().equals(token)) {
    weight = et.getStrength();
    break;
    }
    }
    
    for (ExcitedIndex<Integer> et : exctitedSet2) {
    if (et.getValue().equals(token)) {
    weight = (et.getStrength() < weight) ? et.getStrength() : weight;
    break;
    }
    }
    
    result.add(new ExcitedIndex<Integer>(token, weight));
    }
    
    return result;
    }
     */

    public Map<Integer, Double> toMap(Set<ExcitedIndex<Integer>> set) {
        Map<Integer, Double> result = new HashMap<Integer, Double>();

        for (ExcitedIndex<Integer> ei : set) {
            result.put(ei.getValue(), ei.getStrength());
        }

        return result;
    }

    private double getStrenght(double s1, double s2, ConsensusStrategy strategy) {
        double s = 0.;

        switch (strategy) {
            case MIN:
                s = Math.min(s1, s2);
                break;
            case MAX:
                s = Math.max(s1, s2);
                break;
            case AVERAGE:
                s = (s1 + s2) / 2.;
                break;
            case STR1:
                s = Math.max(s1, s2) - Math.min(s1, s2);
                break;

            case STR2:
                s = Math.max(s1, s2) / Math.min(s1, s2);
                break;

            case EXP:
                s = s1 * s2;
                break;
        }

        return s;
    }
    /*
    @Deprecated
    public SortedSet<ExcitedIndex<Integer>> joinExcitedSetsOld(SortedSet<ExcitedIndex<Integer>> exctitedSet1, SortedSet<ExcitedIndex<Integer>> exctitedSet2, int mode) {
    
    if (exctitedSet1.size() == 0) {
    return exctitedSet2;
    }
    if (exctitedSet2.size() == 0) {
    return exctitedSet1;
    }
    
    SortedSet<ExcitedIndex<Integer>> result = new TreeSet<ExcitedIndex<Integer>>(new ExcitedIndexStrengthComparator());
    
    Map<Integer, Double> tmap1 = toMap(exctitedSet1);
    Map<Integer, Double> tmap2 = toMap(exctitedSet2);
    Set<Integer> tokenSet = new TreeSet<Integer>();
    
    tokenSet.addAll(tmap1.keySet());
    tokenSet.addAll(tmap2.keySet());
    
    for (Integer token : tokenSet) {
    double s1 = tmap1.get(token);
    double s2 = tmap2.get(token);
    
    double s = getStrenght(s1, s2, strategy);
    
    result.add(new ExcitedIndex<Integer>(token, s));
    }
    
    return result;
    }
     */

    public Map<Integer, Double> joinExcitedSets(Map<Integer, Double> tmap1, Map<Integer, Double> tmap2) {

        if (tmap1.isEmpty()) {
            return tmap2;
        }
        if (tmap2.isEmpty()) {
            return tmap1;
        }

        Map<Integer, Double> result = new TreeMap<Integer, Double>();

        Set<Integer> tokenSet = new TreeSet<Integer>();

        tokenSet.addAll(tmap1.keySet());
        tokenSet.addAll(tmap2.keySet());

        for (Integer token : tokenSet) {
            double s1 = tmap1.get(token);
            double s2 = tmap2.get(token);

            double s = getStrenght(s1, s2, strategy);

            result.put(token, s);
        }

        return result;
    }

    public int isNgramConsensus(Vector<Integer> inputWindow) {

        int wsize = WINDOW_SIZE;

        Vector<Integer> window = new Vector<Integer>(inputWindow);
        if (inputWindow.size() != wsize) {
            window = sliceWindow(inputWindow);
            wsize = window.size();
        }

        for (int i = 0; i < wsize; i++) {
            if (window.get(i) == Lexicon.NULL_INDEX) {
                wsize = i;
                break;
            }
        }

        for (int i = 0; i < wsize - 1; i++) {
            for (int j = i + 1; j < wsize; j++) {
                boolean bAsoc = region.getFascicle(j - i - 1).isAssociation(window.get(i), window.get(j));
                if (!bAsoc) {
                    return i + 1;
                }
            }
        }
        return wsize;
    }

    public SortedSet<Integer> unexcite(Set<ExcitedIndex<Integer>> tokens) {

        SortedSet<Integer> rtokens = new TreeSet<Integer>();

        for (ExcitedIndex<Integer> eToken : tokens) {
            rtokens.add(eToken.getValue());
        }

        return rtokens;
    }

    public Vector<Map<Integer, Double>> getConsensusSets(Vector<Integer> phrase) {

        Vector<Map<Integer, Double>> result = new Vector<Map<Integer, Double>>();

        Vector<Map<Integer, Map<Integer, Double>>> candidates = new Vector<Map<Integer, Map<Integer, Double>>>();

        for (int i = 0; i < phrase.size(); i++) {

            Map<Integer, Map<Integer, Double>> c_map = new HashMap<Integer, Map<Integer, Double>>();

            Integer t_i = phrase.get(i);

            if (!t_i.equals(NULL_INDEX)) {
                candidates.add(c_map);
                continue;
            }

            for (int j = 0; j < phrase.size(); j++) {
                int c_dis = j - i;

                if (c_dis == 0) {
                    continue;
                }

                Integer t_j = phrase.get(j);

                if (t_j.equals(NULL_INDEX)) {
                    continue;
                }

                boolean dir = c_dis > 0;

                Fascicle f = region.getFascicle(Math.abs(c_dis) - 1);

                Map<Integer, Double> semiCandidates = f.getTargetTokens(t_j, !dir);

                c_map.put(t_j, semiCandidates);
            }

            candidates.add(c_map);
        }



        for (int i = 0; i < phrase.size(); i++) {

            Integer t_i = phrase.get(i);

            if (!t_i.equals(NULL_INDEX)) {
                result.add(new HashMap<Integer, Double>());
                continue;
            }

            Map<Integer, Map<Integer, Double>> c_map = candidates.get(i);

            Vector<Map<Integer, Double>> v = new Vector<Map<Integer, Double>>();

            for (Map<Integer, Double> m : c_map.values()) {
                v.add(m);
            }

            Map<Integer, Double> semi_result = maxMin(v);

            result.add(semi_result);

        }

        return result;

    }

    public Map<Integer, Double>[] getConsensusALLList(Integer[] qInt) {

        Map<Integer, Double>[] result = new HashMap[qInt.length];

        int NULL_INDEX = Lexicon.NULL_INDEX;

        // i - index of current question token
        for (int i = 0; i < qInt.length; i++) {

            Integer t = qInt[i];
            ArrayList<Map<Integer, Double>> inter = new ArrayList<Map<Integer, Double>>();

            if (t.equals(NULL_INDEX)) {
                // Firing straight fascicles for left tokens
                for (int j = 0; j < i; j++) {
                    if (qInt[j].equals(NULL_INDEX)) {
                        continue;
                    }
                    Fascicle f = region.getFascicle(i - j - 1);
                    Map<Integer, Double> targetTokens = f.getTargetTokens(qInt[j], true);
                    inter.add(targetTokens);
                }

                // Firing inverse fascicles for right tokens
                for (int j = i + 1; j < qInt.length; j++) {
                    if (qInt[j].equals(NULL_INDEX)) {
                        continue;
                    }
                    Fascicle f = region.getFascicle(j - i - 1);
                    Map<Integer, Double> targetTokens = f.getTargetTokens(qInt[j], false);
                    inter.add(targetTokens);
                }

                Vector<Map<Integer, Double>> assumedNew = new Vector<Map<Integer, Double>>();
                for (Map<Integer, Double> eTokens : inter) {
                    Map<Integer, Double> mapx = new HashMap<Integer, Double>();
                    for (Map.Entry<Integer, Double> ei : eTokens.entrySet()) {
                        mapx.put(ei.getKey(), ei.getValue());
                    }

                    assumedNew.add(mapx);
                }

                Map<Integer, Double> consensus = maxMin(assumedNew);

                if (consensus == null) {
                    result[i] = new HashMap<Integer, Double>();
                } else {
                    result[i] = consensus;
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- ctx ---\n");
        sb.append(region.getFascicle(0)).append('\n');
        sb.append(region.getFascicle(1)).append('\n');
        sb.append(region.getFascicle(2)).append('\n');
        sb.append(region.getFascicle(3)).append('\n');
        sb.append("--- ctx ---\n");
        return sb.toString();
    }

    public boolean hasAssociation(int i, int source, int target) {
        return region.getFascicle(i).isAssociation(source, target);
    }

    public Association<Integer> getAssociation(int i, int source, int target) {

        return region.getFascicle(i).getAssociation(source, target);

    }

    private boolean containsExcitedToken(Set<ExcitedToken<Integer>> bigger, ExcitedToken<Integer> token, boolean threshold) {
        for (ExcitedToken<Integer> t : bigger) {
            if (token.getValue().equals(t.getValue())) {
                return threshold ? token.getStrength() >= t.getStrength() : true;
            }
        }
        return false;
    }

    public Map<Integer, Double> getAnswers(Integer[] question) {

        int NULL_INDEX = Lexicon.NULL_INDEX;
        Map<Integer, Double> result;

        // fire non null token association
        result = region.fascicle[0].getTargetTokens(question[question.length - 1], true);
        for (int i = question.length - 2; i >= 0; i--) {
            if (question[i] != NULL_INDEX) {
                Map<Integer, Double> r2 = region.fascicle[question.length - 1 - i].getTargetTokens(question[i], true);
                result = maxMinSets(result, r2);
            }
        }

        return result;
    }

    /*
     * Intersection set operation with min on weights an Intersection between
     * the ExcitedIndexSets. A intersetcs B = C, where C contains tokens with
     * min strenght.
     */
    // TODO same as HoneAll
    SortedSet<ExcitedIndex<Integer>> getSameALL(Set<ExcitedIndex<Integer>> tokensetA, Set<ExcitedIndex<Integer>> tokensetB, int mode) {

        SortedSet<ExcitedIndex<Integer>> result = new TreeSet<ExcitedIndex<Integer>>(new ExcitedIndexStrengthComparator());

        if (tokensetA.isEmpty() || tokensetB.isEmpty()) {
            return result;
        }

        for (ExcitedIndex<Integer> iToken : tokensetA) {
            for (ExcitedIndex<Integer> jToken : tokensetB) {
                if (iToken.getValue().equals(jToken.getValue())) {

                    ExcitedIndex<Integer> eToken;

                    eToken = mode == 0 ? new ExcitedIndex<Integer>(iToken.getValue(), Math.max(iToken.getStrength(), jToken.getStrength())) : new ExcitedIndex<Integer>(iToken.getValue(), Math.min(iToken.getStrength(), jToken.getStrength()));

                    result.add(eToken);
                    break;
                }
            }
        }

        return result;
    }

    public Vector<Integer> sliceWindow(Vector<Integer> inputWindow) {
        Vector<Integer> iw = initWindow();

        for (int i = 0; i < inputWindow.size(); i++) {
            iw.set(i, inputWindow.get(i));
        }

        return iw;
    }

    public void serialize(String fileName) throws IOException {
        System.out.println("serializing regions...");
        region.serialize(fileName + ".reg");
        System.out.println("serializing lexicon...");

    }

    public Cortex deSerialize(String fileName) throws Exception {
        System.out.println("deserializing cortex...");

        if (propertyChangeListener != null) {
            Region.addPropertyChangeListener(propertyChangeListener);
        }

        Cortex cortex = new Cortex(region.deSerialize(fileName + ".reg"));
        System.out.println("deserializing lexicon...");

        return cortex;
    }

    //TODO logic missing
    public boolean isAssociation(Vector<Integer> v1, Vector<Integer> v2) {

        boolean result = true;

        int v1s = v1.size();
        int v2s = v2.size();
        int rs = region.getSize();

        for (int j = 0; j < v2s && j < rs; j++) {
            for (int i = (v1s - 1); i < 0 && i < rs; i--) {
                Integer t1 = v1.get(v1s - i);
                Integer t2 = v2.get(j);

                Fascicle f = region.getFascicle(j - i);
                result = f.isAssociation(t1, t2);
            }
        }

        return result;

    }

    public Map<Integer, Double> getTargetTokens(Set<Integer> source, int f, boolean dir) {

        Map<Integer, Double> result = new HashMap<Integer, Double>();

        Fascicle fas = region.getFascicle(f);

        int c = 0;
        for (Integer i : source) {
            if (c == 0) {
                result = fas.getTargetTokens(i, dir);
            } else {
                maxMinSets(fas.getTargetTokens(i, dir), result);
            }
            c++;
        }

        return result;
    }

    //TODO check
    public SortedSet<HashMap<Vector<Integer>, Double>> getTargetAssociations(Vector<Integer> source, int n) {

        int src_size = source.size();

        SortedSet<HashMap<Vector<Integer>, Double>> result = new TreeSet<HashMap<Vector<Integer>, Double>>();

        if (n > 3 || src_size > 3 || n < 1 || src_size < 1) {
            return result;
        }

        Vector<HashMap<Integer, Double>> semires = new Vector<HashMap<Integer, Double>>(n);

        for (int j = 0; j < n; j++) {
            HashMap<Integer, Double> hm = new HashMap<Integer, Double>();

            for (int i = src_size; i > 0; i--) {

                int t = source.get(i);
                boolean direction = i < 4;
                Map<Integer, Double> tt = region.getFascicle(src_size - i - j - 1).getTargetTokens(t, direction);

                for (Map.Entry<Integer, Double> ei : tt.entrySet()) {
                    if (hm.containsKey(ei.getValue())) {
                        hm.put(ei.getKey(), Math.min(hm.get(ei.getKey()), ei.getValue()));
                    } else {
                        hm.put(ei.getKey(), ei.getValue());
                    }
                }
            }
            semires.add(n, hm);
        }

        for (int j = 0; j < n; j++) {

            // Vector<Integer> tv = new Vector<Integer>();
            // float str = 0;

            HashMap<Integer, Double> hm = semires.get(j);

            for (Integer key : hm.keySet()) {
                for (int i = j; i < n; i++) {
                }
            }
        }
        return result;
    }

    // TODO logic missing
    public SortedSet<List<Integer>> findConsensuses(List<SortedSet<ExcitedIndex<Integer>>> input) {
        SortedSet<List<Integer>> result = new TreeSet<List<Integer>>();

        for (SortedSet<ExcitedIndex<Integer>> ts : input) {
        }
        return result;
    }

    /*
    public SortedSet<ExcitedIndex<List<Integer>>> getTargetAssociations(int src, int distance, int max_lenght) {
    SortedSet<ExcitedIndex<List<Integer>>> result = new TreeSet<ExcitedIndex<List<Integer>>>();
    
    List<Map<Integer,Double>> tokens = new ArrayList<Map<Integer,Double>>();
    
    for (int k = distance; k < (distance + max_lenght) && k < 4; k++) {
    Fascicle f = region.getFascicle(k);
    Map<Integer,Double> tt = f.getTargetTokens(src, true);
    tokens.add(tt);
    }
    
    return result;
    }
     */
}
