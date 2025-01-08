package board;

import java.util.*;

public class Board {

    private static final Point[] TILE_CORDS = {
            new Point(0, 3), new Point(0, 5), new Point(0, 7),
            new Point(2, 2), new Point(2, 4), new Point(2, 6), new Point(2, 8),
            new Point(4, 1), new Point(4, 3), new Point(4, 5), new Point(4, 7), new Point(4, 9),
            new Point(6, 2), new Point(6, 4), new Point(6, 6), new Point(6, 8),
            new Point(8, 3), new Point(8, 5), new Point(8, 7),
    };
    private static final Integer[] TOKENS = {2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12};

    private final Tile[][] tiles;
    private final Vertex[][] vertices;
    private HashMap<Vertex, ArrayList<Vertex>> roadsGraph;
    private Point robberLoc;

    public Board() {
        tiles = new Tile[12][11];
        vertices = new Vertex[12][11];
        placeTiles();
    }

    private void placeTiles() {
        ArrayList<Tile> allTiles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            allTiles.add(new Tile("LUMBER"));
            allTiles.add(new Tile("BRICK"));
            allTiles.add(new Tile("GRAIN"));
            allTiles.add(new Tile("WOOL"));
            allTiles.add(new Tile("ORE"));
        }
        allTiles.add(new Tile("LUMBER"));
        allTiles.add(new Tile("GRAIN"));
        allTiles.add(new Tile("WOOL"));
        allTiles.add(new Tile("DESERT", true));
        Collections.shuffle(allTiles);
        ArrayList<Integer> tokens = new ArrayList<>(Arrays.asList(TOKENS));
        Collections.shuffle(tokens);
        for (int i = 0; i < allTiles.size(); i++) {
            Tile t = allTiles.get(i);
            t.setPosition(TILE_CORDS[i]);
            if (!t.isRobber()) {
                t.setNum(tokens.get(i));
            } else {
                robberLoc = new Point(t.row, t.col);
                tokens.add(i, 7);
            }
            tiles[t.row][t.col] = t;
            for (Point p : t.getVertices()) {
                if (vertices[p.row][p.col] == null) {
                    vertices[p.row][p.col] = new Vertex(p.row, p.col);
                }
            }
        }
    }

    public static void main(String[] args) {
        Board b = new Board();
        for (int i = 0; i < b.tiles.length; i++) {
            for (int j = 0; j < b.tiles[0].length; j++) {
                if (b.tiles[i][j] != null) {
                    System.out.println(b.tiles[i][j]);
                    for (Point p : b.tiles[i][j].getVertices()) {
                        System.out.println(p);
                    }
                }
            }
        }
    }
}
