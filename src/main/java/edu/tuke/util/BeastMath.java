/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.util;

import org.apache.log4j.Logger;

/**
 *
 * @author vrockai
 */
public class BeastMath {

    public static Logger logger = Logger.getRootLogger();

    public static double log2(double d) {
        return Math.log(d) / Math.log(2);
    }

    public static double inf(double pa) {
        return (-1) * pa * log2(pa);
    }

    public static double mutInf(double pab, double pa, double pb) {
        if (pa == 0 || pb == 0 || pab == 0) {
           // logger.debug("0" + pab + "," + pa + "," + pb);
            return 0;
        }

        return log2(pab / (pa * pb));
    }

    public static double mutInf2(double pab, double pa, double pb) {
        if (pa == 0 || pb == 0 || pab == 0) {
           // logger.debug("0" + pab + "," + pa + "," + pb);
            return 0;
        }
        /*
        System.out.println("pab = "+ pab);
        System.out.println("pa = "+ pa);
        System.out.println("pb = "+ pb);
        System.out.println("pab / (pa * pb) = "+ (pab / (pa * pb)));
        System.out.println("log2(pab / (pa * pb)) = "+ log2(pab / (pa * pb)));
        System.out.println("Math.abs(log2(pab / (pa * pb))) = "+ Math.abs(log2(pab / (pa * pb))));
        System.out.println("pab * Math.abs(log2(pab / (pa * pb))) = "+ (pab * Math.abs(log2(pab / (pa * pb)))));
         */
        return pab * Math.abs(log2(pab / (pa * pb)));
    }

    public static double mutWei(double pab, double pa) {
        if (pa == 0 || pab == 0) {
            return 0;
        }

        //return log2(pab / (pa * pb)) - log2(pb);
        return -1*pab*log2(pab / pa);
    }
}
