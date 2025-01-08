package board;

import play.Player;

public class City extends Building {

    public City(Player player, int row, int col) {
        super(player, row, col);
    }

    @Override
    public void collect() {

    }
}
