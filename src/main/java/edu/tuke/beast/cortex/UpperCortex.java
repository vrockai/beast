/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tuke.beast.cortex;

import java.beans.PropertyChangeListener;
import org.apache.log4j.Logger;

/**
 *
 * @author blur
 */
public abstract class UpperCortex {

    protected Cortex cortex = null;
    protected PropertyChangeListener propertyChangeListener;
    public static final Logger logger = Logger.getRootLogger();
    
    public UpperCortex(Cortex c) {
        this.cortex = c;
    }

     public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.propertyChangeListener = pcl;
    }

     protected int getPercents(int delta, int step){

            if (delta > 1) {
                int tr = (int) Math.floor(delta);
                delta -= tr;
            }
            delta += step;

            return delta;
     }

}
