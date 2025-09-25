/**
TileManager handles all the tiles in the game, including loading their images,
setting up their properties, and drawing them on the screen as the player moves around. It also manages the map data for each world.

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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class TileManager {

    protected GameCanvas gameCanvas;
    protected ArrayList<Tile> tiles;
    protected int mapTileNum[][][];
    protected boolean drawPath = false;

    /**
     * Initializes the TileManager, loads all tile images, and sets up the maps for each world.
     * @param gameCanvas the main game canvas reference
     */
    public TileManager(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;
        tiles = new ArrayList<>();
        mapTileNum = new int[gameCanvas.maxMapNum][gameCanvas.maxWorldCol][gameCanvas.maxWorldRow];
        getTileImage();
        loadMap("Map.txt", 0);
        loadMap("Map3.txt", 1);
        loadMap("Map2.txt", 2);
    }

    /**
     * Loads and sets up all tile images. Also assigns collision properties
     */
    public void getTileImage(){
        for (int i = 0; i<=268; i++){
            String tileName = String.format("Tile%03d", i+1);
            boolean isTrue = (i + 1 >= 25 && i +1 <= 32) || (i + 1 >= 55 && i +1 <= 84) || (i + 1 >= 87 && i +1 <= 89) || (i + 1 >= 91 && i +1 <= 94) || (i + 1 >= 98 && i +1 <= 101) || (i + 1 >= 108 && i +1 <= 114) || (i + 1 >= 118 && i +1 <= 124) || (i + 1 == 126) || (i + 1 >= 137 && i + 1 <= 138) || (i + 1 >= 149 && i + 1 <= 151) || (i + 1 >= 152 && i +1 <= 155) || (i + 1 >= 157 && i +1 <= 163) || (i + 1 >= 166 && i +1 <= 174) || (i + 1 >= 176 && i +1 <= 187) || (i == 189) || (i + 1 >= 209 && i +1 <= 211)|| (i + 1 == 216)|| (i + 1 == 218)|| (i + 1 >= 222 && i + 1 <= 231)|| (i + 1 >= 233 && i + 1 <= 246)|| (i + 1 >= 248 && i + 1 <= 258) || (i + 1 >= 261 && i + 1 <= 262) || (i + 1 >= 220 && i + 1 <= 221);
            setup (i, tileName, isTrue);
        }

    }

    /**
     * Scales a tile image to the desired width and height.
     * @param original the original image
     * @param width the target width
     * @param height the target height
     * @return the scaled BufferedImage
     */
    private BufferedImage scaleImage (BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage (width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return scaledImage;
    }

    /**
     * Sets up a single tile by loading its image and assigning its collision property.
     * @param index the tile index
     * @param imagePath the image file path (without extension)
     * @param collision whether this tile should block movement
     */
    public void setup(int index, String imagePath, boolean collision) {
        try {
            tiles.add(new Tile());
            tiles.get(index).image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            tiles.get(index).image = scaleImage(tiles.get(index).image, GameCanvas.tileSize, GameCanvas.tileSize);
            tiles.get(index).collision = collision;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Draws all visible tiles on the screen based on the player's current position.
     * @param g2d graphics to draw on
     */
    public void draw(Graphics2D g2d){
        int col = 0;
        int row = 0;

        int playerScaledSize = (int)(GameCanvas.tileSize * gameCanvas.player.SCALE);

        while (col < gameCanvas.maxWorldCol && row < gameCanvas.maxWorldRow) {
            int tileNum = mapTileNum[gameCanvas.currentMap][col][row];

            int worldX = col * gameCanvas.tileSize;
            int worldY = row * gameCanvas.tileSize;
            int screenX = worldX - gameCanvas.player.worldX + gameCanvas.player.screenX;
            int screenY = worldY - gameCanvas.player.worldY + gameCanvas.player.screenY;

            if (worldX + gameCanvas.tileSize > gameCanvas.player.worldX - gameCanvas.player.screenX
                    && worldX - gameCanvas.tileSize < gameCanvas.player.worldX + gameCanvas.player.screenX + (playerScaledSize - GameCanvas.tileSize)
                    && worldY + gameCanvas.tileSize > gameCanvas.player.worldY - gameCanvas.player.screenY
                    && worldY - gameCanvas.tileSize < gameCanvas.player.worldY + gameCanvas.player.screenY + (playerScaledSize - GameCanvas.tileSize)) {
                g2d.drawImage(tiles.get(tileNum).image, screenX, screenY, null);
            }

            col++;

            if(col == gameCanvas.maxWorldCol){
                col = 0;
                row++;
            }
        }

        if(drawPath){
            g2d.setColor(new Color(255, 0,0, 70));
            for(int i = 0; i < gameCanvas.pathFinder.pathList.size(); i++){
                int worldX = gameCanvas.pathFinder.pathList.get(i).col * GameCanvas.tileSize;
                int worldY = gameCanvas.pathFinder.pathList.get(i).row * GameCanvas.tileSize;
                int screenX = worldX - gameCanvas.player.worldX + gameCanvas.player.screenX;
                int screenY = worldY - gameCanvas.player.worldY + gameCanvas.player.screenY;

                g2d.fillRect(screenX, screenY, GameCanvas.tileSize, GameCanvas.tileSize);
            }
        }
    }

    /**
     * Loads a map from a text file and fills the map data for the specified map index.
     * @param filePath the path to the map text file
     * @param map the map index to load into
     */
    public void loadMap(String filePath, int map) {
    try {
        InputStream is = getClass().getResourceAsStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        for (int row = 0; row < gameCanvas.maxWorldRow; row++) {
            String line = br.readLine();
            if (line == null) break;

            String[] numbers = line.trim().split("\\s+");

            for (int col = 0; col < gameCanvas.maxWorldCol; col++) {
                if (col < numbers.length) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[map][col][row] = num;
                } else {
                    mapTileNum[map][col][row] = 0;
                }
            }
        }

        br.close();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
}
