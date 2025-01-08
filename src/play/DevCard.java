package play;

public class DevCard {

    private final String name;
    private final int type;
    protected static final int KNGIHT = 0;
    protected static final int ROAD_BUILDING = 1;
    protected static final int YEAR_OF_PLENTY = 2;
    protected static final int MONOPOLY = 3;
    protected static final int VP = 4;

    public DevCard(String name, int type) {
        this.name = name;
        this.type = type;
    }
}
