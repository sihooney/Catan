package board;

public class Vertex extends Point {

    public boolean occupied;

    public Vertex(int row, int col) {
        super(row, col);
        occupied = false;
    }

    public Point getPoint() {
        return new Point(row, col);
    }
}
