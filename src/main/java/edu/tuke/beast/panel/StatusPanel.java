/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StatusPanel.java
 *
 * Created on Dec 11, 2008, 7:24:37 PM
 */
package edu.tuke.beast.panel;

import edu.tuke.beast.cortex.Cortex;

/**
 *
 * @author blur
 */
public class StatusPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 6390208384823951711L;
    private final Cortex cortex;

    /** Creates new form StatusPanel */
    public StatusPanel(Cortex c) {
        initComponents();
        this.cortex = c;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(jEditorPane1);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}