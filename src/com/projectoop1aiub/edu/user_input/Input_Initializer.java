package com.projectoop1aiub.edu.user_input;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.SwingUtilities;


public class Input_Initializer implements KeyListener, MouseListener,
    MouseMotionListener, MouseWheelListener
{
    
    public static final Cursor INVISIBLE_CURSOR =
        Toolkit.getDefaultToolkit().createCustomCursor(
            Toolkit.getDefaultToolkit().getImage(""),
            new Point(0,0),
            "invisible");

    
    public static final int MOUSE_MOVE_LEFT = 0;
    public static final int MOUSE_MOVE_RIGHT = 1;
    public static final int MOUSE_MOVE_UP = 2;
    public static final int MOUSE_MOVE_DOWN = 3;
    public static final int MOUSE_WHEEL_UP = 4;
    public static final int MOUSE_WHEEL_DOWN = 5;
    public static final int MOUSE_BUTTON_1 = 6;
    public static final int MOUSE_BUTTON_2 = 7;
    public static final int MOUSE_BUTTON_3 = 8;

    private static final int NUM_MOUSE_CODES = 9;

    private static final int NUM_KEY_CODES = 600;

    private GameReflex[] keyActions =
        new GameReflex[NUM_KEY_CODES];
    private GameReflex[] mouseActions =
        new GameReflex[NUM_MOUSE_CODES];

    private Point mouseLocation;
    private Point centerLocation;
    private Component comp;
    private Robot robot;
    private boolean isRecentering;

  
    public Input_Initializer(Component comp) {
        this.comp = comp;
        mouseLocation = new Point();
        centerLocation = new Point();

        comp.addKeyListener(this);
        comp.addMouseListener(this);
        comp.addMouseMotionListener(this);
        comp.addMouseWheelListener(this);

        comp.setFocusTraversalKeysEnabled(false);
    }


    public void setCursor(Cursor cursor) 
    {
        comp.setCursor(cursor);
    }


    public void setRelativeMouseMode(boolean mode) 
    {
        if (mode == isRelativeMouseMode()) {
            return;
        }

        if (mode) {
            try {
                robot = new Robot();
                recenterMouse();
            }
            catch (AWTException ex) {
                
                robot = null;
            }
        }
        else {
            robot = null;
        }
    }


    public boolean isRelativeMouseMode() {
        return (robot != null);
    }


    public void mapToKey(GameReflex gameReflex, int keyCode) {
        keyActions[keyCode] = gameReflex;
    }


    public void mapToMouse(GameReflex gameReflex,
                           int mouseCode)
    {
        mouseActions[mouseCode] = gameReflex;
    }


    public void clearMap(GameReflex gameReflex)
    {
        for (int i=0; i<keyActions.length; i++) {
            if (keyActions[i] == gameReflex) {
                keyActions[i] = null;
            }
        }

        for (int i=0; i<mouseActions.length; i++) {
            if (mouseActions[i] == gameReflex) {
                mouseActions[i] = null;
            }
        }

        gameReflex.reset();
    }


   
    public List getMaps(GameReflex gameCode) {
        ArrayList list = new ArrayList();

        for (int i=0; i<keyActions.length; i++) {
            if (keyActions[i] == gameCode) {
                list.add(getKeyName(i));
            }
        }

        for (int i=0; i<mouseActions.length; i++) {
            if (mouseActions[i] == gameCode) {
                list.add(getMouseName(i));
            }
        }
        return list;
    }


    
    public void resetAllGameActions() {
        for (int i=0; i<keyActions.length; i++) {
            if (keyActions[i] != null) {
                keyActions[i].reset();
            }
        }

        for (int i=0; i<mouseActions.length; i++) {
            if (mouseActions[i] != null) {
                mouseActions[i].reset();
            }
        }
    }


    
    public static String getKeyName(int keyCode) {
        return KeyEvent.getKeyText(keyCode);
    }


   
    public static String getMouseName(int mouseCode) {
        switch (mouseCode) {
            case MOUSE_MOVE_LEFT: return "Mouse Left";
            case MOUSE_MOVE_RIGHT: return "Mouse Right";
            case MOUSE_MOVE_UP: return "Mouse Up";
            case MOUSE_MOVE_DOWN: return "Mouse Down";
            case MOUSE_WHEEL_UP: return "Mouse Wheel Up";
            case MOUSE_WHEEL_DOWN: return "Mouse Wheel Down";
            case MOUSE_BUTTON_1: return "Mouse Button 1";
            case MOUSE_BUTTON_2: return "Mouse Button 2";
            case MOUSE_BUTTON_3: return "Mouse Button 3";
            default: return "Unknown mouse code " + mouseCode;
        }
    }


   
    public int getMouseX() {
        return mouseLocation.x;
    }


    public int getMouseY() {
        return mouseLocation.y;
    }


   
    private synchronized void recenterMouse() {
        if (robot != null && comp.isShowing()) {
            centerLocation.x = comp.getWidth() / 2;
            centerLocation.y = comp.getHeight() / 2;
            SwingUtilities.convertPointToScreen(centerLocation,
                comp);
            isRecentering = true;
            robot.mouseMove(centerLocation.x, centerLocation.y);
        }
    }


    private GameReflex getKeyAction(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode < keyActions.length) {
            return keyActions[keyCode];
        }
        else {
            return null;
        }
    }

  
    public static int getMouseButtonCode(MouseEvent e) {
         switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                return MOUSE_BUTTON_1;
            case MouseEvent.BUTTON2:
                return MOUSE_BUTTON_2;
            case MouseEvent.BUTTON3:
                return MOUSE_BUTTON_3;
            default:
                return -1;
        }
    }


    private GameReflex getMouseButtonAction(MouseEvent e) {
        int mouseCode = getMouseButtonCode(e);
        if (mouseCode != -1) {
             return mouseActions[mouseCode];
        }
        else {
             return null;
        }
    }



    public void keyPressed(KeyEvent e) {
        GameReflex gameReflex = getKeyAction(e);
        if (gameReflex != null) {
            gameReflex.press();
        }

        e.consume();
    }


    public void keyReleased(KeyEvent e) {
        GameReflex gameReflex = getKeyAction(e);
        if (gameReflex != null) {
            gameReflex.release();
        }

        e.consume();
    }


    public void keyTyped(KeyEvent e) {

        e.consume();
    }


    public void mousePressed(MouseEvent e) {
        GameReflex gameReflex = getMouseButtonAction(e);
        if (gameReflex != null) {
            gameReflex.press();
        }
    }

    public void mouseReleased(MouseEvent e) {
        GameReflex gameReflex = getMouseButtonAction(e);
        if (gameReflex != null) {
            gameReflex.release();
        }
    }



    public void mouseClicked(MouseEvent e) {
        // do nothing
    }

    public void mouseEntered(MouseEvent e) {
        mouseMoved(e);
    }


    public void mouseExited(MouseEvent e) {
        mouseMoved(e);
    }



    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }



    public synchronized void mouseMoved(MouseEvent e) {

        if (isRecentering &&
            centerLocation.x == e.getX() &&
            centerLocation.y == e.getY())
        {
            isRecentering = false;
        }
        else {
            int dx = e.getX() - mouseLocation.x;
            int dy = e.getY() - mouseLocation.y;
            mouseHelper(MOUSE_MOVE_LEFT, MOUSE_MOVE_RIGHT, dx);
            mouseHelper(MOUSE_MOVE_UP, MOUSE_MOVE_DOWN, dy);

            if (isRelativeMouseMode()) {
                recenterMouse();
            }
        }

        mouseLocation.x = e.getX();
        mouseLocation.y = e.getY();

    }


    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseHelper(MOUSE_WHEEL_UP, MOUSE_WHEEL_DOWN,
            e.getWheelRotation());
    }

    private void mouseHelper(int codeNeg, int codePos,
        int amount)
    {
        GameReflex gameReflex;
        if (amount < 0) {
            gameReflex = mouseActions[codeNeg];
        }
        else {
            gameReflex = mouseActions[codePos];
        }
        if (gameReflex != null) {
            gameReflex.press(Math.abs(amount));
            gameReflex.release();
        }
    }

}
