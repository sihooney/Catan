package board;

import play.Player;

public abstract class Building extends Vertex {

    private final Player owner;

    public Building(Player player, int row, int col) {
        super(row, col);
        occupied = false;
        owner = player;
    }

    public abstract void collect();
}
