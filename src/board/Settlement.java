package board;

import game.Player;

public class Settlement extends Building {

    public Settlement(Player owner, int row, int col) {
        super(owner, row, col);
        amtCollect = Building.SETTLEMENT;
    }
}
