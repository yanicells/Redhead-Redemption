/**
Gun represents a firearm item that the player can use, supporting multiple gun types and their images.
This class loads the images for each gun variant and sets the item's name for inventory and display purposes.
  
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

public class Gun extends Item{

    /**
     * Loads the images for all gun types and sets the item's name.
     */
    public Gun() {
        name = "Gun";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("assets/default.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("assets/smg.png"));
            image3 = ImageIO.read(getClass().getResourceAsStream("assets/rifle.png"));
            image4 = ImageIO.read(getClass().getResourceAsStream("assets/shotgun.png"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
