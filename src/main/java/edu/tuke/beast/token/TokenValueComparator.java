package edu.tuke.beast.token;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Kompar?tor sl??iaci na koprovn?vanie hodn?t tokenov.
 */
class TokenValueComparator<E extends Comparable> implements Comparator<Token<E>>, Serializable {

    private static final long serialVersionUID = 5735218237633380295L;

    @Override
    @SuppressWarnings("unchecked")
    public int compare(Token<E> token_1, Token<E> token_2) {
        return (token_1.getValue().compareTo((token_2.getValue())));
    }
}
