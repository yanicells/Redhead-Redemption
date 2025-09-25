/**
 Medkit represents a healing item that restores a portion of the player's health when used.
This class loads the medkit image, sets its healing value, and defines how it heals a character.
 
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

public class Medkit extends Item{
    protected int healingValue;

    /**
     * Loads the medkit image and sets its healing value and type.
     * @param gameCanvas the main game canvas reference
     */
    public Medkit(GameCanvas gameCanvas) {
        super(gameCanvas);
        healingValue = 8;
        type = 2;
        name = "Medkit";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("Medkit.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Restores health to the given character up to their maximum life.
     * @param character the character who will be healed
     */
    @Override
    public void heal(Character character) {
        character.currentLife += healingValue;
        if(character.currentLife > character.maxLife){
            character.currentLife = character.maxLife;
        }
        healingValue = 0;
    }
}
