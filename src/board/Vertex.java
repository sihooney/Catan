package board;

public class Vertex extends Point {

    protected boolean empty;

    public Vertex(int row, int col) {
        super(row, col);
        empty = true;
    }
}
