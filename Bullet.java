/**
Bullet is a projectile fired by the player or mobs, representing a fast-moving attack in the game.
This class sets up the bullet's properties and loads its images for each direction.
 
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

public class Bullet extends Projectile {

    /**
     * Sets up a new Bullet with its default stats and loads its images.
     * @param gameCanvas the main game canvas reference
     */
    public Bullet(GameCanvas gameCanvas) {
        super(gameCanvas);

        name = "Bullet";
        speed = 7;
        maxLife = 40;
        direction = "down";
        currentLife = maxLife;
        attack = 2;
        alive = false;


        getImage();
    }

    /**
     * Loads the images for the bullet in all four directions.
     */
    private void getImage() {
        up1 = setup("bulletUp", GameCanvas.tileSize, GameCanvas.tileSize);
        up2 = up1;
        down1 = setup("bulletDown", GameCanvas.tileSize, GameCanvas.tileSize);
        down2 = down1;
        left1 = setup("bulletLeft", GameCanvas.tileSize, GameCanvas.tileSize);
        left2 = left1;
        right1 = setup("bulletRight", GameCanvas.tileSize, GameCanvas.tileSize);
        right2 = right1;
    }
}
