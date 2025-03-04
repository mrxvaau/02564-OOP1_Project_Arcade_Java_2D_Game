package com.projectoop1aiub.edu.test;

import java.awt.*;
import javax.swing.ImageIcon;

import com.projectoop1aiub.edu.physics.ScreenRatio;


public abstract class GameCore {

    protected static final int FONT_SIZE = 18;

    private static final DisplayMode POSSIBLE_MODES[] = {
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 16, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(640, 480, 16, 0),
        new DisplayMode(640, 480, 32, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(1024, 768, 16, 0),
        new DisplayMode(1024, 768, 32, 0),
        new DisplayMode(1024, 768, 24, 0),
    };

    private boolean isRunning;
    protected ScreenRatio screen;



    public void stop() {
        isRunning = false;
    }


    public void run() {
        try {
            init();
            gameLoop();
        }
        finally {
            screen.restoreScreen();
            lazilyExit();
        }
    }



    public void lazilyExit() {
        Thread thread = new Thread() {
            public void run() {
                // first, wait for the VM exit on its own.
                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException ex) { }
                // system is still running, so force an exit
                System.exit(0);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }



    public void init() 
    {
        screen = new ScreenRatio();
        DisplayMode displayMode =
        screen.findFirstCompatibleMode(POSSIBLE_MODES);
        screen.setFullScreen(displayMode);

        Window window = screen.getFullScreenWindow();
        window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
        window.setBackground(Color.BLACK);
        window.setForeground(Color.WHITE);

        isRunning = true;
    }


    public Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }



    public void gameLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

        while (isRunning) {
            long elapsedTime =
                System.currentTimeMillis() - currTime;
            currTime += elapsedTime;

            // update
            update(elapsedTime);

            // draw the screen
            Graphics2D g = screen.getGraphics();
            draw(g);
            g.dispose();
            screen.update();

            // don't take a nap! run as fast as possible
            /*try {
                Thread.sleep(20);
            }
            catch (InterruptedException ex) { }*/
        }
    }



    public void update(long elapsedTime) {
        // do nothing
    }



    public abstract void draw(Graphics2D g);
}
