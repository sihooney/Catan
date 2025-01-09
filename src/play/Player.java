package play;

import java.awt.*;
import java.util.*;

public class Player {

    private final int SETTLEMENTS = 5;
    private final int CITIES = 4;
    private final int ROADS = 15;
    private final String name;
    private final Color color;
    private final HashMap<Integer, Integer> resources;
    private final HashMap<DevCard, Integer> devCards;
    private int settlements;
    private int cities;
    private int roads;
    private int knights;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        resources = new HashMap<>();
        devCards = new HashMap<>();
        settlements = 2;
        cities = 0;
        roads = 2;
        knights = 0;
    }

}
