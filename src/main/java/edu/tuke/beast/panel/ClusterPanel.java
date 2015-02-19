/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClusterPanel.java
 *
 * Created on Feb 1, 2009, 3:28:04 PM
 */
package edu.tuke.beast.panel;

import edu.tuke.visu.tablemodel.JCommonModel;
import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.SimilarityCortex;
import edu.tuke.beast.gui.BeastPanel;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.token.Token;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.SwingWorker;

/**
 *
 * @author blur
 */
public class ClusterPanel extends BeastPanel {

    private static final long serialVersionUID = 4186912297028787537L;
    SimilarityCortex sc;

    public ClusterPanel(Beast c) {
        super(c);
        this.beast = c;
        this.sc = new SimilarityCortex(cortex);
        initComponents();

        PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                String property = propertyChangeEvent.getPropertyName();
                if ("status".equals(property)) {

                    Integer value = (Integer) propertyChangeEvent.getNewValue();
                    jProgressBar1.setValue(value);
                    jProgressBar1.setString(value + "%");
                }
            }
        };

        sc.addPropertyChangeListener(propertyChangeListener);

    }

    /** Creates new form ClusterPanel */
    public ClusterPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelInput = new javax.swing.JPanel();
        jTextFieldInput = new javax.swing.JTextField();
        jButtonCompute = new javax.swing.JButton();
        jScrollPaneCluster = new javax.swing.JScrollPane();
        jTableCluster = new javax.swing.JTable();
        jProgressBar1 = new javax.swing.JProgressBar();

        setLayout(new java.awt.BorderLayout());

        jPanelInput.setLayout(new java.awt.GridLayout());

        jTextFieldInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldInputKeyPressed(evt);
            }
        });
        jPanelInput.add(jTextFieldInput);

        jButtonCompute.setText("Compute");
        jButtonCompute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonComputeActionPerformed(evt);
            }
        });
        jPanelInput.add(jButtonCompute);

        add(jPanelInput, java.awt.BorderLayout.PAGE_END);

        jTableCluster.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPaneCluster.setViewportView(jTableCluster);

        add(jScrollPaneCluster, java.awt.BorderLayout.CENTER);

        jProgressBar1.setStringPainted(true);
        add(jProgressBar1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonComputeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonComputeActionPerformed

        getClustersAction();

    }//GEN-LAST:event_jButtonComputeActionPerformed

    private void jTextFieldInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldInputKeyPressed
        if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_TAB)) {
            getClustersAction();
        }
    }//GEN-LAST:event_jTextFieldInputKeyPressed

    private void doGetClusters() {
        Lexicon<String> l = beast.getLexicon();
        String[] words = jTextFieldInput.getText().split(",");

        Vector<Map<Integer, Double>> sets = new Vector<Map<Integer, Double>>();
        Map<Integer, Double> result = new HashMap<Integer, Double>();

        for (String word : words) {
            word = word.trim();
            int index = l.getIndex(new Token<String>(word));
            Map<Integer, Double> set = new HashMap<Integer,Double>();// sc.getRelatives(index, l.getHashMap().keySet());
            sets.add(set);
        }

        result = cortex.maxMin(sets);

        System.out.println(result.size());
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        int c = 0;
        for ( Map.Entry<Integer,Double> ei : result.entrySet() ) {
            String token = l.getEntry(ei.getKey()).getValue();

            boolean isQuestion = false;
            for (String word : words) {
                if (isQuestion) {
                    break;
                }
                word = word.trim();
                token = token.trim();
                isQuestion = token.equals(word);
                if (isQuestion) {
                    System.out.println(word + ',' + token);
                }
            }

            if (!isQuestion) {
                Vector<Object> row = new Vector<Object>();
                row.add(c++);
                row.add(token);
                double str = ei.getValue();
                str = str / beast.getLexicon().getOccurences(ei.getKey());
                row.add(str);
                data.add(row);
            }
        }

        Vector<String> columns = new Vector<String>();
        columns.add("#");
        columns.add("word");
        columns.add("strength");

        Class[] classes = {Integer.class, String.class, Double.class};

        jTableCluster = new JTable(new JCommonModel(data, columns, classes));
        jTableCluster.setDefaultRenderer(Double.class, new JCommonModel.DoubleRenderer());
        jTableCluster.setAutoCreateRowSorter(true);
        jScrollPaneCluster.setViewportView(jTableCluster);
    }

    private void getClustersAction() {

        // doGetRelatives();


        SwingWorker worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() {
                jTextFieldInput.setEnabled(false);
                jButtonCompute.setEnabled(false);
                doGetClusters();
                return null;
            }

            @Override
            protected void done() {
                jTextFieldInput.setEnabled(true);
                jButtonCompute.setEnabled(true);
            }
        };
        worker.execute();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCompute;
    private javax.swing.JPanel jPanelInput;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPaneCluster;
    private javax.swing.JTable jTableCluster;
    private javax.swing.JTextField jTextFieldInput;
    // End of variables declaration//GEN-END:variables
/*
    public static void main(String s[]) {
    Cortex cortex = null;
    try {
    cortex = new Cortex("/home/blur/Projects/Beast.data/cortex/CORTEX");
    } catch (Exception ex) {
    Logger.getLogger(BeastGui.class.getName()).log(Level.SEVERE, null, ex);
    }

    JFrame f = new JFrame("ShapesDemo2D");
    f.addWindowListener(new WindowAdapter() {

    public void windowClosing(WindowEvent e) {
    System.exit(0);
    }
    });
    JPanel applet = new ClusterPanel(cortex);

    f.getContentPane().add("Center", applet);
    //applet.init();
    f.pack();

    f.setVisible(true);
    }
     * */
}
