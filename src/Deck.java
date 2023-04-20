import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
    private int numOfDecks;

    public Deck(int numOfDecks) {
        cards = new ArrayList<>();
        for (Rank rank : Rank.values()) {
            for (int i = 0; i < 4; i++) {
                cards.add(new Card(rank));
            }
        }
        this.numOfDecks = numOfDecks;
    }

    public void shuffle() {
        Collections.shuffle(cards);

        // Rigging deck for testing purposes
        cards.add(new Card(Rank.TEN));
        cards.add(new Card(Rank.TEN));
    }

    public Card dealCard() {
        if (cards.size() == 0) {
            return null;
        }
        return cards.remove(cards.size() - 1);
    }
}
