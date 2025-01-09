package board;

import game.Player;

public class City extends Building {

    public City(Player owner, int row, int col) {
        super(owner, row, col);
        amtCollect = Building.CITY;
    }
}
