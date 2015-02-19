/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PhrasePanel.java
 *
 * Created on Dec 8, 2008, 7:15:32 PM
 */
package edu.tuke.beast.panel;

import edu.tuke.beast.Beast;
import edu.tuke.beast.gui.BeastPanel;
import edu.tuke.beast.input.BeastAnalyzer;
import edu.tuke.beast.input.Input;
import edu.tuke.beast.token.Token;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.TokenStream;

/**
 *
 * @author blur
 */
public class PhrasePanel extends BeastPanel {

    private static final long serialVersionUID = -2113933703833778223L;
    
    public static final Token NULL_TOKEN = new Token<String>(null);
    private Token[] tokenWindow = {NULL_TOKEN, NULL_TOKEN, NULL_TOKEN, NULL_TOKEN, NULL_TOKEN};

    /** Creates new form PhrasePanel */
    public PhrasePanel() {
        initComponents();
        jEditorPane1.setContentType("text/html;");
    }

    public PhrasePanel(Beast b) {
        super(b);
        initComponents();
        jEditorPane1.setContentType("text/html;");
        beast = b;
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
        jButton1 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout());

        jButton1.setText("analyze");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jPanel1.add(jButton1);

        add(jPanel1, java.awt.BorderLayout.SOUTH);

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("type your text here...");
        jScrollPane2.setViewportView(jTextArea1);

        jSplitPane1.setRightComponent(jScrollPane2);

        jEditorPane1.setEditable(false);
        jScrollPane1.setViewportView(jEditorPane1);

        jSplitPane1.setTopComponent(jScrollPane1);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void shiftWindow(Token<String> newToken) {
        System.arraycopy(tokenWindow, 1, tokenWindow, 0, tokenWindow.length - 1);
        tokenWindow[tokenWindow.length - 1] = newToken;
    }

    private void recognize() throws IOException, CloneNotSupportedException {
        String text = jTextArea1.getText();
        BeastAnalyzer ba = new BeastAnalyzer();
        TokenStream tokenStream = ba.tokenStream(text);

        StringBuilder output = new StringBuilder();

        boolean phraseProcessing = false;
        output.append("<html>" + "<head>" + "<title>" + "Beast phrase checking module output..." + "</title>" + "<style>" + " body { background: #ffffff; color:#000000; }" + ".phrase { color:#d00000; font-weight:bold; } .null { color:#00d000; } " + "</style>" + "</head>" + "<body>");
        String tagStart = "\n<span class=phrase>";
        String tagEnd = "</span>\n";

        int wordsCount = 0;
        int nullCount = 0;
        int counter = 1;
        int wait = -4;
        for (org.apache.lucene.analysis.Token rawToken = tokenStream.next(); rawToken != null; rawToken = tokenStream.next(), ++counter) {
            shiftWindow((rawToken.termText().equals(Input.NULL_TOKEN)) ? NULL_TOKEN : new Token<String>(rawToken.termText()));

            int[] pi = beast.getLexicon().lexiconizeWindow(tokenWindow);
            Vector<Integer> vpi = new Vector<Integer>();
            for (int u : pi) {
                vpi.add(u);
            }

            int phraseLenght = cortex.isNgramConsensus(vpi);

            if (wait == 0) {
                output.append(tagEnd);
                phraseProcessing = false;
            }

            if (phraseLenght > 1 && !phraseProcessing) {
                output.append(tagStart);
                phraseProcessing = true;
                wait = phraseLenght;
            }

            if (wait > 0) {
                wordsCount++;
            }

            wait--;
            //if (wait < 0)
            //    continue;

            if (tokenWindow[0].equals(NULL_TOKEN)) {
                nullCount++;
            }

            // -------------------------//
            // Output information! //
            // -------------------------//

            if (!beast.getLexicon().contains(tokenWindow[0])) {
                output.append(" <span class='null'>%</span> ");
            } else if (!tokenWindow[0].equals(NULL_TOKEN)) {
                output.append(tokenWindow[0].getValue()).append(' ');
            } else {
                output.append(" <span class='null'>#</span> ");
            }
        }

        float percentage = 100 * (float) wordsCount / (float) (counter - nullCount);
        output.append("<hr><b>words count: </b>").append(wordsCount).append("<br>" + "<b>count: </b>").append(counter).append("<br>" + "<b>notNullCount: </b>").append(nullCount).append("<br>" + "<i>that's ").append(percentage).append("%</i> </body></html>");

        jEditorPane1.setText(output.toString());
    }

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        try {

            recognize();
        } catch (IOException ex) {
            Logger.getLogger(PhrasePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(PhrasePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1MouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
/*
    public static void main(String s[]) {
    
    Cortex cortex = null;
    try {
    cortex = new Cortex("/home/blur/Projects/Beast/CORTEX");
    } catch (Exception ex) {
    Logger.getLogger(BeastGui.class.getName()).log(Level.SEVERE, null, ex);
    }

    JFrame f = new JFrame("PhrasePanel");
    f.addWindowListener(new WindowAdapter() {

    public void windowClosing(WindowEvent e) {
    System.exit(0);
    }
    });
    JPanel applet = new PhrasePanel(cortex);

    f.getContentPane().add("Center", applet);

    f.pack();

    f.setVisible(true);
    }
     * */
}
