/**
GameServer manages all networking for the multiplayer game, handling player connections, data exchange, and game state synchronization.
It keeps track of player positions, health, actions, and mob states, ensuring all clients stay in sync during gameplay.
 
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class GameServer {

    private ServerSocket serverSocket;
    private int numPlayers;
    public static int maxPlayers = 5;
    private Socket[] playerSockets;
    private ReadFromClient[] readFromClients;
    private WriteToClient[] writeToClients;
    private int[][] playerCoordinates;
    private String[] direction;
    private int[] spriteNums;
    private int[] idleNum;
    private int itemIndexTracker = 999;
    private boolean gameOverStatus = false;
    protected boolean teamLost = false;
    private int gameState = 1;
    private int[] mobCounts; // Count of mobs for each map
    private boolean[] resetItem;
    private boolean[] collisionPlayer;
    private int[] playerHealth;
    private boolean[] playerNoDamage;
    private int[][] mobLife; // [mapID][mobIndex]
    private boolean[][] mobAlive; // [mapID][mobIndex]
    private boolean[][] mobDying; // [mapID][mobIndex]
    private boolean[] playerAttacking;
    private boolean[] playerShooting;
    private int[] playerAttackValue;
    private int[] playerProjectileAttackValue;
    private int[] playerCurrentMap; // Track which map each player is on
    private int[] playerStamina;
    private int exp = 0;
    private int nextLvlExp = 5;
    private int maxAmmo = 20;
    private int maxLife = 10;
    private int playerLevel = 1;

    // NETWORKS
    protected int[][][] mobs; // [mapID][mobIndex][x/y]
    protected String[][] mobDirection; // [mapID][mobIndex]
    protected int[][] mobSprite; // [mapID][mobIndex]
    protected boolean[][] mobCollision; // [mapID][mobIndex]
    protected boolean[][] mobPathFind; // [mapID][mobIndex]
    protected int[] mobIndex;
    protected int revivePlayerIndex = -1;
    protected boolean[] playerInControl;
    protected boolean[] playersActive;

    // PROJECTILE
    protected int[][] projectileCoordinates;
    protected boolean[] projectileAlive;
    protected String[] projectileDirection;
    protected boolean[] shouldProjectileDamage;

    protected int[][][] mobProjectileCoordinates; // [mapID][mobIndex][x/y]
    protected boolean[][] mobProjectileAlive; // [mapID][mobIndex]
    protected String[][] mobProjectileDirection; // [mapID][mobIndex]

    private final int maxMapNum = 5; // Match your GameCanvas value

    /**
     * Sets up the server, initializes all player and mob data, and prepares for incoming connections.
     */
    public GameServer() {
        System.out.println("===== GAME SERVER =====");
        numPlayers = 0;

        playerSockets = new Socket[maxPlayers];
        readFromClients = new ReadFromClient[maxPlayers];
        writeToClients = new WriteToClient[maxPlayers];
        playerCoordinates = new int[maxPlayers][2];
        direction = new String[maxPlayers];
        spriteNums = new int[maxPlayers];
        idleNum = new int[maxPlayers];
        playerAttacking = new boolean[maxPlayers];
        playerShooting = new boolean[maxPlayers];
        playerAttackValue = new int[maxPlayers];
        playerProjectileAttackValue = new int[maxPlayers];
        playerCurrentMap = new int[maxPlayers];
        playerStamina = new int[maxPlayers];
        playerInControl = new boolean[maxPlayers];
        playersActive = new boolean[maxPlayers];
        Arrays.fill(playersActive, false);
        Arrays.fill(playerInControl, false);
        playerInControl[0] = true;
        Arrays.fill(playerStamina, 5);
        Arrays.fill(playerCurrentMap, 0); // Start all players on map 0
        Arrays.fill(playerAttacking, false);
        Arrays.fill(playerShooting, false);
        Arrays.fill(playerAttackValue, 0);
        Arrays.fill(playerProjectileAttackValue, 0);

        mobs = new int[maxMapNum][100][2];
        mobDirection = new String[maxMapNum][100];
        mobSprite = new int[maxMapNum][100];
        mobCollision = new boolean[maxMapNum][100];
        mobPathFind = new boolean[maxMapNum][100];
        mobLife = new int[maxMapNum][100];
        mobAlive = new boolean[maxMapNum][100];
        mobDying = new boolean[maxMapNum][100];
        mobProjectileCoordinates = new int[maxMapNum][100][2];
        mobProjectileAlive = new boolean[maxMapNum][100];
        mobProjectileDirection = new String[maxMapNum][100];
        mobCounts = new int[maxMapNum];

        for (int mapId = 0; mapId < maxMapNum; mapId++) {
            Arrays.fill(mobDirection[mapId], "down");
            Arrays.fill(mobSprite[mapId], 0);
            Arrays.fill(mobCollision[mapId], false);
            Arrays.fill(mobPathFind[mapId], false);
            Arrays.fill(mobLife[mapId], 4);
            Arrays.fill(mobAlive[mapId], true);
            Arrays.fill(mobDying[mapId], true);
            Arrays.fill(mobProjectileAlive[mapId], false);
            Arrays.fill(mobProjectileDirection[mapId], "down");
        }

        mobIndex = new int[maxPlayers];
        Arrays.fill(mobIndex, 999);

        projectileCoordinates = new int[maxPlayers][2];
        projectileAlive = new boolean[maxPlayers];
        shouldProjectileDamage = new boolean[maxPlayers];
        projectileDirection = new String[maxPlayers];
        Arrays.fill(projectileAlive, false);
        Arrays.fill(shouldProjectileDamage, false);
        Arrays.fill(projectileDirection, "down");

        resetItem = new boolean[maxPlayers];
        collisionPlayer = new boolean[maxPlayers];
        playerHealth = new int[maxPlayers];
        playerNoDamage = new boolean[maxPlayers];

        for (int i = 0; i < maxPlayers; i++) {
            int worldX = GameCanvas.tileSize * 23; // Example position for X
            int worldY = GameCanvas.tileSize * 21; // Example position for Y

            playerCoordinates[i][0] = worldX; // X coordinate
            playerCoordinates[i][1] = worldY; // Y coordinate
            projectileCoordinates[i][0] = 0;
            projectileCoordinates[i][1] = 0;
            direction[i] = "down";
            spriteNums[i] = 1;
            idleNum[i] = 1;
            resetItem[i] = false;
            collisionPlayer[i] = false;
            playerHealth[i] = 6; // Default max life from Player.java
            playerNoDamage[i] = false;
        }
        Arrays.fill(direction, "down");

        try {
            serverSocket = new ServerSocket(5000);
        } catch (IOException e) {
            System.out.println("IOException from GameServer constructor");
        }
    }

    /**
     * Updates which player currently has control, typically after a disconnect.
     */
    private void updateControl() {
        for (int i = 0; i < playerSockets.length; i++) {
            if (playerSockets[i] != null) {
                playerInControl[i] = true;
                break;
            }
        }
    }

    /**
     * Waits for players to connect, assigns them IDs, and starts the communication threads once all are ready.
     */
    public void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");
            System.out.println(numPlayers + " out of " + maxPlayers + " player/s connected.");

            while (numPlayers < maxPlayers) {
                Socket socket = serverSocket.accept();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                numPlayers++;

                out.writeInt(numPlayers);

                System.out.println("Player " + numPlayers + " has connected");
                System.out.println(numPlayers + " out of " + maxPlayers + " player/s.");

                ReadFromClient rfc = new ReadFromClient(numPlayers, in);
                WriteToClient wtc = new WriteToClient(numPlayers, out);

                playerSockets[numPlayers - 1] = socket;
                readFromClients[numPlayers - 1] = rfc;
                writeToClients[numPlayers - 1] = wtc;
                playersActive[numPlayers - 1] = true;

                if (numPlayers == maxPlayers) {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("\nPress Enter to Start Game!");
                    scanner.nextLine();
                    for (WriteToClient writeRunnable : writeToClients) {
                        writeRunnable.sendStartMsg();
                    }

                    for (int i = 0; i < maxPlayers; i++) {
                        Thread read = new Thread(readFromClients[i]);
                        Thread write = new Thread(writeToClients[i]);
                        read.start();
                        write.start();
                    }
                }
            }
            System.out.println("CONNECTION COMPLETE");
        } catch (IOException e) {
            System.out.println("IOException from acceptConnections()");
        }
    }

    /**
     * ReadFromClient handles incoming data from a specific player, updating their state and relaying game events to the server.
     */
    private class ReadFromClient implements Runnable {
        private int playerID;
        private DataInputStream dataIn;

        /**
         * Sets up the input stream for a player and logs its creation.
         * @param playerID the ID of the player
         * @param dataIn the input stream from the player
         */
        public ReadFromClient(int playerID, DataInputStream dataIn) {
            this.playerID = playerID;
            this.dataIn = dataIn;
            System.out.println("RFC object created for Player " + playerID);
        }
    
        /**
         * Continuously reads data from the client, updating player and mob states on the server.
         */
        @Override
        public void run() {
            try {
                while (true) {
                    boolean lostStatus = dataIn.readBoolean();
                    if (lostStatus) {
                        lostStatus = true;
                        teamLost = true;
                    }
                    playerCoordinates[playerID - 1][0] = dataIn.readInt();
                    playerCoordinates[playerID - 1][1] = dataIn.readInt();
                    direction[playerID - 1] = dataIn.readUTF();
                    spriteNums[playerID - 1] = dataIn.readInt();
                    idleNum[playerID - 1] = dataIn.readInt();
                    collisionPlayer[playerID - 1] = dataIn.readBoolean();
                    playerStamina[playerID - 1] = dataIn.readInt();
                    int itemIndex = dataIn.readInt();
                    boolean reset = dataIn.readBoolean();
                    int revivePlayer = dataIn.readInt();
                    revivePlayerIndex = revivePlayer;
                    playerHealth[playerID - 1] = dataIn.readInt(); // Read health
                    playerNoDamage[playerID - 1] = dataIn.readBoolean(); // Read noDamage
                    playerAttacking[playerID - 1] = dataIn.readBoolean();
                    playerShooting[playerID - 1] = dataIn.readBoolean();
                    mobIndex[playerID - 1] = dataIn.readInt();
                    playerAttackValue[playerID - 1] = dataIn.readInt();
                    int xp = dataIn.readInt();
                    int nextXp = dataIn.readInt();
                    int level = dataIn.readInt();
                    int maxHealth = dataIn.readInt();
                    int ammo = dataIn.readInt();

                    int currentMap = dataIn.readInt();
                    playerCurrentMap[playerID - 1] = currentMap;

                    if (itemIndex != 999) {
                        itemIndexTracker = itemIndex;
                        if (reset) {
                            itemIndexTracker = 999;
                        }
                    }

                    if (dataIn.readBoolean()) {
                        gameOverStatus = true;
                    }

                    projectileAlive[playerID - 1] = dataIn.readBoolean();
                    shouldProjectileDamage[playerID - 1] = dataIn.readBoolean();
                    projectileCoordinates[playerID - 1][0] = dataIn.readInt();
                    projectileCoordinates[playerID - 1][1] = dataIn.readInt();
                    projectileDirection[playerID - 1] = dataIn.readUTF();
                    playerProjectileAttackValue[playerID - 1] = dataIn.readInt();

                    int mobCount = dataIn.readInt();
                    mobCounts[currentMap] = mobCount;

                    for (int i = 0; i < mobCount; i++) {
                        boolean alive = dataIn.readBoolean();
                        int mobX = dataIn.readInt();
                        int mobY = dataIn.readInt();
                        String direction = dataIn.readUTF();
                        int sprite = dataIn.readInt();
                        boolean collision = dataIn.readBoolean();
                        boolean pathFind = dataIn.readBoolean();
                        int mobHealth = dataIn.readInt();
                        boolean mobDyingState = dataIn.readBoolean();
                        boolean isMobProjectileAlive = dataIn.readBoolean();
                        int mobProjectileX = dataIn.readInt();
                        int mobProjectileY = dataIn.readInt();
                        String projectileDirectionMob = dataIn.readUTF();

                        if (playerInControl[playerID - 1]) {
                            mobs[currentMap][i][0] = mobX;
                            mobs[currentMap][i][1] = mobY;
                            mobDirection[currentMap][i] = direction;
                            mobSprite[currentMap][i] = sprite;
                            mobCollision[currentMap][i] = collision;
                            mobPathFind[currentMap][i] = pathFind;
                            mobLife[currentMap][i] = mobHealth;
                            mobAlive[currentMap][i] = alive;
                            mobDying[currentMap][i] = mobDyingState;
                            mobProjectileAlive[currentMap][i] = isMobProjectileAlive;
                            mobProjectileCoordinates[currentMap][i][0] = mobProjectileX;
                            mobProjectileCoordinates[currentMap][i][1] = mobProjectileY;
                            mobProjectileDirection[currentMap][i] = projectileDirectionMob;
                            exp = xp;
                            nextLvlExp = nextXp;
                            playerLevel = level;
                            maxLife = maxHealth;
                            maxAmmo = ammo;
                        }

                        if (!playerInControl[playerID - 1]) {
                            if (playerAttacking[playerID - 1] || projectileAlive[playerID - 1]) {
                                if (mobIndex[playerID - 1] != 999) {
                                    mobLife[currentMap][i] = mobHealth;
                                    mobAlive[currentMap][i] = alive;
                                    mobDying[currentMap][i] = mobDyingState;
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Player " + playerID + " has disconnected!");
                playerSockets[playerID - 1] = null;
                playersActive[playerID - 1] = false;
                Arrays.fill(mobProjectileAlive[playerCurrentMap[playerID - 1]], false);
                updateControl();
            }
        }
    }

    /**
     * WriteToClient sends updated game state information to a specific player.
     */
    private class WriteToClient implements Runnable {
        private int playerID;
        private DataOutputStream dataOut;

        /**
         * Sets up the output stream for a player and logs its creation.
         * @param playerID the ID of the player
         * @param dataOut the output stream to the player
         */
        public WriteToClient(int playerID, DataOutputStream dataOut) {
            this.playerID = playerID;
            this.dataOut = dataOut;
            System.out.println("WTC object created for Player " + playerID);
        }

        /**
         * Continuously sends the latest game state to the client, including player and mob data.
         */
        @Override
        public void run() {
            try {
                while (true) {
                    for (int i = 0; i < maxPlayers; i++) {
                        dataOut.writeInt(i + 1);
                        dataOut.writeBoolean(playerInControl[playerID - 1]);
                        dataOut.writeBoolean(teamLost);
                        dataOut.writeInt(playersActive.length);
                        for (int j = 0; j < playersActive.length; j++) {
                            dataOut.writeBoolean(playersActive[j]);
                        }

                        dataOut.writeInt(playerCoordinates[i][0]);
                        dataOut.writeInt(playerCoordinates[i][1]);

                        dataOut.writeUTF(direction[i]);
                        dataOut.writeInt(spriteNums[i]);
                        dataOut.writeInt(idleNum[i]);
                        dataOut.writeInt(playerStamina[i]);
                        dataOut.writeBoolean(collisionPlayer[i]);

                        dataOut.writeInt(playerHealth[i]); // Send health
                        dataOut.writeBoolean(playerNoDamage[i]); // Send noDamage
                        dataOut.writeBoolean(playerAttacking[i]);
                        dataOut.writeBoolean(playerShooting[i]);
                        dataOut.writeInt(mobIndex[i]);
                        dataOut.writeInt(playerAttackValue[i]);
                        dataOut.writeInt(exp);
                        dataOut.writeInt(nextLvlExp);
                        dataOut.writeInt(playerLevel);
                        dataOut.writeInt(maxLife);
                        dataOut.writeInt(maxAmmo);

                        dataOut.writeInt(playerCurrentMap[i]);
                        dataOut.writeInt(itemIndexTracker);
                        dataOut.writeBoolean(gameOverStatus);
                        dataOut.writeInt(revivePlayerIndex);

                        if (revivePlayerIndex == i && revivePlayerIndex <= playerHealth.length && revivePlayerIndex >= 0) {
                            playerHealth[i] = 10;
                            revivePlayerIndex = -1;
                        }

                        dataOut.writeBoolean(projectileAlive[i]);
                        dataOut.writeBoolean(shouldProjectileDamage[i]);
                        dataOut.writeInt(projectileCoordinates[i][0]);
                        dataOut.writeInt(projectileCoordinates[i][1]);
                        dataOut.writeUTF(projectileDirection[i]);
                        dataOut.writeInt(playerProjectileAttackValue[i]);

                        int currentPlayerMap = playerCurrentMap[i];

                        dataOut.writeInt(mobCounts[currentPlayerMap]);
                        for (int j = 0; j < mobCounts[currentPlayerMap]; j++) {
                            dataOut.writeBoolean(mobAlive[currentPlayerMap][j]);
                            dataOut.writeInt(mobs[currentPlayerMap][j][0]);
                            dataOut.writeInt(mobs[currentPlayerMap][j][1]);
                            dataOut.writeUTF(mobDirection[currentPlayerMap][j]);
                            dataOut.writeInt(mobSprite[currentPlayerMap][j]);
                            dataOut.writeBoolean(mobCollision[currentPlayerMap][j]);
                            dataOut.writeBoolean(mobPathFind[currentPlayerMap][j]);
                            dataOut.writeInt(mobLife[currentPlayerMap][j]);
                            dataOut.writeBoolean(mobDying[currentPlayerMap][j]);

                            dataOut.writeBoolean(mobProjectileAlive[currentPlayerMap][j]);
                            dataOut.writeInt(mobProjectileCoordinates[currentPlayerMap][j][0]);
                            dataOut.writeInt(mobProjectileCoordinates[currentPlayerMap][j][1]);
                            dataOut.writeUTF(mobProjectileDirection[currentPlayerMap][j]);
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
                System.out.println("Player " + playerID + " has disconnected!");
                playerSockets[playerID - 1] = null;
                playersActive[playerID - 1] = false;
                Arrays.fill(mobProjectileAlive[playerCurrentMap[playerID - 1]], false);
                updateControl();
            }
        }

        /**
         * Sends a message to the client indicating that all players have connected and the game can start.
         */
        public void sendStartMsg() {
            try {
                dataOut.writeUTF("All players connected!");
                dataOut.writeInt(maxPlayers);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * The main method starts the server, sets the number of players, and begins accepting connections.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Enter number of players (1 - 5):");
        Scanner scanner = new Scanner(System.in);
        GameServer.maxPlayers = scanner.nextInt();
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}