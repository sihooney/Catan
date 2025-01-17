package board;

import java.util.Objects;

public class DevCard {

    private final String name;
    private final int type;
    public static final int KNIGHT = 0;
    public static final int ROAD_BUILDING = 1;
    public static final int YEAR_OF_PLENTY = 2;
    public static final int MONOPOLY = 3;
    public static final int VP = 4;
    public static final String[] NAMES = {"KNIGHT", "ROAD BUILDING", "YEAR OF PLENTY", "MONOPOLY", "VP CARD"};

    public DevCard(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevCard devCard = (DevCard) o;
        return type == devCard.type && Objects.equals(name, devCard.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
