/**
Drawable is an abstract base class for anything that can be drawn on the game screen, such as items, characters, or projectiles.
It provides utility methods for scaling and loading images, and requires subclasses to implement their own draw logic.
 
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

public abstract class Drawable {
    protected int worldX, worldY;

    /**
     * Draws the object on the screen using the provided graphics context and game canvas.
     * @param g2d the graphics context to draw on
     * @param gameCanvas the main game canvas reference
     */
    public abstract void draw(Graphics2D g2d, GameCanvas gameCanvas);

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
