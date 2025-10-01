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

import java.io.IOException;
import javax.imageio.ImageIO;

public class Profile extends Item{

    /**
     * Loads the profile images for both alive and dead states when a Profile object is created.
     */
    public Profile() {
        name = "Profile";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("assets/Profile.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("assets/ProfileDead.png"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

