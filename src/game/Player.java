package game;

import board.*;
import constants.Items;
import constants.Resource;

import java.awt.*;
import java.util.*;

public class Player {

    private final String name;
    private final Color color;
    public final int[] resources;
    private final HashMap<DevCard, Integer> devCards;
    private final ArrayList<Building> buildings;
    private final HashMap<Vertex, HashSet<Edge>> graph;
    private int settlements;
    private int cities;
    private int roads;
    private int victoryPoints;
    private int knights;
    private boolean longestRoad;
    private boolean largestArmy;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        resources = new int[Resource.NAMES.length];
        devCards = new HashMap<>();
        buildings = new ArrayList<>();
        graph = new HashMap<>();
        settlements = 0;
        cities = 0;
        roads = 0;
        victoryPoints = 0;
        knights = 0;
        longestRoad = false;
        largestArmy = false;
    }

    private boolean[] canAfford() {
        boolean[] items = new boolean[Items.NAMES.length];
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

    private boolean buyRoad(Edge e) {
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
            graph.get(e.getU()).add(new Edge(e.getU(), e.getV()));
            graph.get(e.getV()).add(new Edge(e.getV(), e.getU()));
            return true;
        }
        return false;
    }

    private boolean buySettlement(Building b) {
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

    private boolean buyCity(Building b) {
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

    private boolean buyDevCard(DevCard card) {
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
