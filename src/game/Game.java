package game;

import board.*;
import constants.Resource;

import java.util.*;

import static constants.Colors.COLORS;

/**
 * Game class that contains all the information and actions in a game of Catan
 * Includes the board, deck of development cards, and all player information
 */
public class Game {

    private final int WIN = 10; // Number of victory points to win
    private final int numPlayers; // Number of players
    private final Player[] players; // Players in game
    private final Board board; // Board state
    private final Deck deck; // Deck of development cards
    private int curIdx; // Index of current active player
    private Player curPlayer; // Reference to current player

    /**
     * Creates a new game of Catan
     *
     * @param numPlayers Number of players
     */
    public Game(int numPlayers) {
        this.numPlayers = numPlayers;
        players = new Player[this.numPlayers];
        for (int i = 0; i < this.numPlayers; i++) {
            players[i] = new Player(COLORS[i]); // Initialize each player with their color
        }
        board = new Board(); // Initialize new Board
        deck = new Deck(); // Initialize new Deck
        curIdx = 0;
        curPlayer = players[0];
    }

    /**
     * Simulate rolling 2 dice
     *
     * @return The sum of the two dice
     */
    public int rollDice() {
        return (int) (Math.random() * 6) + 1 + (int) (Math.random() * 6) + 1;
    }

    /**
     * Change the current player index and current player to the next player
     */
    public void nextTurn() {
        curIdx = (curIdx + 1) % numPlayers;
        curPlayer = players[curIdx];
    }

    /**
     * Check if any players has won
     *
     * @return Index of the winning player. If no winning player, return {@code -1}
     */
    public int checkWin() {
        for (int i = 0; i < numPlayers; i++) {
            if (players[i].getVictoryPoints() >= WIN) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get the ranking of players, sorted by victory points descending
     *
     * @return An array containing the players, sorted by victory points descending
     */
    public Player[] ranking() {
        Player[] sorted = Arrays.copyOf(players, players.length);
        Arrays.sort(sorted);
        return sorted;
    }

    /**
     * Distribute the resources based off of the dice roll. Distribute the resources at the start
     * of the game
     *
     * @param roll The dice roll. If equal to {@code -1}, then distribute as according to game setup rules
     */
    public void distributeResources(int roll) {
        for (int i = 0; i < board.getTiles().length; i++) {
            for (int j = 0; j < board.getTiles()[0].length; j++) {
                // If is a tile and has the correct number or start of game
                if (board.getTiles()[i][j] != null && (roll == -1 || board.getTiles()[i][j].getNum() == roll)) {
                    // If not occupied by robber and not the desert
                    if (board.getTiles()[i][j].notRobber() && !(board.getTiles()[i][j].getType() == Resource.DESERT)) {
                        for (Vertex v : board.getTiles()[i][j].getVertices()) {
                            // If the vertex is a building
                            if (board.getVertices()[v.getRow()][v.getCol()] instanceof Building b) {
                                b.giveResource(board.getTiles()[i][j].getType()); // Building generates resource
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Half the resources of each player with more than 7 resources (rounded down)
     */
    public void halfResources() {
        for (int i = 0; i < numPlayers; i++) {
            players[i].discardHalf();
        }
    }

    /**
     * Current player takes a random resource from another player
     *
     * @param p Player that the current player steals from
     */
    public void takeRandomResource(Player p) {
        ArrayList<Integer> validIndices = new ArrayList<>(); // Possible resources types to take
        for (int i = 0; i < p.getResources().length; i++) {
            if (p.getResources()[i] > 0) {
                validIndices.add(i);
            }
        }
        if (validIndices.isEmpty()) {
            return; // If target has 0 resources
        }
        int randIdx = validIndices.get((int) (Math.random() * validIndices.size())); // Determine random resource
        p.getResources()[randIdx]--;
        curPlayer.getResources()[randIdx]++;
    }

    /**
     * Get any buildings on the two vertices of an edge
     *
     * @param e A valid edge on the board
     * @return An array containing 2 Building objects (either or both could be {@code null})
     */
    private Building[] buildingsOnRoad(Edge e) {
        Building[] buildings = new Building[2];
        Vertex u = e.getU();
        Vertex v = e.getV();
        if (board.getVertices()[u.getRow()][u.getCol()] instanceof Building b) {
            buildings[0] = b;
        }
        if (board.getVertices()[v.getRow()][v.getCol()] instanceof Building b) {
            buildings[1] = b;
        }
        return buildings;
    }

    /**
     * Check if a road is connected to a player's other roads or buildings
     *
     * @param e The edge the road occupies
     * @param p The owner of the road
     * @return {@code true} if connected, {@code false} otherwise
     */
    private boolean isRoadConnected(Edge e, Player p) {
        Vertex u = e.getU();
        Vertex v = e.getV();
        // Check if road is connected to building
        if (board.getVertices()[u.getRow()][u.getCol()] instanceof Building b) {
            if (b.getOwner().getColor() == p.getColor()) {
                return true;
            }
        }
        if (board.getVertices()[v.getRow()][v.getCol()] instanceof Building b) {
            if (b.getOwner().getColor() == p.getColor()) {
                return true;
            }
        }
        // Check if road is connected to another road
        HashMap<Edge, Player> uMap = board.getGraph().get(u);
        if (uMap != null) {
            for (Player val : uMap.values()) {
                if (val == p) {
                    return true;
                }
            }
        }
        HashMap<Edge, Player> vMap = board.getGraph().get(v);
        if (vMap != null) {
            for (Player val : vMap.values()) {
                if (val == p) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Lets current player place a road
     *
     * @param e Edge that the road occupies
     * @return {@code true} if road built successfully, {@code false} otherwise
     */
    public boolean placeRoad(Edge e) {
        if (!board.hasEdge(e)) {
            return false; // Cannot place if not a valid edge on the board
        }
        if (board.hasRoad(e)) {
            return false; // Cannot build over existing roads
        }
        Building[] buildings = buildingsOnRoad(e);
        if ((buildings[0] != null && buildings[0].getOwner().getColor() != curPlayer.getColor()) ||
                (buildings[1] != null && buildings[1].getOwner().getColor() != curPlayer.getColor())) {
            return false; // If road connects to another player's building
        }
        if (isRoadConnected(e, curPlayer)) { // If road is connected to the current player's road or building
            if (curPlayer.buyRoad(e)) { // If current player can afford a road
                board.placeRoad(e, curPlayer); // Place the road on the board
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the building is connected to one of current player's roads
     *
     * @param b Building object owned by current player
     * @return {@code true} if connected, {@code false} otherwise
     */
    private boolean buildingTouchesRoad(Building b) {
        Vertex v = new Vertex(b.getRow(), b.getCol());
        return curPlayer.getGraph().get(v) != null && !curPlayer.getGraph().get(v).isEmpty();
    }

    /**
     * Lets the current player place a settlement
     *
     * @param b     Building object of the settlement the current player wants to place
     * @param setup {@code true} if in game setup phase, {@code false} otherwise
     * @return {@code true} if settlement built successfully, {@code false} otherwise
     */
    public boolean placeSettlement(Building b, boolean setup) {
        if (board.getVertices()[b.getRow()][b.getCol()] == null) {
            return false; // If not a valid vertex on the board
        }
        if (board.hasBuildingsAround(new Vertex(b.getRow(), b.getCol()))) {
            return false; // If settlement breaks the distance rule
        }
        if (!setup && !this.buildingTouchesRoad(b)) {
            return false; // If building does not connect to a road
        }
        if (curPlayer.buySettlement()) { // If current player can afford the settlement
            board.placeBuilding(b); // Place settlement on board
            return true;
        }
        return false;
    }

    /**
     * Lets the current player place a city
     *
     * @param b Building object of the settlement the current player wants to place
     * @return {@code true} if city built successfully, {@code false} otherwise
     */
    public boolean placeCity(Building b) {
        // Check if upgrading a settlement
        if (board.getVertices()[b.getRow()][b.getCol()] instanceof Building settlement) {
            if (settlement.getOwner().getColor() != curPlayer.getColor()) {
                return false; // If wrong player
            }
            if (settlement.getAmtCollect() == Building.CITY) {
                return false; // If attempting to upgrade a city
            }
            if (curPlayer.buyCity()) { // If current player can afford a city
                board.placeBuilding(b); // Place city on board
                return true;
            }
        }
        return false;
    }

    /**
     * Lets current player buy a development card
     *
     * @return {@code true} if bought successfully, {@code false} otherwise
     */
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

    /**
     * Gets the total number of players in the game.
     *
     * @return the number of players.
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Retrieves the array of players in the game.
     *
     * @return an array of {@code Player} objects representing all players.
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Retrieves the current game board.
     *
     * @return the {@code Board} object representing the game board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the index of the current player.
     *
     * @return the index of the current player as an integer.
     */
    public int getCurIdx() {
        return curIdx;
    }

    /**
     * Sets the index of the current player.
     *
     * @param idx the new index of the current player.
     */
    public void setCurIdx(int idx) {
        curIdx = idx;
    }

    /**
     * Retrieves the current player.
     *
     * @return the {@code Player} object representing the current player.
     */
    public Player getCurPlayer() {
        return curPlayer;
    }

    /**
     * Updates the current player based on the current index.
     * This method sets {@code curPlayer} to the player at {@code curIdx}.
     */
    public void updateCurPlayer() {
        curPlayer = players[curIdx];
    }

}
