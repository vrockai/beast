/*
 * SymmetricMatrix.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.tuke.util;

/**
 *
 * @author rockaiv
 */
public class TriangularMatrix {

    final int size;
    final Integer[] array;

    /** Creates a new instance of SymmetricMatrix */
    public TriangularMatrix(int s) {
        this.size = s;
        this.array = new Integer[(s * (s + 1)) / 2];
        this.reset();
    }

    private void reset() {
        for (int i = 0; i < array.length; i++) {
            array[i] = 0;
        }
    }

    public Integer get(int i, int j) {
        return i <= j ? array[((i * (i + 1)) / 2) + j] : this.get(j, i);
    }

    public void set(int i, int j, Integer v) {
        if (i <= j) {
            array[((i * (i + 1)) / 2) + j] = v;
        } else {
            set(j, i, v);
        }
    }

    public int getSize() {
        return size;
    }
}
