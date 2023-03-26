import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
    private int numOfDecks;

    public Deck(int numOfDecks) {
        cards = new ArrayList<Card>();
        for (Rank rank : Rank.values()) {
            for (int i = 0; i < 4; i++) {
                cards.add(new Card(rank));
            }
        }
        this.numOfDecks = numOfDecks;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        if (cards.size() == 0) {
            return null;
        }
        Card card = cards.get(0);
        cards.remove(0);
        return card;
    }
}
