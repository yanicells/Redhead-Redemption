/**
Node represents a single cell in the pathfinding grid, storing its position, cost values, and state flags.
Each node keeps track of its parent for path reconstruction and whether it is solid or has been checked during the search.
  
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

public class Node {
    protected Node parent;
    protected  int col;
    protected  int row;
    protected int gCost;
    protected int hCost;
    protected int fCost;
    protected boolean solid;
    protected boolean open;
    protected boolean checked;

    /**
     * Creates a Node at the specified column and row in the grid.
     * @param col the column index of the node
     * @param row the row index of the node
     */
    public Node(int col, int row) {
        this.col = col;
        this.row = row;
    }
}
