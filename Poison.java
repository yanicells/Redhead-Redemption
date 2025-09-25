/**
Poison is a projectile used by poison-type enemies, representing a thrown or shot poison attack.
This class sets up the poison's properties and loads its images for animation in the game.
 
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

public class Poison extends Projectile{
    
    /**
     * Sets up a new Poison projectile with its default stats and loads its images.
     * @param gameCanvas the main game canvas reference
     */
    public Poison(GameCanvas gameCanvas) {
        super(gameCanvas);

        name = "Poison";
        speed = 4;
        maxLife = 80;
        currentLife = maxLife;
        attack = 2;
        alive = false;

        direction = "down";

        getImage();
    }

    /**
     * Loads the images used for the poison projectile's animation in all directions.
     */
    private void getImage(){
        up1 = setup("poisonup_1",GameCanvas.tileSize,GameCanvas.tileSize);
        up2 = setup("poisonup_2",GameCanvas.tileSize,GameCanvas.tileSize);
        down1 = setup("poisondown_1",GameCanvas.tileSize,GameCanvas.tileSize);
        down2 = setup("poisondown_2",GameCanvas.tileSize,GameCanvas.tileSize);
        left1 = setup("poisonleft_1",GameCanvas.tileSize,GameCanvas.tileSize);
        left2 = setup("poisonleft_2",GameCanvas.tileSize,GameCanvas.tileSize);
        right1 = setup("poisonright_1",GameCanvas.tileSize,GameCanvas.tileSize);
        right2 = setup("poisonright_1",GameCanvas.tileSize,GameCanvas.tileSize);
    }
}
