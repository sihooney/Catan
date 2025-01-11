package board;

import game.Player;

public abstract class Building extends Vertex {

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

    protected void giveResource(int resource) {
        owner.resources[resource] += amtCollect;
    }
}
