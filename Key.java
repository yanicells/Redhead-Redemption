/**
The Key class represents a collectible key item that players can use to unlock doors or progress in the game.
It loads the key's image and sets its name, making it ready for use in the game's inventory system.

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

public class Key extends Item{
    /**
     * Creates a Key object, sets its name, and loads its image from resources.
     */
    public Key() {
        name = "Key";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("assets/key.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
