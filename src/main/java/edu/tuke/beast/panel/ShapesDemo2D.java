package edu.tuke.beast.panel;

import edu.tuke.beast.relations.similarity.SimilarityMatrix;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/*
 * This is like the FontDemo applet in volume 1, except that it
 * uses the Java 2D APIs to define and render the graphics and text.
 */
public class ShapesDemo2D extends JPanel implements MouseListener, MouseMotionListener {

    final static Color bg = Color.white;
    final static Color fg = Color.black;
    private static final long serialVersionUID = -3425843200249832545L;
    private Graphics2D matrixBuf = null;
    BufferedImage spriteBuf = null;
    int mx = 0, my = 0;
    private final Dimension dim;
    final SimilarityMatrix sm;

    public ShapesDemo2D(Dimension d, SimilarityMatrix m) {
        super();
        this.dim = d;
        this.sm = m;
        this.setSize(d);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void init() {
        //Initialize drawing colors
        setBackground(new Color(30, 30, 80));
        setForeground(fg);
    }

    private BufferedImage matrixPaint() {
        int sm_size = sm.getSize();

        BufferedImage sprite = new BufferedImage(sm_size * 10, sm_size * 10, BufferedImage.TYPE_INT_ARGB);

        Graphics g = sprite.getGraphics();
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(50, 50, 80));
        g2.draw(new Rectangle(dim));
        g2.fill(new Rectangle(dim));

        //int sm_size = 2;
        int step = 10;

        for (int i = 0; i < sm_size; i++) {
            for (int j = 0; j < sm_size; j++) {
                double v = sm.getValue(i, j);
                int col = (int) (v * 255);
                Rectangle2D.Double square = new Rectangle2D.Double(i * step, j * step, step, step);
                System.out.println((i * step) + "," + (j * step) + ',' + ((i * step) + step) + ',' + ((j * step) + step));
                g2.setColor(new Color(col, col, col));
                g2.draw(square);
                g2.fill(square);
            }
        }

        return sprite;
    }

    @Override
    public void paint(Graphics g) {

        if (spriteBuf == null) {
            this.spriteBuf = matrixPaint();
        }

        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(spriteBuf, 0, 0, null);

        g2.setColor(new Color(255, 255, 0));
        g2.drawString("x:" + mx + ",y:" + my, 10, 10);
        g2.draw(new Rectangle2D.Double(mx, my, 1, 1));

//        Rectangle2D.Double border = new Rectangle2D.Double( 0,0,4*10,4*10 );
//        g2.setColor(new Color(255,0,0));
//        g2.draw(border);        
    }

    public static void main(String s[]) {

        int N = 10;
        int SIZE = 50;

        SimilarityMatrix sm = new SimilarityMatrix(SIZE);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(((i == j) ? 1d : ((double) (i * j) / (double) (SIZE * SIZE)) + ","));
                sm.setValue(i, j, ((i == j) ? 1d : ((double) (i * j) / (double) (SIZE * SIZE))));
            }
            System.out.println("");
        }

        System.out.println("SM:\n" + sm.toString());
        JFrame f = new JFrame("ShapesDemo2D");
        f.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        JPanel applet = new ShapesDemo2D(new Dimension(SIZE * 10, SIZE * 10), sm);

        f.getContentPane().add("Center", applet);
        //applet.init();
        f.pack();

        f.setSize(new Dimension(SIZE * 10, SIZE * 10 + 16));
        f.setVisible(true);
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
}
