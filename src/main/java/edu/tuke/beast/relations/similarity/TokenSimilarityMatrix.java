/*
 * TokenSimilarityMatrix.java
 *
 * Created on May 7, 2007, 1:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.tuke.beast.relations.similarity;

import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.cortex.SimilarityCortex;
import edu.tuke.beast.token.Token;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author blur
 */
public class TokenSimilarityMatrix extends SimilarityMatrix {

    //SimilarityMatrix sm;
    Map<Integer, Token<String>> hm;
    Integer lexArr[];
    String lexVArr[];
    boolean normalized = true;
    double simi_max = 0;

    /** Creates a new instance of TokenSimilarityMatrix */
    public TokenSimilarityMatrix(Map<Integer, Token<String>> hm) {
        super(hm.size());
        this.hm = hm;
        createLexArr();
    }

    public double maxValue() {
        return simi_max;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public void setNormalized(boolean n) {
        normalized = n;
    }

    public void normalize() {

        double k = 1 / simi_max;

        for (int i = 0; i < lexArr.length; i++) {
            for (int j = 0; j < i; j++) {
                this.setValue(i, j, this.getValue(i, j) * k);
            }
        }
    }

    private void createLexArr() {
        Set<Integer> si = hm.keySet();
        Integer[] la = new Integer[si.size()];
        String[] ls = new String[si.size()];

        int c = 0;
        for (Integer i : si) {
            la[c] = i;
            ls[c++] = hm.get(i).getValue();
        }

        this.lexArr = la;
        this.lexVArr = ls;
    }

    public Integer[] getLexArr() {
        return lexArr;
    }

    public String[] getLexVArr() {
        return lexVArr;
    }

    public void compute(Cortex c) {

        createLexArr();
        SimilarityCortex sc = new SimilarityCortex(c);

        for (int i = 0; i < lexArr.length; i++) {
            for (int j = 0; j < i; j++) {

                Integer iToken = lexArr[i];
                Integer jToken = lexArr[j];

                double simi = 0.;
                        /*
                        (float) (sc.getSimilarity(iToken, jToken, 0)) * 0.4f +
                        (float) (sc.getSimilarity(iToken, jToken, 1)) * 0.3f +
                        (float) (sc.getSimilarity(iToken, jToken, 2)) * 0.2f +
                        (float) (sc.getSimilarity(iToken, jToken, 3)) * 0.1f;
                        */

                if (simi > simi_max) {
                    simi_max = simi;
                }


                this.setValue(i, j, simi);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }

    public void saveHTML(File page) {

        StringBuilder sb = new StringBuilder();
        createLexArr();

        sb.append("<html><head><title>SimiMatrix</title></head><body>");
        sb.append("<table border=1>");

        sb.append("<tr><td>Simi</td>");
        for (Integer aLexArr : lexArr) {
            sb.append("<td>").append(hm.get(aLexArr).getValue()).append("</td>");
        }
        sb.append("</tr>");

        for (int i = 0; i < lexArr.length; i++) {
            sb.append("<tr><td>").append(hm.get(lexArr[i]).getValue()).append("</td>");
            for (int j = 0; j < lexArr.length; j++) {
                sb.append("<td>").append(this.getValue(i, j)).append("</td>");
            }
            sb.append("</tr>");
        }

        sb.append("</table>");
        sb.append("</body></html>");
        FileOutputStream fileOutputStream;
        try {

            fileOutputStream = new FileOutputStream(page);
            PrintStream htmlPrintStream = new PrintStream(fileOutputStream);
            htmlPrintStream.print(sb.toString());
            htmlPrintStream.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
