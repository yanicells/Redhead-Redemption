/**
Item is the base class for all items in the game, including weapons, medkits, and collectibles.
It manages the item's image, collision area, and provides basic methods for drawing and interaction.
 
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
import java.awt.image.BufferedImage;

public class Item extends Drawable{
    protected BufferedImage image, image2, image3, image4;
    protected String name;
    protected boolean collision;
    protected Rectangle solidArea;
    protected int solidAreaX, solidAreaY;
    protected GameCanvas gameCanvas;
    protected int type = 999;

    protected int attackDamage;

    /**
     * Creates a generic item with default collision and solid area settings.
     */
    public Item() {
        collision = false;
        solidArea = new Rectangle(0,0,48,48);
        solidAreaX = solidArea.x;
        solidAreaY = solidArea.y;
    }

   /**
     * Creates an item and links it to the main game canvas.
     * @param gameCanvas the main game canvas reference
     */
    public Item(GameCanvas gameCanvas) {
        this();
        this.gameCanvas = gameCanvas;
    }

    /**
     * Draws the item on the screen if it is within the visible area.
     * @param g2d the graphics to draw on
     * @param gameCanvas the main game canvas reference
     */
    public void draw(Graphics2D g2d, GameCanvas gameCanvas) {
        int screenX = worldX - gameCanvas.player.worldX + gameCanvas.player.screenX;
        int screenY = worldY - gameCanvas.player.worldY + gameCanvas.player.screenY;

        if(worldX + gameCanvas.tileSize > gameCanvas.player.worldX - gameCanvas.player.screenX
                && worldX - gameCanvas.tileSize < gameCanvas.player.worldX + gameCanvas.player.screenX
                && worldY + gameCanvas.tileSize > gameCanvas.player.worldY - gameCanvas.player.screenY
                && worldY - gameCanvas.tileSize < gameCanvas.player.worldY + gameCanvas.player.screenY){
            g2d.drawImage(image, screenX, screenY, gameCanvas.tileSize, gameCanvas.tileSize, null);
        }

    }

    /**
     * Resets the item's solid area to its original position.
     */
    public void resetValues(){
        solidArea.x = solidAreaX;
        solidArea.y = solidAreaY;
    }

    /**
     * Heals the given character; meant to be overridden by subclasses that provide healing.
     * @param character the character to heal
     */
    public void heal(Character character){};
}
