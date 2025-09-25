/**
Border draws a lighting effect around the player, creating a visible area and darkening the rest of the screen.
This class is used to add atmosphere and focus to the player's surroundings by masking the edges of the game window.
 
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
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class Border {
    GameCanvas gameCanvas;
    int size;
  
    /**
     * Sets up the Border with a reference to the game canvas and the size of the visible area.
     * @param gameCanvas the main game canvas reference
     * @param size the diameter of the visible circle around the player
     */
    public Border(GameCanvas gameCanvas, int size) {
        this.gameCanvas = gameCanvas;
        this.size = size;
    }

    /**
     * Draws the lighting border effect, keeping the area around the player visible and darkening the rest.
     * @param g2d the graphics context to draw on
     */
    public void draw(Graphics2D g2d) {
        BufferedImage lighting = new BufferedImage(gameCanvas.screenWidth, gameCanvas.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D lg2d = lighting.createGraphics();

        Area screenArea = new Area(new Rectangle2D.Double(0, 0, gameCanvas.screenWidth, gameCanvas.screenHeight));
        int centerX = gameCanvas.player.screenX + (gameCanvas.tileSize / 2);
        int centerY = gameCanvas.player.screenY + (gameCanvas.tileSize / 2);

        double x = centerX - size / 2.0;
        double y = centerY - size / 2.0;

        Shape circleShape = new Ellipse2D.Double(x, y, size, size);
        Area lightArea = new Area(circleShape);
        screenArea.subtract(lightArea);

        Color[] color = new Color[5];
        float[] fraction = new float[5];

        color[0] = new Color(0, 0, 0, 0f);
        color[1] = new Color(0, 0, 0, 0.25f);
        color[2] = new Color(0, 0, 0, 0.50f);
        color[3] = new Color(0, 0, 0, 0.75f);
        color[4] = new Color(0, 0, 0, 0.95f);

        fraction[0] = 0f;
        fraction[1] = 0.25f;
        fraction[2] = 0.5f;
        fraction[3] = 0.75f;
        fraction[4] = 1f;

        RadialGradientPaint gradientCircle = new RadialGradientPaint(centerX, centerY, (size / 2), fraction, color);

        lg2d.setPaint(gradientCircle);
        lg2d.fill(lightArea);
        lg2d.fill(screenArea);

        lg2d.dispose();

        g2d.drawImage(lighting, 0, 0, null);
    }
}