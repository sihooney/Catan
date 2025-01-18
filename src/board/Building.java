package board;

import game.Player;

public class Building extends Vertex {

    public static int SETTLEMENT = 1;
    public static int CITY = 2;
    public final Player owner;
    public int amtCollect;

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
