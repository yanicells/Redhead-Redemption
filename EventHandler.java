/**
EventHandler manages all in-game events that are triggered by the player, such as area damage, healing, weapon pickups, and map changes.
It checks for event triggers based on the player's position and handles the logic for reviving, swapping weapons, and progressing through the game.
 
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

public class EventHandler {
    protected GameCanvas gameCanvas;
    protected TriggerRectangle[][][] triggerRectangles;
    protected int previousEventX, previousEventY;
    protected boolean canTrigger = true;

     /**
     * Sets up the event handler and initializes all trigger rectangles for the maps.
     * @param gameCanvas the main game canvas reference
     */
    public EventHandler(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;

        triggerRectangles = new TriggerRectangle[gameCanvas.maxMapNum][gameCanvas.maxWorldCol][gameCanvas.maxWorldRow];

        int map = 0;
        int col = 0;
        int row = 0;

        while (map < gameCanvas.maxMapNum && col < gameCanvas.maxWorldCol && row < gameCanvas.maxWorldRow) {
            triggerRectangles[map][col][row] = new TriggerRectangle();
            triggerRectangles[map][col][row].x = 23;
            triggerRectangles[map][col][row].y = 23;
            triggerRectangles[map][col][row].width = 2;
            triggerRectangles[map][col][row].height = 2;
            triggerRectangles[map][col][row].triggerRectangleDefaultX = triggerRectangles[map][col][row].x;
            triggerRectangles[map][col][row].triggerRectangleDefaultY = triggerRectangles[map][col][row].y;
            col++;
            if (col == gameCanvas.maxWorldCol) {
                col = 0;
                row++;

                if (row == gameCanvas.maxWorldRow) {
                    row = 0;
                    map++;
                }
            }
        }

    }

    /**
     * Checks if the player is in range to trigger any events, and processes map changes if needed.
     * @param changeMap true if map transitions should be checked
     */
    public void checkEvent(boolean changeMap) {
        int xDistance = Math.abs(gameCanvas.player.worldX - previousEventX);
        int yDistance = Math.abs(gameCanvas.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > GameCanvas.tileSize) {
            canTrigger = true;
        }
        if (canTrigger) {
            checkAreaDamage();
            checkAreaHeal();
            checkWeapon();
            checkRevive();

            if (changeMap) {
                if (gameCanvas.currentMap == 0) {
                    if (hit(0, 66, 25)) {
                        changeMap(1, 53, 74);
                    }
                } else if (gameCanvas.currentMap == 1) {
                    gameCanvas.player.interactedItemIndex = 999;
                    changeMap(2, 48, 66);
                } else if (gameCanvas.currentMap == 2) {
                    changeMap(1, 66, 19);
                }
            }
        }
    }

    /**
     * Finds a player with zero health and sets them to be revived.
     */
    protected void revivePlayer() {
        for (int i = 0; i < gameCanvas.players.length; i++) {
            if (gameCanvas.players[i] != null) {
                if (gameCanvas.players[i].currentLife <= 0) {
                    gameCanvas.revivePlayer = i;
                    break;
                }
            }
            gameCanvas.revivePlayer = -1;
        }
    }

    /**
     * Gives the player a shotgun and updates its stats if the swap key is pressed.
     */
    protected void getShotgun() {
        if (gameCanvas.keyHandler.swapPressed) {
            gameCanvas.soundRefill.play();
            gameCanvas.player.projectile.maxLife = 40;
            gameCanvas.player.projectile.currentLife = gameCanvas.player.projectile.maxLife;
            gameCanvas.player.projectile.speed = 7;
        }
    }

    /**
     * Gives the player an SMG and updates its stats if the swap key is pressed.
     */
    protected void getSMG() {
        if (gameCanvas.keyHandler.swapPressed) {
            gameCanvas.soundRefill.play();
            gameCanvas.player.projectile.maxLife = 30;
            gameCanvas.player.projectile.currentLife = gameCanvas.player.projectile.maxLife;
            gameCanvas.player.projectile.speed = 8;
        }
    }

    /**
     * Gives the player a rifle and updates its stats if the swap key is pressed.
     */
    protected void getRifle() {
        if (gameCanvas.keyHandler.swapPressed) {
            gameCanvas.soundRefill.play();
            gameCanvas.player.projectile.maxLife = 55;
            gameCanvas.player.projectile.currentLife = gameCanvas.player.projectile.maxLife;
            gameCanvas.player.projectile.speed = 6;
        }
    }

    /**
     * Checks if the player has reached the goal area, considering adjacent tiles as well.
     * @param map the map number
     * @param col the column of the goal
     * @param row the row of the goal
     * @param player the player to check
     * @return true if the goal is reached, false otherwise
     */
    protected boolean goalReached(int map, int col, int row, Player player) {
        if (hit(map, col, row, player)) {
            return true;
        }

        if (hit(map, col, row - 1, player)) {
            return true;
        }

        if (hit(map, col, row + 1, player)) {
            return true;
        }

        if (hit(map, col - 1, row, player)) {
            return true;
        }

        if (hit(map, col + 1, row, player)) {
            return true;
        }
        return false;
    }

    /**
     * Changes the current map and updates all players' positions and music.
     * @param map the new map number
     * @param col the column to place the player
     * @param row the row to place the player
     */
    protected void changeMap(int map, int col, int row) {
        gameCanvas.currentMap = map;
        gameCanvas.player.interactedItemIndex = 999;
        gameCanvas.player.worldX = GameCanvas.tileSize * col;
        gameCanvas.player.worldY = GameCanvas.tileSize * row;


        if (gameCanvas.currentMusic != null){
            gameCanvas.currentMusic.stop();
        }

        String musicFile = "assets/bg.wav";
        if (map == 1) {
            musicFile = "assets/piano.wav";
        } else if (map == 2) {
            musicFile = "assets/trees.wav";
        }
        gameCanvas.currentMusic = new Sound(musicFile);
        gameCanvas.currentMusic.loop(); // or .play()

        previousEventX = gameCanvas.player.worldX;
        previousEventY = gameCanvas.player.worldY;
        canTrigger = true;

        for (int i = 0; i < gameCanvas.players.length; i++) {
            if (gameCanvas.players[i] != null) {
                gameCanvas.playerCurrentMap[i] = map;

                gameCanvas.players[i].worldX = GameCanvas.tileSize * col;
                gameCanvas.players[i].worldY = GameCanvas.tileSize * row;

                gameCanvas.players[i].gameCanvas.eventHandler.previousEventX = gameCanvas.player.worldX;
                gameCanvas.players[i].gameCanvas.eventHandler.previousEventY = gameCanvas.player.worldY;
                gameCanvas.players[i].gameCanvas.eventHandler.canTrigger = true; // Reset for all players

                gameCanvas.players[i].gameCanvas.currentMap = map;
            }
        }
    }

    /**
     * Checks if the main player is colliding with a trigger rectangle at the given map, column, and row.
     * @param map the map number
     * @param eventCol the column of the event
     * @param eventRow the row of the event
     * @return true if the player hits the trigger, false otherwise
     */
    public boolean hit(int map, int eventCol, int eventRow) {
        boolean hit = false;

        if (map == gameCanvas.currentMap) {
            gameCanvas.player.solidArea.x = gameCanvas.player.worldX + gameCanvas.player.solidArea.x;
            gameCanvas.player.solidArea.y = gameCanvas.player.worldY + gameCanvas.player.solidArea.y;
            triggerRectangles[map][eventCol][eventRow].x = eventCol * GameCanvas.tileSize + triggerRectangles[map][eventCol][eventRow].x;
            triggerRectangles[map][eventCol][eventRow].y = eventRow * GameCanvas.tileSize + triggerRectangles[map][eventCol][eventRow].y;

            if (gameCanvas.player.solidArea.intersects(triggerRectangles[map][eventCol][eventRow])) {
                hit = true;
                previousEventX = gameCanvas.player.worldX;
                previousEventY = gameCanvas.player.worldY;
            }

            gameCanvas.player.resetValues();
            triggerRectangles[map][eventCol][eventRow].x = triggerRectangles[map][eventCol][eventRow].triggerRectangleDefaultX;
            triggerRectangles[map][eventCol][eventRow].y = triggerRectangles[map][eventCol][eventRow].triggerRectangleDefaultY;
        }

        return hit;
    }

   /**
     * Checks if the specified player is colliding with a trigger rectangle at the given map, column, and row.
     * @param map the map number
     * @param eventCol the column of the event
     * @param eventRow the row of the event
     * @param player the player to check
     * @return true if the player hits the trigger, false otherwise
     */
    public boolean hit(int map, int eventCol, int eventRow, Player player) {
        boolean hit = false;

        if (map == gameCanvas.currentMap) {
            player.solidArea.x = player.worldX + player.solidArea.x;
            player.solidArea.y = player.worldY + player.solidArea.y;
            triggerRectangles[map][eventCol][eventRow].x = eventCol * GameCanvas.tileSize + triggerRectangles[map][eventCol][eventRow].x;
            triggerRectangles[map][eventCol][eventRow].y = eventRow * GameCanvas.tileSize + triggerRectangles[map][eventCol][eventRow].y;

            if (player.solidArea.intersects(triggerRectangles[map][eventCol][eventRow])) {
                hit = true;
                previousEventX = player.worldX;
                previousEventY = player.worldY;
            }

            player.resetValues();
            triggerRectangles[map][eventCol][eventRow].x = triggerRectangles[map][eventCol][eventRow].triggerRectangleDefaultX;
            triggerRectangles[map][eventCol][eventRow].y = triggerRectangles[map][eventCol][eventRow].triggerRectangleDefaultY;
        }

        return hit;
    }


    /**
     * Damages the player and sets the game state when they enter a damaging area.
     * @param gameState the game state to set after taking damage
     */
    private void areaDamage(int gameState) {
        gameCanvas.gameState = gameState;

        gameCanvas.player.currentLife -= 1;
        canTrigger = false;
    }

    /**
     * Heals the player by one point and plays a sound if the heal key is pressed.
     * @param gameState the game state to set after healing
     */
    private void areaHeal(int gameState) {
        if (gameCanvas.keyHandler.healPressed) {
            gameCanvas.soundHeal.play();
            gameCanvas.gameState = gameState;

            if (gameCanvas.player.currentLife + 1 <= gameCanvas.player.maxLife) {
                gameCanvas.player.currentLife += 1;
            }
        }
    }

    /**
     * Refills the player's ammo by five, up to their maximum, and plays a sound if the heal key is pressed.
     * @param gameState the game state to set after refilling ammo
     */
    private void getAmmo(int gameState) {
        if (gameCanvas.keyHandler.healPressed) {
            gameCanvas.soundRefill.play();
            gameCanvas.gameState = gameState;

            if (gameCanvas.player.currentAmmo + 5 <= gameCanvas.player.maxAmmo) {
                gameCanvas.player.currentAmmo += 5;
            } else {
                gameCanvas.player.currentAmmo = gameCanvas.player.maxAmmo;
            }
        }
    }

    /**
     * Checks if the player is in a revive area and triggers the revive logic if so.
     */
    private void checkRevive() {
        if (hit(0, 33, 48)) {
            if (gameCanvas.player.currentLife > 0) {
                revivePlayer();
            } else {
                gameCanvas.revivePlayer = -1;
            }
        }

        if (hit(0, 34, 48)) {
            if (gameCanvas.player.currentLife > 0) {
                revivePlayer();
            } else {
                gameCanvas.revivePlayer = -1;
            }
        }

        if (hit(0, 33, 49)) {
            if (gameCanvas.player.currentLife > 0) {
                revivePlayer();
            } else {
                gameCanvas.revivePlayer = -1;
            }
        }

        if (hit(0, 34, 49)) {
            if (gameCanvas.player.currentLife > 0) {
                revivePlayer();
            } else {
                gameCanvas.revivePlayer = -1;
            }
        }

        if (hit(1, 61, 18)) {
            if (gameCanvas.player.currentLife > 0) {
                revivePlayer();
            } else {
                gameCanvas.revivePlayer = -1;
            }
        }

        if (hit(2, 31, 28)) {
            if (gameCanvas.player.currentLife > 0) {
                revivePlayer();
            } else {
                gameCanvas.revivePlayer = -1;
            }
        }
    }

    /**
     * Checks if the player is in a weapon or ammo pickup area and gives the appropriate item.
     */
    private void checkWeapon() {
        if (hit(0, 32, 46)) {
            getShotgun();
        }

        if (hit(0, 30, 46)) {
            getRifle();
        }

        if (hit(0, 31, 46)) {
            getAmmo(gameCanvas.gameState);
        }

        if (hit(0, 33, 46)) {
            getAmmo(gameCanvas.gameState);
        }

        if (hit(0, 34, 46)) {
            getSMG();
        }

        if (hit(1, 60, 18)) {
            getAmmo(gameCanvas.gameState);
        }

        if (hit(1, 56, 63)) {
            getAmmo(gameCanvas.gameState);
        }

        if (hit(1, 56, 60)) {
            getRifle();
        }

        if (hit(1, 56, 61)) {
            getShotgun();
        }

        if (hit(1, 56, 62)) {
            getSMG();
        }

        if (hit(2, 33, 26)) {
            getSMG();
        }

        if (hit(2, 32, 26)) {
            getShotgun();
        }

        if (hit(2, 31, 26)) {
            getRifle();
        }

        if (hit(2, 30, 26)) {
            getAmmo(gameCanvas.gameState);
        }
    }

    /**
     * Checks if the player is in a damaging area and applies damage if so.
     */
    private void checkAreaDamage() {
        if (hit(0, 41, 51)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 42, 51)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 43, 51)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 63, 61)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 64, 61)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 65, 61)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 66, 61)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 63, 33)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 64, 33)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 65, 33)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 63, 34)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 63, 35)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 64, 35)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 65, 35)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 43, 31)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 44, 31)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 43, 32)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 44, 32)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(0, 44, 33)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(1, 50, 67)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(1, 50, 62)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(1, 51, 62)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(1, 54, 55)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(1, 52, 51)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(1, 55, 39)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(1, 50, 36)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(1, 51, 36)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(1, 55, 27)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(1, 53, 24)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(1, 54, 24)) {
            areaDamage(gameCanvas.gameState);
        }

        if (hit(1, 55, 24)) {
            areaDamage(gameCanvas.gameState);
        }
    }

    /**
     * Checks if the player is in a healing area and applies healing if so.
     */
    private void checkAreaHeal() {
        if (hit(0, 30, 48)) {
            areaHeal(gameCanvas.gameState);
        }
        if (hit(0, 31, 48)) {
            areaHeal(gameCanvas.gameState);
        }
        if (hit(0, 30, 49)) {
            areaHeal(gameCanvas.gameState);
        }
        if (hit(0, 31, 49)) {
            areaHeal(gameCanvas.gameState);
        }
        if (hit(1, 59, 18)) {
            areaHeal(gameCanvas.gameState);
        }
        if (hit(2, 30, 28)) {
            areaHeal(gameCanvas.gameState);
        }
    }
}
