package board;

/**
 * Models a vertex on the board
 */
public class Vertex extends Point {

    protected boolean occupied; // If there is a building on the vertex

    /**
     * Create an unoccupied vertex
     *
     * @param row Row coordinate
     * @param col Column coordinate
     */
    public Vertex(int row, int col) {
        super(row, col);
        occupied = false;
    }

    /**
     * Get if the vertex is occupied
     *
     * @return {@code true} if occupied, {@code false} otherwise
     */
    public boolean isOccupied() {
        return occupied;
    }
}
