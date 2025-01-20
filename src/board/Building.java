package board;

import game.Player;

/**
 * Models a building (settlement or city) in Catan
 */
public class Building extends Vertex {

    public static int SETTLEMENT = 1;
    public static int CITY = 2;
    private final Player owner; // Owner of building
    private final int amtCollect; // 1 for settlement, 2 for city, differentiates a settlement and city

    /**
     * Creates a new Building on a specified vertex
     *
     * @param owner Player that owns the building
     * @param row   Row coordinate of the vertex
     * @param col   Column coordinate of vertex
     * @param type  Either {@code Building.SETTLEMENT} or {@code Building.CITY}
     */
    public Building(Player owner, int row, int col, int type) {
        super(row, col);
        occupied = true;
        this.owner = owner;
        this.amtCollect = type;
    }

    /**
     * Get building owner
     *
     * @return Player object of the owner
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Get amount that the building generates
     *
     * @return Amount the building generates, 1 or 2
     */
    public int getAmtCollect() {
        return amtCollect;
    }

    /**
     * Give the building owner resources
     *
     * @param resource Resource that the building gives
     */
    public void giveResource(int resource) {
        owner.getResources()[resource] += amtCollect;
    }
}
