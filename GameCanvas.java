/**
GameCanvas is the main component where all game logic, drawing, and updates happen.
It manages the game loop, player and mob updates, networking, and handles all rendering for the game window.
  
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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GameCanvas extends JComponent implements Runnable {

    // GAME SETTINGS
    private final int originalTileSize;
    private final int scale;

    public static int tileSize = 48;
    protected final int maxScreenColumn;
    protected final int maxScreenRow;
    protected final int screenWidth;
    protected final int screenHeight;
    private final int FPS;

    protected Thread gameThread;
    protected KeyHandler keyHandler;
    protected TileManager tileManager;
    protected BorderManager borderManager;
    protected CollisionChecker collisionChecker;
    protected boolean gameStarted = false;

    // PLAYER SETTINGS
    protected Player player;

    // WORLD SETTINGS
    protected final int maxWorldCol;
    protected final int maxWorldRow;
    protected int maxMapNum = 5;
    protected int currentMap = 0; // This will be sent to the server to track which map the player is on
    protected final int worldWidth;
    protected final int worldHeight;

    // NETWORKING
    protected Player[] players;
    protected Projectile[] teamProjectiles;
    protected Projectile[] mobProjectiles;
    protected int playerID;
    private Socket socket;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;
    protected int totalActivePlayers;
    protected int[] playerCurrentMap; // Track which map each player is on
    protected int revivePlayer = -1;
    protected boolean playerControl = false;
    public boolean[] playersActive;

    // WORLD
    protected WorldSetter worldSetter;
    protected GameOverlay overlay;
    protected EventHandler eventHandler;
    protected Sound currentMusic;
    protected Sound zombieSound, soundRefill, soundHeal;

    // GAME STATE
    protected int gameState;
    protected int playState;
    protected int dialogueState;
    protected int characterStatus;
    protected boolean shouldChangeMap = false;
    protected boolean teamLost = false;

    // NPC
    protected ArrayList<ArrayList<Character>> mobs = new ArrayList<>();
    protected ArrayList<ArrayList<Item>> items = new ArrayList<>();
    protected ArrayList<Projectile> projectiles = new ArrayList<>();
    protected ArrayList<Drawable> drawables = new ArrayList<>();
    protected PathFinder pathFinder = new PathFinder(this);

    /**
     * Sets up the game canvas, initializes all game systems, and loads resources.
     */
    public GameCanvas() {
        originalTileSize = 16;
        scale = 4;
        tileSize = originalTileSize * scale; // 16 * 4 = 64
        maxScreenColumn = 16;
        maxScreenRow = 12;
        screenWidth = tileSize * maxScreenColumn; // 64 * 16 = 1024
        screenHeight = tileSize * maxScreenRow; // 64 * 12 = 768

        maxWorldCol = 100;
        maxWorldRow = 100;
        worldWidth = tileSize * maxWorldCol;
        worldHeight = tileSize * maxWorldRow;

        playersActive = new boolean[GameServer.maxPlayers];
        Arrays.fill(playersActive, false);

        FPS = 60;

        keyHandler = new KeyHandler(this);
        player = new Player(this, keyHandler);
        tileManager = new TileManager(this);
        borderManager = new BorderManager(this);
        collisionChecker = new CollisionChecker(this);

        // WORLD
        populate();
        worldSetter = new WorldSetter(this);
        overlay = new GameOverlay(this);
        
        overlay.dialogue = new String[]{
                "THE WORLD ENDED 2 YEARS AGO.\n[SPACE]",
                "SOME OVERFUNDED BIO LAB TRIED TO MAKE\nA NEW HAIR DYE THAT WOULD LAST FOREVER.\nTHEY USED SOME UNKNOWN CHEMICALS.\n[SPACE]",
                "CLASSIC MISTAKE.\n[SPACE] ",
                "TWO WEEKS LATER, HALF OF THE WORLD WAS \nEATING THE OTHER HALF.\n[SPACE] ",
                "BUT HERE IS THE KICKER: \nTHE INFECTED WOULDN'T TOUCH US GINGERS\n[SPACE]", 
                "SCIENTISTS SAID IT WAS SOMETHING IN\nTHE GENE, THE SAME THING THAT GAVE US \nRED HAIR AND PALE SKIN.\n[SPACE]",
                "SUDDENLY, BEING A GINGER WAS NO LONGER\nA CURSE. AND WHILE WE COULD'NT TURN,\nWE COULD STILL DIE.\n[SPACE]",
                "WE HAVE HEARD OF A SAFE ZONE UP NORTH\nRUN BY GINGERS. A HELICOPTER IS WAITING\nON THE OTHER SIDE OF TOWN. \n[SPACE]",
                "THERE ARE HEALING STATIONS AROUND THE MAP.\nI ALSO HEARD OF AMMO AND GUN STATIONS\nTHERE ARE REVIVE STATIONS TOO.",
                "WASD - MOVE\nLEFT CLICK - SHOOT\nRIGHT CLICK - MELEE\nSHIFT - SPRINT",
                "F - CHANGE WEAPON, PICK UP MEDKIT\nG - USE MEDKIT\nE - INTERACT WITH AMMO AND HEAL\nC - CONTROLS LIST",
                "IT'S TIME FOR A REDHEAD REDEMPTION.\n[SPACE]",
        };

        overlay.dialogueIndex = 0;

        eventHandler = new EventHandler(this);
        playState = 1;
        dialogueState = 3;
        characterStatus = 4;
        gameState = dialogueState;
        currentMusic = new Sound("assets/bg.wav");
        currentMusic.loop();
        zombieSound = new Sound("assets/zombie.wav");
        zombieSound.loop();
        soundRefill = new Sound("assets/refill.wav");
        soundHeal = new Sound("assets/heal.wav");


        playerCurrentMap = new int[5];
        Arrays.fill(playerCurrentMap, 0); // Start all players on map 0

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.addKeyListener(keyHandler);
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }

    /**
     * Fills the world with empty lists for mobs, items, and projectiles, and creates player and projectile objects for each slot.
     */
    private void populate() {
        for (int i = 0; i < maxMapNum; i++) {
            mobs.add(new ArrayList<>());
            items.add(new ArrayList<>());
        }

        players = new Player[GameServer.maxPlayers];
        teamProjectiles = new Projectile[GameServer.maxPlayers];
        for (int i = 0; i < players.length; i++) {
            Player otherPlayer = new Player(this, null);
            Projectile bullet = new Bullet(this);
            players[i] = otherPlayer;
            teamProjectiles[i] = bullet;
        }

        mobProjectiles = new Projectile[100];

        for (int i = 0; i < 100; i++) {
            Projectile poison = new Poison(this);
            mobProjectiles[i] = poison;
        }
    }

    /**
     * Starts the main game thread that runs the game loop.
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Sets up the world by placing objects, mobs, and borders in their starting positions.
     */
    public void setUpWorld() {
        worldSetter.setObject();
        worldSetter.setMobs();
        borderManager.setup();
    }

    /**
     * The main game loop that updates the game state and triggers repainting at a fixed frame rate.
     */
    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
//                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    /**
     * Updates the state of all players, mobs, projectiles, and handles game events each frame.
     */
    public void update() {
        if (gameState == dialogueState) {
            if (keyHandler.spacePressed) {
                keyHandler.spacePressed = false;
                if (overlay.dialogueIndex < overlay.dialogue.length - 1) {
                    overlay.dialogueIndex++;
                } else {
                    gameState = playState;
                }
            }
        }

        if (gameState == playState || gameState == dialogueState || gameState == characterStatus) {
            boolean hasLost = true;
            for (Player otherPlayer : players) {
                if (otherPlayer != null) {
                    if (otherPlayer.currentLife > 0) {
                        hasLost = false;
                        break;
                    }
                }
            }
            if (player.currentLife > 0) {
                hasLost = false;
            }

            teamLost = hasLost;

            
            if (currentMap == 1 && eventHandler.goalReached(1, 45, 19, player) && player.fuelCollected == 6) {
                boolean allPlayersInTrigger = true;
                for (int i = 0; i < players.length; i++) {
                    if (players[i] != null && playerCurrentMap[i] == currentMap) {
                        if (!eventHandler.goalReached(1, 45, 19, players[i])) {
                            allPlayersInTrigger = false;
                            break;
                        }
                    }
                }
                if (allPlayersInTrigger) {
                    overlay.gameOverScreen();
                    overlay.gameOver = true;
                }
            }else if (currentMap == 1 && eventHandler.goalReached(1, 45, 19, player) && player.fuelCollected != 6){
                overlay.dialogue = new String [] {"OH NO! THE HELICOPTER HAS NO GAS!\nYOU NEED 6 FUEL CANS!"};
                overlay.dialogueIndex = 0;
                gameState = dialogueState;
            }


            if (currentMap == 0 && eventHandler.hit(0, 66, 25)) {
                boolean allPlayersInTrigger = true;
                for (int i = 0; i < players.length; i++) {
                    if (players[i] != null && playerCurrentMap[i] == currentMap) {
                        if (!eventHandler.hit(0, 66, 25, players[i])) {
                            allPlayersInTrigger = false;
                            break;
                        }
                    }
                }

                if (allPlayersInTrigger) {
                    eventHandler.changeMap(1, 53, 74);
                    overlay.dialogue = new String [] {"THE HELICOPTER IS ON THE OTHER SIDE OF THE\nBRIDGE! MAKE YOUR WAY THROUGH THE ZOMBIES\n BE CAREFUL, IT ONLY GETS HARDER FROM HERE..."};
                    overlay.dialogueIndex = 0;
                    gameState = dialogueState;
                }
            } else if (currentMap == 1 && eventHandler.hit(1, 85, 19)) {
                boolean allPlayersInTrigger = true;
                for (int i = 0; i < players.length; i++) {
                    if (players[i] != null && playerCurrentMap[i] == currentMap) {
                        if (!eventHandler.hit(1, 85, 19, players[i])) {
                            allPlayersInTrigger = false;
                            break;
                        }
                    }
                }

                if (allPlayersInTrigger) {
                    player.interactedItemIndex = 999;
                    eventHandler.changeMap(2, 48, 66);
                }
            } else if (currentMap == 2 && eventHandler.hit(2, 48, 68)) {
                boolean allPlayersInTrigger = true;
                for (int i = 0; i < players.length; i++) {
                    if (players[i] != null && playerCurrentMap[i] == currentMap) {
                        if (!eventHandler.hit(2, 48, 68, players[i])) {
                            allPlayersInTrigger = false;
                            break;
                        }
                    }
                }

                if (allPlayersInTrigger) {
                    eventHandler.changeMap(1, 83, 19);
                    worldSetter.respawnMobs();
                }
            }

            boolean hasDeadPlayer = false;
            for (int i = 0; i < players.length; i++) {
                if (players[i] != null) {
                    if ((players[i].currentLife <= 0)) {
                        hasDeadPlayer = true;
                        break;
                    }
                }
            }

            if (!hasDeadPlayer && player.currentLife > 0) {
                revivePlayer = -1;
            }

            if (revivePlayer >= 0) {
                if (players[revivePlayer].currentLife > 0) {
                    revivePlayer = -1;
                }
            }

            if (player.currentLife > 0) {
                player.update();
            }

            for (int i = 0; i < players.length; i++) {
                if (players[i] != null) {
                    if (playerCurrentMap[i] == currentMap && players[i].currentLife > 0) {
                        players[i].update();
                    }
                }
            }

            ArrayList<Character> currentMobs = mobs.get(currentMap);

            if (currentMobs != null) {
                for (int i = 0; i < currentMobs.size(); i++) {
                    Character mob = currentMobs.get(i);
                    if (mob != null) {
                        if (mob.alive && !mob.dying) {
                            mob.update();
                        }
                        if (!mob.alive) {
                            currentMobs.set(i, null);
                        }
                    }
                }
            }

            for (Projectile projectile : mobProjectiles) {
                if (!playerControl) {
                    projectile.update();
                }
            }

            for (int i = 0; i < projectiles.size(); i++) {
                if (projectiles.get(i) != null) {
                    if (projectiles.get(i).alive) {
                        projectiles.get(i).update();
                    }
                    if (!projectiles.get(i).alive) {
                        projectiles.remove(i);
                    }
                }
            }

            for (int i = 0; i < teamProjectiles.length; i++) {
                if (teamProjectiles[i] != null) {
                    if (teamProjectiles[i].alive) {
                        teamProjectiles[i].update();
                    }
                }
            }

            for (int i = 0; i < mobProjectiles.length; i++) {
                if (mobProjectiles[i] != null) {
                    if (!playerControl && mobProjectiles[i].alive) {
                        mobProjectiles[i].update();
                    }
                }
            }
        }
    }

    /**
     * Draws all game elements and overlays onto the screen in the correct order.
     * @param g the graphics to draw on
     */
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (!gameStarted) {
            BufferedImage startScreen = null;
            try {
                startScreen = ImageIO.read(getClass().getResourceAsStream("assets/startscreen.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            g2d.drawImage(startScreen, 0, 0, 1024, 768, null);
            return;
        }

        tileManager.draw(g2d);

        if (player.currentLife > 0) {
            drawables.add(player);
        }

        ArrayList<Item> currentItems = items.get(currentMap);
        if (currentItems != null) {
            for (Item item : currentItems) {
                if (item != null) {
                    drawables.add(item);
                }
            }
        }

        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                if (playerCurrentMap[i] == currentMap && players[i].currentLife > 0) {
                    drawables.add(players[i]);
                }
            }
        }

        ArrayList<Character> currentMobs = mobs.get(currentMap);
        if (currentMobs != null) {
            for (Character mob : currentMobs) {
                if (mob != null) {
                    drawables.add(mob);
                }
            }
        }

        for (Projectile projectile : projectiles) {
            if (projectile != null) {
                drawables.add(projectile);
            }
        }

        for (Projectile mobProjectile : mobProjectiles) {
            if (!playerControl) {
                if (mobProjectile != null) {
                    if (mobProjectile.alive) {
                        drawables.add(mobProjectile);
                    }
                }
            }
        }

        // SORT
        drawables.sort(new Comparator<Drawable>() {
            @Override
            public int compare(Drawable o1, Drawable o2) {
                return Integer.compare(o1.worldY, o2.worldY);
            }
        });

        for (Projectile teamProjectile : teamProjectiles) {
            if (teamProjectile != null) {
                if (teamProjectile.alive) {
                    drawables.add(teamProjectile);
                }
            }
        }

        for (Drawable drawable : drawables) {
            drawable.draw(g2d, this);
        }

        drawables.clear();
        if (gameState == dialogueState) {
            borderManager.draw(g2d);
            overlay.drawDialogue(g2d);
            overlay.draw(g2d);
        }
        if (gameState == playState) {
            borderManager.draw(g2d);
            overlay.draw(g2d);
        }
        if (gameState == characterStatus) {
            overlay.draw(g2d);
            borderManager.draw(g2d);
        }
        g2d.dispose();
    }

    /**
     * Connects the client to the game server and sets up networking for multiplayer.
     */
    public void connectToServer() {
        try {
            Scanner scanner = new Scanner(System.in);
            String IPAddress = scanner.nextLine();
            socket = new Socket(IPAddress, 5000);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt();
            if (playerID == 1) {
                playerControl = true;
            }

            System.out.println("You are Player " + playerID);
            if (playerID != GameServer.maxPlayers) {
                System.out.println("Waiting for the other players.");
            }

            rfsRunnable = new ReadFromServer(in);
            wtsRunnable = new WriteToServer(out);
            rfsRunnable.waitForStartMsg();

            for (int i = 0; i < players.length; i++) {
                Player otherPlayer = new Player(this, null);
                Projectile bullet = new Bullet(this);
                if (i != playerID - 1 && i < totalActivePlayers) {
                    players[i] = otherPlayer;
                    teamProjectiles[i] = bullet;
                } else {
                    players[i] = null;
                    teamProjectiles[i] = null;
                }
            }

            gameStarted = true;
            startGameThread();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            if (gameThread != null) {
                System.out.println("Unable to connect to server.");
                System.exit(0);
            }
            if (!teamLost && !overlay.gameOver) {
                System.exit(0);
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (gameThread != null) {
                System.out.println("Unable to connect to server.");
                System.exit(0);
            }
            if (!teamLost && !overlay.gameOver) {
                System.exit(0);
            }
        }
    }

    /**
     * Handles incoming data from the server and updates the local game state for all players and mobs.
     */
    private class ReadFromServer implements Runnable {
        private DataInputStream dataIn;

        public ReadFromServer(DataInputStream dataIn) {
            this.dataIn = dataIn;
            System.out.println("Player " + playerID + " RFS created!");
        }

        /**
         * Continuously reads updates from the server and applies them to the game state.
         */
        @Override
        public void run() {
            try {
                while (true) {
                    int playerDataID = dataIn.readInt();
                    playerControl = dataIn.readBoolean();
                    boolean lostStatus = dataIn.readBoolean();

                    int activeCount = dataIn.readInt();
                    for (int i = 0; i < activeCount; i++) {
                        boolean isActive = dataIn.readBoolean();
                        playersActive[i] = isActive;
                        if (!isActive) {
                            players[i] = null;
                        }
                    }

                    int otherPlayerX = dataIn.readInt();
                    int otherPlayerY = dataIn.readInt();
                    String currentDirection = dataIn.readUTF();
                    int currentSpriteNum = dataIn.readInt();
                    int currentIdleNum = dataIn.readInt();
                    int playerStamina = dataIn.readInt();
                    boolean collision = dataIn.readBoolean();
                    int playerHealth = dataIn.readInt(); // Read health
                    boolean playerNoDamage = dataIn.readBoolean(); // Read noDamage
                    boolean playerAttacking = dataIn.readBoolean();
                    boolean playerShooting = dataIn.readBoolean();
                    int mobIndex = dataIn.readInt();
                    int playerCurrentAttack = dataIn.readInt();

                    int xp = dataIn.readInt();
                    int nextXp = dataIn.readInt();
                    int level = dataIn.readInt();
                    int maxHealth = dataIn.readInt();
                    int ammo = dataIn.readInt();

                    int otherPlayerMap = dataIn.readInt();

                    int itemIndex = dataIn.readInt();
                    boolean gameOverStatus = dataIn.readBoolean();
                    int revivePlayerIndex = dataIn.readInt();
                    if (revivePlayerIndex + 1 == playerID) {
                        player.currentLife = player.maxLife;
                    }
                    boolean isProjectileAlive = dataIn.readBoolean();
                    boolean shouldProjectileDamage = dataIn.readBoolean();
                    int projectileWorldX = dataIn.readInt();
                    int projectileWorldY = dataIn.readInt();
                    String projectileDirection = dataIn.readUTF();
                    int projectileDamage = dataIn.readInt();

                    if (isProjectileAlive) {
                        if (playerDataID > 0 && playerDataID <= teamProjectiles.length) {
                            if (teamProjectiles[playerDataID - 1] != null) {
                                teamProjectiles[playerDataID - 1].alive = true;
                                teamProjectiles[playerDataID - 1].shouldDamage = shouldProjectileDamage;
                                teamProjectiles[playerDataID - 1].set(projectileWorldX, projectileWorldY, projectileDirection, true, players[playerDataID - 1]);
                            }
                        }
                    } else {
                        if (playerDataID > 0 && playerDataID <= teamProjectiles.length) {
                            if (teamProjectiles[playerDataID - 1] != null) {
                                teamProjectiles[playerDataID - 1].alive = false;
                                teamProjectiles[playerDataID - 1].shouldDamage = shouldProjectileDamage;
                                if (playerDataID == playerID) {
                                    player.projectile.alive = false;
                                }
                            }
                        }
                    }

                    int mobCount = dataIn.readInt();
                    for (int i = 0; i < mobCount; i++) {
                        boolean stillAlive = dataIn.readBoolean();
                        int mobX = dataIn.readInt();
                        int mobY = dataIn.readInt();
                        String mobDirection = dataIn.readUTF();
                        int mobSprite = dataIn.readInt();
                        boolean mobCollision = dataIn.readBoolean(); // Read mob collision state
                        boolean mobPathFind = dataIn.readBoolean();
                        int mobHealth = dataIn.readInt();
                        boolean mobDyingState = dataIn.readBoolean();
                        boolean mobProjectileAlive = dataIn.readBoolean();
                        int mobProjectileX = dataIn.readInt();
                        int mobProjectileY = dataIn.readInt();
                        String mobProjectileDirection = dataIn.readUTF();

                        if (otherPlayerMap == currentMap) {
                            if (stillAlive) {
                                if (mobs != null && mobs.get(currentMap).size() > i) {
                                    if (mobs.get(currentMap).get(i) != null && !playerControl) {
                                        mobs.get(currentMap).get(i).worldX = mobX;
                                        mobs.get(currentMap).get(i).worldY = mobY;
                                        mobs.get(currentMap).get(i).direction = mobDirection;
                                        mobs.get(currentMap).get(i).spriteNum = mobSprite;
                                        mobs.get(currentMap).get(i).collisionOn = mobCollision; // Apply mob collision state
                                        mobs.get(currentMap).get(i).findPath = mobPathFind;
                                        mobs.get(currentMap).get(i).currentLife = mobHealth;
                                        mobs.get(currentMap).get(i).dying = mobDyingState;
                                        mobs.get(currentMap).get(i).projectile.alive = mobProjectileAlive;
                                        mobs.get(currentMap).get(i).projectile.worldX = mobProjectileX;
                                        mobs.get(currentMap).get(i).projectile.worldY = mobProjectileY;
                                        mobs.get(currentMap).get(i).projectile.direction = mobProjectileDirection;
                                        player.exp = xp;
                                        player.nextLevelExp = nextXp;
                                        player.level = level;
                                        player.maxLife = maxHealth;
                                        player.maxAmmo = ammo;

                                        if (mobProjectileAlive) {
                                            if (mobProjectiles[i] != null) {
                                                mobProjectiles[i].alive = true;
                                                mobProjectiles[i].set(mobProjectileX, mobProjectileY, mobProjectileDirection, true, mobs.get(currentMap).get(i));
                                            }
                                        } else {
                                            if (mobProjectiles[i] != null) {
                                                mobProjectiles[i].alive = false;
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (mobs.get(currentMap).size() > i) {
                                    if (mobs.get(currentMap).get(i) != null) {
                                        mobs.get(currentMap).get(i).dying = mobDyingState;
                                        mobs.get(currentMap).set(i, null);
                                    }
                                }
                            }
                        }
                    }
                    //END OF READING

                    if (players[playerDataID - 1] != null) {
                        playerCurrentMap[playerDataID - 1] = otherPlayerMap;

                        players[playerDataID - 1].screenX = otherPlayerX - player.worldX + player.screenX;
                        players[playerDataID - 1].screenY = otherPlayerY - player.worldY + player.screenY;

                        players[playerDataID - 1].worldX = otherPlayerX;
                        players[playerDataID - 1].worldY = otherPlayerY;
                        players[playerDataID - 1].direction = currentDirection;
                        players[playerDataID - 1].spriteNum = currentSpriteNum;
                        players[playerDataID - 1].idleNum = currentIdleNum;
                        players[playerDataID - 1].stamina = playerStamina;
                        players[playerDataID - 1].collisionOn = collision;
                        players[playerDataID - 1].currentLife = playerHealth; // Update health
                        players[playerDataID - 1].noDamage = playerNoDamage; // Update noDamage
                        players[playerDataID - 1].attacking = playerAttacking; // Update noDamage
                        players[playerDataID - 1].shooting = playerShooting;
                        players[playerDataID - 1].projectile.shouldDamage = shouldProjectileDamage;
                        players[playerDataID - 1].attack = playerCurrentAttack;
                        players[playerDataID - 1].projectile.attack = projectileDamage;
                    }

                    if (players[playerDataID - 1] != null && otherPlayerMap == currentMap) {
                        if (playerAttacking || playerShooting) {
                            if (mobIndex != 999) {
                                if (playerAttacking) {
                                    player.damageMob(mobIndex, players[playerDataID - 1].attack);
                                } else if (playerShooting) {
                                    player.damageMob(mobIndex, players[playerDataID - 1].projectile.attack);
                                }
                                if (players[playerDataID - 1] != null) {
                                    players[playerDataID - 1].projectile.shouldDamage = false;
                                }
                            }
                        }
                    }

                    if (itemIndex != 999 && items.get(currentMap).size() > itemIndex) {
                        if (items.get(currentMap).get(itemIndex) != null) {
                            if (itemIndex == 0 || itemIndex == 1 || itemIndex == 2 || itemIndex == 3) {
                                items.get(currentMap).set(itemIndex, null);
                                if (players[playerDataID - 1] != null) {
                                    players[playerDataID - 1].resetItemIndex = true;
                                }
                            } else {
                                player.pickUpItem(itemIndex);
                            }
                        }
                    }

                    if (teamLost) {
                        overlay.lostScreen();
                        gameThread = null;
                    }

                    if (overlay.gameOver) {
                        overlay.gameOverScreen();
                        gameThread = null;
                    }
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
                if (gameThread != null) {
                    System.out.println("Unable to connect to server.");
                    System.exit(0);
                }
                if (!teamLost && !overlay.gameOver) {
                    System.exit(0);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                if (gameThread != null) {
                    System.out.println("Unable to connect to server.");
                    System.exit(0);
                }
                if (!teamLost && !overlay.gameOver) {
                    System.exit(0);
                }
            }
        }

        /**
         * Waits for the server's start message before beginning the game loop.
         */
        public void waitForStartMsg() {
            try {
                String startMsg = dataIn.readUTF();
                totalActivePlayers = dataIn.readInt();
                System.out.println("Message from Server: " + startMsg);
                Thread readThread = new Thread(rfsRunnable);
                Thread writeThread = new Thread(wtsRunnable);
                readThread.start();
                writeThread.start();
            } catch (IOException e) {
                System.err.println("Error waiting for start message: " + e.getMessage());
            }
        }
    }

    /**
     * Sends the local player's data and game state to the server at regular intervals.
     */
    private class WriteToServer implements Runnable {
        private DataOutputStream dataOut;

        /**
         * Sets up the output stream for sending data to the server.
         * @param dataOut the output stream to the server
         */
        public WriteToServer(DataOutputStream dataOut) {
            this.dataOut = dataOut;
            System.out.println("Player " + playerID + " WTS created!");
        }

        /**
         * Continuously sends the latest player and mob data to the server.
         */
        @Override
        public void run() {
            try {
                while (true) {
                    if (player != null) {
                        dataOut.writeBoolean(teamLost);
                        dataOut.writeInt(player.worldX);
                        dataOut.writeInt(player.worldY);
                        dataOut.writeUTF(player.direction);
                        dataOut.writeInt(player.spriteNum);
                        dataOut.writeInt(player.idleNum);
                        dataOut.writeBoolean(player.collisionOn);
                        dataOut.writeInt(player.stamina);

                        dataOut.writeInt(player.interactedItemIndex);
                        dataOut.writeBoolean(player.resetItemIndex);
                        dataOut.writeInt(revivePlayer);

                        dataOut.writeInt(player.currentLife); // Send health
                        dataOut.writeBoolean(player.noDamage); // Send noDamage
                        dataOut.writeBoolean(player.attacking);
                        dataOut.writeBoolean(player.shooting);
                        dataOut.writeInt(player.mobInteractIndex);
                        dataOut.writeInt(player.attack);
                        dataOut.writeInt(player.exp);
                        dataOut.writeInt(player.nextLevelExp);
                        dataOut.writeInt(player.level);
                        dataOut.writeInt(player.maxLife);
                        dataOut.writeInt(player.maxAmmo);

                        dataOut.writeInt(currentMap);
                        dataOut.writeBoolean(overlay.gameOver);

                        dataOut.writeBoolean(player.projectile.alive);
                        dataOut.writeBoolean(player.projectile.shouldDamage);
                        dataOut.writeInt(player.projectile.worldX);
                        dataOut.writeInt(player.projectile.worldY);
                        dataOut.writeUTF(player.projectile.direction);
                        dataOut.writeInt(player.projectile.attack);

                        dataOut.writeInt(mobs.get(currentMap).size());
                        ArrayList<Character> currentMobs = mobs.get(currentMap);

                        for (Character mob : currentMobs) {
                            if (mob != null) {
                                dataOut.writeBoolean(true);
                                dataOut.writeInt(mob.worldX);
                                dataOut.writeInt(mob.worldY);
                                dataOut.writeUTF(mob.direction);
                                dataOut.writeInt(mob.spriteNum);
                                dataOut.writeBoolean(mob.collisionOn); // Send mob collision state
                                dataOut.writeBoolean(mob.findPath);
                                dataOut.writeInt(mob.currentLife);
                                dataOut.writeBoolean(mob.dying);

                                dataOut.writeBoolean(mob.projectile.alive);
                                dataOut.writeInt(mob.projectile.worldX);
                                dataOut.writeInt(mob.projectile.worldY);
                                dataOut.writeUTF(mob.projectile.direction);

                            } else {
                                dataOut.writeBoolean(false);
                                dataOut.writeInt(0);
                                dataOut.writeInt(0);
                                dataOut.writeUTF("down");
                                dataOut.writeInt(0);
                                dataOut.writeBoolean(false); // Send mob collision state
                                dataOut.writeBoolean(false);
                                dataOut.writeInt(0);
                                dataOut.writeBoolean(true);
                                dataOut.writeBoolean(false);
                                dataOut.writeInt(0);
                                dataOut.writeInt(0);
                                dataOut.writeUTF("down");
                            }
                        }
                        dataOut.flush();
                    }
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                if (gameThread != null) {
                    System.out.println("Unable to connect to server.");
                    System.exit(0);
                }
                if (!teamLost && !overlay.gameOver) {
                    System.exit(0);
                }
                System.err.println("Error in WriteToServer: " + e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                if (gameThread != null) {
                    System.out.println("Unable to connect to server.");
                    System.exit(0);
                }
                if (!teamLost && !overlay.gameOver) {
                    System.exit(0);
                }
            }
        }
    }
}
