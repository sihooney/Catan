package board;

import play.Player;

public class Road extends Edge {

    private Player owner;

    public Road(Player owner, Vertex u, Vertex v) {
        super(u, v);
        this.owner = owner;
        occupied = true;
    }
}
