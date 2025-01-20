package board;

import java.util.Objects;

/**
 * Base class that models an ordered pair of (row, column) coordinates
 */
public class Point {

    protected int row;
    protected int col;

    /**
     * Create a new Point with row and column initialized to -1
     */
    public Point() {
        row = -1;
        col = -1;
    }

    /**
     * Create a new Point
     *
     * @param row Row coordinate
     * @param col Column coordinate
     */
    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point that = (Point) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
