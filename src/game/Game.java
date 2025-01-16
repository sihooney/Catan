package game;

import board.*;

import java.awt.*;

public class Game {

    private final Color[] COLORS = {Color.RED, Color.BLUE, Color.ORANGE, Color.WHITE};
    private final int N;
    public final Player[] players;
    public final Board board;
    public final Deck deck;
    public int curIndex;
    public Player curPlayer;

    public Game(int numPlayers) {
        N = numPlayers;
        this.players = new Player[N];
        for (int i = 0; i < N; i++) {
            players[i] = new Player(COLORS[i]);
        }
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

    public boolean initialRoad(Edge e) {
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
