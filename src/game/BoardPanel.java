package game;

import board.Board;
import constants.Resource;

import javax.swing.*;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

public class BoardPanel extends JPanel {

    private final int RADIUS = 75;
    private final int X_OFFSET = 300;
    private final int Y_OFFSET = 50;
    public Board board;
    private Point[][] centers;

    public BoardPanel(Board board) {
        this.board = board;
        this.setPreferredSize(new Dimension(1200, 700));
        this.setBackground(Color.BLUE);
        getTileCenters();
    }

    private void getTileCenters() {
        centers = new Point[12][11];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 11; j++) {
                if (board.tiles[i][j] != null) {
                    long x = Math.round(j * RADIUS * Math.sqrt(3.0) / 2);
                    long y = Math.round((i / 2.0 + 1) * RADIUS + (i / 2.0) * RADIUS / 2);
                    centers[i][j] = new Point((int) (X_OFFSET + x), (int) (Y_OFFSET + y));
                    System.out.println(i + " " + j + " " + x + " " + y);
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

    private void drawNum(Graphics g, int r, int c) {
        g.setColor(Color.WHITE);
        g.setFont(new Font(null, Font.PLAIN, 20));
        if (r == board.robberLoc.getRow() && c == board.robberLoc.getCol()) {
            g.drawString("R", centers[r][c].x, centers[r][c].y);
        } else {
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
            }
        }
    }
}
