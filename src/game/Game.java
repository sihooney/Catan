package game;

import board.*;

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

    public void nextTurn() {
        curIndex = (curIndex + 1) % N;
        curPlayer = players[1];
    }

    public boolean placeRoad(Edge e, boolean secondRoad) {
        if (board.hasRoad(e)) {
            return false;
        }
        if (curPlayer.buyRoad(e, secondRoad)) {
            board.placeRoad(e, curPlayer);
        }
        return false;
    }

    public boolean initialSettlement(Building b) {
        return false;
    }

    public boolean placeSecondRoad(Edge e) {
        return false;
    }

    public boolean placeSettlement(Building b) {
        return false;
    }

    public boolean placeCity(Building b) {
        return false;
    }

    public boolean buyDevCard() {
        if (deck.peek() == null) {
            return false;
        }
        if (curPlayer.buyDevCard(deck.peek())) {
            deck.draw();
            return true;
        }
        return false;
    }
}
