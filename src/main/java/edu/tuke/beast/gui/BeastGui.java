/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BeastGui.java
 *
 * Created on Dec 7, 2008, 1:27:11 AM
 */
package edu.tuke.beast.gui;

import edu.tuke.beast.Beast;
import edu.tuke.beast.cortex.Cortex;
import edu.tuke.beast.fascicle.Fascicle;
import edu.tuke.beast.panel.*;
import edu.tuke.beast.properties.PropertiesSingelton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

/**
 *
 * @author blur
 */
public class BeastGui extends javax.swing.JFrame {

    private LoggerPanel loggerPanel = new LoggerPanel();
    public static final Logger logger = Logger.getRootLogger();
    private static final long serialVersionUID = 189698408304698265L;
    private Beast beast;
    
    //private Cortex cortex;

    private final List<BeastPanel> panels = new ArrayList<BeastPanel>();
/*
    private BeastPanel jRelativesPanel;
    private BeastPanel jAnalyzePanel;
    private BeastPanel jLexiconPanel;
    private BeastPanel jPhrasePanel;
    private BeastPanel jContentPanel;
    private BeastPanel jConceptPanel;
    private BeastPanel jOperatorsPanel;
    private BeastPanel jFasciclePanel;
    private BeastPanel jStatusPanel;
    private BeastPanel jContextPanel;
    private BeastPanel jClusterPanel;
    private BeastPanel jLearnPanel;
 * */
    private final JFileChooser fc = new JFileChooser();
    private final SplashScreen splash = new SplashScreen();

    public static final Properties properties = PropertiesSingelton.getInstance().getProperties();

    private void splashScreen() {
        if("true".equals(properties.getProperty("showsplash")))
            splash.showSplash();
    }

    private void setBeast(Beast b) {
        
        this.beast = b;

        for (BeastPanel beastPanel : panels) {
            beastPanel.setBeast(beast);
        }

        updateStatus();
    }

    public void setBeast(String fpath) throws Exception {
        this.beast = new Beast(fpath);
        setBeast(beast);
    }

    /** Creates new form BeastGui */
    public BeastGui() {
      //  addLogger();
        this.beast = new Beast();        
        initComponents();
        updateStatus();
        initTabs();
        initProperties();
        splash.hideSplash();

    }

    private void initProperties(){
        Cortex.ConsensusStrategy consStr = Cortex.getStrategy();
        jComboBoxConsensusStr.setSelectedItem(consStr);
        logger.debug("Consensus strategy selected: " + jComboBoxConsensusStr.getSelectedItem());

        Fascicle.SigStrategy fasStr = beast.getCortex().getRegion().getFascicle(0).getStrategy();
        jComboBoxFascicleStr.setSelectedItem(fasStr);
        logger.debug("Fascicle strategy selected: " + jComboBoxFascicleStr.getSelectedItem());
        
    }

    private void updateStatus() {
        if (beast.getCortex() != null) {
            jLabelStatusVal.setText("on");
            jLabelRegVal.setText(String.valueOf(beast.getCortex().getRegion().fascicle.length));
            jLabelTresVal.setText(String.valueOf(beast.getCortex().getTreshold()));
        } else {
            jLabelStatusVal.setText("off");
            jLabelLexVal.setText("-");
            jLabelRegVal.setText("-");
            jLabelTresVal.setText("-");
        }
    }

    public BeastGui(String beast_path) {

        PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                String property = propertyChangeEvent.getPropertyName();
                if ("status".equals(property)) {
                    Integer value = (Integer) propertyChangeEvent.getNewValue();
                    splash.setStatus(value);
                }
            }
        };

        Beast.addPropertyChangeListener(propertyChangeListener);

        try {
            splashScreen();
            initComponents();
            beast = new Beast();
            beast = beast.deSerialize(beast_path);

        } catch (Exception ex) {
            logger.error("Problem with loading cortex: " + beast_path);
            logger.error(ex);
            logger.info("Starting blank Beast without cortex");
            beast = new Beast();

        } finally {

            //addLogger();            
            updateStatus();
            initTabs();
            splash.setStatus(100);
            splash.hideSplash();

        }
        initProperties();
    }

    public BeastGui(Beast b) throws Exception {

       // addLogger();
        initComponents();        
        setBeast(b);
        updateStatus();
        initTabs();
        initProperties();
        splash.hideSplash();
    }

    private void initTabs() {        
        panels.add(new FasciclePanel(beast));
        panels.add(new RelativesPanel(beast));
        panels.add(new AnalyzePanel(beast));
        panels.add(new LexiconPanel(beast));
        panels.add(new ContextPanel(beast));
        panels.add(new LearnPanel(beast));
        panels.add(new LearnPanel1(beast));
        panels.add(new OperatorsPanel(beast));
        panels.add(new ConceptPanel(beast));
        panels.add(new ConceptDetailsPanel(beast));
        panels.add(new OperatorDetailsPanel(beast));
        panels.add(new ConsensusPanel(beast));
        panels.add(new CGRecognitionPanel(beast));
        panels.add(new ConcepClusterPanel(beast));

        for (BeastPanel beastPanel : panels) {
            jTabbedPane.add(beastPanel.getName(), beastPanel);
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

        jRadioButton1 = new javax.swing.JRadioButton();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelStatus = new javax.swing.JPanel();
        jLabelStatus = new javax.swing.JLabel();
        jLabelStatusVal = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelLex = new javax.swing.JLabel();
        jLabelLexVal = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabelReg = new javax.swing.JLabel();
        jLabelRegVal = new javax.swing.JLabel();
        jLabelTres = new javax.swing.JLabel();
        jLabelTresVal = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxConsensusStr = new javax.swing.JComboBox();
        jCheckBoxInvWei = new javax.swing.JCheckBox();
        jToolBar3 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxFascicleStr = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldTreshold = new javax.swing.JTextField();
        jButtonTreshold = new javax.swing.JButton();
        jCheckBoxCache = new javax.swing.JCheckBox();
        jButtonCache = new javax.swing.JButton();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuOpen = new javax.swing.JMenuItem();
        jMenuSave = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        jMenuSavePrefs = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuQuit = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemAbout = new javax.swing.JMenuItem();

        jRadioButton1.setText("jRadioButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Beast!");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(640, 480));
        setName("beast"); // NOI18N

        jTabbedPane.setMaximumSize(new java.awt.Dimension(800, 800));
        jTabbedPane.setMinimumSize(new java.awt.Dimension(200, 200));
        getContentPane().add(jTabbedPane, java.awt.BorderLayout.CENTER);

        jPanelStatus.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jLabelStatus.setText("status:");
        jPanelStatus.add(jLabelStatus);

        jLabelStatusVal.setText("off");
        jPanelStatus.add(jLabelStatusVal);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jSeparator2.setMinimumSize(new java.awt.Dimension(10, 10));
        jSeparator2.setPreferredSize(new java.awt.Dimension(4, 15));
        jPanelStatus.add(jSeparator2);

        jLabelLex.setText("lexicon:");
        jPanelStatus.add(jLabelLex);

        jLabelLexVal.setText("-");
        jPanelStatus.add(jLabelLexVal);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jSeparator3.setMinimumSize(new java.awt.Dimension(10, 10));
        jSeparator3.setPreferredSize(new java.awt.Dimension(4, 15));
        jPanelStatus.add(jSeparator3);

        jLabelReg.setText("region:");
        jPanelStatus.add(jLabelReg);

        jLabelRegVal.setText("-");
        jPanelStatus.add(jLabelRegVal);

        jLabelTres.setText("treshold:");
        jPanelStatus.add(jLabelTres);

        jLabelTresVal.setText("-");
        jPanelStatus.add(jLabelTresVal);

        getContentPane().add(jPanelStatus, java.awt.BorderLayout.PAGE_END);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jToolBar1.setRollover(true);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setLabelFor(jComboBoxConsensusStr);
        jLabel1.setText("Consensus strategy: ");
        jLabel1.setMaximumSize(new java.awt.Dimension(131, 24));
        jLabel1.setMinimumSize(new java.awt.Dimension(131, 24));
        jLabel1.setPreferredSize(new java.awt.Dimension(131, 24));
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jLabel1);

        jComboBoxConsensusStr.setModel(new DefaultComboBoxModel(Cortex.ConsensusStrategy.values()));
        jComboBoxConsensusStr.setToolTipText("strategy");
        jComboBoxConsensusStr.setMaximumSize(new java.awt.Dimension(100, 24));
        jComboBoxConsensusStr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxConsensusStrActionPerformed(evt);
            }
        });
        jToolBar1.add(jComboBoxConsensusStr);

        jCheckBoxInvWei.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jCheckBoxInvWei.setText("Inverse weight");
        jCheckBoxInvWei.setFocusable(false);
        jCheckBoxInvWei.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jCheckBoxInvWei.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jCheckBoxInvWei.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxInvWeiActionPerformed(evt);
            }
        });
        jToolBar1.add(jCheckBoxInvWei);

        jPanel1.add(jToolBar1);

        jToolBar3.setRollover(true);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setLabelFor(jComboBoxFascicleStr);
        jLabel2.setText("Fascicle strategy:");
        jLabel2.setMaximumSize(new java.awt.Dimension(98, 24));
        jLabel2.setMinimumSize(new java.awt.Dimension(98, 24));
        jLabel2.setPreferredSize(new java.awt.Dimension(98, 24));
        jLabel2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jLabel2);

        jComboBoxFascicleStr.setModel(new DefaultComboBoxModel(Fascicle.SigStrategy.values()));
        jComboBoxFascicleStr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFascicleStrActionPerformed(evt);
            }
        });
        jToolBar3.add(jComboBoxFascicleStr);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Treshold:");
        jLabel3.setMaximumSize(new java.awt.Dimension(71, 24));
        jLabel3.setMinimumSize(new java.awt.Dimension(51, 24));
        jLabel3.setPreferredSize(new java.awt.Dimension(71, 24));
        jToolBar3.add(jLabel3);

        jTextFieldTreshold.setText("1");
        jTextFieldTreshold.setMaximumSize(new java.awt.Dimension(50, 24));
        jTextFieldTreshold.setMinimumSize(new java.awt.Dimension(40, 24));
        jTextFieldTreshold.setPreferredSize(new java.awt.Dimension(40, 24));
        jTextFieldTreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTresholdActionPerformed(evt);
            }
        });
        jToolBar3.add(jTextFieldTreshold);

        jButtonTreshold.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/tango/16x16/actions/go-top.png"))); // NOI18N
        jButtonTreshold.setText("Set threshold");
        jButtonTreshold.setToolTipText("Set threshold");
        jButtonTreshold.setAlignmentX(0.5F);
        jButtonTreshold.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jButtonTreshold.setFocusable(false);
        jButtonTreshold.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButtonTreshold.setMaximumSize(new java.awt.Dimension(140, 24));
        jButtonTreshold.setMinimumSize(new java.awt.Dimension(140, 24));
        jButtonTreshold.setOpaque(false);
        jButtonTreshold.setPreferredSize(new java.awt.Dimension(140, 24));
        jButtonTreshold.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonTreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTresholdActionPerformed(evt);
            }
        });
        jToolBar3.add(jButtonTreshold);

        jCheckBoxCache.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jCheckBoxCache.setSelected(true);
        jCheckBoxCache.setText("caching");
        jCheckBoxCache.setFocusable(false);
        jCheckBoxCache.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jCheckBoxCache.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jCheckBoxCache.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCacheActionPerformed(evt);
            }
        });
        jToolBar3.add(jCheckBoxCache);

        jButtonCache.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jButtonCache.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/tango/16x16/actions/edit-clear.png"))); // NOI18N
        jButtonCache.setText("clear cache");
        jButtonCache.setAlignmentX(0.5F);
        jButtonCache.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jButtonCache.setFocusable(false);
        jButtonCache.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButtonCache.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonCache.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCacheActionPerformed(evt);
            }
        });
        jToolBar3.add(jButtonCache);

        jPanel1.add(jToolBar3);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jMenuFile.setText("File");

        jMenuOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/tango/16x16/actions/document-open.png"))); // NOI18N
        jMenuOpen.setText("Open cortex...");
        jMenuOpen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuOpenMousePressed(evt);
            }
        });
        jMenuFile.add(jMenuOpen);

        jMenuSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/tango/16x16/actions/document-save-as.png"))); // NOI18N
        jMenuSave.setText("Save cortex...");
        jMenuSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuSaveMousePressed(evt);
            }
        });
        jMenuFile.add(jMenuSave);
        jMenuFile.add(jSeparator5);

        jMenuSavePrefs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/tango/16x16/actions/document-save.png"))); // NOI18N
        jMenuSavePrefs.setText("Save preferences");
        jMenuSavePrefs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuSavePrefsMousePressed(evt);
            }
        });
        jMenuFile.add(jMenuSavePrefs);
        jMenuFile.add(jSeparator1);

        jMenuQuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/tango/16x16/actions/process-stop.png"))); // NOI18N
        jMenuQuit.setText("Quit");
        jMenuQuit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuQuitMousePressed(evt);
            }
        });
        jMenuFile.add(jMenuQuit);

        jMenuBar.add(jMenuFile);

        jMenuHelp.setText("Help");

        jMenuItemAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/tango/16x16/actions/go-home.png"))); // NOI18N
        jMenuItemAbout.setText("About");
        jMenuHelp.add(jMenuItemAbout);

        jMenuBar.add(jMenuHelp);

        setJMenuBar(jMenuBar);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-603)/2, (screenSize.height-480)/2, 603, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonTresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTresholdActionPerformed

        logger.debug("getting treshold");
        String fString = jTextFieldTreshold.getText();
        Float f = Float.parseFloat(fString);
        logger.debug("setting treshold");
        if (f != null) {
            logger.debug("treshold has nonnull value");
            setTreshold(f);
        }
}//GEN-LAST:event_jButtonTresholdActionPerformed

    private void jTextFieldTresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTresholdActionPerformed

        String fString = jTextFieldTreshold.getText();
        Float f = Float.parseFloat(fString);
        if (f != null) {
            setTreshold(f);
        }
}//GEN-LAST:event_jTextFieldTresholdActionPerformed

    private void jMenuOpenMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuOpenMousePressed
        int state = fc.showOpenDialog(this);
        if (state == JFileChooser.APPROVE_OPTION) {
            try {
                String filename = fc.getSelectedFile().toString();
                String cortexSuffix = filename.split("\\.")[filename.split("\\.").length-1];
                String cortexPrefix = filename.substring(0, filename.length() - cortexSuffix.length() - 1);

                setBeast(cortexPrefix);
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
}//GEN-LAST:event_jMenuOpenMousePressed

    private void jMenuSaveMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuSaveMousePressed
        int state = fc.showOpenDialog(this);
        if (state == JFileChooser.APPROVE_OPTION) {
            try {
                String corPath = fc.getSelectedFile().toString();
                beast.getLexicon().serialize(corPath);
                beast.getCortex().serialize(corPath);
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
}//GEN-LAST:event_jMenuSaveMousePressed

    private void jMenuQuitMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuQuitMousePressed
        System.exit(0);
}//GEN-LAST:event_jMenuQuitMousePressed

    private void jMenuSavePrefsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuSavePrefsMousePressed
        PropertiesSingelton.getInstance().saveProperties();
    }//GEN-LAST:event_jMenuSavePrefsMousePressed

    private void jComboBoxConsensusStrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxConsensusStrActionPerformed
        String conStr = jComboBoxConsensusStr.getSelectedItem().toString();
        logger.debug("Consensus strategy selected: " + jComboBoxConsensusStr.getSelectedItem());
        Cortex.ConsensusStrategy consStr = Cortex.ConsensusStrategy.valueOf(conStr);
        Cortex.setStrategy(consStr);        
    }//GEN-LAST:event_jComboBoxConsensusStrActionPerformed

    private void jComboBoxFascicleStrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFascicleStrActionPerformed
        String fasStrS = jComboBoxFascicleStr.getSelectedItem().toString();
        logger.debug("Fascicle strategy selected: " + jComboBoxFascicleStr.getSelectedItem());
        Fascicle.SigStrategy fasStr = Fascicle.SigStrategy.valueOf(fasStrS);
        beast.getCortex().getRegion().setFascicleStrategy(fasStr);
    }//GEN-LAST:event_jComboBoxFascicleStrActionPerformed

    private void jCheckBoxInvWeiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxInvWeiActionPerformed
        boolean sel = jCheckBoxInvWei.isSelected();
        logger.debug("Wei inv: "+sel);
        Fascicle.setInvWei(sel);
    }//GEN-LAST:event_jCheckBoxInvWeiActionPerformed

    private void jCheckBoxCacheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCacheActionPerformed
        boolean cache = jCheckBoxCache.isSelected();
        Fascicle.setCaching(cache);
    }//GEN-LAST:event_jCheckBoxCacheActionPerformed

    private void jButtonCacheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCacheActionPerformed
        for(int i=0;i<beast.getCortex().getRegion().getSize(); i++){
            beast.getCortex().getRegion().getFascicle(i).clearCache();
        }
    }//GEN-LAST:event_jButtonCacheActionPerformed

    private void setTreshold(float tres) {
        logger.debug("COR: setting");
        beast.getCortex().setTreshold(tres);
        logger.debug("setting text");
        jLabelTresVal.setText(String.valueOf(tres));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCache;
    private javax.swing.JButton jButtonTreshold;
    private javax.swing.JCheckBox jCheckBoxCache;
    private javax.swing.JCheckBox jCheckBoxInvWei;
    private javax.swing.JComboBox jComboBoxConsensusStr;
    private javax.swing.JComboBox jComboBoxFascicleStr;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelLex;
    private javax.swing.JLabel jLabelLexVal;
    private javax.swing.JLabel jLabelReg;
    private javax.swing.JLabel jLabelRegVal;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelStatusVal;
    private javax.swing.JLabel jLabelTres;
    private javax.swing.JLabel jLabelTresVal;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuOpen;
    private javax.swing.JMenuItem jMenuQuit;
    private javax.swing.JMenuItem jMenuSave;
    private javax.swing.JMenuItem jMenuSavePrefs;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelStatus;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTextField jTextFieldTreshold;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar3;
    // End of variables declaration//GEN-END:variables

    public class SplashScreen extends JWindow {

        private static final long serialVersionUID = -7189332188706202727L;
        private int status;
        private final SplashPanel content = new SplashPanel();

        public SplashScreen() {
            this.add(content);
            logger.debug("AOT:" + this.isAlwaysOnTop());
        }

        public void setStatus(int i) {
            status = i;
            content.setStatus(i);
        //this.repaint();
        }

        // A simple little method to show a title screen in the center
        // of the screen for the amount of time given in the constructor
        public void showSplash() {

            content.setBackground(Color.white);
            //this.setContentPane(content);

            int width = 636;
            int height = 410;
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screen.width - width) / 2;
            int y = (screen.height - height) / 2;

            setBounds(x, y, width, height);

            setVisible(true);
            
        }

        public void hideSplash() {
            final int pause = 1000;
            final Runnable closerRunner = new Runnable() {

                @Override
                public void run() {
                    setVisible(false);
                    dispose();
                }
            };


            Runnable waitRunner = new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(pause);
                        SwingUtilities.invokeAndWait(closerRunner);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            };


            Thread splashThread = new Thread(waitRunner, "SplashThread");
            splashThread.start();

        // setVisible(false);
        }

        public void showSplashAndExit() {

            showSplash();
            System.exit(0);

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FileAppender fa = (FileAppender) (logger.getAppender("R"));
        logger.info("Writing into log file: "+fa.getFile());
        if (args.length > 0) {
            logger.info("Starting Beast with cortex: " + args[0]);
            new BeastGui(args[0]).setVisible(true);
        } else {
            logger.info("Starting blank Beast");
            new BeastGui().setVisible(true);
        }
    }
}
