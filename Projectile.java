/**
Projectile represents any bullet or thrown object in the game that can move, collide, and deal damage.
It keeps track of its origin, direction, and handles its own movement, collision, and drawing logic.

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

public class Projectile extends Character {
    protected Character character;
    protected int startWorldX, startWorldY;
    protected boolean shouldDamage;

    /**
     * Creates a new projectile and sets up its default collision area.
     * @param gameCanvas the main game canvas reference
     */
    public Projectile(GameCanvas gameCanvas) {
        super(gameCanvas);
        alive = false;
        shouldDamage = false;
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 4;
        solidArea.height = 4;

        solidAreaX = solidArea.x;
        solidAreaY = solidArea.y;
    }

    /**
     * Initializes the projectile's position, direction, and owner when it is fired.
     */
    public void set(int worldX, int worldY, String direction, boolean alive, Character player) {
        startWorldX = worldX;
        startWorldY = worldY;
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.character = player;
        this.currentLife = maxLife;
    }

    /**
     * Updates the projectile's position, checks for collisions, and handles its lifespan.
     */
    public void update() {

        if (character == gameCanvas.player || character instanceof Player) {
            int monsterIndex = gameCanvas.collisionChecker.checkNPCCollision(this, gameCanvas.mobs);
            gameCanvas.player.mobInteractIndex = monsterIndex;
            if (monsterIndex != 999) {
                shouldDamage = true;
                gameCanvas.player.damageMob(monsterIndex, attack);
                alive = false;
            } else {
                shouldDamage = false;
            }
        }

        if (character != gameCanvas.player && !(character instanceof Player)) {
            boolean hitPlayer = gameCanvas.collisionChecker.checkPlayerCollision(this);
            if (!gameCanvas.player.noDamage && hitPlayer) {
                damagePlayer(attack);
            }
        }

        if (character != gameCanvas.player && !(character instanceof Player)) {
            boolean hitPlayer = gameCanvas.collisionChecker.checkOtherPlayerCollision(this);
            if (hitPlayer) {
                damagePlayer(0);
                alive = false;
            }
        }

        switch (direction) {
            case "up":
                solidArea.width = 35;
                solidArea.height = 15;
                solidArea.x = (GameCanvas.tileSize / 2) - (solidArea.width / 2) - 17;
                solidArea.y = GameCanvas.tileSize - solidArea.height;
                worldY -= speed;
                break;
            case "down":
                solidArea.width = 35;
                solidArea.height = 15;
                solidArea.x = (GameCanvas.tileSize / 2) - (solidArea.width / 2) + 17;
                solidArea.y = 0;
                worldY += speed;
                break;
            case "left":
                solidArea.width = 15;
                solidArea.height = 35;
                solidArea.x = GameCanvas.tileSize - solidArea.width;
                solidArea.y = (GameCanvas.tileSize / 2) - (solidArea.height / 2) + 7;
                worldX -= speed;
                break;
            case "right":
                solidArea.width = 15;
                solidArea.height = 35;
                solidArea.x = 0;
                solidArea.y = (GameCanvas.tileSize / 2) - (solidArea.height / 2) + 7;
                worldX += speed;
                break;
        }

        currentLife--;
        if (currentLife <= 0) {
            alive = false;
        }

        spriteCounter++;

        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    /**
     * Draws the projectile on the screen if it is within the visible area.
     * @param g2d the graphics context to draw on
     * @param gameCanvas the main game canvas reference
     */
    @Override
    public void draw(Graphics2D g2d, GameCanvas gameCanvas) {
        BufferedImage image = null;

        int screenX = worldX - gameCanvas.player.worldX + gameCanvas.player.screenX;
        int screenY = worldY - gameCanvas.player.worldY + gameCanvas.player.screenY;

        if (worldX + gameCanvas.tileSize > gameCanvas.player.worldX - gameCanvas.player.screenX
                && worldX - gameCanvas.tileSize < gameCanvas.player.worldX + gameCanvas.player.screenX
                && worldY + gameCanvas.tileSize > gameCanvas.player.worldY - gameCanvas.player.screenY
                && worldY - gameCanvas.tileSize < gameCanvas.player.worldY + gameCanvas.player.screenY) {

            switch (direction) {
                case "up":
                    if (spriteNum == 1) {
                        image = up1;
                    } else {
                        image = up2;
                    }
                    break;
                case "down":
                    if (spriteNum == 1) {
                        image = down1;
                    } else {
                        image = down2;
                    }
                    break;
                case "left":
                    if (spriteNum == 1) {
                        image = left1;
                    } else {
                        image = left2;
                    }
                    break;
                case "right":
                    if (spriteNum == 1) {
                        image = right1;
                    } else {
                        image = right2;
                    }
                    break;
                default:
                    image = up1;
                    break;
            }
            if (alive) {
                g2d.drawImage(image, screenX, screenY, GameCanvas.tileSize, GameCanvas.tileSize, null);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                // g2d.setColor(Color.green);
                // g2d.drawRect(solidArea.x + screenX, solidArea.y + screenY, solidArea.width, solidArea.height);
            }
        }
    }

    /**
     * Checks if the given character has enough ammo to fire this projectile.
     * @param character the character whose ammo is being checked
     * @return true if the character has ammo, false otherwise
     */
    protected boolean hasAmmo(Character character) {
        boolean hasAmmo = false;
        if (character.currentAmmo > 0) {
            hasAmmo = true;
        }
        return hasAmmo;
    }

    /**
     * Reduces the character's ammo count by one when the projectile is fired.
     * @param character the character whose ammo will be reduced
     */
    protected void useAmmo(Character character) {
        character.currentAmmo -= 1;
    }
}
