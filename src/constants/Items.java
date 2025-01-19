package constants;

public class Items {

    public static final int ROAD = 0;
    public static final int SETTLEMENT = 1;
    public static final int CITY = 2;
    public static final int DEVCARD = 3;
    public static final String[] ITEMS = {"ROAD", "SETTLEMENT", "CITY", "DEVELOPMENT CARD"};
    public static final String BUILDING_COSTS = """
            BUILDING COSTS:
            
            ROAD:
            1 LUMBER, 1 BRICK, 0 VP
            
            SETTLEMENT:
            1 LUMBER, 1 BRICK, 1 GRAIN, 1 WOOL, 1 VP
            
            CITY:
            2 GRAIN, 2 ORE, 2 VPs
            
            DEVELOPMENT CARD:
            1 GRAIN, 1 WOOL, 1 ORE, ? VPs""";

}
