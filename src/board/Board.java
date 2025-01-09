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
    private final HashMap<Vertex, HashSet<Edge>> adj;
    private Point robberLoc;

    public Board() {
        tiles = new Tile[12][11];
        vertices = new Vertex[12][11];
        adj = new HashMap<>();
        initialize();
    }

    private void addEdge(Vertex u, Vertex v) {
        if (!adj.containsKey(u)) {
            adj.put(u, new HashSet<>());
        }
        if (!adj.containsKey(v)) {
            adj.put(v, new HashSet<>());
        }
        adj.get(u).add(new Edge(u, v));
        adj.get(v).add(new Edge(v, u));
    }

    private void initialize() {
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
            Vertex[] points = t.getVertices();
            for (Vertex u : points) {
                if (vertices[u.row][u.col] == null) {
                    vertices[u.row][u.col] = new Vertex(u.row, u.col);
                }
            }
            addEdge(vertices[points[0].row][points[0].col], vertices[points[1].row][points[1].col]);
            addEdge(vertices[points[0].row][points[0].col], vertices[points[2].row][points[2].col]);
            addEdge(vertices[points[1].row][points[1].col], vertices[points[3].row][points[3].col]);
            addEdge(vertices[points[2].row][points[2].col], vertices[points[4].row][points[4].col]);
            addEdge(vertices[points[3].row][points[3].col], vertices[points[5].row][points[5].col]);
            addEdge(vertices[points[4].row][points[4].col], vertices[points[5].row][points[5].col]);
        }
    }
}
