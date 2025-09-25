/**
PathFinder handles pathfinding logic for moving characters or mobs around obstacles in the game world.
It uses a grid of nodes and a simple A* algorithm to find the shortest path between two points on the map.
 
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

public class PathFinder {
    protected GameCanvas gameCanvas;
    protected Node[][] nodes;
    protected ArrayList<Node> openList = new ArrayList<>();
    protected ArrayList<Node> pathList = new ArrayList<>();
    protected Node startNode, goalNode, currentNode;
    protected boolean goalReached;
    protected int step = 0;

    /**
     * Sets up the PathFinder with a reference to the game canvas and initializes the node grid.
     * @param gameCanvas the main game canvas reference
     */
    public PathFinder(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;
        setUpPathFinder();
    }

   /**
     * Initializes the node grid for the entire map, creating a Node for each tile.
     */
    protected void setUpPathFinder() {
        nodes = new Node[gameCanvas.maxWorldCol][gameCanvas.maxWorldRow];

        int col = 0;
        int row = 0;

        while (col < gameCanvas.maxWorldCol && row < gameCanvas.maxWorldRow) {
            nodes[col][row] = new Node(col, row);

            col++;
            if (col == gameCanvas.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Resets all nodes and clears the open and path lists, preparing for a new pathfinding operation.
     */
    protected void reset() {

        if (nodes == null || nodes.length == 0) {
            setUpPathFinder();
            return;
        }

        int col = 0;
        int row = 0;

        while (col < gameCanvas.maxWorldCol && row < gameCanvas.maxWorldRow) {
            nodes[col][row].open = false;
            nodes[col][row].checked = false;
            nodes[col][row].solid = false;

            col++;
            if (col == gameCanvas.maxWorldCol) {
                col = 0;
                row++;
            }
        }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    /**
     * Sets the start and goal nodes for the pathfinding search and calculates costs for each node.
     * @param startCol the starting column
     * @param startRow the starting row
     * @param goalCol the goal column
     * @param goalRow the goal row
     */
    protected void setNode(int startCol, int startRow, int goalCol, int goalRow) {
        reset();

        startNode = nodes[startCol][startRow];
        currentNode = startNode;
        goalNode = nodes[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while (col < gameCanvas.maxWorldCol && row < gameCanvas.maxWorldRow) {

            int tileNum = gameCanvas.tileManager.mapTileNum[gameCanvas.currentMap][col][row];
            if (gameCanvas.tileManager.tiles.get(tileNum).collision) {
                nodes[col][row].solid = true;
            }
            getCost(nodes[col][row]);

            col++;
            if (col == gameCanvas.maxWorldCol) {
                col = 0;
                row++;
            }

        }
    }

    /**
     * Calculates the movement and heuristic costs for a given node.
     * @param node the node whose costs will be calculated
     */
    private void getCost(Node node) {
        // G cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;
        // H cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;
        // F cost
        node.fCost = node.gCost + node.hCost;
    }

   /**
     * Runs the pathfinding search and returns true if a path to the goal was found.
     * @return true if the goal was reached, false otherwise
     */
    protected boolean search() {
        while (!goalReached && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.checked = true;
            openList.remove(currentNode);

            // Open the Up node
            if (row - 1 >= 0){
                openNode(nodes[col][row - 1]);
            }
            // Open the left node
            if (col - 1 >= 0){
                openNode(nodes[col - 1][row]);
            }
            // Open the down node
            if (row + 1 < gameCanvas.maxWorldRow) {
                openNode(nodes[col][row + 1]);
            }
            // Open the right node
            if (col + 1 < gameCanvas.maxWorldCol) {
                openNode(nodes[col + 1][row]);
            }

            // Find best node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openList.size(); i++){
                if(openList.get(i).fCost < bestNodefCost){
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }else if(openList.get(i).fCost == bestNodefCost){
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }
            }

            if(openList.size() == 0){
                break;
            }

            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode){
                goalReached = true;
                trackPath();
            }
            step++;
        }

        return goalReached;
    }


    /**
     * Traces back from the goal node to the start node to build the final path.
     */
    protected void trackPath(){
        Node current = goalNode;

        while (current != startNode){
            pathList.add(0, current);
            current = current.parent;
        }
    }

    /**
     * Opens a node for consideration if it is not already open, checked, or solid.
     * @param node the node to open
     */
    protected void openNode(Node node){
        if(!node.open && !node.checked && !node.solid){
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
}
