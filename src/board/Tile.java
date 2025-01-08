package board;

public class Tile extends Point {

    private int num;
    private boolean robber;
    private final String type;

    public Tile(String type) {
        super();
        num = -1;
        this.type = type;
        robber = false;
    }

    public Tile(String type, boolean robber) {
        super();
        num = -1;
        this.type = type;
        this.robber = robber;
    }

    public void setPosition(Point point) {
        this.row = point.row;
        this.col = point.col;
    }

    public boolean isRobber() {
        return robber;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "num=" + num +
                ", robber=" + robber +
                ", type='" + type + '\'' +
                ", row=" + row +
                ", col=" + col +
                '}';
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setRobber(boolean hasRobber) {
        robber = hasRobber;
    }

    public Point[] getVertices() {
        return new Point[]{new Point(row, col), new Point(row + 1, col - 1), new Point(row + 1, col + 1),
                new Point(row + 2, col - 1), new Point(row + 2, col + 1), new Point(row + 3, col)};
    }
}
