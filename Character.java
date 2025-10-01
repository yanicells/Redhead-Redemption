/**
Character is the base class for all moving entities in the game, including players, mobs, and NPCs.
It manages movement, animation, collision, attacking, and all the core stats and behaviors shared by characters.
 
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
import java.io.IOException;
import javax.imageio.ImageIO;

public class Character extends Drawable {

    protected int screenX, screenY;

    protected BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3, shootingUp, shootingDown, shootingRight, shootingLeft, idleUp1, idleUp2, idleDown1, idleDown2, idleLeft1, idleLeft2, idleRight1, idleRight2;
    protected BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    protected String direction;
    protected final float SCALE = 1.4f;
    protected int scaledSize = (int) (GameCanvas.tileSize * SCALE);

    protected int spriteCounter;
    protected int spriteNum;
    protected int idleNum;
    protected int idleCounter;

    protected Rectangle solidArea;
    protected int solidAreaX, solidAreaY;
    protected boolean collisionOn;
    protected Sound hitSound;

    protected Rectangle attackArea;

    protected GameCanvas gameCanvas;
    protected int actionCounter;
    protected boolean attacking = false;
    protected int attackCooldown = 0;

    protected String name;
    protected int speed;
    protected int type; // 0 = P; 1 = NPC; 2 = MOB;
    protected int maxLife;
    protected int currentLife;
    protected boolean noDamage = false;
    protected int eventCounter;

    protected boolean alive = true;
    protected boolean dying = false;
    protected int dyingCounter = 0;
    protected boolean findPath = false;

    // CHARACTER STATS
    protected int level;
    protected int strength;
    protected int attack;
    protected int exp;
    protected int nextLevelExp;
    protected Item meleeWeapon;

    //BULLETS
    protected Projectile projectile;
    protected int shotCounter = 0;
    protected int currentAmmo;
    protected int maxAmmo;
    protected boolean shooting = false;

    /**
     * Sets up a new Character with default stats, collision areas, and images.
     */
    public Character() {
        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;

        attackArea = new Rectangle(0, 0, 0, 0);

        spriteCounter = 0;
        spriteNum = 1;
        idleCounter = 0;
        idleNum = 1;
        collisionOn = false;
        hitSound = new Sound("assets/hit.wav");
    }

    /**
     * Creates a Character and links it to the main game canvas.
     * @param gameCanvas the main game canvas reference
     */
    public Character(GameCanvas gameCanvas) {
        this();
        this.gameCanvas = gameCanvas;
    }

    /**
     * Resets the character's solid area to its original position.
     */
    public void resetValues() {
        solidArea.x = solidAreaX;
        solidArea.y = solidAreaY;
    }

    /**
     * Draws the character on the screen with the correct sprite and overlays, including health bars and death animation.
     * @param g2d the graphics context to draw on
     * @param gameCanvas the main game canvas reference
     */
    @Override
    public void draw(Graphics2D g2d, GameCanvas gameCanvas) {
        BufferedImage image = null;

        screenX = worldX - gameCanvas.player.worldX + gameCanvas.player.screenX;
        screenY = worldY - gameCanvas.player.worldY + gameCanvas.player.screenY;

        if (true) {
            switch (direction) {
                case "up":
                    if (spriteNum == 1) {
                        image = up1;
                    } else if (spriteNum == 2) {
                        image = up2;
                    } else {
                        image = up3;
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

            // HP
            if (type == 2) {
                double scale = (double) GameCanvas.tileSize / maxLife;
                double hpValue;

                if (currentLife < 0) {
                    hpValue = 0;
                } else {
                    hpValue = scale * currentLife;
                }

                g2d.setColor(new Color(35, 35, 35));
                g2d.fillRect(screenX - 1, screenY - 16, GameCanvas.tileSize + 2, 12);
                g2d.setColor(new Color(255, 0, 30));
                g2d.fillRect(screenX, screenY - 15, (int) hpValue, 10);
            }

            if (dying) {
                deathAnimation(g2d);
            }

            g2d.drawImage(image, screenX, screenY, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            // g2d.setColor(Color.red);
            // g2d.drawRect(solidArea.x + screenX, solidArea.y + screenY, solidArea.width, solidArea.height);
        }
    }

    /**
     * Damages the main player by the given attack value and triggers the hit sound.
     * @param attack the amount of damage to deal
     */
    protected void damagePlayer(int attack) {
        if (!gameCanvas.player.noDamage) {
            hitSound.play();
            gameCanvas.player.currentLife -= attack;
            gameCanvas.player.noDamage = true;
        }
    }

    /**
     * Damages a specific player by the given attack value and resets projectile state.
     * @param attack the amount of damage to deal
     * @param targetPlayer the player to damage
     */
    protected void damagePlayer(int attack, Player targetPlayer) {
        if (targetPlayer != null && !targetPlayer.noDamage) {
            targetPlayer.currentLife -= attack;
            attackCooldown = 0;
            projectile.alive = false;
        }
    }

    /**
     * Placeholder for subclasses to define their own action logic each frame.
     */
    public void setAction() {

    }

    /**
     * Placeholder for subclasses to define how they react after taking damage.
     */
    public void damageReaction() {

    }

    /**
     * Uses pathfinding to set the direction toward a goal tile on the map.
     * @param goalCol the target column
     * @param goalRow the target row
     */
    protected void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x) / GameCanvas.tileSize;
        int startRow = (worldY + solidArea.y) / GameCanvas.tileSize;

        gameCanvas.pathFinder.setNode(startCol, startRow, goalCol, goalRow);

        if (gameCanvas.pathFinder.search()) {
            int nextX = gameCanvas.pathFinder.pathList.get(0).col * GameCanvas.tileSize;
            int nextY = gameCanvas.pathFinder.pathList.get(0).row * GameCanvas.tileSize;

            int npcLeftX = worldX + solidArea.x;
            int npcRightX = worldX + solidArea.x + solidArea.width;
            int npcTopY = worldY + solidArea.y;
            int npcBottomY = worldY + solidArea.y + solidArea.height;

            if (npcTopY > nextY && npcLeftX >= nextX && npcRightX < nextX + GameCanvas.tileSize) {
                direction = "up";
            } else if (npcTopY < nextY && npcLeftX >= nextX && npcRightX < nextX + GameCanvas.tileSize) {
                direction = "down";
            } else if (npcTopY >= nextY && npcBottomY < nextY + GameCanvas.tileSize) {
                // Check left or right
                if (npcLeftX > nextX) {
                    direction = "left";
                }
                if (npcLeftX < nextX) {
                    direction = "right";
                }
            } else if (npcTopY > nextY && npcLeftX > nextX) {
                // Check up or left
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            } else if (npcTopY > nextY && npcLeftX < nextX) {
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            } else if (npcTopY < nextY && npcLeftX > nextX) {
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            } else if (npcTopY < nextY && npcLeftX < nextX) {
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            }

            int nextCol = gameCanvas.pathFinder.pathList.get(0).col;
            int nextRow = gameCanvas.pathFinder.pathList.get(0).row;
            if (nextCol == goalCol && nextRow == goalRow) {
                findPath = false;
            }
        }
    }

    /**
     * Checks for collisions with tiles, items, NPCs, and players, and applies damage if needed.
     */
    protected void checkCollision() {
        collisionOn = false;
        gameCanvas.collisionChecker.checkTile(this);
        gameCanvas.collisionChecker.checkItem(this, false);
        gameCanvas.collisionChecker.checkNPCCollision(this, gameCanvas.mobs);
        boolean hitPlayer = gameCanvas.collisionChecker.checkPlayerCollision(this);

        if (this.type == 2 && hitPlayer) {
            if (gameCanvas.player != null && gameCanvas.player.solidArea.intersects(this.solidArea) && attackCooldown == 80) {
                damagePlayer(attack, gameCanvas.player);
            }
        }
    }

    /**
     * Updates the character's state, handles movement, animation, damage cooldowns, and attack cooldowns.
     */
    public void update() {
        setAction();
        checkCollision();

        if (!collisionOn) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
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

        if (noDamage) {
            eventCounter++;
            if (eventCounter > 40) {
                noDamage = false;
                eventCounter = 0;
            }
        }

        if (shotCounter < 30) {
            shotCounter++;
        }

        if (attackCooldown < 80) {
            attackCooldown++;
            attacking = false;
        }
    }

    /**
     * Draws the death animation by flashing the character sprite before setting them as not alive.
     * @param g2d the graphics context to draw on
     */
    public void deathAnimation(Graphics2D g2d) {
        dyingCounter++;
        int i = 5;

        if (dyingCounter <= i) {
            changeAlpha(g2d, 0f);
        }
        if (dyingCounter > i && dyingCounter <= i * 2) {
            changeAlpha(g2d, 1f);
        }
        if (dyingCounter > i * 2 && dyingCounter <= i * 3) {
            changeAlpha(g2d, 0f);
        }
        if (dyingCounter > i * 3 && dyingCounter <= i * 4) {
            changeAlpha(g2d, 1f);
        }
        if (dyingCounter > i * 4 && dyingCounter <= i * 5) {
            changeAlpha(g2d, 0f);
        }
        if (dyingCounter > i * 5 && dyingCounter <= i * 6) {
            changeAlpha(g2d, 1f);
        }
        if (dyingCounter > i * 6 && dyingCounter <= i * 7) {
            changeAlpha(g2d, 0f);
        }
        if (dyingCounter > i * 7 && dyingCounter <= i * 8) {
            changeAlpha(g2d, 1f);
        }
        if (dyingCounter > i * 8) {
            alive = false;
        }
    }

    /**
     * Changes the alpha transparency of the character's sprite for effects like dying.
     * @param g2d the graphics context
     * @param value the alpha value to set (0.0 to 1.0)
     */
    public void changeAlpha(Graphics2D g2d, float value) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, value));
    }
    
    /**
     * Scales a BufferedImage to the specified width and height.
     * @param original the original image
     * @param width the target width
     * @param height the target height
     * @return the scaled BufferedImage
     */
    private BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return scaledImage;
    }

    /**
     * Loads an image from resources, scales it to the given size, and returns the result.
     * @param imagePath the path to the image file (without extension)
     * @param width the desired width
     * @param height the desired height
     * @return the loaded and scaled BufferedImage
     */
    public BufferedImage setup(String imagePath, int width, int height) {
        BufferedImage scaledImage;
        try {
            scaledImage = ImageIO.read(getClass().getResourceAsStream("assets/" + imagePath + ".png"));
            scaledImage = scaleImage(scaledImage, width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return scaledImage;
    }
}