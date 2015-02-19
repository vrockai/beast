package edu.tuke.beast.token;

import java.util.Vector;

public class Phrase {

    final Vector<Integer> phrase;

    public Phrase(int... tokens) {
        phrase = new Vector<Integer>(tokens.length);
        for (int i : tokens) {
            phrase.add(i);
        }
    }

    public double getStrenght() {
        return -1;
    }
}
