/**
Tile represents a single tile in the game world that holds its image and collision property.
Each tile can be drawn on the map and may or may not block player movement depending on its collision setting.

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

import java.awt.image.BufferedImage;

public class Tile {
    protected BufferedImage image;
    protected boolean collision;

    /**
     * Constructs a Tile with collision set to false by default.
     */
    public Tile() {
        collision = false;
    }
}
