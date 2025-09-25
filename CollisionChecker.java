/**
CollisionChecker handles all collision detection in the game, making sure characters, items, and mobs interact properly with the world.
It checks for collisions with tiles, items, NPCs, and players, and helps prevent characters from moving through solid objects.
  
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

import java.util.ArrayList;

public class CollisionChecker {
    private GameCanvas gameCanvas;

    /**
     * Sets up the CollisionChecker with a reference to the main game canvas.
     * @param gameCanvas the main game canvas reference
     */
    public CollisionChecker(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;
    }

    /**
     * Checks if a character is about to collide with a solid tile based on their direction and speed.
     * @param entity the character to check for tile collision
     */
    public void checkTile(Character entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gameCanvas.tileSize;
        int entityRightCol = entityRightWorldX / gameCanvas.tileSize;
        int entityTopRow = entityTopWorldY / gameCanvas.tileSize;
        int entityBottomRow = entityBottomWorldY / gameCanvas.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gameCanvas.tileSize;
                tileNum1 = gameCanvas.tileManager.mapTileNum[gameCanvas.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gameCanvas.tileManager.mapTileNum[gameCanvas.currentMap][entityRightCol][entityTopRow];
                if (gameCanvas.tileManager.tiles.get(tileNum1).collision || gameCanvas.tileManager.tiles.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gameCanvas.tileSize;
                tileNum1 = gameCanvas.tileManager.mapTileNum[gameCanvas.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gameCanvas.tileManager.mapTileNum[gameCanvas.currentMap][entityRightCol][entityBottomRow];
                if (gameCanvas.tileManager.tiles.get(tileNum1).collision || gameCanvas.tileManager.tiles.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gameCanvas.tileSize;
                tileNum1 = gameCanvas.tileManager.mapTileNum[gameCanvas.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gameCanvas.tileManager.mapTileNum[gameCanvas.currentMap][entityLeftCol][entityBottomRow];
                if (gameCanvas.tileManager.tiles.get(tileNum1).collision || gameCanvas.tileManager.tiles.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gameCanvas.tileSize;
                tileNum1 = gameCanvas.tileManager.mapTileNum[gameCanvas.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gameCanvas.tileManager.mapTileNum[gameCanvas.currentMap][entityRightCol][entityBottomRow];
                if (gameCanvas.tileManager.tiles.get(tileNum1).collision || gameCanvas.tileManager.tiles.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    /**
     * Checks if a character is colliding with any item on the current map and returns the index of the item if found.
     * @param character the character to check for item collision
     * @param player true if the character is the player, false otherwise
     * @return the index of the item collided with, or 999 if none
     */
    public int checkItem(Character character, boolean player) {
        int index = 999;

        // Get items from current map only
        ArrayList<Item> currentItems = gameCanvas.items.get(gameCanvas.currentMap);

        if (currentItems != null) {
            for (int i = 0; i < currentItems.size(); i++) {
                Item item = currentItems.get(i);
                if (item != null) {
                    // Original position calculations
                    character.solidArea.x = character.worldX + character.solidArea.x;
                    character.solidArea.y = character.worldY + character.solidArea.y;

                    item.solidArea.x = item.worldX + item.solidArea.x;
                    item.solidArea.y = item.worldY + item.solidArea.y;

                    // Movement prediction
                    switch (character.direction) {
                        case "up":
                            character.solidArea.y -= character.speed;
                            break;
                        case "down":
                            character.solidArea.y += character.speed;
                            break;
                        case "left":
                            character.solidArea.x -= character.speed;
                            break;
                        case "right":
                            character.solidArea.x += character.speed;
                            break;
                    }

                    // Collision check
                    if (character.solidArea.intersects(item.solidArea)) {
                        if (item.collision) {
                            character.collisionOn = true;
                        }
                        if (player) {
                            index = i;
                        }
                    }
                }

                // Reset values (same as original)
                character.resetValues();
                if (item != null) {
                    item.resetValues();
                }
            }
        }
        return index;
    }

    /**
     * Checks if a character is colliding with any NPC on the current map and returns the index of the NPC if found.
     * @param character the character to check for NPC collision
     * @param NPCs the list of all NPCs in the game
     * @return the index of the NPC collided with, or 999 if none
     */
    public int checkNPCCollision(Character character, ArrayList<ArrayList<Character>> NPCs) {
        int index = 999;

        // Get NPCs from current map only
        ArrayList<Character> currentNPCs = NPCs.get(gameCanvas.currentMap);

        if (currentNPCs != null) {
            for (int i = 0; i < currentNPCs.size(); i++) {
                Character npc = currentNPCs.get(i);
                if (npc != null) {
                    // Original position calculations
                    character.solidArea.x = character.worldX + character.solidArea.x;
                    character.solidArea.y = character.worldY + character.solidArea.y;

                    npc.solidArea.x = npc.worldX + npc.solidArea.x;
                    npc.solidArea.y = npc.worldY + npc.solidArea.y;

                    // Movement prediction
                    switch (character.direction) {
                        case "up":
                            character.solidArea.y -= character.speed;
                            break;
                        case "down":
                            character.solidArea.y += character.speed;
                            break;
                        case "left":
                            character.solidArea.x -= character.speed;
                            break;
                        case "right":
                            character.solidArea.x += character.speed;
                            break;
                    }

                    // Collision check
                    if (character.solidArea.intersects(npc.solidArea)) {
                        if(npc != character) {  // Don't collide with self
                            character.collisionOn = true;
                            index = i;
                        }
                    }

                    // Reset values (same as original)
                    character.resetValues();
                    npc.resetValues();
                }
            }
        }
        return index;
    }

    /**
     * Checks if a character is colliding with any NPC on the current map and returns the index of the NPC if found.
     * @param character the character to check for NPC collision
     * @param NPCs the list of all NPCs in the game
     * @return the index of the NPC collided with, or 999 if none
     */
    public boolean checkPlayerCollision(Character character) {
        boolean hitPlayer = false;

        // Check collision with the local player
        character.solidArea.x = character.worldX + character.solidArea.x;
        character.solidArea.y = character.worldY + character.solidArea.y;

        if (gameCanvas.player != null && gameCanvas.player.currentLife > 0) {
            gameCanvas.player.solidArea.x = gameCanvas.player.worldX + gameCanvas.player.solidArea.x;
            gameCanvas.player.solidArea.y = gameCanvas.player.worldY + gameCanvas.player.solidArea.y;

            switch (character.direction) {
                case "up":
                    character.solidArea.y -= character.speed;
                    break;
                case "down":
                    character.solidArea.y += character.speed;
                    break;
                case "left":
                    character.solidArea.x -= character.speed;
                    break;
                case "right":
                    character.solidArea.x += character.speed;
                    break;
            }

            if (character.solidArea.intersects(gameCanvas.player.solidArea)) {
                character.collisionOn = true;
                hitPlayer = true;
            }
            gameCanvas.player.resetValues();
        }

        for (Player networkedPlayer : gameCanvas.players) {
            if (networkedPlayer != null && networkedPlayer.gameCanvas.currentMap == gameCanvas.currentMap && networkedPlayer.currentLife > 0) {
                networkedPlayer.solidArea.x = networkedPlayer.worldX + networkedPlayer.solidArea.x;
                networkedPlayer.solidArea.y = networkedPlayer.worldY + networkedPlayer.solidArea.y;

                // Reuse the character's predicted position
                if (character.solidArea.intersects(networkedPlayer.solidArea)) {
                    character.collisionOn = true;
                    networkedPlayer.collisionOn = true;
                }
                networkedPlayer.resetValues();
            }
        }

        character.resetValues();
        return hitPlayer;
    }

    /**
     * Checks if a character is colliding with any networked player (excluding the local player).
     * @param character the character to check for collision
     * @return true if a collision with another player occurs, false otherwise
     */
    protected boolean checkOtherPlayerCollision(Character character){
        boolean hitPlayer = false;

        character.solidArea.x = character.worldX + character.solidArea.x;
        character.solidArea.y = character.worldY + character.solidArea.y;

        for (Player networkedPlayer : gameCanvas.players) {
            if (networkedPlayer != null && networkedPlayer.currentLife > 0) {
                networkedPlayer.solidArea.x = networkedPlayer.worldX + networkedPlayer.solidArea.x;
                networkedPlayer.solidArea.y = networkedPlayer.worldY + networkedPlayer.solidArea.y;

                switch (character.direction) {
                    case "up":
                        character.solidArea.y -= character.speed;
                        break;
                    case "down":
                        character.solidArea.y += character.speed;
                        break;
                    case "left":
                        character.solidArea.x -= character.speed;
                        break;
                    case "right":
                        character.solidArea.x += character.speed;
                        break;
                }

                // Reuse the character's predicted position
                if (character.solidArea.intersects(networkedPlayer.solidArea)) {
                    character.collisionOn = true;
                    hitPlayer = true;
                    networkedPlayer.collisionOn = true;
                }
                networkedPlayer.resetValues();
            }
        }
        character.resetValues();
        return hitPlayer;
    }
}
