package play;

import java.util.*;

public class Deck {

    private final ArrayList<DevCard> cards;

    public Deck() {
        cards = new ArrayList<>(25);
        for (int i = 0; i < 14; i++) {
            cards.add(new DevCard("Knight", DevCard.KNGIHT));
        }
        cards.add(new DevCard("Road building", DevCard.ROAD_BUILDING));
        cards.add(new DevCard("Road building", DevCard.ROAD_BUILDING));
        cards.add(new DevCard("Year of plenty", DevCard.YEAR_OF_PLENTY));
        cards.add(new DevCard("Year of plenty", DevCard.YEAR_OF_PLENTY));
        cards.add(new DevCard("Monopoly", DevCard.MONOPOLY));
        cards.add(new DevCard("Monopoly", DevCard.MONOPOLY));
        for (int i = 0; i < 5; i++) {
            cards.add(new DevCard("Victory Point", DevCard.VP));
        }
        Collections.shuffle(cards);
    }

    public DevCard draw() {
        if (!cards.isEmpty()) {
            return cards.remove(cards.size() - 1);
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
