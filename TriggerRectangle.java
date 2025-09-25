/**
TriggerRectangle is a rectangle used for event triggering in the game world. It keeps track of its default position and whether its associated event has already occurred.

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

public class TriggerRectangle extends Rectangle {
    protected int triggerRectangleDefaultX, triggerRectangleDefaultY;
    protected boolean eventDone;

    /**
     * Creates a new TriggerRectangle and marks its event as false.
     */
    public TriggerRectangle() {
        eventDone = false;
    }
}
