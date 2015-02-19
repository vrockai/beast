package edu.tuke.beast.fascicle;

import edu.tuke.beast.fascicle.storage.CommonMatrix;
import java.io.*;
import java.util.Map;
import java.util.HashMap;

import edu.tuke.beast.association.Association;
import edu.tuke.beast.fascicle.storage.Matrix;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.util.BeastMath;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.naming.SizeLimitExceededException;
import org.apache.log4j.Logger;

public class Fascicle implements Serializable {

    static boolean caching = false;
    private static final Logger logger = Logger.getRootLogger();
    private static final long serialVersionUID = 42L;
    private TargetTokensCache cache = new TargetTokensCache(10000);

    public enum SigStrategy {

        LOG, SIMPLE, EXP
    }
    private SigStrategy sigStrategy = SigStrategy.SIMPLE;
    private String name;
    private final Matrix storage;
    private int learningEvents = 0;
    private Map<Integer, Integer> sourceTokenEvents = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> targetTokenEvents = new HashMap<Integer, Integer>();
    private double treshold = 1.0;
    private int assocCount = 0;
    private double energy = 0;
    private static boolean invWei = false;

    public void clearCache() {
        cache.resetCache();
    }

    public static boolean isInvWei() {
        return invWei;
    }

    public static void setInvWei(boolean invWei) {
        Fascicle.invWei = invWei;
    }

    public static void setCaching(boolean caching) {
        Fascicle.caching = caching;
    }

    public static boolean getCaching() {
        return caching;
    }

    private Fascicle(String name, Integer learningEvents, double treshold, Map<Integer, Integer> sourceTokenEvents, Map<Integer, Integer> targetTokenEvents, CommonMatrix storage) {
        this.name = name;
        this.storage = storage;
        this.learningEvents = learningEvents;
        this.sourceTokenEvents = sourceTokenEvents;
        this.targetTokenEvents = targetTokenEvents;
        this.treshold = treshold;
    }

    public Fascicle.SigStrategy getStrategy() {
        return sigStrategy;
    }

    public void setStrategy(Fascicle.SigStrategy s) {
        logger.debug("F:" + name + " | strategy: " + s);
        sigStrategy = s;
    }

    private Integer getCount(int i, int j) {
        if (i < 0 || j < 0) {
            throw new IllegalArgumentException("token out of bounds (negative).");
        }

        return storage.get(i, j);
    }

    public double getProb(int a) {
        Integer c = sourceTokenEvents.get(a);

        if (c == null) {
            return 0;
        }

        double t = learningEvents;
        return c / t;
    }

    public double getProb2(int a) {
        Integer c = targetTokenEvents.get(a);

        if (c == null) {
            return 0;
        }

        double t = learningEvents;
        return c / t;
    }

    public double getProb(int a, int b) {
        double c = getCount(a, b);
        double t = learningEvents;
        return c / t;
    }

    public double getTreshold() {
        return treshold;
    }

    public void setTreshold(double tresh) {
        this.treshold = tresh;
    }

    public int getSourceTokenLearningEvents(Integer t) {
        Integer events = sourceTokenEvents.get(t);
        return (events == null) ? 0 : events;
    }

    public int getTargetTokenLearningEvents(Integer t) {
        Integer events = targetTokenEvents.get(t);
        return (events == null) ? 0 : events;
    }

    // CONSTRUCTOR
    public Fascicle(String name, int size) {
        this.name = name;
        storage = new CommonMatrix(size);
    }

    public Fascicle(String name, CommonMatrix cm) {
        this.name = name;
        this.storage = cm;
    }

    public final void store(Integer t1, Integer t2) throws SizeLimitExceededException {

        if (t1.equals(Lexicon.NULL_INDEX))
            return;
        if (t2.equals(Lexicon.NULL_INDEX))
            return;

        if ((t1 == null) || (t2 == null)) {
            return;
        }

       // System.out.println(t1+":"+t2);
        updateLearningEvents(t1, t2);
        storage.set(t1, t2, storage.get(t1, t2) + 1);
    }

    public Vector<Double> getTokenFeatures(int i) {
        Vector<Double> result = new Vector<Double>();

        double ene = 0;
        int assCount = 0;

        for (int j : storage.keySet()) {
            double s = getSignificance(i, j);

            if (s <= treshold) {
                continue;
            }
            
            double w = getWeigth(i, j);
            assCount++;
            ene += w;
        }

        result.add((double) assCount);
        result.add(ene);

        return result;
    }

    public Vector<Double> getFeatures() {
        Vector<Double> result = new Vector<Double>();

        double ener = 0;
        int assCount = 0;

        for (int i : storage.keySet()) {
            for (int j : storage.keySet()) {
                double s = getSignificance(i, j);

                if (s <= treshold) {
                    continue;
                }
                System.out.println(s);
                double w = getWeigth(i, j);
                assCount++;
                ener += w;
            }
        }
        this.assocCount = assCount;
        this.energy = ener;

        result.add((double) assCount);
        result.add(ener);

        return result;
    }

    public double getEnergy() {

        int size = storage.getSize();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                energy += getWeigth(i, j);
            }
        }

        return energy;

    }

    public int getAsocCount(Integer t, Set<Integer> lex) {
        assocCount = 0;

        for (Integer j : lex) {
            assocCount += getSignificance(t, j) > 1 ? 1 : 0;
        }

        return assocCount;
    }

    public int getAsocCount() {
        assocCount = 0;
        int size = storage.getSize();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                assocCount += getSignificance(i, j) > 1 ? 1 : 0;
            }
        }

        return assocCount;

    }

    public Map<Integer, Integer> getAssociations(Integer t, boolean dir) {

        if (t == null) {
            return new HashMap<Integer, Integer>();
        }

        return dir ? storage.getRow(t) : storage.getColumn(t);

    }

    public boolean isAssociation(Integer t1, Integer t2) {

        if ((t1 == null) || (t2 == null)) {
            return false;
        }

        return (getSignificance(t1, t2) >= treshold);
    }

    public Association<Integer> getAssociation(Integer idx1, Integer idx2) {

        if ((idx1 == null) || (idx2 == null)) {
            return null;
        }

        if (!isAssociation(idx1, idx2)) {
            return null;
        }

        return new Association<Integer>(idx1, idx2, getWeigth(idx1, idx2));
    }

    public Map<Integer, Double> getTargetTokens(Integer src, boolean dir) {

        Map<Integer, Double> result;

        if (caching) {
            result = cache.get(src, treshold, dir);
            if (result != null) {
                return result;
            }
        }

        Map<Integer, Integer> targets = getAssociations(src, dir);

        // get the target tokens out of associations

        result = new HashMap<Integer, Double>();

        for (Integer target : targets.keySet()) {
            if (dir ? isAssociation(src, target) : isAssociation(target, src)) {
                result.put(target, dir ? getWeigth(src, target) : getWeigth(target, src));
            }
        }

        if (caching) {
            cache.put(src, treshold, dir, result);
        }


        return result;
    }
    /*
    public Map<Integer, Double> getTargetTokens(Set<Integer> source, boolean dir){
    Map<Integer, Double> result = new HashMap<Integer,Double>();
    Map<Integer, Double> semi;
    
    if (source.size()>0)
    semi = getTargetTokens(source, dir);
    
    int c=0;
    for (Integer t: source){
    if (c==0)
    semi = getTargetTokens(t, dir);
    else
    semi =
    c++
    }
    
    return result;
    }
    
    @Deprecated
    public SortedSet<ExcitedIndex<Integer>> getTargetTokensOld(Integer src, boolean dir) {
    
    Map<Integer, Integer> targets = getAssociations(src, dir);
    
    // get the target tokens out of associations
    
    SortedSet<ExcitedIndex<Integer>> ts = new TreeSet<ExcitedIndex<Integer>>(new ExcitedIndexStrengthComparator());
    //Map<Integer,Double> result = new HashMap<Integer,Double>();
    
    for (Integer target : targets.keySet()) {
    if ((dir ? getSignificance(src, target) : getSignificance(target, src)) >= treshold) {
    //result.put(target, dir ? getWeigth(src, target) : getWeigth(target, src));
    ts.add(new ExcitedIndex<Integer>(target, (dir ? getWeigth(src, target) : getWeigth(target, src))));
    }
    }
    
    return ts;
    }
    
    @Deprecated
    public SortedSet<ExcitedIndex<Integer>> getTargetTokensOld(Set<Integer> srcs) {
    
    SortedSet<ExcitedIndex<Integer>> targets = new TreeSet<ExcitedIndex<Integer>>(new ExcitedIndexStrengthComparator());
    Map<Integer, Double> tmpAdded = new HashMap<Integer, Double>();
    
    for (Integer source : srcs) {
    
    Map<Integer, Integer> map = getAssociations(source, true);
    if (map != null) {
    
    for (Integer target : map.keySet()) {
    if (getSignificance(source, target) >= treshold) {
    if (!tmpAdded.keySet().contains(target)) {
    tmpAdded.put(target, getWeigth(source, target));
    } else {
    double weightOld = tmpAdded.get(target);
    double weightNew = getWeigth(source, target);
    
    // TODO viac metod na vypocet novej vahy
    
    if (weightNew > weightOld) {
    tmpAdded.put(target, weightNew);
    }
    }
    }
    }
    }
    }
    
    for (Integer token : tmpAdded.keySet()) {
    
    double weight = tmpAdded.get(token);
    targets.add(new ExcitedIndex<Integer>(token, weight));
    }
    
    return targets;
    }
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLearningEvents() {
        return learningEvents;
    }

    public Set<Integer> getNeighbourghs(Integer t, boolean dir) {
        /*
        Map<Integer, Integer> result = dir ? storage.getRow(t) : storage.getColumn(t);
        Set<Integer> nei = new HashSet<Integer>();
        
        for (Integer i : result.keySet()) {
        
        if (dir && isAssociation(t, i)) {
        nei.add(i);
        }
        if (!dir && isAssociation(i, t)) {
        nei.add(i);
        }
        }
        
        return nei;
         *
         */
        return new HashSet<Integer>(getTargetTokens(t, dir).keySet());
    }

    private void updateLearningEvents(Integer t1, Integer t2) {
        learningEvents++;

        Integer sourceCount = sourceTokenEvents.get(t1);
        Integer targetCount = targetTokenEvents.get(t2);

        sourceCount = (sourceCount == null) ? 1 : sourceCount + 1;
        targetCount = (targetCount == null) ? 1 : targetCount + 1;

        sourceTokenEvents.put(t1, sourceCount);
        targetTokenEvents.put(t2, targetCount);
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Fascicle)) {
            return false;
        }
        Fascicle f = (Fascicle) o;

        if (f.getLearningEvents() != learningEvents) {
            return false;
        }
        if (!f.getName().equals(name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 17 * hash + (this.storage != null ? this.storage.hashCode() : 0);
        hash = 17 * hash + this.learningEvents;
        hash = 17 * hash + (this.sourceTokenEvents != null ? this.sourceTokenEvents.hashCode() : 0);
        hash = 17 * hash + (this.targetTokenEvents != null ? this.targetTokenEvents.hashCode() : 0);
        return hash;
    }

    /*  Output file format:
     *  1st line:
     *      name,learningEvents,treshold
     *  2nd line: (t1:c1,t2:c2,)
     *      source token events
     *  3rd line: (t1:c1,t2:c2,)
     *      target token events
     *  4th line - EOF: (t1#t2:C12,t3:C13)
     *      t1, t2 - tokens
     *      C12, C13 - counts
     *      t1 and t2 were seen together C12 times
     *      t1 and t3 were seen together C13 times
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        int c = 0;
        int k;
        sb.append(storage.getSize());
        sb.append(name).append(',').append(learningEvents).append(',').append(treshold);

        k = sourceTokenEvents.size();
        if (k > 0) {
            sb.append('\n');
            for (Map.Entry<Integer, Integer> integerIntegerEntry : sourceTokenEvents.entrySet()) {
                sb.append(integerIntegerEntry.getKey()).append(':').append(integerIntegerEntry.getValue());
                if (k != 0) {
                    sb.append(',');
                }
                k--;
            }
        }

        k = targetTokenEvents.size();
        if (k > 0) {
            sb.append('\n');
            for (Map.Entry<Integer, Integer> integerIntegerEntry : targetTokenEvents.entrySet()) {
                sb.append(integerIntegerEntry.getKey()).append(':').append(integerIntegerEntry.getValue());
                if (k != 0) {
                    sb.append(',');
                }
                k--;
            }
        }

        if (!storage.keySet().isEmpty()) {
            sb.append('\n');
            for (int i : storage.keySet()) {
                sb.append(i).append('#');
                Map<Integer, Integer> tree = getAssociations(i, true);

                if (tree != null) {
                    k = tree.size();
                    c += tree.size();
                    for (Map.Entry<Integer, Integer> integerIntegerEntry : tree.entrySet()) {
                        sb.append(integerIntegerEntry.getKey()).append(':').append(integerIntegerEntry.getValue());
                        if (k != 0) {
                            sb.append(',');
                        }
                        k--;
                    }
                }
                sb.append('\n');
            }
        }

        sb.append("\n\n").append(storage);
        return sb.toString();
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();

        sb.append("name: ").append(name).append('\n');
        sb.append("learningEvents: ").append(learningEvents).append('\n');

        int c = 0;

        for (Integer key : storage.keySet()) {
            Map<Integer, Integer> tm = this.getAssociations(key, true);
            c += tm.keySet().size();
        }
        sb.append("storage rows count : ").append(storage.keySet().size()).append('\n');
        sb.append("storage items count: ").append(c).append('\n');

        //sb.append(storage);

        return sb.toString();
    }

    public int getMatrixSize() {
        return storage.getSize();
    }

    public void serialize(String fileName) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));

        int c = 0;
        int k;
        out.println(storage.getSize());
        out.println(name + ',' + learningEvents + ',' + treshold);

        k = sourceTokenEvents.size();
        for (Map.Entry<Integer, Integer> integerIntegerEntry2 : sourceTokenEvents.entrySet()) {
            out.print(integerIntegerEntry2.getKey() + ":" + integerIntegerEntry2.getValue());
            if (k != 0) {
                out.print(",");
            }
            k--;
        }
        out.println();
        k = targetTokenEvents.size();
        for (Map.Entry<Integer, Integer> integerIntegerEntry1 : targetTokenEvents.entrySet()) {
            out.print(integerIntegerEntry1.getKey() + ":" + integerIntegerEntry1.getValue());
            if (k != 0) {
                out.print(",");
            }
            k--;
        }
        out.println();

        for (int i : storage.keySet()) {

            Map<Integer, Integer> tree = this.getAssociations(i, true);
            if (tree != null && !tree.isEmpty()) {
                out.print(i + "#");
                k = tree.size();
                c += tree.size();
                for (Map.Entry<Integer, Integer> integerIntegerEntry : tree.entrySet()) {
                    if (integerIntegerEntry.getKey() != null) {
                        out.print(integerIntegerEntry.getKey() + ":" + integerIntegerEntry.getValue());
                        if (k != 0) {
                            out.print(",");
                        }
                        k--;
                    }
                }
                out.print("\n");
            }
        }
        out.close();
    }

    public static Fascicle deSerialize(String filename) throws IOException, SizeLimitExceededException {

        long t = System.nanoTime();
        int c = 0;

        final BufferedReader bfr = new BufferedReader(new FileReader(filename), 500);

        final String name;
        final Integer learningEvents;
        final double treshold;
        final Map<Integer, Integer> sourceTokenEvents = new Hashtable<Integer, Integer>();
        final Map<Integer, Integer> targetTokenEvents = new Hashtable<Integer, Integer>();

        String line,
                s;
        String[] membs;

        line = bfr.readLine();
        int size = Integer.decode(line) + 1;

        line = bfr.readLine();
        membs = line.split(",");
        name = membs[0];
        learningEvents = Integer.decode(membs[1]);
        treshold = Double.parseDouble(membs[2]);

        CommonMatrix cm = new CommonMatrix(size);

        line = bfr.readLine();
        membs = line.split(",");
        for (String value : membs) {
            String[] v = value.split(":");
            sourceTokenEvents.put(Integer.decode(v[0]), Integer.decode(v[1]));
        }

        line = bfr.readLine();
        membs = line.split(",");
        for (String value : membs) {
            String[] v = value.split(":");
            targetTokenEvents.put(Integer.decode(v[0]), Integer.decode(v[1]));
        }

        while ((s = bfr.readLine()) != null) {
            membs = s.split("#");
            if (membs.length > 1) {
                String[] valueSplit = membs[1].split(",");
                Integer source = Integer.decode(membs[0]);
                for (String value : valueSplit) {
                    c++;
                    String[] v = value.split(":");
                    cm.set(source, Integer.decode(v[0]), Integer.decode(v[1]));
                }
            }
        }
        bfr.close();
        Fascicle desFas = new Fascicle(name, learningEvents, treshold, sourceTokenEvents, targetTokenEvents, cm);

        logger.debug("F: " + name + " deserialized ( " + ((System.nanoTime() - t) / 1000000) + "s , count: " + (100f * (float) (c)) / (float) (5000 * 5000) + " %)..." + c + ": strange");

        return desFas;
    }

    /*                          c(i,j)  
     *                         --------
     *           p(i,j)           Ct
     * S(i,j) =----------- = -------------
     *          p(i)*p(j)     c(i)   c(j)
     *                        ---- * ----
     *                         Ct     Ct
     */
    public final double getSignificance(int src, int des) {

        double sig;

        Integer ic_i = sourceTokenEvents.get(src);
        Integer ic_j = targetTokenEvents.get(des);

        if (ic_i == null || ic_j == null) {
            return 0;
        }

        double c_i = ic_i;
        double c_j = ic_j;

        double c_ij = storage.get(src, des);// + storage.get(des, src);

        //logger.debug( storage.get(src, des) +" - "+ storage.get(des, src));
        //logger.debug(c_ij+" - "+c_i+" - "+c_j);

        if (c_ij == 0) {
            return 0;
        }

        double t = learningEvents;     
        switch (sigStrategy) {
            case LOG:
                sig = BeastMath.mutInf(c_ij / t, c_i / t, c_j / t);
                break;
            case SIMPLE:
                sig = (t * c_ij) / (c_i * c_j);
                break;
            case EXP:
                sig = BeastMath.mutInf2(c_ij / t, c_i / t, c_j / t);
                break;
            default:
                sig = (t * c_ij) / (c_i * c_j);
                break;
        }
        /*
        if (sig<0)
        logger.error("negativne significance for : "+src+","+des);
         */
        return sig;
    }

    /*        
     *                        c(i,j)
     *                       --------
     *            p(i,j)        Ct
     * w(i,j) =  -------- = ----------
     *             p(j)        c(i)
     *                         ---- 
     *                          Ct
     */
    public final double getWeigth(int src, int des) {

        if (invWei) {
            return getWeigthInv(src, des);
        }

        double wei;

        Integer ic_i = sourceTokenEvents.get(src);
        Integer ic_j = targetTokenEvents.get(des);

        if (ic_i == null || ic_j == null) {
            return 0;
        }

        double c_i = ic_i;
        double c_j = ic_j;

        double c_ij = storage.get(src, des);

        if (c_ij == 0) {
            return 0;
        }

        double t = learningEvents;

        switch (sigStrategy) {
            case LOG:
                wei = BeastMath.mutWei(c_ij / t, c_i / t);
                break;
            case SIMPLE:
                wei = c_i == 0 ? 0 : (c_ij / c_i);
                break;
            case EXP:
                wei = c_i == 0 ? 0 : (c_ij / c_i);
                break;
            //wei = BeastMath.mutWei(c_ij / t, c_i / t, c_j / t); break;
            default:
                wei = c_i == 0 ? 0 : (c_ij / c_i);
                break;
        }

        if (wei < 0) {
            logger.error("negativne significance for : " + src + "," + des);
        }

        return wei;

    }

    public final double getWeigth(Set<Integer> src, int des) {
        double w = 0;
        for (Integer s : src) {
            w += getWeigthInv(s, des);
            //w = Math.min(w, getWeigthInv(s, des));
        }
        return w / src.size();
    }

    public final double getWeigthInv(Set<Integer> src, int des) {
        double w = 0;
        for (Integer s : src) {
            w += getWeigthInv(s, des);
            //w = Math.min(w, getWeigthInv(s, des));
        }
        return w / src.size();
    }

    public final double getWeigth(int src, Set<Integer> des) {
        double w = 0;
        for (Integer d : des) {
            w += getWeigthInv(src, d);
            //w = Math.min(w, getWeigthInv(src, d));
        }
        return w / des.size();
    }

    public final double getWeigthInv(int src, Set<Integer> des) {
        double w = 0;
        for (Integer d : des) {
            w += getWeigthInv(src, d);
            //w = Math.min(w, getWeigthInv(src, d));
        }
        return w / des.size();
    }

    public final double getWeigthInv(int src, int des) {

        double wei;

        Integer ic_i = sourceTokenEvents.get(src);
        Integer ic_j = targetTokenEvents.get(des);

        if (ic_i == null || ic_j == null) {
            return 0;
        }

        double c_i = ic_i;
        double c_j = ic_j;

        double c_ij = storage.get(src, des);

        if (c_ij == 0) {
            return 0;
        }

        double t = learningEvents;

        switch (sigStrategy) {
            case LOG:
                wei = BeastMath.mutWei(c_ij / t, c_j / t);
                break;
            case SIMPLE:
                wei = c_j == 0 ? 0 : (c_ij / c_j);
                break;
            case EXP:
                wei = c_j == 0 ? 0 : (c_ij / c_j);
                break;
            //wei = BeastMath.mutWei(c_ij / t, c_i / t, c_j / t); break;
            default:
                wei = c_j == 0 ? 0 : (c_ij / c_j);
                break;
        }

        if (wei < 0) {
            logger.error("negativne significance for : " + src + "," + des);
        }

        return wei;

    }

    private class TargetTokensCache {

        private Map<Double, Map<Integer, Map<Integer, Double>>> cache_t = new HashMap<Double, Map<Integer, Map<Integer, Double>>>();
        private Map<Double, Map<Integer, Map<Integer, Double>>> cache_f = new HashMap<Double, Map<Integer, Map<Integer, Double>>>();
        private Map<Double, Map<Integer, Integer>> stats_s = new HashMap<Double, Map<Integer, Integer>>();
        private Map<Double, Map<Integer, Integer>> stats_i = new HashMap<Double, Map<Integer, Integer>>();
        int capacity = 1000;
        int size = 0;

        public TargetTokensCache(int c) {
            this.capacity = c;
        }

        private void clean(int s) {

            if (size + s < capacity) {
                return;
            }

            if (stats_s.isEmpty() || stats_i.isEmpty()) {
                return;
            }

            removeWeakest(true);

            if (size + s < capacity) {
                return;
            }

            removeWeakest(false);

            if (size + s < capacity) {
                clean(s);
            }
        }

        private void resetCache() {
            stats_s = new HashMap<Double, Map<Integer, Integer>>();
            stats_i = new HashMap<Double, Map<Integer, Integer>>();
        }

        private void removeWeakest(boolean dir) {

            Map<Double, Map<Integer, Integer>> stats = getStats(dir);

            Double it_d = stats.keySet().iterator().next();
            Integer it_i = stats.get(it_d).keySet().iterator().next();
            int min = stats.get(it_d).get(it_i);
            Double tresh = it_d;
            Integer token = it_i;

            for (Double d : stats.keySet()) {
                for (Map.Entry<Integer, Integer> e : stats.get(d).entrySet()) {
                    int act = e.getValue();
                    if (act < min) {
                        tresh = d;
                        token = e.getKey();
                        min = e.getValue();
                    }
                }
            }

            size -= getMap(dir).get(tresh).get(token).size();

            stats.get(tresh).remove(token);
            if (stats.get(tresh).isEmpty()) {
                stats.remove(tresh);
            }
        }

        public void put(Integer t, double tresh, boolean dir, Map<Integer, Double> map) {
            if (map == null) {
                return;
            }
            if (!caching) {
                return;
            }

            int s = map.size();

            clean(s);
            resetCache();

            if (getMap(dir).get(tresh) == null) {
                getMap(dir).put(tresh, new HashMap<Integer, Map<Integer, Double>>());
            }


            getMap(dir).get(tresh).put(t, map);
            size += map.size();
        }

        public Map<Integer, Double> get(Integer t, double tresh, boolean dir) {

            if (!caching) {
                return null;
            }

            if (getMap(dir).get(tresh) == null) {
                return null;
            }

            updateStatistics(t, tresh, dir);

            return getMap(dir).get(tresh).get(t);
        }

        private Map<Double, Map<Integer, Map<Integer, Double>>> getMap(boolean dir) {
            if (dir) {
                return cache_t;
            } else {
                return cache_f;
            }
        }

        private Map<Double, Map<Integer, Integer>> getStats(boolean dir) {
            if (dir) {
                return stats_s;
            } else {
                return stats_i;
            }
        }

        private void updateStatistics(Integer t, double tresh, boolean dir) {
            Map<Double, Map<Integer, Integer>> stats = getStats(dir);

            Map<Integer, Integer> map = stats.get(tresh);

            if (map == null) {
                map = new HashMap<Integer, Integer>();
            }

            Integer usage = map.get(t);

            map.put(t, usage != null ? usage++ : 1);

        }
    }
}
