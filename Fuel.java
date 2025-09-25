/**
Fuel represents a collectible item that players can pick up to refuel or progress in the game.
This class loads the fuel image and sets up the item's basic properties for use in the world.
 
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

import java.io.IOException;
import javax.imageio.ImageIO;

public class Fuel extends Item{

    /**
     * Loads the fuel image and sets the item's name.
     */
    public Fuel() {
        name = "Fuel";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("fuel.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
