package game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CatanBoard extends JPanel {

    private final int HEX_SIZE = 40; // radius of the hexagons
    private final int BOARD_SIZE = 5; // size of the board, 5 represents a 5x5 board (center plus surrounding hexes)

    public CatanBoard() {
        this.setPreferredSize(new Dimension(600, 600));
    }

    // Method to get the coordinates of each hexagon
    private List<Point> generateHexagonPoints(int x, int y, int size) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i);
            int px = (int) (x + size * Math.cos(angle));
            int py = (int) (y + size * Math.sin(angle));
            points.add(new Point(px, py));
        }
        return points;
    }

    // Method to draw hexagonal tiles
    private void drawHexagon(Graphics g, int x, int y, int size) {
        List<Point> points = generateHexagonPoints(x, y, size);
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            xPoints[i] = points.get(i).x;
            yPoints[i] = points.get(i).y;
        }
        g.fillPolygon(xPoints, yPoints, 6);
    }

    // Method to draw the entire Catan board
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.YELLOW); // Set color for hexagons

        int offsetX = 100; // Horizontal offset to center the board
        int offsetY = 100; // Vertical offset to center the board
        int spacing = HEX_SIZE * 2; // Space between hexagons

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int x = offsetX + j * spacing + (i % 2) * (spacing / 2);
                int y = offsetY + i * (int)(Math.sqrt(3) * HEX_SIZE); // Adjust for vertical offset
                drawHexagon(g, x, y, HEX_SIZE);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Catan Board");
        CatanBoard panel = new CatanBoard();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
