/**
The DormantMob class represents an enemy that remains inactive until damaged, at which point it begins to pursue players.
It handles pathfinding, attacking, and state changes based on player proximity and its own health.

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

public class DormantMob extends Mob {
    /**
     * Constructs a DormantMob and sets its initial state to inactive.
     */
    public DormantMob(GameCanvas gameCanvas) {
        super(gameCanvas);
        findPath = false;
    }

    /**
     * Determines the DormantMob's next action, including pathfinding and attacking if conditions are met.
     */
    @Override
    public void setAction() {
        if (gameCanvas.playerControl) {
            if (findPath) {
                // Find the closest player
                Player closestPlayer = findClosestPlayer();
                if (closestPlayer != null) {
                    int goalCol = (closestPlayer.worldX + closestPlayer.solidArea.x) / GameCanvas.tileSize;
                    int goalRow = (closestPlayer.worldY + closestPlayer.solidArea.y) / GameCanvas.tileSize;

                    searchPath(goalCol, goalRow);

                    int i = new Random().nextInt(200) + 1;
                    if (i > 196 && !projectile.alive && shotCounter == 30) {
                        projectile.set(worldX, worldY, direction, true, this);
                        gameCanvas.projectiles.add(projectile);
                        shotCounter = 0;
                    }
                } else {
                    findPath = false; // No players found, stop pathfinding
                }
            }
        }
    }

    /**
     * Updates the DormantMob's state each frame, activating it if damaged and managing its pursuit logic.
     */
    @Override
    public void update() {
        if (currentLife != maxLife) {
            findPath = true;
            super.update();

            if (gameCanvas.playerControl) {
                // Find the closest player
                Player closestPlayer = findClosestPlayer();
                int tileDistance = Integer.MAX_VALUE;

                if (closestPlayer != null) {
                    int xDistance = Math.abs(worldX - closestPlayer.worldX);
                    int yDistance = Math.abs(worldY - closestPlayer.worldY);
                    tileDistance = (xDistance + yDistance) / GameCanvas.tileSize;
                }


                if (findPath && tileDistance > 15) {
                    findPath = false;
                    setAction();
                }
            }
        }
    }

    /**
     * Triggers the DormantMob to start chasing players immediately after taking damage.
     */
    @Override
    public void damageReaction() {
        actionCounter = 0;
        findPath = true;
    }
}
