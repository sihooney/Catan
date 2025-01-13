package board;

import java.util.*;

public class Deck {

    private final LinkedList<DevCard> cards;

    public Deck() {
        cards = new LinkedList<>();
        for (int i = 0; i < 14; i++) {
            cards.add(new DevCard("Knight", DevCard.KNIGHT));
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

    public DevCard peek() {
        return cards.peekLast();
    }

    public void draw() {
        cards.pollLast();
    }
}
