public class Card {
    private final Rank rank;

    public Card(Rank rank) {
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public int getValue(int currentTotal) {
        if (rank == Rank.ACE) {
            if (currentTotal + 11 <= 21) {
                return 11;
            } else {
                return 1;
            }
        } else {
            return rank.getValue();
        }
    }

    public String toString() {
        return rank.toString();
    }
}
