/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SentencePanel.java
 *
 * Created on Dec 2, 2008, 6:43:35 PM
 */
package edu.tuke.visu.canvas;

import edu.tuke.beast.token.ExcitedIndex;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author blur
 */
public class WordcircleCanvas extends javax.swing.JPanel {

    private static final long serialVersionUID = -9087539658446671173L;
    int p_x = 0;
    int p_y = 0;
    List<ExcitedIndex<String>> sentence;
    double sig_M[][];
    double wei_M[][];
    final Color node = new Color(100, 100, 100);
    final Color node_h = new Color(250, 250, 230);
    final Color nodeBad = new Color(100, 0, 0);
    final Color nodeBad_h = new Color(250, 0, 0);
    final Color nodet = new Color(255, 255, 255);
    final Color nodet_h = new Color(0, 0, 0);
    final Color edge = new Color(230, 230, 230);
    final Color edge_h = new Color(255, 255, 0);
    final Color edgeBad = new Color(230, 0, 0);
    final Color edgeBad_h = new Color(255, 0, 0);
    final Color edget = new Color(130, 130, 130);
    final Color edget_h = new Color(255, 255, 255);
    final Color back = new Color(0, 0, 0);

    /** Creates new form SentencePanel */
    @SuppressWarnings("empty-statement")
    public WordcircleCanvas() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(back);
        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                p_x = e.getX();
                p_y = e.getY();
                repaint();
            }
        });

        sentence = new ArrayList<ExcitedIndex<String>>();
        sentence.add(new ExcitedIndex<String>("ja", 1));
        sentence.add(new ExcitedIndex<String>("som", 2));
        sentence.add(new ExcitedIndex<String>("velmi", 3));
        sentence.add(new ExcitedIndex<String>("pekny", 4));

        sig_M = new double[][]{{0.2, 0.3, 0.4, 3, -1}, {1, 2, 3, 4, -1}, {2, 3, 4, 5, -1}, {3, 3, 4.4, 3, -1}};
        wei_M = new double[][]{{.3, 4.3, 6.4, 7}, {2.4, 2, 4, 5}, {7, 32.3, 4, 5}, {43, 53, 64.4, 3.3}};
    }

    public void setSentence(List<ExcitedIndex<String>> sentence, double[][] s, double[][] w) {
        this.sentence = sentence;
        this.sig_M = Arrays.copyOf(s, s.length);
        this.wei_M = Arrays.copyOf(w, w.length);
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 300);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (sentence.size() > 1) {
            printSentence(sentence, g);
        }
    }

    public void printSentence(List<ExcitedIndex<String>> sentence, Graphics g) {

        double border = 0.2;
        int node_border = 4;

        int width = getWidth();
        int height = getHeight();
        FontMetrics metrics = g.getFontMetrics();
        int x = 0;
        double y = 0;
        final int size = sentence.size();


        int x_step = width / (size - 1);
        double y_step = Math.PI / (size - 1);

        //draw edges
        int xi = 0;
        double yi = 0;

        for (int i = 0; i < size; i++) {
            int ifontWidth = metrics.stringWidth(sentence.get(i).getValue());
            int ifontHeight = metrics.getHeight();
            int wi_x = (int) (border / 2 * (double) width) + (int) ((1 - border) * (double) xi) - ifontWidth / 2;
            int wi_y = -(int) (border / 2 * (double) height) + height - (int) ((1 - border) * Math.sin(yi) * height);

            int xj = (i + 1) * x_step;
            double yj = (i + 1) * y_step;

            for (int j = i + 1; j < size && j - i < 5; j++) {

                int jfontWidth = metrics.stringWidth(sentence.get(j).getValue());
                int jfontHeight = metrics.getHeight();

                int wj_x = (int) (border / 2 * (double) width) + (int) ((1 - border) * (double) xj) - jfontWidth / 2;
                int wj_y = -(int) (border / 2 * (double) height) + height - (int) ((1 - border) * Math.sin(yj) * height);

                boolean isHover = ((p_x > wi_x && p_x < wi_x + ifontWidth) && (p_y > wi_y - ifontHeight && p_y < wi_y)) ||
                        ((p_x > wj_x && p_x < wj_x + jfontWidth) && (p_y > wj_y - jfontHeight && p_y < wj_y));
                if (isHover) {
                    g.setColor(sig_M[i][j] > 0 ? edge_h : edgeBad_h);
                } else {
                    g.setColor(sig_M[i][j] > 0 ? edge : edgeBad);
                }

                g.drawLine(wi_x, wi_y, wj_x, wj_y);

                if (isHover) {
                    g.setColor(edget_h);
                } else {
                    g.setColor(edget);
                }
                String value = ""+sig_M[i][j];// + ":" + wei_M[i][j];
                String value2 = ""+wei_M[i][j];//sig_M[i][j] / ((wei_M[i][j] + wei_M[j][i]) / 2) + ":" + wei_M[j][i];
                String value3 = ""+wei_M[j][i];
                String value4 = "" + (wei_M[i][j] - wei_M[j][i]);
                int vfontWidth = metrics.stringWidth(value);

                g.drawString(value, (wi_x + wj_x) / 2 - vfontWidth / 2, (wi_y + wj_y) / 2);
                g.drawString(value2, (wi_x + wj_x) / 2 - vfontWidth / 2, 15 + (wi_y + wj_y) / 2);
                g.drawString(value3, (wi_x + wj_x) / 2 - vfontWidth / 2, 30 + (wi_y + wj_y) / 2);
                g.drawString(value4, (wi_x + wj_x) / 2 - vfontWidth / 2, 45 + (wi_y + wj_y) / 2);

                xj += x_step;
                yj += y_step;
            }

            xi += x_step;
            yi += y_step;
        }

        // draw points

        for (int i = 0; i < size; i++) {
            int fontWidth = metrics.stringWidth(sentence.get(i).getValue());
            int fontHeight = metrics.getHeight();

            int w_x = (int) (border / 2 * (double) width) + (int) ((1 - border) * (double) x) - fontWidth / 2;
            int w_y = -(int) (border / 2 * (double) height) + height - (int) ((1 - border) * Math.sin(y) * height);

            boolean isHover = (p_x > w_x && p_x < w_x + fontWidth) && (p_y > w_y - fontHeight && p_y < w_y);
            if (isHover) {
                g.setColor(sentence.get(i).getStrength() > 0 ? node_h : nodeBad_h);
            } else {
                g.setColor(sentence.get(i).getStrength() > 0 ? node : nodeBad);
            }
            g.fillRect(w_x - node_border, w_y - fontHeight - node_border / 2, fontWidth + node_border * 2, fontHeight + node_border * 2);


            if (isHover) {
                g.setColor(nodet_h);
            } else {
                g.setColor(nodet);
            }
            g.drawString(sentence.get(i).getValue(), w_x, w_y);

            x += x_step;
            y += y_step;
        }


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                System.out.println("Created GUI on EDT? " +
                        SwingUtilities.isEventDispatchThread());
                JFrame f = new JFrame("Swing Paint Demo");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.add(new WordcircleCanvas());
                f.pack();
                f.setVisible(true);

            }
        });
    }
}
