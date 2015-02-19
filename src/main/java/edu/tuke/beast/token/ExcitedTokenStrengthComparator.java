package edu.tuke.beast.token;

import java.io.Serializable;
import java.util.Comparator;

public class ExcitedTokenStrengthComparator implements Comparator<ExcitedToken<String>>, Serializable {

    private static final long serialVersionUID = 826069260615752868L;

    //stronger first
    @Override
    public int compare(ExcitedToken<String> t1, ExcitedToken<String> t2) {

        if (Float.compare(t1.getStrength(), t2.getStrength()) == 0) {
            if (t2.getValue() instanceof Comparable) {
                return ((Comparable) t2.getValue()).compareTo((t1).getValue());
            }
            return -1;
        }
        return Float.compare(t2.getStrength(), t1.getStrength());
    }
}
