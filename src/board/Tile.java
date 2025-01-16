package board;

public class Tile extends Point {

    private int num;
    private boolean robber;
    private final int type;

    public Tile(int type) {
        super();
        num = -1;
        this.type = type;
        robber = false;
    }

    public Tile(int type, boolean robber) {
        super();
        num = -1;
        this.type = type;
        this.robber = robber;
    }

    public int getType() {
        return type;
    }

    public void setPosition(Point p) {
        this.row = p.row;
        this.col = p.col;
    }

    public int getNum() {
        return num;
    }

    public boolean isRobber() {
        return robber;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setRobber(boolean hasRobber) {
        robber = hasRobber;
    }

    public Vertex[] getVertices() {
        return new Vertex[]{new Vertex(row, col), new Vertex(row + 1, col - 1), new Vertex(row + 1, col + 1),
                new Vertex(row + 2, col - 1), new Vertex(row + 2, col + 1), new Vertex(row + 3, col)};
    }
}
