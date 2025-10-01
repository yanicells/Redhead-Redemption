/**
Blood loads the blood overlay images used to show player damage on the screen.
This class provides the different blood effect images for light, medium, and heavy damage states.
 
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

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Blood {
    BufferedImage lightDamage, mediumDamage, hardDamage;

    /**
     * Loads the blood overlay images for light, medium, and heavy damage.
     */
    public Blood() {
        try {
            lightDamage = ImageIO.read(getClass().getResourceAsStream("assets/lightDamage.png"));
            mediumDamage = ImageIO.read(getClass().getResourceAsStream("assets/mediumDamage.png"));
            hardDamage = ImageIO.read(getClass().getResourceAsStream("assets/hardDamage.png"));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

