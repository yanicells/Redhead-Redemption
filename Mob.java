/**
Mob represents a basic enemy in the game, handling its movement, attack logic, and interactions with the player.
This class sets up the mob's stats, images, and AI for chasing or wandering, and manages how it reacts to damage.
  
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

import java.awt.*;
import java.util.Random;

public abstract class Mob extends Character{

    /**
     * Creates a new Mob with default stats, sets up its collision area, and loads its images.
     * @param gameCanvas the main game canvas reference
     */
    public Mob(GameCanvas gameCanvas) {
        super(gameCanvas);

        name = "Slime";
        direction = "down";
        speed = 1;
        maxLife = 6;
        currentLife = maxLife;
        attack = 1;
        exp = 3;


        int scaledX = (int)(8 * SCALE) + 16;
        int scaledY = (int)(16 * SCALE) + 8;
        int scaledWidth = (int)(32 * SCALE) - 8;
        int scaledHeight = (int)(32 * SCALE);
        solidArea = new Rectangle(scaledX, scaledY, scaledWidth, scaledHeight);

        solidAreaX = solidArea.x;
        solidAreaY = solidArea.y;

        type = 2;

        projectile = new Poison(gameCanvas);

        getMobImage();
    }

    /**
     * Loads the mob's walking images for all directions.
     */
    private void getMobImage(){
        up1 = setup("Zombie_Walking_Up_1", scaledSize, scaledSize);
        up2 = setup("Zombie_Walking_Up_2", scaledSize, scaledSize);
        down1 = setup("Zombie_Walking_Down_1", scaledSize, scaledSize);
        down2 = setup("Zombie_Walking_Down_2", scaledSize, scaledSize);
        left1 = setup("Zombie_Walking_Left_1", scaledSize, scaledSize);
        left2 = setup("Zombie_Walking_Left_2", scaledSize, scaledSize);
        right1 = setup("Zombie_Walking_Right_1", scaledSize, scaledSize);
        right2 = setup("Zombie_Walking_Right_2", scaledSize, scaledSize);
    }

    /**
     * Determines the mob's current action, such as chasing the player or wandering randomly.
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

                    int i = new Random().nextInt(200) + 1;
                    if (i > 198 && !projectile.alive && shotCounter == 30) {
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
     * Updates the mob's state each frame, including movement and attack logic.
     */
    @Override
    public void update() {
        super.update();

    }

    /**
     * Finds and returns the closest player to this mob, checking both the main and networked players.
     * @return the closest Player object, or null if none are alive
     */
    protected Player findClosestPlayer() {
        Player closestPlayer = null;
        int minDistance = Integer.MAX_VALUE;

        if (gameCanvas.player != null) {
            int xDistance = Math.abs(worldX - gameCanvas.player.worldX);
            int yDistance = Math.abs(worldY - gameCanvas.player.worldY);
            int distance = xDistance + yDistance;
            if (distance < minDistance) {
                minDistance = distance;
                if(gameCanvas.player.currentLife > 0){
                    closestPlayer = gameCanvas.player;
                }
            }
        }

        for (Player networkedPlayer : gameCanvas.players) {
            if (networkedPlayer != null) {
                int xDistance = Math.abs(worldX - networkedPlayer.worldX);
                int yDistance = Math.abs(worldY - networkedPlayer.worldY);
                int distance = xDistance + yDistance;
                if (distance < minDistance) {
                    minDistance = distance;
                    if(networkedPlayer.currentLife > 0){
                        closestPlayer = networkedPlayer;
                    }
                }
            }
        }

        return closestPlayer;
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
