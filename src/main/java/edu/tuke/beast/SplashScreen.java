package edu.tuke.beast;

import edu.tuke.beast.panel.SplashPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

import org.apache.log4j.Logger;

public class SplashScreen extends JWindow {

    private static final long serialVersionUID = -545980459399968696L;
    SplashPanel content;
    private final int duration;
    private PropertyChangeListener propertyChangeListener;
    public static final Logger logger = Logger.getRootLogger();

    public SplashScreen(int d) {
        duration = d;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.propertyChangeListener = pcl;
    }

    public void setStatus(int i) {
        content.setStatus(i);
    }

    public void showSplash() {

        content = new SplashPanel();
        content.setBackground(Color.white);
        this.setContentPane(content);

        int width = 450;
        int height = 115;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        setVisible(true);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                setVisible(false);
                dispose();
            }
        });


        // Wait a little while, maybe while loading resources
        try {
            Thread.sleep(duration);
        } catch (Exception ex) {
            logger.error(ex);
        }

        setVisible(false);

    }

    public void showSplashAndExit() {

        showSplash();
        System.exit(0);

    }

    public static void main(String[] args) {

        // Throw a nice little title page up on the screen first
        SplashScreen splash = new SplashScreen(10000);

        // Normally, we'd call splash.showSplash() and get on
        // with the program. But, since this is only a test...
        splash.showSplashAndExit();

    }
}