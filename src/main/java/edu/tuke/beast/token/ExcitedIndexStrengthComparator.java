package edu.tuke.beast.token;

import java.io.Serializable;
import java.util.Comparator;

public class ExcitedIndexStrengthComparator implements Comparator<ExcitedIndex<Integer>>, Serializable {

    private static final long serialVersionUID = -4075518441170385247L;

    // stronger first
    @Override
    public int compare(ExcitedIndex<Integer> t1, ExcitedIndex<Integer> t2) {

        Double f1 = t1.getStrength();
        Double f2 = t2.getStrength();

        int c = Double.compare(f2, f1);

        if (c == 0) {
            return (t2.getValue()).compareTo((t1).getValue());
        }

        return c;
    }
}
