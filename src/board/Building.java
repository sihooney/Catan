package board;

import game.Player;

public class Building extends Vertex {

    protected static int SETTLEMENT = 1;
    protected static int CITY = 2;
    protected final Player owner;
    protected int amtCollect;

    public Building(Player owner, int row, int col, int type) {
        super(row, col);
        occupied = true;
        this.owner = owner;
        this.amtCollect = type;
    }

    public void giveResource(int resource) {
        owner.resources[resource] += amtCollect;
    }
}
