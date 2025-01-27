package game;

import board.*;
import constants.*;

import java.awt.*;
import java.util.*;

/**
 * Contains all player information and actions
 */
public class Player implements Comparable<Player> {

    private final Color color; // Player color
    private final int[] resources; // Array of player resources
    private final HashMap<DevCard, Integer> devCards; // Development cards owned by player
    private final HashMap<Vertex, HashSet<Edge>> graph; // Undirected graph of the player's road network
    private final boolean[] ports;
    private int settlements; // Number of settlements
    private int cities; // Number of cities
    private int roads; // Number of roads
    private int victoryPoints; // Number of victory points
    private int knights;
    private boolean longestRoad;
    private boolean largestArmy;

    /**
     * Create a new player
     *
     * @param color Color of player
     */
    public Player(Color color) {
        this.color = color;
        resources = new int[Resource.RESOURCES.length]; // Initialize starting resources to 2 settlements, 2 roads
        resources[Resource.BRICK] = 4;
        resources[Resource.WOOL] = 2;
        resources[Resource.LUMBER] = 4;
        resources[Resource.GRAIN] = 2;
        resources[Resource.ORE] = 0;
        devCards = new HashMap<>();
        graph = new HashMap<>();
        ports = new boolean[Ports.PORTS.length];
        settlements = 0;
        cities = 0;
        roads = 0;
        victoryPoints = 0;
        knights = 0;
        longestRoad = false;
        largestArmy = false;
    }

    /**
     * Get which items the player can afford
     *
     * @return A boolean array of which items the player can afford, using {@code constants/Items.java}
     */
    private boolean[] canAfford() {
        boolean[] items = new boolean[Items.ITEMS.length];
        Arrays.fill(items, true);
        if (resources[Resource.BRICK] < 1 || resources[Resource.LUMBER] < 1) {
            items[Items.ROAD] = false;
            items[Items.SETTLEMENT] = false;
        }
        if (resources[Resource.GRAIN] < 1 || resources[Resource.WOOL] < 1) {
            items[Items.SETTLEMENT] = false;
            items[Items.DEVCARD] = false;
        }
        if (resources[Resource.ORE] < 1) {
            items[Items.DEVCARD] = false;
        }
        if (resources[Resource.GRAIN] < 2 || resources[Resource.ORE] < 3) {
            items[Items.CITY] = false;
        }
        return items;
    }

    /**
     * Lets the player buy a road, assuming it is valid
     *
     * @param e Edge that the road occupies
     * @return {@code true} if player can buy, {@code false} otherwise
     */
    public boolean buyRoad(Edge e) {
        if (canAfford()[Items.ROAD]) { // If player can afford a road
            if (roads + 1 > 15) {
                return false; // Player cannot buy more than 15 roads
            }
            roads++;
            // Update player resources
            resources[Resource.BRICK]--;
            resources[Resource.LUMBER]--;
            if (!graph.containsKey(e.u())) {
                graph.put(e.u(), new HashSet<>());
            }
            if (!graph.containsKey(e.v())) {
                graph.put(e.v(), new HashSet<>());
            }
            // Add road to the player graph
            graph.get(e.u()).add(new Edge(e.u(), e.v()));
            graph.get(e.v()).add(new Edge(e.v(), e.u()));
            return true;
        }
        return false;
    }

    /**
     * Lets the player buy a settlement, assuming it is valid
     *
     * @return {@code true} if player can buy, {@code false} otherwise
     */
    public boolean buySettlement() {
        if (canAfford()[Items.SETTLEMENT]) { // If player can afford a settlement
            if (settlements + 1 > 5) { // Player cannot have more than 5 settlements
                return false;
            }
            settlements++;
            victoryPoints++; // Increase victory points
            // Update resources
            resources[Resource.BRICK]--;
            resources[Resource.LUMBER]--;
            resources[Resource.GRAIN]--;
            resources[Resource.WOOL]--;
            return true;
        }
        return false;
    }

    /**
     * Lets the player buy a city, assuming it is valid
     *
     * @return {@code true} if player can buy, {@code false} otherwise
     */
    public boolean buyCity() {
        if (canAfford()[Items.CITY]) { // If player can afford a city
            if (cities + 1 > 4) {
                return false; // Player cannot have more than 4 cities
            }
            cities++;
            settlements--;
            victoryPoints++; // Increase victory points
            // Update resources
            resources[Resource.GRAIN] -= 2;
            resources[Resource.ORE] -= 3;
            return true;
        }
        return false;
    }

    public boolean buyDevCard(DevCard card) {
        if (canAfford()[Items.DEVCARD]) {
            resources[Resource.WOOL]--;
            resources[Resource.GRAIN]--;
            resources[Resource.ORE]--;
            if (!devCards.containsKey(card)) {
                devCards.put(card, 0);
            }
            devCards.put(card, devCards.get(card) + 1);
            if (card.getType() == DevCard.VP) {
                victoryPoints++;
            } else if (card.getType() == DevCard.KNIGHT) {
                knights++;
            }
            return true;
        }
        return false;
    }

    /**
     * If player has more than 7 resources, randomly discard half (rounded down)
     */
    public void discardHalf() {
        int sum = 0;
        for (int n : resources) {
            sum += n;
        }
        if (sum <= 7) {
            return; // No need to discard if less than 7 resources
        }
        int remove = sum / 2;
        while (remove > 0) {
            int idx = (int) (Math.random() * resources.length); // Randomly choose which resource to discard
            if (resources[idx] > 0) {
                int decrease = 1 + (int) (Math.random() * Math.min(remove, resources[idx])); // Discard random amount
                resources[idx] -= decrease;
                remove -= decrease;
            }
        }
    }

    /**
     * Discard 4 identical resources for 1 resource of another type
     *
     * @param receive Type of resource to receive
     * @param give    Type of resource to give
     * @return {@code true} if trade successful, {@code false} otherwise
     */
    public boolean selfTrade(int receive, int give) {
        if (receive == give) {
            return false; // Cannot give and receive same resource
        }
        if (resources[give] < 4) {
            return false; // Not enough resource to give
        }
        resources[receive]++;
        resources[give] -= 4;
        return true;
    }

    /**
     * Gets the color associated with the player or entity.
     *
     * @return the {@code Color} object representing the player's color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Retrieves the array of resources the player or entity possesses.
     *
     * @return an integer array representing the quantities of each resource.
     */
    public int[] getResources() {
        return resources;
    }

    /**
     * Gets the graph representing the player's current network of vertices and edges.
     *
     * @return the graph representing the player's current network of vertices and edges.
     */
    public HashMap<Vertex, HashSet<Edge>> getGraph() {
        return graph;
    }

    /**
     * Gets the number of victory points the player has.
     *
     * @return the total victory points as an integer.
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Resources: ");
        for (int i = 0; i < resources.length; i++) {
            sb.append(String.format("%s: %d, ", Resource.RESOURCES[i], resources[i]));
        }
        sb.append("\nDevelopment Cards: ");
        for (Map.Entry<DevCard, Integer> entry : devCards.entrySet()) {
            sb.append(String.format("%s: %d, ", DevCard.NAMES[entry.getKey().getType()], entry.getValue()));
        }
        sb.append("\nVictory Points: ");
        sb.append(String.format("\n%s: %b, %s: %b, %s: %d", "Longest Road", longestRoad, "Largest Army", largestArmy,
                "Victory Points", victoryPoints));
        return sb.toString();
    }

    @Override
    public int compareTo(Player p) {
        return Integer.compare(p.victoryPoints, victoryPoints);
    }
}
