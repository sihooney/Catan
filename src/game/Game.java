package game;

import board.Board;
import board.Deck;

public class Game {

    private final int N;
    private final Player[] players;
    private final Board board;
    private final Deck deck;
    private int curIndex;
    private Player curPlayer;

    public Game(Player[] players) {
        N = players.length;
        this.players = players;
        board = new Board();
        deck = new Deck();
        curIndex = 0;
        curPlayer = players[0];
    }

    public int diceRoll() {
        return (int) (Math.random() * 6) + 1 + (int) (Math.random() * 6) + 1;
    }

    private void nextTurn() {
        curIndex = (curIndex + 1) % N;
        curPlayer = players[1];
    }

    private boolean placeSettlement() {
        return false;
    }

    private boolean placeCity() {
        return false;
    }

    private boolean placeRoad() {
        return false;
    }
}
