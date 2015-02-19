package edu.tuke.beast.association;

import java.io.Serializable;

public class Association<T> implements Serializable {

    private static final long serialVersionUID = 42L;
    private T sourceToken;
    private T targetToken;
    private double strength;

    @Override
    public String toString() {
        return '{' + sourceToken.toString() + "->" + targetToken.toString() + "}:" + strength;
    }

    public Association(T sourceToken, T targetToken, double strength) {
        this.sourceToken = sourceToken;
        this.targetToken = targetToken;
        this.strength = strength;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.sourceToken != null ? this.sourceToken.hashCode() : 0);
        hash = 97 * hash + (this.targetToken != null ? this.targetToken.hashCode() : 0);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.strength) ^ (Double.doubleToLongBits(this.strength) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Association)) {
            return false;
        }
        
        Association<T> a = (Association<T>) object;
        return (a.sourceToken.equals(sourceToken)) && (a.getTargetToken().equals(targetToken)) && (a.getStrength() == strength);
    }

    public Association() {
        this.sourceToken = null;
        this.targetToken = null;
        this.strength = 0;
    }

    public T getSourceToken() {
        return sourceToken;
    }

    public void setSourceToken(T sourceToken) {
        this.sourceToken = sourceToken;
    }

    public T getTargetToken() {
        return targetToken;
    }

    public void setTargetToken(T targetToken) {
        this.targetToken = targetToken;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }
}
