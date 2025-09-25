/**
PoisonMob is a type of enemy that can shoot poison projectiles and has its own movement and attack logic.
This class controls how the poison mob behaves, including how it chases players and when it decides to attack.
  
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

public class PoisonMob extends Mob{

    /**
     * Sets up a PoisonMob with its default speed and attack power.
     * @param gameCanvas the main game canvas reference
     */
    public PoisonMob(GameCanvas gameCanvas) {
        super(gameCanvas);
        speed = 1;
        attack = 3;
    }

    /**
     * Determines the poison mob's current action, such as chasing the player or wandering randomly.
     */
    @Override
    public void setAction() {
        if (gameCanvas.playerControl) {
            if (findPath) {
                Player closestPlayer = findClosestPlayer();
                if (closestPlayer != null) {
                    int goalCol = (closestPlayer.worldX + closestPlayer.solidArea.x) / GameCanvas.tileSize;
                    int goalRow = (closestPlayer.worldY + closestPlayer.solidArea.y) / GameCanvas.tileSize;

                    searchPath(goalCol, goalRow);

                    int i = new Random().nextInt(150) + 1;
                    if (i > 138 && !projectile.alive && shotCounter == 30) {
                        projectile.set(worldX, worldY, direction, true, this);
                        gameCanvas.projectiles.add(projectile);
                        shotCounter = 0;
                    }
                } else {
                    findPath = false;
                }
            } else {
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
    }

    /**
     * Updates the poison mob's state, including whether it should start or stop chasing the player.
     */
    @Override
    public void update() {
        super.update();

        if (gameCanvas.playerControl) {
            Player closestPlayer = findClosestPlayer();
            int tileDistance = Integer.MAX_VALUE;

            if (closestPlayer != null) {
                int xDistance = Math.abs(worldX - closestPlayer.worldX);
                int yDistance = Math.abs(worldY - closestPlayer.worldY);
                tileDistance = (xDistance + yDistance) / GameCanvas.tileSize;
            }

            if (!findPath && tileDistance < 5) {
                int i = new Random().nextInt(100) + 1;
                if (i > 50) {
                    findPath = true;
                }
            }

            if (findPath && tileDistance > 20) {
                findPath = false;
                setAction();
            }
        }
    }

    /**
     * Resets the mob's action counter and makes it chase the player after taking damage.
     */
    @Override
    public void damageReaction() {
        actionCounter = 0;
        findPath = true;
    }
}
