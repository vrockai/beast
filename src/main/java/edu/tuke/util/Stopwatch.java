package edu.tuke.util;

/**
 * A class to help benchmark code It simulates a real stop watch
 */
public class Stopwatch {

    private int startTime = -1;
    private int stopTime = -1;
    private boolean running = false;

    public Stopwatch start() {
        startTime = (int) System.currentTimeMillis();
        running = true;
        return this;
    }

    public Stopwatch stop() {
        stopTime = (int) System.currentTimeMillis();
        running = false;
        return this;
    }

    /**
     * returns elapsed time in milliseconds if the watch has never been started
     * then return zero
     */
    public int getElapsedTime() {
        if (startTime == -1) {
            return 0;
        }
        return running ? (int) System.currentTimeMillis() - startTime : stopTime - startTime;
    }

    public Stopwatch reset() {
        startTime = -1;
        stopTime = -1;
        running = false;
        return this;
    }
}