/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.association;

/**
 *
 * @author vrockai
 */
public class NewAssociation {

    private static final long serialVersionUID = 42L;
    // source and target tokens
    int token_a, token_b;
    // contextual distance
    int distance;
    // weight
    double strenght;

    public NewAssociation(int token_a, int token_b, int distance, double strenght) {
        this.token_a = token_a;
        this.token_b = token_b;
        this.distance = distance;
        this.strenght = strenght;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NewAssociation other = (NewAssociation) obj;
        if (this.token_a != other.token_a) {
            return false;
        }
        if (this.token_b != other.token_b) {
            return false;
        }
        if (this.distance != other.distance) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.token_a;
        hash = 97 * hash + this.token_b;
        hash = 97 * hash + this.distance;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.strenght) ^ (Double.doubleToLongBits(this.strenght) >>> 32));
        return hash;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public double getStrenght() {
        return strenght;
    }

    public void setStrenght(double strenght) {
        this.strenght = strenght;
    }

    public int getToken_a() {
        return token_a;
    }

    public void setToken_a(int token_a) {
        this.token_a = token_a;
    }

    public int getToken_b() {
        return token_b;
    }

    public void setToken_b(int token_b) {
        this.token_b = token_b;
    }
}
