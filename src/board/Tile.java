package board;

/**
 * Models a hexagonal board tile
 */
public class Tile extends Point {

    private int num; // Number on tile
    private boolean robber; // If the robber occupies the tile
    private final int type; // Resource the tile generates, or desert

    /**
     * Create a new Tile unoccupied by the robber
     *
     * @param type Type of tile
     */
    public Tile(int type) {
        super();
        num = -1;
        this.type = type;
        robber = false;
    }

    /**
     * Creates a Tile with optional robber occupation
     *
     * @param type   Type of tile
     * @param robber {@code true} if has robber, {@code false} otherwise
     */
    public Tile(int type, boolean robber) {
        super();
        num = -1;
        this.type = type;
        this.robber = robber;
    }

    /**
     * Get the tile's type
     *
     * @return Type of the tile
     */
    public int getType() {
        return type;
    }

    /**
     * Set row and column coordinates of the tile
     *
     * @param p Point object containing the row and column coordinates
     */
    public void setPosition(Point p) {
        this.row = p.row;
        this.col = p.col;
    }

    /**
     * Get the number on the tile
     *
     * @return Number on the tile
     */
    public int getNum() {
        return num;
    }

    /**
     * Set the number on the tile
     *
     * @param num Number on the tile
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * Check if the robber is not on the tile
     *
     * @return {@code true} if robber not on tile, {@code false} otherwise
     */
    public boolean notRobber() {
        return !robber;
    }

    /**
     * Set if the robber is on the tile or not
     *
     * @param hasRobber {@code true} if robber on tile, {@code false} otherwise
     */
    public void setRobber(boolean hasRobber) {
        robber = hasRobber;
    }

    /**
     * Get the 6 vertices of the tile
     *
     * @return An array containing the 6 Vertex objects of the tile's vertices
     */
    public Vertex[] getVertices() {
        return new Vertex[]{new Vertex(row, col), new Vertex(row + 1, col - 1), new Vertex(row + 1, col + 1),
                new Vertex(row + 2, col - 1), new Vertex(row + 2, col + 1), new Vertex(row + 3, col)};
    }
}
