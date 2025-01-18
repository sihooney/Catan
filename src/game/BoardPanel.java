package game;

import board.*;
import constants.Resource;

import javax.swing.*;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

public class BoardPanel extends JPanel {

    private final double RADIUS = 75;
    public Board board;
    private Point[][] centers;

    public BoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(1200, 700));
        setBackground(Color.BLUE);
        getTileCenters();
    }

    private Point calculateVertex(int r, int c) {
        long x = Math.round(c * RADIUS * Math.sqrt(3.0) / 2);
        long y = Math.round((r / 2 + r % 2) * RADIUS / 2 + (r / 2) * RADIUS);
        int X_OFFSET = 300;
        int Y_OFFSET = 50;
        return new Point((int) (X_OFFSET + x), (int) (Y_OFFSET + y));
    }

    private void getTileCenters() {
        centers = new Point[12][11];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 11; j++) {
                if (board.tiles[i][j] != null) {
                    Point p = calculateVertex(i, j);
                    centers[i][j] = new Point(p.x, (int) (p.y + RADIUS));
                }
            }
        }
    }

    private void drawHexagon(Graphics g, int r, int c, int type) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i - 30);
            int px = (int) (centers[r][c].x + RADIUS * Math.cos(angle));
            int py = (int) (centers[r][c].y + RADIUS * Math.sin(angle));
            xPoints[i] = px;
            yPoints[i] = py;
        }
        g.setColor(Resource.COLORS[type]);
        g.fillPolygon(xPoints, yPoints, 6);
        g.setColor(Color.BLUE);
        g.drawPolygon(xPoints, yPoints, 6);
    }

    private void drawSettlement(Graphics g, int r, int c, Color color) {
        g.setColor(color);
        Point p = calculateVertex(r, c);
        int x = p.x;
        int y = p.y;
        int size = 15;
        g.fillPolygon(new int[]{x - size, x, x + size, x + size, x - size},
                new int[]{y, y - size, y, y + size, y + size}, 5);
    }

    private void drawCity(Graphics g, int r, int c, Color color) {
        g.setColor(color);
    }

    private void drawNum(Graphics g, int r, int c) {
        g.setColor(Color.WHITE);
        g.setFont(new Font(null, Font.PLAIN, 20));
        if (r == board.robberLoc.getRow() && c == board.robberLoc.getCol()) {
            g.drawString("R", centers[r][c].x, centers[r][c].y);
        } else if (board.tiles[r][c].getNum() != -1) {
            g.drawString(String.valueOf(board.tiles[r][c].getNum()), centers[r][c].x, centers[r][c].y);
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 11; j++) {
                if (centers[i][j] != null) {
                    drawHexagon(g, i, j, board.tiles[i][j].getType());
                    drawNum(g, i, j);
                }
                if (board.vertices[i][j] != null && board.vertices[i][j].occupied) {
                    Building b = (Building) board.vertices[i][j];
                    System.out.println(i + " " + j);
                    if (b.amtCollect == 1) {
                        drawSettlement(g, i, j, b.owner.color);
                    } else {
                        drawCity(g, i, j, b.owner.color);
                    }
                }
            }
        }
    }
}
