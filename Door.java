/**
Door represents a solid, non-passable object in the game world that players may need to unlock or interact with to progress.
This class loads the door image and sets up its collision property so it blocks movement by default.
 
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

public class Door extends Item{
    
    /**
     * Loads the door image, sets its name, and marks it as a collidable object.
     */
    public Door() {
        name = "Door";

        collision = true;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("assets/door.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
