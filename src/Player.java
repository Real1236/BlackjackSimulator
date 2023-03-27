import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Hand> hands;
    private int money;
    private int bet;

    public Player() {
    }

    public Player(int startingMoney) {
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

    public List<Hand> getHands() {
        return hands;
    }

    public void addHand() {
        hands.add(new Hand());
    }

    public Hand getHand() {
        return hands.get(hands.size() - 1);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void clearHand() {
        hands.remove(hands.size() - 1);
    }

    public boolean canSplit() {
        if (getHand().getCards().size() == 2 && getHand().getCards().get(0).getRank() == getHand().getCards().get(1).getRank() && bet <= money) {
            return true;
        } else {
            return false;
        }
    }

    public void bet(int amount) {
        money -= amount;
    }

    public void win(int bet) {
        money += bet;
    }
}
