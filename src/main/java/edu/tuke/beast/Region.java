package edu.tuke.beast;

import edu.tuke.beast.fascicle.Fascicle;

import edu.tuke.beast.fascicle.Fascicle.SigStrategy;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import javax.naming.SizeLimitExceededException;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA. User: blur Date: Mar 11, 2005 Time: 3:25:50 PM To
 * change this template use File | Settings | File Templates.
 */
public final class Region implements Serializable {
    public static Logger logger = Logger.getRootLogger();
    private static final long serialVersionUID = -9077446483930346023L;

    //temporary
    private int LEXICON_SIZE = 10;
    public Fascicle[] fascicle;
    private int size = 4;
    int status = 0;
    public static PropertyChangeListener propertyChangeListener;

    public static void addPropertyChangeListener(PropertyChangeListener pcl) {
        Region.propertyChangeListener = pcl;
    // System.out.println("region added");
    }

    public int getSize() {
        return size;
    }

    public Region() {
        fascicle = new Fascicle[size];

        fascicle[0] = new Fascicle("A", LEXICON_SIZE);
        fascicle[1] = new Fascicle("B", LEXICON_SIZE);
        fascicle[2] = new Fascicle("C", LEXICON_SIZE);
        fascicle[3] = new Fascicle("D", LEXICON_SIZE);

    }

    public Region(int s) {
        fascicle = new Fascicle[size];

        fascicle[0] = new Fascicle("A", s);
        fascicle[1] = new Fascicle("B", s);
        fascicle[2] = new Fascicle("C", s);
        fascicle[3] = new Fascicle("D", s);

    }

    public double[] getWeigths(){
        int[] le = new int[size];
        int max = 0;

        double[] result = new double[size];

        for(int i=0;i<size;i++){
            le[i] = fascicle[i].getLearningEvents();
            max += le[i];
        }

        for(int i=0;i<size;i++){
            result[i] = (double)le[i]/max;
            logger.debug("W1 = "+result[i]);
        }
        return result;
    }

    private Region(Fascicle[] f) {
        size = f.length;
        // System.out.println("Region size: " + size);
        fascicle = f;

        getWeigths();
    }

    public Fascicle getFascicle(int i) {

        if ((i < 0) || (i > size - 1)) {
            //System.out.println(i+"bad!");
            return null;
        }
        //System.out.println(i+"good!");
        return fascicle[i];
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();

        sb.append("Region status:\n\n");
        //System.out.println("Number of Fas: " + size + "\n\n");
        sb.append("Number of Fas: ").append(size).append("\n\n");

        for (int i = 0; i < size; i++) {
            sb.append("fas[").append(i).append("] = ").append(fascicle[i].getStatus()).append('\n');
        }

        return sb.toString();
    }

    /*
     * public Fascicle<Integer> getFascicle(int i, boolean direct) { if((i<0)||(i>size*2-1))
     * return null; if (direct) return fascicle[i]; else return fascicle[i+4]; }
     */
    public void setTreshold(double treshold) {

        for (int i = 0; i < 4; i++) {
            fascicle[i].setTreshold(treshold);
        }
    }

    public void serialize(String fileName) throws IOException {

        for (int i = 0; i < 4; i++) {
            fascicle[i].serialize(fileName + i);
        }

    }

    public Region deSerialize(String filename) throws IOException, ClassNotFoundException, SizeLimitExceededException {
        final int regsize = 4;
        
        Fascicle[] f = new Fascicle[regsize];

        for (int i = 0; i < regsize; i++) {
            f[i] = Fascicle.deSerialize(filename + i);
            int old_st = this.status;
            this.status = (i + 1) * 100 / regsize;
            if (propertyChangeListener != null) {        
                PropertyChangeEvent pce = new PropertyChangeEvent(this, "status", old_st, this.status);
                propertyChangeListener.propertyChange(pce);
            }
        }

        return new Region(f);

    }

    public double getEnergy() {
        double energy = 0.;
        for (int i = 0; i < size; i++) {
            energy += fascicle[i].getEnergy();
        }
        return energy;
    }
    
    public int getAsocCount(int i) {
        int asocCount = fascicle[i].getAsocCount();        
        return asocCount;
    }

    public int getAsocCount() {
        int asocCount = 0;
        for (int i = 0; i < size; i++) {
            asocCount += getAsocCount(i);
        }
        return asocCount;
    }

    public int getAssociationsCount() {
        int asCount = 0;

        return -1;
    }

    public void getFascicleStrategy(SigStrategy sigStrategy) {
         for (int i = 0; i < size; i++) {
            fascicle[i].setStrategy(sigStrategy);
        }
    }

    public void setFascicleStrategy(SigStrategy sigStrategy) {
         for (int i = 0; i < size; i++) {
            fascicle[i].setStrategy(sigStrategy);
        }
    }
/*
    @Override
    public String toString() {
        return this.;
    }
 */
}
