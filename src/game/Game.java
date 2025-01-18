package game;

import board.*;

import static constants.Colors.COLORS;

public class Game {

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

    public int checkWin() {
        for (int i = 0; i < N; i++) {
            if (players[i].victoryPoints >= 10) {
                return i;
            }
        }
        return -1;
    }

    public int diceRoll() {
        return (int) (Math.random() * 6) + 1 + (int) (Math.random() * 6) + 1;
    }

    public void distributeResources(int roll) {
        for (int i = 0; i < board.tiles.length; i++) {
            for (int j = 0; j < board.tiles[0].length; j++) {
                if (board.tiles[i][j] != null && board.tiles[i][j].getNum() == roll && !board.tiles[i][j].isRobber()) {
                    for (Vertex v : board.tiles[i][j].getVertices()) {
                        if (board.vertices[v.getRow()][v.getCol()].occupied) {
                            Building b = (Building) v;
                            b.giveResource(board.tiles[i][j].getType());
                        }
                    }
                }
            }
        }
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
        if (board.vertices[b.getRow()][b.getCol()] == null) {
            return false;
        }
        if (!board.hasBuilding(new Vertex(b.getRow(), b.getCol()), 0)) {
            return false;
        }
        if (curPlayer.buySettlement(b)) {
            board.placeBuilding(b);
            return true;
        }
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
