package game;

import board.*;
import constants.Items;
import constants.Ports;
import constants.Resource;

import java.awt.*;
import java.util.*;

public class Player {

    public final Color color;
    public final int[] resources;
    private final HashMap<DevCard, Integer> devCards;
    private final ArrayList<Building> buildings;
    private final HashMap<Vertex, HashSet<Edge>> graph;
    public final boolean[] ports;
    private int settlements;
    private int cities;
    private int roads;
    private int victoryPoints;
    private int knights;
    private boolean longestRoad;
    private boolean largestArmy;

    public Player(Color color) {
        this.color = color;
        resources = new int[Resource.RESOURCES.length];
        devCards = new HashMap<>();
        buildings = new ArrayList<>();
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

    public boolean buyRoad(Edge e, boolean secondRoad) {
        if (canAfford()[Items.ROAD]) {
            if (roads + 1 > 15) {
                return false;
            }
            roads++;
            resources[Resource.BRICK]--;
            resources[Resource.LUMBER]--;
            if (!graph.containsKey(e.getU())) {
                graph.put(e.getU(), new HashSet<>());
            }
            if (!graph.containsKey(e.getV())) {
                graph.put(e.getV(), new HashSet<>());
            }
            if (hasBuilding(e.getU()) || hasBuilding(e.getV())) {
                graph.get(e.getU()).add(new Edge(e.getU(), e.getV()));
                graph.get(e.getV()).add(new Edge(e.getV(), e.getU()));
                return true;
            } else if (!secondRoad && (hasRoad(e.getU()) || hasRoad(e.getV()))) {
                graph.get(e.getU()).add(new Edge(e.getU(), e.getV()));
                graph.get(e.getV()).add(new Edge(e.getV(), e.getU()));
                return true;
            }
        }
        return false;
    }

    private boolean hasBuilding(Vertex v) {
        for (Building b : buildings) {
            if (b.getPoint() == v.getPoint()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasRoad(Vertex v) {
        return graph.containsKey(v) && !graph.get(v).isEmpty();
    }

    public boolean buySettlement(Building b) {
        if (canAfford()[Items.SETTLEMENT]) {
            if (settlements + 1 > 5) {
                return false;
            }
            settlements++;
            victoryPoints++;
            resources[Resource.BRICK]--;
            resources[Resource.LUMBER]--;
            resources[Resource.GRAIN]--;
            resources[Resource.WOOL]--;
            buildings.add(b);
            return true;
        }
        return false;
    }

    public boolean buyCity(Building b) {
        if (canAfford()[Items.CITY]) {
            if (cities + 1 > 4) {
                return false;
            }
            cities++;
            victoryPoints += 2;
            resources[Resource.GRAIN] -= 2;
            resources[Resource.ORE] -= 3;
            buildings.add(b);
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
}
