package edu.tuke.beast.token;

import java.io.Serializable;

/**
 * ?peci?lna trieda dediaca z triedy Token. Obsahuje aj ??slen? hodnotu v?hy.
 * 
 */
public class ExcitedIndex<E> implements Comparable<ExcitedIndex<E>>, Serializable {

    private static final long serialVersionUID = 6514852048134728545L;
    private final E value;
    private final double strength;

    public ExcitedIndex() {
        this.value = null;
        this.strength = -1;
    }

    public ExcitedIndex(E i, double w) {
        this.value = i;
        this.strength = w;
    }

    public E getValue() {
        return value;
    }

    public Double getStrength() {
        return strength;
    }

    public String toString() {
        if (value == null) {
            return "NULL";
        }
        return value.toString() + '(' + strength + ')';
    }

    @Override
    public int compareTo(ExcitedIndex<E> o) {

        if (o.getStrength() == strength) {
            if (value instanceof Comparable) {
                return ((Comparable) value).compareTo(o.value);
            }
            return -1;
        }
        return Double.compare(o.getStrength(), strength);


    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ExcitedIndex)) {
            return false;
        }
        return super.equals(object) && strength == ((ExcitedIndex<E>) object).strength;
    }
}	