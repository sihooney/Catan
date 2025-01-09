package board;

import java.util.Objects;

public class DevCard {

    private final String name;
    private final int type;
    protected static final int KNIGHT = 0;
    protected static final int ROAD_BUILDING = 1;
    protected static final int YEAR_OF_PLENTY = 2;
    protected static final int MONOPOLY = 3;
    protected static final int VP = 4;

    public DevCard(String name, int type) {
        this.name = name;
        this.type = type;
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
