package game;

import board.Building;
import board.DevCard;
import board.Edge;
import board.Vertex;

import java.awt.*;
import java.util.*;

public class Player {

    private final int SETTLEMENTS = 5;
    private final int CITIES = 4;
    private final int ROADS = 15;
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
        resources = new int[5];
        devCards = new HashMap<>();
        buildings = new ArrayList<>();
        graph = new HashMap<>();
        settlements = 2;
        cities = 0;
        roads = 2;
        victoryPoints = 2;
        knights = 0;
        longestRoad = false;
        largestArmy = false;
    }
}
