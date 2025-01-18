package game;

import board.*;
import constants.Resource;

import static constants.Colors.COLORS;

public class Game {

    public final int N;
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
                if (board.tiles[i][j] != null && (roll == -1 || board.tiles[i][j].getNum() == roll) &&
                        !board.tiles[i][j].isRobber() && !(board.tiles[i][j].getType() == Resource.DESERT)) {
                    for (Vertex v : board.tiles[i][j].getVertices()) {
                        if (board.vertices[v.getRow()][v.getCol()] != null &&
                                board.vertices[v.getRow()][v.getCol()] instanceof Building b) {
                            b.giveResource(board.tiles[i][j].getType());
                        }
                    }
                }
            }
        }
    }

    public void setPlayer() {
        curPlayer = players[curIndex];
    }

    public void nextTurn() {
        curIndex = (curIndex + 1) % N;
        curPlayer = players[1];
    }

    public boolean placeRoad(Edge e) {
        if (board.hasRoad(e)) {
            return false;
        }
        if (curPlayer.buyRoad(e)) {
            board.placeRoad(e, curPlayer);
        }
        return false;
    }

    public boolean connected(Building b) {
        Vertex v = new Vertex(b.getRow(), b.getCol());
        return curPlayer.graph.get(v) != null && !curPlayer.graph.get(v).isEmpty();
    }

    public boolean placeSettlement(Building b, boolean setup) {
        if (board.vertices[b.getRow()][b.getCol()] == null) {
            return false;
        }
        if (board.hasBuildingsAround(new Vertex(b.getRow(), b.getCol()))) {
            return false;
        }
        if (!setup && !this.connected(b)) {
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
