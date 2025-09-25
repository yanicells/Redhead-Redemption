/**
This class loads the profile images used for displaying player status in the game's UI. It represents the player's profile icon, including both the normal and dead versions.
 
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
import java.awt.Graphics2D;

public class BorderManager {

    GameCanvas gameCanvas;
    Border lighting;

    public BorderManager(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;
    }

    public void setup() {

        lighting = new Border(gameCanvas, 700);
    }

    public void draw(Graphics2D g2d) {

        lighting.draw(g2d);
    }
}
