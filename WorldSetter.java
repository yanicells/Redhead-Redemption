/**
WorldSetter is responsible for placing all items and mobs into the world
at the start of the game. It handles the initial
setup of medkits, keys, doors, fuel, and all mob types for each map.
 
This class ensures that every map is filled with the correct objects and
enemies at their locations, and respawn mobs.

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

public class WorldSetter {
    protected GameCanvas gameCanvas;

    /**
     * Constructs WorldSetter with a reference to the main GameCanvas.
     * This allows the WorldSetter to access and modify the game's item and mob lists.
     * 
     * @param gameCanvas the main game canvas where items and mobs will be placed
     */
    public WorldSetter(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;
    }

    /**
     * Populates the game world with items such as medkits, keys, doors, and fuel.
     */
    public void setObject(){

        int mapNum = 0;

        gameCanvas.items.get(mapNum).add(0, new Medkit(gameCanvas));
        gameCanvas.items.get(mapNum).get(0).worldX = 46 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(0).worldY = 51 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(1, new Medkit(gameCanvas));
        gameCanvas.items.get(mapNum).get(1).worldX = 52 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(1).worldY = 64 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(2, new Medkit(gameCanvas));
        gameCanvas.items.get(mapNum).get(2).worldX = 70 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(2).worldY = 54 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(3, new Medkit(gameCanvas));
        gameCanvas.items.get(mapNum).get(3).worldX = 59 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(3).worldY = 47 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(4, new Key());
        gameCanvas.items.get(mapNum).get(4).worldX = 64 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(4).worldY = 34 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(5, new Key());
        gameCanvas.items.get(mapNum).get(5).worldX = 37 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(5).worldY = 34 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(6, new Key());
        gameCanvas.items.get(mapNum).get(6).worldX = 66 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(6).worldY = 65 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(7, new Door()); //The real door
        gameCanvas.items.get(mapNum).get(7).worldX = 66 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(7).worldY = 27 * GameCanvas.tileSize;


        mapNum = 1;
        gameCanvas.items.get(mapNum).add(0, new Medkit(gameCanvas));
        gameCanvas.items.get(mapNum).get(0).worldX = 58 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(0).worldY = 20 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(1, new Medkit(gameCanvas));
        gameCanvas.items.get(mapNum).get(1).worldX = 54 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(1).worldY = 15 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(2, new Medkit(gameCanvas));
        gameCanvas.items.get(mapNum).get(2).worldX = 49 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(2).worldY = 38 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(3, new Medkit(gameCanvas));
        gameCanvas.items.get(mapNum).get(3).worldX = 56 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(3).worldY = 50 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(4, new Fuel());
        gameCanvas.items.get(mapNum).get(4).worldX = 49 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(4).worldY = 61 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(5, new Fuel());
        gameCanvas.items.get(mapNum).get(5).worldX = 56 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(5).worldY = 43 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(6, new Fuel());
        gameCanvas.items.get(mapNum).get(6).worldX = 49 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(6).worldY = 34 * GameCanvas.tileSize;

        mapNum = 2;
        gameCanvas.items.get(mapNum).add(0, new Medkit(gameCanvas));
        gameCanvas.items.get(mapNum).get(0).worldX = 38 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(0).worldY = 33 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(1, new Medkit(gameCanvas));
        gameCanvas.items.get(mapNum).get(1).worldX = 34 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(1).worldY = 44 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(2, new Medkit(gameCanvas));
        gameCanvas.items.get(mapNum).get(2).worldX = 57 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(2).worldY = 37 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(3, new Medkit(gameCanvas));
        gameCanvas.items.get(mapNum).get(3).worldX = 47 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(3).worldY = 46 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(4, new Fuel());
        gameCanvas.items.get(mapNum).get(4).worldX = 62 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(4).worldY = 55 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(5, new Fuel());
        gameCanvas.items.get(mapNum).get(5).worldX = 34 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(5).worldY = 28 * GameCanvas.tileSize;

        gameCanvas.items.get(mapNum).add(6, null);

        gameCanvas.items.get(mapNum).add(7, new Fuel());
        gameCanvas.items.get(mapNum).get(7).worldX = 57 * GameCanvas.tileSize;
        gameCanvas.items.get(mapNum).get(7).worldY = 32 * GameCanvas.tileSize;
    }

    /**
     * Places all mobs into the game world for each map.
     */
    public void setMobs(){
        int mapNum = 0;

        gameCanvas.mobs.get(mapNum).add(0, new PassiveMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(0).worldX = GameCanvas.tileSize * 49;
        gameCanvas.mobs.get(mapNum).get(0).worldY = GameCanvas.tileSize * 66;

        gameCanvas.mobs.get(mapNum).add(1, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(1).worldX = GameCanvas.tileSize * 56;
        gameCanvas.mobs.get(mapNum).get(1).worldY = GameCanvas.tileSize * 67;

        gameCanvas.mobs.get(mapNum).add(2, new PassiveMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(2).worldX = GameCanvas.tileSize * 44;
        gameCanvas.mobs.get(mapNum).get(2).worldY = GameCanvas.tileSize * 57;

        gameCanvas.mobs.get(mapNum).add(3, new PassiveMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(3).worldX = GameCanvas.tileSize * 31;
        gameCanvas.mobs.get(mapNum).get(3).worldY = GameCanvas.tileSize * 53;

        gameCanvas.mobs.get(mapNum).add(4, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(4).worldX = GameCanvas.tileSize * 43;
        gameCanvas.mobs.get(mapNum).get(4).worldY = GameCanvas.tileSize * 37;

        gameCanvas.mobs.get(mapNum).add(5, new PassiveMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(5).worldX = GameCanvas.tileSize * 50;
        gameCanvas.mobs.get(mapNum).get(5).worldY = GameCanvas.tileSize * 46;

        gameCanvas.mobs.get(mapNum).add(6, new PassiveMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(6).worldX = GameCanvas.tileSize * 51;
        gameCanvas.mobs.get(mapNum).get(6).worldY = GameCanvas.tileSize * 60;

        gameCanvas.mobs.get(mapNum).add(7, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(7).worldX = GameCanvas.tileSize * 60;
        gameCanvas.mobs.get(mapNum).get(7).worldY = GameCanvas.tileSize * 37;

        gameCanvas.mobs.get(mapNum).add(8, new PassiveMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(8).worldX = GameCanvas.tileSize * 69;
        gameCanvas.mobs.get(mapNum).get(8).worldY = GameCanvas.tileSize * 34;

        gameCanvas.mobs.get(mapNum).add(9, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(9).worldX = GameCanvas.tileSize * 43;
        gameCanvas.mobs.get(mapNum).get(9).worldY = GameCanvas.tileSize * 58;

        gameCanvas.mobs.get(mapNum).add(10, new PassiveMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(10).worldX = GameCanvas.tileSize * 69;
        gameCanvas.mobs.get(mapNum).get(10).worldY = GameCanvas.tileSize * 46;

        gameCanvas.mobs.get(mapNum).add(11, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(11).worldX = GameCanvas.tileSize * 60;
        gameCanvas.mobs.get(mapNum).get(11).worldY = GameCanvas.tileSize * 59;

        gameCanvas.mobs.get(mapNum).add(12, new PassiveMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(12).worldX = GameCanvas.tileSize * 54;
        gameCanvas.mobs.get(mapNum).get(12).worldY = GameCanvas.tileSize * 54;

        mapNum = 1;
        gameCanvas.mobs.get(mapNum).add(0, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(0).worldX = GameCanvas.tileSize * 53;
        gameCanvas.mobs.get(mapNum).get(0).worldY = GameCanvas.tileSize * 61;

        gameCanvas.mobs.get(mapNum).add(1, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(1).worldX = GameCanvas.tileSize * 49;
        gameCanvas.mobs.get(mapNum).get(1).worldY = GameCanvas.tileSize * 56;

        gameCanvas.mobs.get(mapNum).add(2, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(2).worldX = GameCanvas.tileSize * 54;
        gameCanvas.mobs.get(mapNum).get(2).worldY = GameCanvas.tileSize * 52;

        gameCanvas.mobs.get(mapNum).add(3, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(3).worldX = GameCanvas.tileSize * 50;
        gameCanvas.mobs.get(mapNum).get(3).worldY = GameCanvas.tileSize * 48;

        gameCanvas.mobs.get(mapNum).add(4, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(4).worldX = GameCanvas.tileSize * 55;
        gameCanvas.mobs.get(mapNum).get(4).worldY = GameCanvas.tileSize * 40;

        gameCanvas.mobs.get(mapNum).add(5, new PoisonMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(5).worldX = GameCanvas.tileSize * 51;
        gameCanvas.mobs.get(mapNum).get(5).worldY = GameCanvas.tileSize * 30;

        gameCanvas.mobs.get(mapNum).add(6, new PassiveMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(6).worldX = GameCanvas.tileSize * 50;
        gameCanvas.mobs.get(mapNum).get(6).worldY = GameCanvas.tileSize * 25;

        gameCanvas.mobs.get(mapNum).add(7, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(7).worldX = GameCanvas.tileSize * 49;
        gameCanvas.mobs.get(mapNum).get(7).worldY = GameCanvas.tileSize * 18;

        gameCanvas.mobs.get(mapNum).add(8, new PoisonMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(8).worldX = GameCanvas.tileSize * 59;
        gameCanvas.mobs.get(mapNum).get(8).worldY = GameCanvas.tileSize * 19;

        gameCanvas.mobs.get(mapNum).add(9, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(9).worldX = GameCanvas.tileSize * 67;
        gameCanvas.mobs.get(mapNum).get(9).worldY = GameCanvas.tileSize * 21;

        gameCanvas.mobs.get(mapNum).add(10, new PoisonMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(10).worldX = GameCanvas.tileSize * 71;
        gameCanvas.mobs.get(mapNum).get(10).worldY = GameCanvas.tileSize * 18;

        gameCanvas.mobs.get(mapNum).add(11, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(11).worldX = GameCanvas.tileSize * 80;
        gameCanvas.mobs.get(mapNum).get(11).worldY = GameCanvas.tileSize * 21;

        gameCanvas.mobs.get(mapNum).add(12, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(12).worldX = GameCanvas.tileSize * 82;
        gameCanvas.mobs.get(mapNum).get(12).worldY = GameCanvas.tileSize * 17;

        gameCanvas.mobs.get(mapNum).add(13, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(13).worldX = GameCanvas.tileSize * 56;
        gameCanvas.mobs.get(mapNum).get(13).worldY = GameCanvas.tileSize * 35;

        gameCanvas.mobs.get(mapNum).add(14, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(14).worldX = GameCanvas.tileSize * 55;
        gameCanvas.mobs.get(mapNum).get(14).worldY = GameCanvas.tileSize * 46;

        gameCanvas.mobs.get(mapNum).add(15, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(15).worldX = GameCanvas.tileSize * 69;
        gameCanvas.mobs.get(mapNum).get(15).worldY = GameCanvas.tileSize * 20;

        mapNum = 2;
        gameCanvas.mobs.get(mapNum).add(0, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(0).worldX = GameCanvas.tileSize * 50;
        gameCanvas.mobs.get(mapNum).get(0).worldY = GameCanvas.tileSize * 62;

        gameCanvas.mobs.get(mapNum).add(1, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(1).worldX = GameCanvas.tileSize * 40;
        gameCanvas.mobs.get(mapNum).get(1).worldY = GameCanvas.tileSize * 60;

        gameCanvas.mobs.get(mapNum).add(2, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(2).worldX = GameCanvas.tileSize * 51;
        gameCanvas.mobs.get(mapNum).get(2).worldY = GameCanvas.tileSize * 57;

        gameCanvas.mobs.get(mapNum).add(3, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(3).worldX = GameCanvas.tileSize * 46;
        gameCanvas.mobs.get(mapNum).get(3).worldY = GameCanvas.tileSize * 54;

        gameCanvas.mobs.get(mapNum).add(4, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(4).worldX = GameCanvas.tileSize * 55;
        gameCanvas.mobs.get(mapNum).get(4).worldY = GameCanvas.tileSize * 40;

        gameCanvas.mobs.get(mapNum).add(5, new PoisonMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(5).worldX = GameCanvas.tileSize * 54;
        gameCanvas.mobs.get(mapNum).get(5).worldY = GameCanvas.tileSize * 55;

        gameCanvas.mobs.get(mapNum).add(6, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(6).worldX = GameCanvas.tileSize * 39;
        gameCanvas.mobs.get(mapNum).get(6).worldY = GameCanvas.tileSize * 40;

        gameCanvas.mobs.get(mapNum).add(7, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(7).worldX = GameCanvas.tileSize * 34;
        gameCanvas.mobs.get(mapNum).get(7).worldY = GameCanvas.tileSize * 32;

        gameCanvas.mobs.get(mapNum).add(8, new PoisonMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(8).worldX = GameCanvas.tileSize * 32;
        gameCanvas.mobs.get(mapNum).get(8).worldY = GameCanvas.tileSize * 40;

        gameCanvas.mobs.get(mapNum).add(9, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(9).worldX = GameCanvas.tileSize * 62;
        gameCanvas.mobs.get(mapNum).get(9).worldY = GameCanvas.tileSize * 38;

        gameCanvas.mobs.get(mapNum).add(10, new PoisonMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(10).worldX = GameCanvas.tileSize * 60;
        gameCanvas.mobs.get(mapNum).get(10).worldY = GameCanvas.tileSize * 46;

        gameCanvas.mobs.get(mapNum).add(11, new DormantMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(11).worldX = GameCanvas.tileSize * 43;
        gameCanvas.mobs.get(mapNum).get(11).worldY = GameCanvas.tileSize * 42;

        gameCanvas.mobs.get(mapNum).add(12, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(12).worldX = GameCanvas.tileSize * 34;
        gameCanvas.mobs.get(mapNum).get(12).worldY = GameCanvas.tileSize * 52;
    }

    /**
     * Respawns mobs by replacing existing mobs with new instances 
     */
    public void respawnMobs(){
        int mapNum = 1;
        gameCanvas.mobs.get(mapNum).set(0, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(0).worldX = GameCanvas.tileSize * 73;
        gameCanvas.mobs.get(mapNum).get(0).worldY = GameCanvas.tileSize * 16;

        gameCanvas.mobs.get(mapNum).set(1, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(1).worldX = GameCanvas.tileSize * 73;
        gameCanvas.mobs.get(mapNum).get(1).worldY = GameCanvas.tileSize * 22;

        gameCanvas.mobs.get(mapNum).set(2, new PoisonMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(2).worldX = GameCanvas.tileSize * 67;
        gameCanvas.mobs.get(mapNum).get(2).worldY = GameCanvas.tileSize * 20;

        gameCanvas.mobs.get(mapNum).set(3, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(3).worldX = GameCanvas.tileSize * 63;
        gameCanvas.mobs.get(mapNum).get(3).worldY = GameCanvas.tileSize * 17;

        gameCanvas.mobs.get(mapNum).set(4, new PoisonMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(4).worldX = GameCanvas.tileSize * 61;
        gameCanvas.mobs.get(mapNum).get(4).worldY = GameCanvas.tileSize * 21;

        gameCanvas.mobs.get(mapNum).set(5, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(5).worldX = GameCanvas.tileSize * 51;
        gameCanvas.mobs.get(mapNum).get(5).worldY = GameCanvas.tileSize * 19;

        gameCanvas.mobs.get(mapNum).set(6, new PoisonMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(6).worldX = GameCanvas.tileSize * 52;
        gameCanvas.mobs.get(mapNum).get(6).worldY = GameCanvas.tileSize * 22;

        gameCanvas.mobs.get(mapNum).set(7, new AgroMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(7).worldX = GameCanvas.tileSize * 55;
        gameCanvas.mobs.get(mapNum).get(7).worldY = GameCanvas.tileSize * 16;

        gameCanvas.mobs.get(mapNum).set(7, new PoisonMob(gameCanvas));
        gameCanvas.mobs.get(mapNum).get(7).worldX = GameCanvas.tileSize * 45;
        gameCanvas.mobs.get(mapNum).get(7).worldY = GameCanvas.tileSize * 21;
    }
}
