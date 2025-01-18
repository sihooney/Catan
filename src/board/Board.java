package board;

import constants.Resource;
import game.Player;

import java.util.*;

public class Board {

    private static final Point[] TILE_CORDS = {new Point(0, 3), new Point(0, 5), new Point(0, 7), new Point(2, 2), new Point(2, 4), new Point(2, 6), new Point(2, 8), new Point(4, 1), new Point(4, 3), new Point(4, 5), new Point(4, 7), new Point(4, 9), new Point(6, 2), new Point(6, 4), new Point(6, 6), new Point(6, 8), new Point(8, 3), new Point(8, 5), new Point(8, 7),};
    private static final Integer[] TOKENS = {2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12};
    public final Tile[][] tiles;
    public final Vertex[][] vertices;
    public final HashMap<Vertex, HashMap<Edge, Player>> graph;
    public Point robberLoc;

    public Board() {
        tiles = new Tile[12][11];
        vertices = new Vertex[12][11];
        graph = new HashMap<>();
        initialize();
    }

    public boolean hasBuilding(Vertex v, int distance) {
        if (vertices[v.getRow()][v.getCol()].occupied) {
            return false;
        }
        if (distance < 2) {
            for (Edge e : graph.get(v).keySet()) {
                hasBuilding(e.getV(), distance + 1);
            }
        }
        return true;
    }

    public void placeBuilding(Building b) {
        vertices[b.getRow()][b.getCol()] = b;
    }

    public boolean hasEdge(Edge e) {
        return graph.containsKey(e.getU()) && graph.get(e.getU()).containsKey(e) && graph.containsKey(e.getV()) && graph.get(e.getV()).containsKey(new Edge(e.getV(), e.getU()));
    }

    public boolean hasRoad(Edge e) {
        Edge reverse = new Edge(e.getV(), e.getU());
        if (hasEdge(e) && hasEdge(reverse)) {
            return graph.get(e.getU()).get(e) == null && graph.get(e.getV()).get(reverse) == null;
        }
        return false;
    }

    public void placeRoad(Edge e, Player p) {
        graph.get(e.getU()).put(e, p);
        graph.get(e.getV()).put(new Edge(e.getV(), e.getU()), p);
    }

    public boolean addEdge(Vertex u, Vertex v) {
        if (!graph.containsKey(u)) {
            graph.put(u, new HashMap<>());
        }
        if (!graph.containsKey(v)) {
            graph.put(v, new HashMap<>());
        }
        if (graph.get(u).containsKey(new Edge(u, v)) || graph.get(v).containsKey(new Edge(v, u))) {
            return false;
        }
        graph.get(u).put(new Edge(u, v), null);
        graph.get(v).put(new Edge(v, u), null);
        return true;
    }

    private void initialize() {
        ArrayList<Tile> allTiles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            allTiles.add(new Tile(Resource.BRICK));
            allTiles.add(new Tile(Resource.LUMBER));
            allTiles.add(new Tile(Resource.GRAIN));
            allTiles.add(new Tile(Resource.WOOL));
            allTiles.add(new Tile(Resource.ORE));
        }
        allTiles.add(new Tile(Resource.LUMBER));
        allTiles.add(new Tile(Resource.GRAIN));
        allTiles.add(new Tile(Resource.WOOL));
        allTiles.add(new Tile(Resource.DESERT, true));
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
