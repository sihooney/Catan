package board;

import constants.Resource;
import game.Player;

import java.util.*;

/**
 * Models a Catan board and contains all data related to the board state
 * Includes the tiles, vertices, buildings, roads, and robber position on a board
 */
public class Board {

    // Coordinates for all tiles, specified by their top vertex
    private static final Point[] TILE_CORDS = {
            new Point(0, 3), new Point(0, 5), new Point(0, 7),
            new Point(2, 2), new Point(2, 4), new Point(2, 6), new Point(2, 8),
            new Point(4, 1), new Point(4, 3), new Point(4, 5), new Point(4, 7), new Point(4, 9),
            new Point(6, 2), new Point(6, 4), new Point(6, 6), new Point(6, 8),
            new Point(8, 3), new Point(8, 5), new Point(8, 7),
    };
    // All token numbers to be placed on tiles
    private static final Integer[] TOKENS = {2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12};
    // The directions for a vertex on the board to its adjacent vertices
    private static final int[][][] NEIGHBORS = {{{-1, 0}, {1, -1}, {1, 1}}, {{-1, -1}, {-1, 1}, {1, 0}}};

    private final Tile[][] tiles; // Tiles on the board
    private final Vertex[][] vertices; // Vertices on the board
    private final HashMap<Vertex, HashMap<Edge, Player>> graph; // Undirected graph representing edges and roads
    private Point robberLoc; // Location of robber, specified by the tile's top vertex

    /**
     * Creates a new board with a random configuration of tiles and numberings
     */
    public Board() {
        tiles = new Tile[12][11];
        vertices = new Vertex[12][11];
        graph = new HashMap<>();
        initialize();
    }

    /**
     * Moves the robber to a new tile
     *
     * @param p Coordinates of top vertex to move robber to
     * @return {@code true} if proper move, {@code false} otherwise
     */
    public boolean moveRobber(Point p) {
        if (p.equals(robberLoc)) {
            return false;
        }
        if (tiles[p.getRow()][p.getCol()] == null) {
            return false;
        }
        tiles[robberLoc.getRow()][robberLoc.getCol()].setRobber(false);
        robberLoc = p;
        tiles[robberLoc.getRow()][robberLoc.getCol()].setRobber(true);
        return true;
    }

    /**
     * Checks if there are any buildings on or adjacent to a vertex
     *
     * @param v Coordinates of a vertex
     * @return {@code true} if buildings present, {@code false}  otherwise
     */
    public boolean hasBuildingsAround(Vertex v) {
        if (vertices[v.getRow()][v.getCol()].isOccupied()) {
            return true;
        }
        int i = v.getRow() % 2;
        for (int j = 0; j < 3; j++) {
            try {
                int nr = v.getRow() + NEIGHBORS[i][j][0];
                int nc = v.getCol() + NEIGHBORS[i][j][1];
                if (vertices[nr][nc] != null && vertices[nr][nc].isOccupied()) {
                    return true;
                }
            } catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {

            }

        }
        return false;
    }

    /**
     * Places a building on the board
     *
     * @param b Building to be placed
     */
    public void placeBuilding(Building b) {
        vertices[b.getRow()][b.getCol()] = b;
    }

    /**
     * Places a road on the board
     *
     * @param e Edge that road occupies
     * @param p Owner of the road
     */
    public void placeRoad(Edge e, Player p) {
        graph.get(e.u()).put(e, p);
        graph.get(e.v()).put(new Edge(e.v(), e.u()), p);
    }

    /**
     * Checks if the board contains the edge specified
     *
     * @param e An edge between two vertices
     * @return {@code true} if board contains edge, {@code false} otherwise
     */
    public boolean hasEdge(Edge e) {
        return graph.containsKey(e.u()) && graph.get(e.u()).containsKey(e) &&
                graph.containsKey(e.v()) && graph.get(e.v()).containsKey(new Edge(e.v(), e.u()));
    }

    /**
     * Checks if a road occupies an edge
     *
     * @param e An edge between two vertices
     * @return {@code true} if board contains road on the edge, {@code false} otherwise
     */
    public boolean hasRoad(Edge e) {
        Edge reverse = new Edge(e.v(), e.u());
        return graph.get(e.u()).get(e) != null || graph.get(e.v()).get(reverse) != null;
    }

    /**
     * Adds an undirected edge to the board between two vertices
     *
     * @param u A vertex
     * @param v A vertex
     */
    public void addEdge(Vertex u, Vertex v) {
        if (!graph.containsKey(u)) {
            graph.put(u, new HashMap<>());
        }
        if (!graph.containsKey(v)) {
            graph.put(v, new HashMap<>());
        }
        if (graph.get(u).containsKey(new Edge(u, v)) || graph.get(v).containsKey(new Edge(v, u))) {
            return;
        }
        graph.get(u).put(new Edge(u, v), null);
        graph.get(v).put(new Edge(v, u), null);
    }

    /**
     * Initialize the board with random tile placements, random tile numbers, all edges between vertices, and
     * a random desert (robber) location
     */
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
            if (t.notRobber()) {
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

    /**
     * Get the tiles of the board
     *
     * @return Tiles of the board
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Get the vertices of the board
     *
     * @return Vertices of the board
     */
    public Vertex[][] getVertices() {
        return vertices;
    }

    /**
     * Get all the edges and roads of the board as an undirected graph
     *
     * @return The edges and roads of the board
     */
    public HashMap<Vertex, HashMap<Edge, Player>> getGraph() {
        return graph;
    }

    /**
     * Get the robber location as the tile's top vertex
     *
     * @return The top vertex of the tile the robber occupies
     */
    public Point getRobberLoc() {
        return robberLoc;
    }
}
