import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int getTotal() {
        int total = 0;
        int numAces = 0;
        for (Card card : cards) {
            total += card.getValue(total);
            if (card.getRank() == Rank.ACE) {
                numAces++;
            }
        }
        while (numAces > 0 && total > 21) {
            total -= 10;
            numAces--;
        }
        return total;
    }

    public List<Card> getCards() {
        return cards;
    }

    public Card removeCard() {
        return cards.remove(cards.size() - 1);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
}
