package edu.tuke.visualisation;

import edu.tuke.beast.relations.similarity.TokenSimilarityMatrix;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/*
 * This is like the FontDemo applet in volume 1, except that it
 * uses the Java 2D APIs to define and render the graphics and text.
 */
public class SimilarityMatrixJPanel extends JPanel implements MouseListener, MouseMotionListener {

    final static Color bg = Color.white;
    final static Color fg = Color.black;
    private static final long serialVersionUID = -9094153205311664930L;
    //private Graphics2D matrixBuf = null;
    BufferedImage spriteBuf = null;
    int sm_size = 0;
    int step = 10;
    int mx = 0, my = 0;
    private final Dimension dim;
    boolean normalized = true;
    final TokenSimilarityMatrix sm;

    public boolean isNormalized() {
        return normalized;
    }

    public void setNormalized(boolean n) {
        normalized = n;
    }

    public void setStepSize(int ss) {
        this.step = ss;
    }

    public int getStepSize() {
        return step;
    }

    public SimilarityMatrixJPanel(TokenSimilarityMatrix m) {
        super();
        this.dim = new Dimension(m.getSize() * step, m.getSize() * step);
        this.sm = m;
        this.setSize(dim);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void init() {
        //Initialize drawing colors
        setBackground(new Color(30, 30, 80));
        setForeground(fg);
    }

    private BufferedImage matrixPaint() {

        double k = normalized ? 1 / sm.maxValue() : 1;

        sm_size = sm.getSize();
        BufferedImage sprite = new BufferedImage(sm_size * 10, sm_size * 10, BufferedImage.TYPE_INT_ARGB);

        Graphics g = sprite.getGraphics();
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(50, 50, 80));
        g2.draw(new Rectangle(dim));
        g2.fill(new Rectangle(dim));

        for (int j = 0; j < sm_size; j++) {
            for (int i = 0; i < j; i++) {
                double v = sm.getValue(i, j);
                int col = (int) (v * 255 * k);
                Rectangle2D.Double square = new Rectangle2D.Double(i * step, j * step, step, step);
                g2.setColor(new Color(col, col, col));
                g2.draw(square);
                g2.fill(square);
            }
        }

        String[] lv = sm.getLexVArr();

        for (int i = 0; i < sm_size - 1; i++) {

            g2.setColor(new Color(30, 30, 30));
            g2.drawString(lv[i], (step * i) + 13, (i * step) + 11);

            g2.setColor(new Color(225, 165, 225));
            g2.drawString(lv[i], (step * i) + 12, (i * step) + 10);
        }

        return sprite;
    }

    @Override
    public void paint(Graphics g) {

        String[] lv = sm.getLexVArr();

        if (spriteBuf == null) {
            this.spriteBuf = matrixPaint();
        }


        Graphics2D g2 = (Graphics2D) g;

        int x = (mx / step);
        int y = (my / step);
        if (x < sm_size && y < sm_size) {
            int offset = 0;
            int st = sm_size / 3;


            double simValue = sm.getValue(x, y);

            String note = '\"' + lv[x] + "\"~\"" + lv[y] + "\" = " + simValue;

            if ((x > st) && (x < st * 2)) {
                offset = -1 * (note.length() * 7) / 2;
            }

            if ((x > st * 2)) {
                offset = -1 * (note.length() * 7);
            }

            g2.drawImage(spriteBuf, 0, 0, null);

            g2.setColor(new Color(30, 55, 55));
            Rectangle2D.Double underNote = new Rectangle2D.Double(offset + mx - 3, my - 12, (note.length() * 7) + 3, 16);
            Rectangle2D.Double borderNote = new Rectangle2D.Double(offset + mx - 3, my - 12, (note.length() * 7) + 3, 16);
            g2.fill(underNote);
            g2.draw(underNote);
            g2.setColor(new Color(180, 155, 155));
            g2.draw(borderNote);

            g2.setColor(new Color(30, 30, 30));
            g2.drawString(note, offset + mx + 1, my + 1);

            g2.setColor(new Color(235, 205, 0));
            g2.drawString(note, offset + mx, my);

            g2.draw(new Rectangle2D.Double(offset + mx, my, 1, 1));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
        this.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
        this.repaint();
    }

    public static void main(String s[]) {
        /*
        int N = 10;
        
        Lexicon<String> l = new Lexicon<String>();
        l.addToken(new Token<String>("Viliam"));
        l.addToken(new Token<String>("Pavla"));
        l.addToken(new Token<String>("Jesus"));
        l.addToken(new Token<String>("Gabriel"));
        l.addToken(new Token<String>("Marec"));
        l.addToken(new Token<String>("Samuel"));
        l.addToken(new Token<String>("Kristof"));
        l.addToken(new Token<String>("Karol"));
        l.addToken(new Token<String>("Marcel"));
        l.addToken(new Token<String>("Bukovecky"));
        
        int SIZE = l.getHashMap().size();
        
        TokenSimilarityMatrix sm = new TokenSimilarityMatrix(l.getHashMap());
        
        for(int i=0;i<SIZE;i++){
        for(int j=0;j<SIZE;j++){
        sm.setValue(i,j, ((i==j) ? 1d : ((double)(i*j)/(double)(SIZE*SIZE))) );
        }
        }
        
        //System.out.println("SM:\n"+sm.toString());
        JFrame f = new JFrame("SimilarityMatrixVisualisation");
        f.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        JPanel applet = new SimilarityMatrixJPanel(sm);
        
        f.getContentPane().add("Center", applet);
        //applet.init();
        f.pack();
        
        f.setSize(new Dimension(SIZE*10,SIZE*10+16));
        f.setVisible(true);
         * */
    }
}
