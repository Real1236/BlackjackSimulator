import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
    private List<Hand> hands;
    private int money;
    private int bet;

    public Player(int id, int startingMoney) {
        this.id = id;
        hands = new ArrayList<>();
        money = startingMoney;
        bet = 0;
    }

    public void placeBet(int betAmount) {
        if (betAmount <= money) {
            bet = betAmount;
            money -= betAmount;
        } else {
            System.out.println("Error: not enough money to place bet.");
        }
    }

    public int getId() {
        return id;
    }

    public int getBet() {
        return bet;
    }

    public void winBet() {
        money += 2 * bet;
    }

    public void pushBet() {
        money += bet;
    }

    public void addCard(Card card) {
        this.getHand().addCard(card);
    }

    public void addCard(Card card, int hand) {
        this.getHand(hand).addCard(card);
    }

    public List<Hand> getHands() {
        return hands;
    }

    public void addHand() {
        hands.add(new Hand());
    }

    public Hand getHand() {
        return hands.get(hands.size() - 1);
    }

    public Hand getHand(int hand) {
        return hands.get(hand);
    }

    public int getNumOfHands() {
        return hands.size();
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void clearHand(int index) {
        hands.remove(index);
    }

    public boolean canSplit(int hand) {
        return getHand(hand).getCards().size() == 2
                && getHand(hand).getCards().get(0).getRank().getValue() == getHand(hand).getCards().get(1).getRank().getValue()
                && bet <= money;
    }

    public void bet(int amount) {
        money -= amount;
    }

    public void win(int bet) {
        money += bet;
    }
}
