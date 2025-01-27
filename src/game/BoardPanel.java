package game;

import board.*;
import constants.Resource;

import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * JPanel that displays the game board, complete with tiles, roads, settlements, cities, and the robber
 */
public class BoardPanel extends JPanel {

    private final double SIDE_LENGTH = 75; // Side length of hexagonal tile
    private final Board board; // Board object of game board
    private Point[][] centers; // Centers of each hexagonal tile

    /**
     * Creates a new BoardPanel from a Board object
     *
     * @param board A Board object
     */
    public BoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(1200, 700));
        setBackground(new Color(146, 206, 226)); // Background color is light blue
        getTileCenters(); // Precompute the points of tile centers
    }

    /**
     * Calculate the coordinates of a vertex on the panel
     *
     * @param r Row coordinate of vertex
     * @param c Column coordinate of vertex
     * @return {@code java.awt.Point} object of (x, y) coordinates of vertex on the panel
     */
    private Point calculateVertex(int r, int c) {
        // Using 30-60-90 triangle
        long x = Math.round(c * SIDE_LENGTH * Math.sqrt(3.0) / 2);
        long y = Math.round((r / 2 + r % 2) * SIDE_LENGTH / 2 + (r / 2) * SIDE_LENGTH);
        // Offset values from panel borders
        int X_OFFSET = 300;
        int Y_OFFSET = 50;
        return new Point((int) (X_OFFSET + x), (int) (Y_OFFSET + y));
    }

    /**
     * Precompute the coordinates of the tile centers on the panel
     */
    private void getTileCenters() {
        centers = new Point[12][11];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 11; j++) {
                if (board.getTiles()[i][j] != null) {
                    Point p = calculateVertex(i, j); // Calculate coordinates of top vertex
                    // Tile center is shifted down from top vertex
                    centers[i][j] = new Point(p.x, (int) (p.y + SIDE_LENGTH));
                }
            }
        }
    }

    /**
     * Draw a hexagonal tile
     *
     * @param g    {@code Graphics} object
     * @param r    Row coordinate of top vertex
     * @param c    Column coordinate of top vertex
     * @param type Type of tile
     */
    private void drawHexagon(Graphics g, int r, int c, int type) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            // Basic trigonometry
            double angle = Math.toRadians(60 * i - 30);
            int px = (int) (centers[r][c].x + SIDE_LENGTH * Math.cos(angle));
            int py = (int) (centers[r][c].y + SIDE_LENGTH * Math.sin(angle));
            xPoints[i] = px;
            yPoints[i] = py;
        }
        g.setColor(Resource.COLORS[type]); // Set color depending on tile type
        g.fillPolygon(xPoints, yPoints, 6); // Draw hexagonal polygon
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 6); // Draw tile outline
    }

    /**
     * Draw a road connecting two vertices on the panel
     *
     * @param g2d   {@code Graphics2D} object
     * @param r1    Row coordinate of one vertex
     * @param c1    Column coordinate of one vertex
     * @param r2    Row coordinate of another vertex
     * @param c2    Column coordinate of another vertex
     * @param color Color of road
     */
    private void drawRoad(Graphics2D g2d, int r1, int c1, int r2, int c2, Color color) {
        Point p1 = calculateVertex(r1, c1);
        Point p2 = calculateVertex(r2, c2);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(10)); // Draw outline of road
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(7)); // Fill in the road with specified color
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    /**
     * Draw a settlement on a vertex on the panel
     *
     * @param g     {@code Graphics} object
     * @param r     Row coordinate of settlement
     * @param c     Column coordinate of settlement
     * @param color Color of settlement
     */
    private void drawSettlement(Graphics g, int r, int c, Color color) {
        Point p = calculateVertex(r, c);
        int x = p.x;
        int y = p.y;
        int size = 15;
        int[] xPoints = {x - size, x, x + size, x + size, x - size};
        int[] yPoints = {y, y - size, y, y + size, y + size};
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, 5); // Draw polygon shape of settlement with specified color
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 5); // Draw outline of settlement
    }

    /**
     * Draw a city on a vertex on the panel
     *
     * @param g     {@code Graphics} object
     * @param r     Row coordinate of city
     * @param c     Column coordinate of city
     * @param color Color of city
     */
    private void drawCity(Graphics g, int r, int c, Color color) {
        Point p = calculateVertex(r, c);
        int x = p.x;
        int y = p.y;
        int size = 15;
        int[] xPoints = {x - 2 * size, x - size, x, x + size, x + size, x - 2 * size};
        int[] yPoints = {y, y - size, y, y, y + size, y + size};
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, 6); // Draw polygon shape of city with specified color
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 6); // Draw outline of city
    }

    /**
     * Draw a number on a tile and indicate if robber is on the tile
     *
     * @param g {@code Graphics} object
     * @param r Row coordinate of tile
     * @param c Column coordinate of tile
     */
    private void drawNum(Graphics g, int r, int c) {
        g.setColor(Color.BLUE);
        g.setFont(new Font(null, Font.PLAIN, 20));
        // If the tile is desert, then do not draw the number
        String s = board.getTiles()[r][c].getNum() == -1 ? "" : String.valueOf(board.getTiles()[r][c].getNum());
        if (r == board.getRobberLoc().getRow() && c == board.getRobberLoc().getCol()) {
            s += " (R)"; // Indicate if the robber is on the tile
        }
        g.drawString(s, centers[r][c].x, centers[r][c].y); // Draw the string in tile center
    }

    /**
     * Paint the board panel
     *
     * @param g the <code>Graphics</code> object to protect
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 11; j++) {
                if (centers[i][j] != null) {
                    drawHexagon(g, i, j, board.getTiles()[i][j].getType()); // Draw hexagonal tile
                    drawNum(g, i, j); // Draw number and/or robber
                }
                if (board.getVertices()[i][j] != null && board.getVertices()[i][j].isOccupied()) {
                    Building b = (Building) board.getVertices()[i][j];
                    if (b.getAmtCollect() == 1) {
                        drawSettlement(g, i, j, b.getOwner().getColor()); // Draw settlement
                    } else {
                        drawCity(g, i, j, b.getOwner().getColor()); // Draw city
                    }
                }
            }
        }
        Graphics2D g2d = (Graphics2D) g;
        for (HashMap<Edge, Player> map : board.getGraph().values()) {
            for (Map.Entry<Edge, Player> entry : map.entrySet()) {
                if (entry.getValue() != null) {
                    Vertex u = entry.getKey().u();
                    Vertex v = entry.getKey().v();
                    Color c = entry.getValue().getColor();
                    drawRoad(g2d, u.getRow(), u.getCol(), v.getRow(), v.getCol(), c); // Draw road
                }
            }
        }
    }
}
