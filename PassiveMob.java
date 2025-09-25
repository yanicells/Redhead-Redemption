/**
PassiveMob represents a non-aggressive creature in the game that simply wanders around the map.
This class handles the random movement and basic behavior for passive mobs, which do not actively attack the player.
 

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

import java.util.Random;

public class PassiveMob extends Mob {

    /**
     * Creates a PassiveMob with default speed and attack values.
     * @param gameCanvas the main game canvas reference
     */
    public PassiveMob(GameCanvas gameCanvas) {
        super(gameCanvas);
        speed = 1;
        attack = 1;
    }

    /**
     * Randomly changes the mob's direction every few seconds to simulate wandering.
     */
    @Override
    public void setAction() {
        if (gameCanvas.playerControl) {
            actionCounter++;

            if (actionCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1;

                if (i <= 25) {
                    direction = "up";
                } else if (i > 25 && i <= 50) {
                    direction = "down";
                } else if (i > 50 && i <= 75) {
                    direction = "left";
                } else if (i > 75 && i <= 100) {
                    direction = "right";
                }
                actionCounter = 0;
            }
        }
    }

    /**
     * Updates the passive mob's state and triggers its wandering logic.
     */
    @Override
    public void update() {
        super.update();

        if (gameCanvas.playerControl) {
            setAction();
        }
    }


    /**
     * Resets the mob's action counter when it takes damage.
     */
    @Override
    public void damageReaction() {
        actionCounter = 0;
    }
}
