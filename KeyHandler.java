/**
KeyHandler manages all keyboard and mouse input for the game, translating user actions into game commands.
It tracks which keys and mouse buttons are pressed and updates the game state or player actions accordingly.
  
@author Edrian Miguel E. Capistrano (240939)
@author Sofia Dion Y. Torres (244566)
@version May 20, 2025

I have not discussed the Java language code in my program
with anyone other than my instructor or the teaching assistants
assigned to this course.
I have not used Java language code obtained from another student,
or any other unauthorized source, either modified or unmodified.
If any Java language code or documentation used in my program
was obtained from another source, such as a textbook or website,
that has been clearly noted with a proper citation in the comments
of my program.
**/

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KeyHandler implements KeyListener, MouseListener {
    protected boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, healPressed, swapPressed, useHealPressed, triggerPressed, shiftPressed, spacePressed;
    private int upKey, downKey, leftKey, rightKey, pauseKey, healKey, statusKey, swapKey, useHealKey, shootKey, sprintKey, dialogueKey;
    protected GameCanvas gameCanvas;


    /**
     * Sets up the KeyHandler and registers mouse input with the game canvas.
     * @param gameCanvas the main game canvas reference
     */
    public KeyHandler(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;
        setKeyBindings();

        gameCanvas.addMouseListener(this);
    }

   /**
     * Assigns the default key bindings for movement, actions, and other controls.
     */
    private void setKeyBindings() {
        this.upKey = KeyEvent.VK_W;
        this.downKey = KeyEvent.VK_S;
        this.leftKey = KeyEvent.VK_A;
        this.rightKey = KeyEvent.VK_D;
        this.pauseKey = KeyEvent.VK_P;
        this.healKey = KeyEvent.VK_E;
        this.statusKey = KeyEvent.VK_C;
        this.swapKey = KeyEvent.VK_F;
        this.useHealKey = KeyEvent.VK_G;
        this.shootKey = KeyEvent.VK_R;
        this.sprintKey = KeyEvent.VK_SHIFT;
        this.dialogueKey = KeyEvent.VK_SPACE;
    }

    /**
     * Not used, but required by the KeyListener interface.
     * @param e the key event
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }


    /**
     * Handles logic for when a key is pressed, updating the relevant flags or game state.
     * @param e the key event
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == upKey) {
            upPressed = true;
        } else if (code == downKey) {
            downPressed = true;
        } else if (code == leftKey) {
            leftPressed = true;
        } else if (code == rightKey) {
            rightPressed = true;
        } else if (code == healKey) {
            healPressed = true;
        } else if (code == statusKey) {
            gameCanvas.gameState = gameCanvas.characterStatus;
        } else if (code == swapKey) {
            swapPressed = true;
        } else if (code == useHealKey) {
            useHealPressed = true;
        } else if (code == sprintKey) {
            shiftPressed = true;
        } else if (code == dialogueKey) {
            spacePressed = true;
        }

    }

    /**
     * Handles logic for when a key is released, resetting the relevant flags or game state.
     * @param e the key event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == upKey) {
            upPressed = false;
        } else if (code == downKey) {
            downPressed = false;
        } else if (code == leftKey) {
            leftPressed = false;
        } else if (code == rightKey) {
            rightPressed = false;
        } else if (code == healKey) {
            healPressed = false;
        } else if (code == statusKey) {
            gameCanvas.gameState = gameCanvas.playState;
        } else if (code == swapKey) {
            swapPressed = false;
        } else if (code == useHealKey) {
            useHealPressed = false;
        } else if (code == sprintKey) {
            shiftPressed = false;
        } else if (code == dialogueKey) {
            spacePressed = false;
        }
    }

    /**
     * Handles logic for when a mouse button is pressed, setting flags for shooting or interacting.
     * @param e the mouse event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            triggerPressed = true;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            enterPressed = true;
        }
    }

    /**
     * Handles logic for when a mouse button is released, resetting flags for shooting or interacting.
     * @param e the mouse event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            triggerPressed = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            enterPressed = false;
        }
    }

    /**
     * Not used, but required by the MouseListener interface.
     * @param e the mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Not used, but required by the MouseListener interface.
     * @param e the mouse event
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Not used, but required by the MouseListener interface.
     * @param e the mouse event
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }
}