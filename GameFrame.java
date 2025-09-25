/**
GameFrame sets up the main game window and attaches the game canvas where everything is drawn.
It handles the creation of the game window and configures the GUI for the player.
 
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

import javax.swing.*;

public class GameFrame {
    private JFrame gameWindow;
    protected GameCanvas gameCanvas;

    /**
     * Constructs the GameFrame, initializes the game window, and creates the game canvas.
     */
    public GameFrame() {
        gameWindow = new JFrame();
        gameCanvas = new GameCanvas();
    }
    
    /**
     * Sets up the player GUI, configures the window, and makes the game visible on the screen.
     */
    public void setUpPlayerGUI(){
        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameWindow.setResizable(false);

        gameWindow.setTitle("Read Head Redemption");

        gameWindow.add(gameCanvas);

        gameWindow.pack();
        gameCanvas.setUpWorld();
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setVisible(true);
    }
}
