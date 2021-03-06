/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ContextPanel.java
 *
 * Created on Dec 28, 2008, 6:30:32 PM
 */
package edu.tuke.beast.panel;

import edu.tuke.visu.tablemodel.JCommonModel;
import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.ContextCortex;
import edu.tuke.beast.gui.BeastPanel;
import edu.tuke.beast.lexicon.Lexicon;
import edu.tuke.beast.token.Token;
import java.util.Map;
import java.util.Vector;
import javax.swing.JTable;

/**
 *
 * @author vrockai
 */
public class ContextPanel1 extends BeastPanel {

    private static final long serialVersionUID = 4227161040751571202L;
    Beast beast = null;

    /** Creates new form ContextPanel */
    public ContextPanel1() {
        initComponents();
    }

    public ContextPanel1(Beast b) {
        this.beast = b;
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

        jPanel1 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableContext = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jTextField2.setText("getrelativesbol2");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField2);

        add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jTableContext.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTableContext);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed

        Lexicon<String> l = beast.getLexicon();
        String[] words = jTextField2.getText().split(",");
        Vector<Map<Integer, Double>> sets = new Vector<Map<Integer, Double>>();

        //for(String word: words){
        //    word = word.trim();
        sets.add(countConsensus(l.getIndex(new Token<String>(words[0])), l.getIndex(new Token<String>(words[1]))));
        //}

        Map<Integer, Double> result = cortex.maxMin(sets);

        System.out.println("result size: " + result.size());
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        int c = 0;
        for (Map.Entry<Integer, Double> ei : result.entrySet()) {
            String token = l.getEntry(ei.getKey()).getValue();
            Vector<Object> row = new Vector<Object>();
            row.add(c++);
            row.add(token);
            row.add(ei.getValue());
            data.add(row);
        }

        Vector<String> columns = new Vector<String>();
        columns.add("#");
        columns.add("word");
        columns.add("strength");
        Class[] classes = {Integer.class, String.class, Double.class};
        jTableContext = new JTable(new JCommonModel(data, columns, classes));
        jTableContext.setDefaultRenderer(Double.class, new JCommonModel.DoubleRenderer());
        jTableContext.setAutoCreateRowSorter(true);
        jScrollPane1.setViewportView(jTableContext);
    }//GEN-LAST:event_jTextField2ActionPerformed

    private Map<Integer, Double> countConsensus(int t1, int t2) {
        ContextCortex cc = new ContextCortex(cortex);
        Lexicon<String> l = beast.getLexicon();
        return cc.getRelativesBool2(t1, t2, l.getHashMap().keySet(), null);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableContext;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
/*
    public static void main(String s[]) {
    Cortex cortex = null;
    try {
    cortex = new Cortex("/home/vrockai/Projects/Beast/CORTEX");
    } catch (Exception ex) {
    Logger.getLogger(BeastGui.class.getName()).log(Level.SEVERE, null, ex);
    }

    JFrame f = new JFrame("ShapesDemo2D");
    f.addWindowListener(new WindowAdapter() {

    public void windowClosing(WindowEvent e) {
    System.exit(0);
    }
    });
    JPanel applet = new ContextPanel1(cortex);

    f.getContentPane().add("Center", applet);
    //applet.init();
    f.pack();

    f.setVisible(true);
    }
     * */
}
