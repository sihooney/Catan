package board;

import game.Player;

public class Road extends Edge {

    private final Player owner;

    public Road(Player owner, Vertex u, Vertex v) {
        super(u, v);
        this.owner = owner;
        occupied = true;
    }
}
