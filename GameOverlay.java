/**
GameOverlay handles all the on-screen UI elements, including player status bars, inventory icons, messages, and overlays for game events.
It draws the HUD, manages dialogue windows, and displays screens for game over, death, and other important states.
 
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
import java.io.InputStream;
import javax.imageio.ImageIO;

public class GameOverlay {
    protected GameCanvas gameCanvas;
    private Graphics2D g2d;
    private Font font, largeFont, pixel;
    private BufferedImage keyImage, fuelImage, medkitImage, ammoImage, defaultImage, smgImage, shotgunImage, rifleImage, profPic, profPicDead, blood1, blood2, blood3;
    private BufferedImage startScreen, diedScreen, lostScreen, gameOverScreen;
    protected boolean messageOn;
    protected String message;
    private int messageCounter;
    protected boolean gameOver;
    protected String dialogue[];
    protected int dialogueIndex;
    public String currentDialogue = "";

    private final int SCREEN_WIDTH = 1024;
    private final int SCREEN_HEIGHT = 768;

    private final int ITEM_SIZE = 40;
    private final int PROFILE_SIZE = 35;
    private final int BAR_HEIGHT = 13;
    private final int BAR_WIDTH = 100;
    private final int ICON_TEXT_SPACING = 10;
    private final int ITEM_SPACING = 120;
    private final int TOP_MARGIN = 14;
    private final int SIDE_MARGIN = 14;
    private final int PLAYER_SPACING = 192;

    /**
     * Sets up the overlay, loads all UI images, fonts, and initializes status variables.
     * @param gameCanvas the main game canvas reference
     */
    public GameOverlay(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;
        font = new Font("Arial", Font.ITALIC, 40);
        largeFont = new Font("Arial", Font.BOLD, 80);
        InputStream pixelFont = getClass().getResourceAsStream("Pixel.ttf");
        try {
            pixel = Font.createFont(Font.TRUETYPE_FONT, pixelFont);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            startScreen = ImageIO.read(getClass().getResourceAsStream("startscreen.png"));
            diedScreen = ImageIO.read(getClass().getResourceAsStream("diedscreen.png"));
            lostScreen = ImageIO.read(getClass().getResourceAsStream("lostscreen.png"));
            gameOverScreen = ImageIO.read(getClass().getResourceAsStream("gameoverscreen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        keyImage = new Key().image;
        fuelImage = new Fuel().image;
        medkitImage = new Medkit(gameCanvas).image;
        ammoImage = new Ammo().image;
        defaultImage = new Gun().image;
        smgImage = new Gun().image2;
        rifleImage = new Gun().image3;
        shotgunImage = new Gun().image4;

        messageOn = false;
        message = "";
        gameOver = false;
        dialogueIndex = 0;

        Profile profile = new Profile();
        profPic = profile.image;
        profPicDead = profile.image2;
        Blood blood = new Blood();
        blood1 = blood.lightDamage;
        blood2 = blood.mediumDamage;
        blood3 = blood.hardDamage;
    }

    /**
     * Draws the appropriate overlay elements depending on the current game state.
     * @param g2d the graphics to draw on
     */
    public void draw(Graphics2D g2d) {
        this.g2d = g2d;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        if(gameOver){
            gameOverScreen();
            return;
        }

        if (gameCanvas.gameState == gameCanvas.playState) {
            if (gameOver) {
                gameOverScreen();
                drawBloodOverlay();
                drawTopRowElements();
                drawBottomRowPlayers();
            } else if (gameCanvas.teamLost) {
                lostScreen();
            } else if (gameCanvas.player.currentLife <= 0) {
                deathScreen();
            } else {
                drawBloodOverlay();
                drawTopRowElements();
                drawBottomRowPlayers();
            }
        } else if (gameCanvas.gameState == gameCanvas.characterStatus) {
            showCharacterStatus();
            drawBloodOverlay();
            drawTopRowElements();
            drawBottomRowPlayers();
        } else if (gameCanvas.gameState == gameCanvas.dialogueState) {
            drawBloodOverlay();
            drawTopRowElements();
            drawBottomRowPlayers();
            drawDialogue(g2d);
        }
    }

    /**
     * Draws a blood overlay on the screen based on the player's current health.
     */
    private void drawBloodOverlay() {
        if (gameCanvas.player.currentLife == 8) {
            g2d.drawImage(blood1, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        } else if (gameCanvas.player.currentLife < 8 && gameCanvas.player.currentLife >= 4) {
            g2d.drawImage(blood2, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        } else if (gameCanvas.player.currentLife < 4 && gameCanvas.player.currentLife >= 1) {
            g2d.drawImage(blood3, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        }
    }

    /**
     * Draws the top row of the HUD, including the main player's profile and inventory items.
     */
    private void drawTopRowElements() {
        int x = SIDE_MARGIN;
        int y = TOP_MARGIN;

        drawMainPlayerProfile(x, y);

        x += BAR_WIDTH + PROFILE_SIZE + ITEM_SPACING; // Move to after player health/stamina
        drawInventoryItems(x, y);
    }

    /**
     * Draws the main player's profile icon, health bar, and stamina bar.
     * @param x the x-coordinate for drawing
     * @param y the y-coordinate for drawing
     */
    private void drawMainPlayerProfile(int x, int y) {
        g2d.setColor(new Color(35, 35, 35));
        g2d.fillRect(x, y, PROFILE_SIZE + 10, PROFILE_SIZE + 10);

        if (gameCanvas.player.currentLife <= 0) {
            g2d.drawImage(profPicDead, x + 5, y + 2, PROFILE_SIZE, PROFILE_SIZE, null);
        } else {
            g2d.drawImage(profPic, x + 5, y + 2, PROFILE_SIZE, PROFILE_SIZE, null);
        }

        int barX = x + PROFILE_SIZE + 20;
        int healthBarY = y + 5;
        drawHealthBar(barX, healthBarY, gameCanvas.player.currentLife, gameCanvas.player.maxLife);

        int staminaBarY = healthBarY + BAR_HEIGHT + 12;
        drawStaminaBar(barX, staminaBarY, gameCanvas.player.stamina);
    }

    /**
     * Draws the player's inventory items and their counts in the HUD.
     * @param startX the starting x-coordinate
     * @param y the y-coordinate for drawing
     */
    private void drawInventoryItems(int startX, int y) {
        int x = startX + 150;
        if (gameCanvas.player.inventory[2] != null) {
            g2d.drawImage(medkitImage, x + ITEM_SPACING / 3, y - 14, ITEM_SIZE + 30, ITEM_SIZE + 30, null);
        }
        x += ITEM_SPACING;

        g2d.setFont(font);
        g2d.setColor(Color.white);
        g2d.drawImage(keyImage, x, y, ITEM_SIZE, ITEM_SIZE, null);
        g2d.drawString(": " + gameCanvas.player.keysCollected, x + ITEM_SIZE + ICON_TEXT_SPACING, y + 35);

        x += ITEM_SPACING;
        g2d.drawImage(fuelImage, x, y, ITEM_SIZE, ITEM_SIZE, null);
        g2d.drawString(": " + gameCanvas.player.fuelCollected, x + ITEM_SIZE + ICON_TEXT_SPACING, y + 35);

        x += ITEM_SPACING;
        BufferedImage gunImage = getGunImage();
        g2d.drawImage(gunImage, x, y, 50, ITEM_SIZE, null);
        g2d.drawString(": " + gameCanvas.player.currentAmmo + " / " + gameCanvas.player.maxAmmo, x + ITEM_SIZE + ICON_TEXT_SPACING, y + 35);
    }

    /**
     * Returns the correct gun image based on the player's current weapon.
     * @return the BufferedImage for the equipped gun
     */
    private BufferedImage getGunImage() {
        int maxLife = gameCanvas.player.projectile.maxLife;

        if (maxLife == 30) {
            return smgImage;
        } else if (maxLife == 40) {
            return shotgunImage;
        } else if (maxLife == 55) {
            return rifleImage;
        } else {
            return defaultImage;
        }
    }

    /**
     * Draws the status bars and icons for all players at the bottom of the screen.
     */
    private void drawBottomRowPlayers() {
        int bottomY = SCREEN_HEIGHT - 80;
        int x = SIDE_MARGIN;

        for (int i = 0; i < gameCanvas.players.length; i++) {
            Player otherPlayer = gameCanvas.players[i];
            if (otherPlayer != null) {
                drawOtherPlayerStatus(x, bottomY, otherPlayer, gameCanvas.playersActive[i]);
                x += PLAYER_SPACING;
            }
        }
    }

    /**
     * Draws another player's profile, health, and stamina bar in the bottom row.
     * @param x the x-coordinate for drawing
     * @param y the y-coordinate for drawing
     * @param player the player whose status is being drawn
     * @param isActive whether the player is currently active
     */
    private void drawOtherPlayerStatus(int x, int y, Player player, boolean isActive) {
        g2d.setColor(new Color(35, 35, 35));
        g2d.fillRect(x, y, PROFILE_SIZE + 10, PROFILE_SIZE + 10);

        if (!isActive || player.currentLife <= 0) {
            g2d.drawImage(profPicDead, x + 1, y, PROFILE_SIZE + 11, PROFILE_SIZE + 11, null);
        } else {
            g2d.drawImage(profPic, x + 5, y + 2, PROFILE_SIZE, PROFILE_SIZE, null);
        }

        int barX = x + PROFILE_SIZE + 20;
        int healthBarY = y + 5;
        drawHealthBar(barX, healthBarY, player.currentLife, gameCanvas.player.maxLife);

        int staminaBarY = healthBarY + BAR_HEIGHT + 12;
        if (player.currentLife <= 0) {
            drawStaminaBar(barX, staminaBarY, 0);
        } else {
            drawStaminaBar(barX, staminaBarY, player.stamina);
        }
    }


    /**
     * Draws the health bar for a player at the specified location.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param currentLife the player's current health
     * @param maxLife the player's maximum health
     */
    private void drawHealthBar(int x, int y, int currentLife, int maxLife) {
        g2d.setColor(new Color(35, 35, 35));
        g2d.fillRect(x, y, BAR_WIDTH, BAR_HEIGHT);

        double hpValue;
        if (currentLife <= 0) {
            hpValue = 0;
        } else {
            double hpFraction = Math.max(0, Math.min(1.0, (double) currentLife / maxLife));
            hpValue = BAR_WIDTH * hpFraction;
        }

        g2d.setColor(new Color(255, 0, 30));
        g2d.fillRect(x, y, (int) hpValue, BAR_HEIGHT);
    }

    /**
     * Draws the stamina bar for a player at the specified location.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param stamina the player's current stamina
     */
    private void drawStaminaBar(int x, int y, double stamina) {
        g2d.setColor(new Color(35, 35, 35));
        g2d.fillRect(x, y, BAR_WIDTH, BAR_HEIGHT);

        double staminaValue;
        if (stamina <= 0) {
            staminaValue = 0;
        } else {
            double maxStamina = 5.0;
            double staminaFraction = Math.min(1.0, stamina / maxStamina);
            staminaValue = BAR_WIDTH * staminaFraction;
        }

        g2d.setColor(new Color(6, 25, 128));
        g2d.fillRect(x, y, (int) staminaValue, BAR_HEIGHT);
    }

    /**
     * Draws the current dialogue window and text on the screen.
     * @param g2d the graphics context to draw on
     */
    public void drawDialogue(Graphics2D g2d) {
        if (dialogueIndex < dialogue.length) {
            currentDialogue = dialogue[dialogueIndex];
            int x = 48;
            int y = 100;
            int width = 928;
            int height = 200;
            drawWindow(g2d, x, y, width, height);
            g2d.setFont(pixel.deriveFont(Font.PLAIN, 32F)); // Use the pixel font
            g2d.setColor(Color.white);

            x += 60;
            y += 60;
            String[] lines = currentDialogue.split("\n");
            int lineHeight = g2d.getFontMetrics().getHeight();
            for (String line : lines) {
                g2d.drawString(line, x, y);
                y += lineHeight;
        }

        } else {
            gameCanvas.gameState = gameCanvas.playState;
        }
    }

    /**
     * Draws a rounded window background for dialogue or status screens.
     * @param g2d the graphics context
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param width the width of the window
     * @param height the height of the window
     */
    public void drawWindow(Graphics2D g2d, int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 150);
        g2d.setColor(c);
        g2d.fillRoundRect(x, y, width, height, 30, 30);

        c = new Color(255, 255, 255);
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 20, 20);
    }

    /**
     * Calculates the x-coordinate needed to center text on the screen.
     * @param text the text to center
     * @param font the font used for the text
     * @return the x-coordinate for centered text
     */
    private int centerTextX(String text, Font font) {
        FontMetrics metrics = g2d.getFontMetrics(font);
        int textWidth = metrics.stringWidth(text);
        return (SCREEN_WIDTH - textWidth) / 2;
    }

    /**
     * Draws the character status and controls overlay when the player opens the status screen.
     */
    private void showCharacterStatus() {
        int frameX = SCREEN_WIDTH / 2 - 250;
        int frameY = 200;
        int frameWidth = 500;
        int frameHeight = 400;

        Color c = new Color(0, 0, 0, 210);
        g2d.setColor(c);
        g2d.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 35, 35);

        c = new Color(255, 255, 255);
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(frameX + 5, frameY + 5, frameWidth - 10, frameHeight - 10, 25, 25);

        g2d.setColor(Color.YELLOW);
        g2d.setFont(font.deriveFont(Font.BOLD, 36F));
        String header = "CONTROLS";
        int headerX = centerTextX(header, g2d.getFont());
        g2d.drawString(header, headerX, frameY + 50);

        g2d.setColor(Color.WHITE);
        g2d.setFont(pixel.deriveFont(Font.PLAIN, 28F));

        int textX = frameX + 50;
        int textY = frameY + 100;
        int lineHeight = 40;

        g2d.setColor(Color.CYAN);
        g2d.drawString("WASD", textX, textY);
        g2d.setColor(Color.WHITE);
        g2d.drawString(" - MOVE AND AIM", textX + 100, textY);
        textY += lineHeight;

        g2d.setColor(Color.CYAN);
        g2d.drawString("LEFT CLICK", textX, textY);
        g2d.setColor(Color.WHITE);
        g2d.drawString("   - SHOOT", textX + 150, textY);
        textY += lineHeight;

        g2d.setColor(Color.CYAN);
        g2d.drawString("RIGHT CLICK ", textX, textY);
        g2d.setColor(Color.WHITE);
        g2d.drawString("   - MELEE ATTACK", textX + 150, textY);
        textY += lineHeight;

        g2d.setColor(Color.CYAN);
        g2d.drawString("SHIFT", textX, textY);
        g2d.setColor(Color.WHITE);
        g2d.drawString("- SPRINT", textX + 100, textY);
        textY += lineHeight;

        g2d.setColor(Color.CYAN);
        g2d.drawString("F", textX, textY);
        g2d.setColor(Color.WHITE);
        g2d.drawString("- PICKUP MEDKIT/WEAPON", textX + 50, textY);
        textY += lineHeight;

        g2d.setColor(Color.CYAN);
        g2d.drawString("E", textX, textY);
        g2d.setColor(Color.WHITE);
        g2d.drawString("- INTERACT", textX + 50, textY);
        textY += lineHeight;

        g2d.setColor(Color.CYAN);
        g2d.drawString("G", textX, textY);
        g2d.setColor(Color.WHITE);
        g2d.drawString("- USE MEDKIT", textX + 50, textY);
    }

    /**
     * Displays a temporary message on the screen.
     * @param text the message to show
     */
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    /**
     * Draws the game over screen and stops the game thread.
     */
    protected void gameOverScreen() {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 1024, 768);
        g2d.drawImage(gameOverScreen, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        gameCanvas.gameThread = null;
    }

    /**
     * Draws the death screen and stops the idle sound.
     */
    protected void deathScreen() {
        g2d.drawImage(diedScreen, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        gameCanvas.player.soundIdle.stop();
    }

    /**
     * Draws the team lost screen and stops the game thread.
     */
    protected void lostScreen() {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 1024, 768);
        g2d.drawImage(lostScreen, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        gameCanvas.gameThread = null;
    }
}
