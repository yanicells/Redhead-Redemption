/**
Melee represents a melee weapon item, such as a sword, that the player can use for close-range attacks.
This class loads the sword's image and sets its attack damage and properties.
 
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

public class Melee extends Item{
    /**
     * Loads the sword image (placeholder) and sets up its attack damage and type.
     * @param gameCanvas the main game canvas reference
     */
    public Melee(GameCanvas gameCanvas) {
        super(gameCanvas);
        type = 1;

        name = "Sword";

        image = null;

        attackDamage = 1;
    }
}
