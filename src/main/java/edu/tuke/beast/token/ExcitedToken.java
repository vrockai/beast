package edu.tuke.beast.token;

import java.io.Serializable;

/**
 *  ?peci?lna trieda dediaca z triedy Token. Obsahuje aj ??slen? hodnotu v?hy.
 *
 */
public class ExcitedToken<T> extends Token<T> implements Cloneable, Comparable, Serializable {

    private static final long serialVersionUID = -7760407230411325595L;
    private float strength;

    public ExcitedToken(T targetToken, float strength) {
        super(targetToken);
        this.strength = strength;
    }

    /**
     * vr?ti silu, akou je token excitovan? ( v?hu ).
     * @return
     */
    public float getStrength() {
        return strength;
    }

    /**
     * nastav? silu excit?cie.
     * @param strength
     */
    public void setStrength(float strength) {
        this.strength = strength;
    }

    @Override
    public String toString() {
        if (super.value == null) {
            return "NULL";
        }
        return super.value.toString() + '(' + strength + ')';
    }

    public int compareTo(Token o) {
        if (o instanceof ExcitedToken) {
            if (((ExcitedToken) o).getStrength() == strength) {
                if (value instanceof Comparable) {
                    return ((Comparable) value).compareTo(((ExcitedToken) o).value);
                }
                return -1;
            }
            return Float.compare(((ExcitedToken) o).getStrength(), strength);
        } else {
            return ((Comparable) this.value).compareTo(o.getValue());
        }
    }

    //TODO overit
    @Override
    public int hashCode() {
        return super.hashCode() + (int) strength ^ 123;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ExcitedToken)) {
            return false;
        }
        return super.equals(object) && (strength == ((ExcitedToken) object).strength);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
