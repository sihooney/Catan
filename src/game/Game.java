package game;

import board.*;
import constants.Resource;

import java.lang.reflect.Array;
import java.util.*;

import static constants.Colors.COLORS;

public class Game {

    public final int WIN = 10;
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

    public int rollDice() {
        return (int) (Math.random() * 6) + 1 + (int) (Math.random() * 6) + 1;
    }

    public void nextTurn() {
        curIndex = (curIndex + 1) % N;
        curPlayer = players[curIndex];
    }

    public void setPlayer() {
        curPlayer = players[curIndex];
    }

    public int checkWin() {
        for (int i = 0; i < N; i++) {
            if (players[i].victoryPoints >= WIN) {
                return i;
            }
        }
        return -1;
    }

    public Player[] ranking() {
        Player[] sorted = Arrays.copyOf(players, players.length);
        Arrays.sort(sorted);
        return sorted;
    }

    public void distributeResources(int roll) {
        for (int i = 0; i < board.tiles.length; i++) {
            for (int j = 0; j < board.tiles[0].length; j++) {
                if (board.tiles[i][j] != null && (roll == -1 || board.tiles[i][j].getNum() == roll) &&
                        !board.tiles[i][j].isRobber() && !(board.tiles[i][j].getType() == Resource.DESERT)) {
                    for (Vertex v : board.tiles[i][j].getVertices()) {
                        if (board.vertices[v.getRow()][v.getCol()] instanceof Building b) {
                            b.giveResource(board.tiles[i][j].getType());
                        }
                    }
                }
            }
        }
    }

    public void halfResources() {
        for (int i = 0; i < N; i++) {
            players[i].discardHalf();
        }
    }

    public void takeRandomResource(Player p) {
        ArrayList<Integer> validIndices = new ArrayList<>();
        for (int i = 0; i < p.resources.length; i++) {
            if (p.resources[i] > 0) {
                validIndices.add(i);
            }
        }
        if (validIndices.isEmpty()) {
            return;
        }
        int randIdx = validIndices.get((int) (Math.random() * validIndices.size()));
        p.resources[randIdx]--;
        curPlayer.resources[randIdx]++;
    }

    private Building[] buildingsOnRoad(Edge e) {
        Building[] buildings = new Building[2];
        Vertex u = e.getU();
        Vertex v = e.getV();
        if (board.vertices[u.getRow()][u.getCol()] instanceof Building b) {
            buildings[0] = b;
        }
        if (board.vertices[v.getRow()][v.getCol()] instanceof Building b) {
            buildings[1] = b;
        }
        return buildings;
    }

    private boolean isRoadConnected(Edge e, Player p) {
        Vertex u = e.getU();
        Vertex v = e.getV();
        if (board.vertices[u.getRow()][u.getCol()] instanceof Building b && b.owner.color == p.color) {
            return true;
        }
        if (board.vertices[v.getRow()][v.getCol()] instanceof Building b && b.owner.color == p.color) {
            return true;
        }
        HashMap<Edge, Player> uMap = board.graph.get(u);
        if (uMap != null) {
            for (Player val : uMap.values()) {
                if (val == p) {
                    return true;
                }
            }
        }
        HashMap<Edge, Player> vMap = board.graph.get(v);
        if (vMap != null) {
            for (Player val : vMap.values()) {
                if (val == p) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean placeRoad(Edge e) {
        if (!board.hasEdge(e)) {
            return false;
        }
        if (board.hasRoad(e)) {
            return false;
        }
        Building[] buildings = buildingsOnRoad(e);
        if ((buildings[0] != null && buildings[0].owner.color != curPlayer.color) ||
                (buildings[1] != null && buildings[1].owner.color != curPlayer.color)) {
            return false;
        }
        if (isRoadConnected(e, curPlayer)) {
            if (curPlayer.buyRoad(e)) {
                board.placeRoad(e, curPlayer);
                return true;
            }
        }
        return false;
    }

    private boolean buildingTouchesRoad(Building b) {
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
        if (!setup && !this.buildingTouchesRoad(b)) {
            return false;
        }
        if (curPlayer.buySettlement(b)) {
            board.placeBuilding(b);
            return true;
        }
        return false;
    }

    public boolean placeCity(Building b) {
        if (board.vertices[b.getRow()][b.getCol()] instanceof Building settlement) {
            if (settlement.owner.color != curPlayer.color) {
                return false;
            }
            if (settlement.amtCollect == Building.CITY) {
                return false;
            }
            if (curPlayer.buyCity(b)) {
                board.placeBuilding(b);
                return true;
            }
        }
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
