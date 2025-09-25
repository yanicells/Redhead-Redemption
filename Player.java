/**
The Player class represents the main character controlled by the user, handling movement, combat, inventory, and interactions with the world.
It manages the player's state, input, animation, and all the logic for picking up items, attacking, and responding to game events.
  
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

public class Player extends Character {
    protected KeyHandler keyHandler;
    protected int screenX, screenY;
    protected boolean shouldDraw = true;
    protected int keysCollected;
    protected int fuelCollected;
    protected int stamina;
    protected int drawCounter;
    protected int interactedItemIndex = 999;
    protected boolean resetItemIndex = false;
    protected boolean noDamage = false;
    protected int eventCounter = 0;
    protected Item[] inventory;
    protected int mobInteractIndex = 999;
    private int attackCooldown = 0;
    protected final float SCALE = 1.5f;
    protected final int scaledSize = (int)(GameCanvas.tileSize * SCALE);
    protected Sound soundWalk, soundRun, soundAttack, soundShoot, soundIdle, soundPickup, soundHit;

    /**
     * Creates a new Player and sets up all default values, images, and sounds.
     * @param gameCanvas the main game canvas reference
     * @param keyHandler the handler for keyboard input
     */
    public Player(GameCanvas gameCanvas, KeyHandler keyHandler) {
        super(gameCanvas);
        this.keyHandler = keyHandler;
        keysCollected = 0;
        drawCounter = 0;
        fuelCollected = 0;

        int scaledX = (int)(8 * SCALE) + 20;
        int scaledY = (int)(16 * SCALE) + 16;
        int scaledWidth = (int)(32 * SCALE) - 15;
        int scaledHeight = (int)(32 * SCALE) - 8;

        solidArea = new Rectangle(scaledX, scaledY, scaledWidth, scaledHeight);
        solidAreaX = solidArea.x;
        solidAreaY = solidArea.y;

        attackArea.width = (int)(36 * SCALE);
        attackArea.height = (int)(36 * SCALE);

        screenX = gameCanvas.screenWidth / 2 - (scaledSize / 2);
        screenY = gameCanvas.screenHeight / 2 - (scaledSize / 2);

        setDefaultValues();
        getPlayerImage();
        getAttackImage();

        soundWalk = new Sound("walk.wav");
        soundRun = new Sound("run.wav");
        soundAttack = new Sound("attack.wav");
        soundShoot = new Sound("shoot.wav");
        soundIdle = new Sound("idle.wav");
        soundPickup = new Sound("pickup.wav");
        soundHit = new Sound("hit.wav");

    }

    /**
     * Sets the player's starting position, stats, and inventory to their default values.
     */
    public void setDefaultValues() {
        worldX = GameCanvas.tileSize * 33;
        worldY = GameCanvas.tileSize * 68;

        speed = 4;
        stamina = 5;
        direction = "down";
        maxLife = 20;
        currentLife = maxLife;
        level = 1;
        strength = 1;
        exp = 0;
        nextLevelExp = 5;
        meleeWeapon = new Melee(gameCanvas);

        setDefaultInventory();

        attack = getAttackValue();

        projectile = new Bullet(gameCanvas);
        maxAmmo = 20;
        currentAmmo = maxAmmo;
    }

    /**
     * Calculates the player's attack value based on their strength and equipped weapon.
     * @return the computed attack value
     */
    private int getAttackValue() {
        return strength * inventory[1].attackDamage;
    }

    /**
     * Initializes the player's inventory with default items.
     */
    private void setDefaultInventory() {
        inventory = new Item[4];
        inventory[1] = meleeWeapon;
    }

    /**
     * Updates the player's state each frame, including movement, attacking, shooting, and handling input.
     */
    public void update() {
        boolean moving = true;
        if (keyHandler != null) {
            moving = keyHandler.upPressed || keyHandler.downPressed || keyHandler.rightPressed || keyHandler.leftPressed
                    || keyHandler.enterPressed || keyHandler.useHealPressed || keyHandler.healPressed || keyHandler.swapPressed;
        }

        if (attacking && keyHandler != null) {
            if (attackCooldown == 30) {
                attack();
                soundAttack.loop();
            }
        } else if (keyHandler != null) {

            if (moving) {
                shooting = false;
                if(!(keyHandler.enterPressed || keyHandler.useHealPressed || keyHandler.healPressed || keyHandler.swapPressed)){
                    if (spriteNum == 0) {
                        spriteNum = 1;
                    }
                }
                if (keyHandler.upPressed) {
                    direction = "up";
                } else if (keyHandler.downPressed) {
                    direction = "down";
                } else if (keyHandler.leftPressed) {
                    direction = "left";
                } else if (keyHandler.rightPressed) {
                    direction = "right";
                }

                collisionOn = false;
                gameCanvas.collisionChecker.checkTile(this);
                int itemIndex = gameCanvas.collisionChecker.checkItem(this, true);
                pickUpItem(itemIndex);

                triggerAction();

                int mobIndex = gameCanvas.collisionChecker.checkNPCCollision(this, gameCanvas.mobs);
                mobEvent(mobIndex);

                gameCanvas.eventHandler.checkEvent(gameCanvas.shouldChangeMap);

                if (!collisionOn && (keyHandler.upPressed || keyHandler.downPressed || keyHandler.rightPressed || keyHandler.leftPressed)) {
                    switch (direction) {
                        case "up":
                            if (gameCanvas.keyHandler.shiftPressed && stamina > 0) {
                                soundIdle.stop();
                                soundWalk.stop();
                                soundRun.loop();
                                worldY -= 1 + speed;
                                if (drawCounter > 30) {
                                    stamina -= 1;
                                    drawCounter = 0;
                                }
                            } else {
                                soundIdle.stop();
                                soundRun.stop();
                                soundWalk.loop();
                                worldY -= speed;
                            }
                            break;
                        case "down":
                            if (gameCanvas.keyHandler.shiftPressed && stamina > 0) {
                                soundIdle.stop();
                                soundWalk.stop();
                                soundRun.loop();
                                worldY += 1 + speed;
                                if (drawCounter > 30) {
                                    stamina -= 1;
                                    drawCounter = 0;

                                }
                            } else {
                                soundIdle.stop();
                                soundRun.stop();
                                soundWalk.loop();
                                worldY += speed;
                            }
                            break;
                        case "left":
                            if (gameCanvas.keyHandler.shiftPressed && stamina > 0) {
                                soundIdle.stop();
                                soundWalk.stop();
                                soundRun.loop();
                                worldX -= 1 + speed;
                                if (drawCounter > 30) {
                                    stamina -= 1;
                                    drawCounter = 0;

                                }
                            } else {
                                soundIdle.stop();
                                soundRun.stop();
                                soundWalk.loop();
                                worldX -= speed;
                            }
                            break;
                        case "right":
                            if (gameCanvas.keyHandler.shiftPressed && stamina > 0) {
                                soundIdle.stop();
                                soundWalk.stop();
                                soundRun.loop();
                                worldX += 1 + speed;
                                if (drawCounter > 30) {
                                    stamina -= 1;
                                    drawCounter = 0;
                                }
                            } else {
                                soundIdle.stop();
                                soundRun.stop();
                                soundWalk.loop();
                                worldX += speed;
                            }
                            break;
                    }
                }

                if (keyHandler.useHealPressed && inventory[2] != null) {
                    inventory[2].heal(this);
                    inventory[2] = null;
                }

                gameCanvas.keyHandler.enterPressed = false;
                gameCanvas.keyHandler.healPressed = false;

                if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.rightPressed || keyHandler.leftPressed) {
                    spriteCounter++;
                    if (spriteCounter > 12) {
                        switch (spriteNum) {
                            case 1:
                                spriteNum = 2;
                                break;
                            case 2:
                                spriteNum = 3;
                                break;
                            case 3:
                                spriteNum = 1;
                                break;
                        }
                        spriteCounter = 0;
                    }
                }
                idleCounter = 0;
            }
        }

        if (!moving) {
            soundRun.stop();
            soundWalk.stop();
            soundIdle.loop();
            spriteNum = 0;
            idleCounter++;
            if (idleCounter >= 20) {
                idleNum = (idleNum == 1) ? 2 : 1;
                idleCounter = 0;
                if (stamina < 6 && drawCounter > 30) {
                    stamina += 1;
                    drawCounter = 0;
                }
            }

            if (keyHandler != null) {
                if (gameCanvas.keyHandler.triggerPressed && !projectile.alive && shotCounter == 30 && projectile.hasAmmo(this)) {
                    int tempWorldX = worldX;
                    if(direction.equals("up")){
                        tempWorldX += GameCanvas.tileSize / 2;
                    }
                    projectile.set(tempWorldX, worldY, direction, true, this);
                    shooting = true;
                    projectile.useAmmo(this);
                    gameCanvas.projectiles.add(projectile);
                    shotCounter = 0;
                    soundShoot.play();
                }

                if (projectile == null) {
                    shooting = false;
                } else if (!projectile.alive) {
                    shooting = false;
                }
            }

            if (noDamage) {
                eventCounter++;
                if (eventCounter > 60) {
                    noDamage = false;
                    eventCounter = 0;
                }
            }

            if (shotCounter < 30) {
                shotCounter++;
            }

            if (attackCooldown < 30) {
                attackCooldown++;
                attacking = false;
                soundAttack.stop();
            }
        }


        if (keyHandler == null) {
            collisionOn = false;
            gameCanvas.collisionChecker.checkTile(this);

            int mobIndex = gameCanvas.collisionChecker.checkNPCCollision(this, gameCanvas.mobs);
            mobEvent(mobIndex);

            gameCanvas.eventHandler.checkEvent(gameCanvas.shouldChangeMap);
        }
        drawCounter++;
    }

    /**
     * Handles the player's attack animation and applies damage to mobs in range.
     */
    private void attack() {
        if (keyHandler != null) {
            spriteCounter++;

            if (spriteCounter <= 5) {
                spriteNum = 1;
            }
        }

        if (spriteCounter > 5 && spriteCounter <= 25) {
            if (keyHandler != null) {
                spriteNum = 2;
            }

            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            switch (direction) {
                case "up":
                    worldY -= attackArea.height;
                    break;
                case "down":
                    worldY += attackArea.height;
                    break;
                case "left":
                    worldX -= attackArea.width;
                case "right":
                    worldX += attackArea.width;
            }

            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            int monsterIndex = gameCanvas.collisionChecker.checkNPCCollision(this, gameCanvas.mobs);
            mobInteractIndex = monsterIndex;
            damageMob(monsterIndex, attack);

            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }

        if (keyHandler != null) {
            if (spriteCounter > 25) {
                spriteNum = 1;
                spriteCounter = 0;
                attacking = false;
                attackCooldown = 0;
            }
        }
    }

    /**
     * Handles picking up items from the ground and updating the player's inventory or state.
     * @param index the index of the item to pick up
     */
    public void pickUpItem(int index){
        if(index != 999){
            resetItemIndex = false;
            interactedItemIndex = index;
            String itemName = gameCanvas.items.get(gameCanvas.currentMap).get(interactedItemIndex).name;

            if(itemName.equals("Key")){
                keysCollected++;
                soundPickup.play();
                gameCanvas.items.get(gameCanvas.currentMap).set(interactedItemIndex, null);
                gameCanvas.overlay.dialogue = new String [] {"YOU GOT A KEY!"};
                gameCanvas.overlay.dialogueIndex = 0;
                gameCanvas.gameState = gameCanvas.dialogueState;
            }else if (itemName.equals("Fuel")){
                fuelCollected++;
                soundPickup.play();
                gameCanvas.items.get(gameCanvas.currentMap).set(interactedItemIndex, null);
                gameCanvas.overlay.dialogue = new String [] {"YOU GOT FUEL!"};
                gameCanvas.overlay.dialogueIndex = 0;
                gameCanvas.gameState = gameCanvas.dialogueState;
            }else if (itemName.equals("Door")){
                if(keysCollected > 2){
                    soundPickup.play();
                    gameCanvas.items.get(gameCanvas.currentMap).set(interactedItemIndex, null);
                    keysCollected-=3;
                    gameCanvas.overlay.dialogue = new String [] {"YOU OPENED THE GATE!"};
                    gameCanvas.overlay.dialogueIndex = 0;
                    gameCanvas.gameState = gameCanvas.dialogueState;
                }else {
                    gameCanvas.overlay.dialogue = new String [] {"YOU NEED 3 KEYS TO OPEN THE GATE"};
                    gameCanvas.overlay.dialogueIndex = 0;
                    gameCanvas.gameState = gameCanvas.dialogueState;
                    resetItemIndex = true;
                }
            }else if(itemName.equals("Medkit")){
                if(keyHandler.swapPressed){
                    if(gameCanvas.player.inventory[gameCanvas.items.get(gameCanvas.currentMap).get(interactedItemIndex).type] != null){
                        interactedItemIndex = 999;
                        return;
                    }
                    Item previousItem = null;
                    if(gameCanvas.player.inventory[gameCanvas.items.get(gameCanvas.currentMap).get(interactedItemIndex).type] != null){
                        previousItem = gameCanvas.player.inventory[gameCanvas.items.get(gameCanvas.currentMap).get(interactedItemIndex).type];
                    }
                    int itemX = gameCanvas.items.get(gameCanvas.currentMap).get(interactedItemIndex).worldX;
                    int itemY = gameCanvas.items.get(gameCanvas.currentMap).get(interactedItemIndex).worldY;
                    gameCanvas.player.inventory[gameCanvas.items.get(gameCanvas.currentMap).get(interactedItemIndex).type] = gameCanvas.items.get(gameCanvas.currentMap).get(interactedItemIndex);
                    gameCanvas.items.get(gameCanvas.currentMap).set(interactedItemIndex, previousItem);
                    if(previousItem != null){
                        gameCanvas.items.get(gameCanvas.currentMap).get(interactedItemIndex).worldX = itemX;
                        gameCanvas.items.get(gameCanvas.currentMap).get(interactedItemIndex).worldY = itemY;
                    }
                    soundPickup.play();
                    gameCanvas.overlay.showMessage("Got Medkit");
                    gameCanvas.player.keyHandler.swapPressed = false;
                } else {
                    interactedItemIndex = 999;
                }
            }
        }
    }

   /**
     * Draws the player character on the screen with the correct sprite and animation.
     * @param g2d the graphics to draw on
     * @param gameCanvas the main game canvas reference
     */
    @Override
    public void draw(Graphics2D g2d, GameCanvas gameCanvas) {
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        boolean isRemote = (keyHandler == null);

        switch (direction) {
            case "up":
                if (!attacking) {
                    if (spriteNum == 0) {
                        if (idleNum == 1) {
                            image = idleUp1;
                        } else {
                            image = idleUp2;
                        }
                    } else if (spriteNum == 1) {
                        image = up1;
                    } else if (spriteNum == 2) {
                        image = up2;
                    } else {
                        image = up3;
                    }
                }
                if (attacking && (isRemote || attackCooldown == 30)) {
                    tempScreenY -= GameCanvas.tileSize;
                    if (spriteNum == 1) {
                        image = attackUp1;
                    } else {
                        image = attackUp2;
                    }
                }
                if (shooting) {
                    image = shootingUp;
                }
                break;
            case "down":
                if (!attacking) {
                    if (spriteNum == 0) {
                        if (idleNum == 1) {
                            image = idleDown1;
                        } else {
                            image = idleDown2;
                        }
                    } else if (spriteNum == 1) {
                        image = down1;
                    } else if (spriteNum == 2) {
                        image = down2;
                    } else {
                        image = down3;
                    }
                }
                if (attacking && (isRemote || attackCooldown == 30)) {
                    if (spriteNum == 1) {
                        image = attackDown1;
                    } else {
                        image = attackDown2;
                    }
                }
                if (shooting) {
                    image = shootingDown;
                }
                break;
            case "left":
                if (!attacking) {
                    if (spriteNum == 0) {
                        if (idleNum == 1) {
                            image = idleLeft1;
                        } else {
                            image = idleLeft2;
                        }
                    } else if (spriteNum == 1) {
                        image = left1;
                    } else if (spriteNum == 2) {
                        image = left2;
                    } else {
                        image = left3;
                    }
                }
                if (attacking && (isRemote || attackCooldown == 30)) {
                    tempScreenX -= GameCanvas.tileSize;
                    if (spriteNum == 1) {
                        image = attackLeft1;
                    } else {
                        image = attackLeft2;
                    }
                }
                if (shooting) {
                    image = shootingLeft;
                }
                break;
            case "right":
                if (!attacking) {
                    if (spriteNum == 0) {
                        if (idleNum == 1) {
                            image = idleRight1;
                        } else {
                            image = idleRight2;
                        }
                    } else if (spriteNum == 1) {
                        image = right1;
                    } else if (spriteNum == 2) {
                        image = right2;
                    } else {
                        image = right3;
                    }
                }
                if (attacking && (isRemote || attackCooldown == 30)) {
                    if (spriteNum == 1) {
                        image = attackRight1;
                    } else {
                        image = attackRight2;
                    }
                }
                if (shooting) {
                    image = shootingRight;
                }
                break;
            default:
                image = up1;
                break;
        }

        if (shouldDraw) {
            g2d.drawImage(image, tempScreenX, tempScreenY, null);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            // g2d.setColor(Color.red);
            // g2d.drawRect(solidArea.x + screenX, solidArea.y + screenY, solidArea.width, solidArea.height);
        }
    }

    /**
     * Triggers an action such as attacking when the appropriate key is pressed.
     */
    private void triggerAction() {
        if (gameCanvas.keyHandler.enterPressed) {
            if (attackCooldown == 30) {
                attacking = true;
            }
        }
    }

    /**
     * Handles what happens when the player collides with a mob, including taking damage and playing a sound.
     * @param index the index of the mob collided with
     */
    private void mobEvent(int index) {
        if (index != 999) {
            if (!noDamage && !gameCanvas.mobs.get(gameCanvas.currentMap).get(index).dying) {
                currentLife -= gameCanvas.mobs.get(gameCanvas.currentMap).get(index).attack;
                noDamage = true;
                soundHit.play();
            }
        }
    }

    /**
     * Applies damage to a mob and handles its death, experience gain, and level up checks.
     * @param index the index of the mob to damage
     * @param attack the amount of damage to deal
     */
    protected void damageMob(int index, int attack){
        if(gameCanvas.playerControl){
            if(index != 999 && gameCanvas.mobs.get(gameCanvas.currentMap) != null && gameCanvas.mobs.get(gameCanvas.currentMap).size() > index && gameCanvas.mobs.get(gameCanvas.currentMap).get(index) != null){
                if(!gameCanvas.mobs.get(gameCanvas.currentMap).get(index).noDamage){

                    gameCanvas.mobs.get(gameCanvas.currentMap).get(index).currentLife -= attack;
                    gameCanvas.mobs.get(gameCanvas.currentMap).get(index).noDamage = true;
                    gameCanvas.mobs.get(gameCanvas.currentMap).get(index).damageReaction();

                    if(gameCanvas.mobs.get(gameCanvas.currentMap).get(index).currentLife <= 0){
                        gameCanvas.mobs.get(gameCanvas.currentMap).get(index).dying = true;
                        if(gameCanvas.playerControl){
                            exp += gameCanvas.mobs.get(gameCanvas.currentMap).get(index).exp;
                            checkLevel();
                        }
                    }
                    mobInteractIndex = 999;
                }
            }
        }
    }

    /**
     * Checks if the player has enough experience to level up and increases stats if so.
     */
    private void checkLevel() {
        if (exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp * 2;
            maxLife += 2;
            maxAmmo += 15;
        }
    }

    /**
     * Loads the player's movement and idle images for all directions.
     */
    private void getPlayerImage() {
        up1 = setup("Sprite_Run_Up_1", scaledSize, scaledSize);
        up2 = setup("Sprite_Run_Up_2", scaledSize, scaledSize);
        up3 = setup("Sprite_Run_Up_3", scaledSize, scaledSize);

        down1 = setup("Sprite_Run_Down_1", scaledSize, scaledSize);
        down2 = setup("Sprite_Run_Down_2", scaledSize, scaledSize);
        down3 = setup("Sprite_Run_Down_3", scaledSize, scaledSize);

        left1 = setup("Sprite_Run_Left_1", scaledSize, scaledSize);
        left2 = setup("Sprite_Run_Left_2", scaledSize, scaledSize);
        left3 = setup("Sprite_Run_Left_3", scaledSize, scaledSize);

        right1 = setup("Sprite_Run_Right_1", scaledSize, scaledSize);
        right2 = setup("Sprite_Run_Right_2", scaledSize, scaledSize);
        right3 = setup("Sprite_Run_Right_3", scaledSize, scaledSize);

        shootingUp = setup("Sprite_Shooting_Up", scaledSize, scaledSize);
        shootingDown = setup("Sprite_Shooting_Down", scaledSize, scaledSize);
        shootingLeft = setup("Sprite_Shooting_Left", scaledSize, scaledSize);
        shootingRight = setup("Sprite_Shooting_Right", scaledSize, scaledSize);

        idleUp1 = setup("Sprite_Idle_Up_1", scaledSize, scaledSize);
        idleUp2 = setup("Sprite_Idle_Up_2", scaledSize, scaledSize);
        idleDown1 = setup("Sprite_Idle_Down_1", scaledSize, scaledSize);
        idleDown2 = setup("Sprite_Idle_Down_2", scaledSize, scaledSize);
        idleLeft1 = setup("Sprite_Idle_Left_1", scaledSize, scaledSize);
        idleLeft2 = setup("Sprite_Idle_Left_2", scaledSize, scaledSize);
        idleRight1 = setup("Sprite_Idle_Right_1", scaledSize, scaledSize);
        idleRight2 = setup("Sprite_Idle_Right_2", scaledSize, scaledSize);
    }

    /**
     * Loads the player's attack images based on the equipped weapon.
     */
    private void getAttackImage() {
        if (inventory[1].name.equals("Sword")) {
            attackUp1 = setup("Sprite_Melee_Up_1", scaledSize, scaledSize * 2);
            attackUp2 = setup("Sprite_Melee_Up_2", scaledSize, scaledSize * 2);
            attackDown1 = setup("Sprite_Melee_Down_1", scaledSize, scaledSize * 2);
            attackDown2 = setup("Sprite_Melee_Down_2", scaledSize, scaledSize * 2);

            attackLeft1 = setup("Sprite_Melee_Left_1", scaledSize * 2, scaledSize);
            attackLeft2 = setup("Sprite_Melee_Left_2", scaledSize * 2, scaledSize);
            attackRight1 = setup("Sprite_Melee_Right_1", scaledSize * 2, scaledSize);
            attackRight2 = setup("Sprite_Melee_Right_2", scaledSize * 2, scaledSize);
        } else if (inventory[1].name.equals("Axe")) {
            attackUp1 = setup("boy_axe_up_1", scaledSize, scaledSize * 2);
            attackUp2 = setup("boy_axe_up_2", scaledSize, scaledSize * 2);
            attackDown1 = setup("boy_axe_down_1", scaledSize, scaledSize * 2);
            attackDown2 = setup("boy_axe_down_2", scaledSize, scaledSize * 2);
            attackLeft1 = setup("boy_axe_left_1", scaledSize * 2, scaledSize);
            attackLeft2 = setup("boy_axe_left_2", scaledSize * 2, scaledSize);
            attackRight1 = setup("boy_axe_right_1", scaledSize * 2, scaledSize);
            attackRight2 = setup("boy_axe_right_2", scaledSize * 2, scaledSize);
        }
    }
}
