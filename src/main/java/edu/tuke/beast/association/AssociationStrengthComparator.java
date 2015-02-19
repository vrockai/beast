package edu.tuke.beast.association;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: Oct 9, 2004
 * Time: 10:32:21 PM
 */
class AssociationStrengthComparator implements Comparator<Association>, Serializable {

    private static final long serialVersionUID = 8348107936489790638L;

    @Override
    public int compare(Association a1, Association a2) {
        return ((int) (a1.getStrength() - a2.getStrength()));
    }
}
