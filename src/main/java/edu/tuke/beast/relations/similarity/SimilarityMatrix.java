/*
 * DistanceMatrix.java
 *
 * Created on 4. kvten 2007, 12:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.tuke.beast.relations.similarity;

/**
 *
 * @author rockaiv
 */
public class SimilarityMatrix {

    double[] matrix;
    int size;

    /** Creates a new instance of DistanceMatrix */
    public SimilarityMatrix(int s) {
        size = s;
        matrix = new double[fib(s)];
    }

    public int getSize() {
        return size;
    }

    public double getValue(int a, int b) {
        //a-=1;b-=1;

        if (a == b) {
            return 1;
        } else {
            int m1, m2;
            m1 = Math.max(a, b);
            m2 = Math.min(a, b);
            return matrix[fib(m1) + m2];
        }

    // return -1.;
    }

    public void setValue(int a, int b, double v) {
        //a-=1;b-=1;

        if (a == b) {
            //matrix[fib(a)+b] = 1;
            return;
        }

        if (a < size && b < size) {
            int m1, m2;

            m1 = Math.max(a, b);
            m2 = Math.min(a, b);

            matrix[fib(m1) + m2] = v;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("lenght:").append(matrix.length).append('\n');
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sb.append(getValue(i, j)).append(' ');
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    public static int fib(int n) {
        if (n < 1) {
            return n;
        } else {
            int v = 0;
            for (int i = 1; i < n; i++) {
                v += i;
            }
            return v;
        }
    }

    public static void main(String[] args) {
        int N = 10;
        int SIZE = 6;

        SimilarityMatrix sm = new SimilarityMatrix(SIZE);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(((i == j) ? 1 : i * j) + ",");
                sm.setValue(i, j, ((i == j) ? 1 : i * j));
            }
            System.out.println("");
        }


        System.out.println("SM:\n" + sm.toString());
    }
}
