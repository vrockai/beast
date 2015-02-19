package edu.tuke.beast.token;

import java.io.Serializable;

/**
 * Token - trieda reprezentuj?ca token, ktor?ho hodntou m??e by? in?tancia
 * akejko?vek triedy. Vyu??va generick? typy..
 */
public class Token<T> implements Comparable, Serializable {

    private static final long serialVersionUID = 42L;
    protected T value;

    public Token(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Token)) {
            return false;
        }
        if (value == null) {
            return ((Token) object).value == null;
        }
        return value.equals(((Token) object).value);
    }

    @Override
    public int hashCode() {
        if (value == null) {
            return -1;
        }
        return value.hashCode();
    }

    @Override
    public String toString() {
        if (value == null) {
            return "NULL";
        }
        
        return value.toString();
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return 1;
        }
        return this.hashCode() - o.hashCode();
    }
}
