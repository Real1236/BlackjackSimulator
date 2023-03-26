public class Player {
    private Hand hand;
    private int money;
    private int bet;

    public Player() {
    }

    public Player(int startingMoney) {
        hand = new Hand();
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
        hand.addCard(card);
    }

    public Hand getHand() {
        return hand;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void clearHand() {
        hand = new Hand();
    }

    public boolean canSplit() {
        if (hand.getCards().size() == 2 && hand.getCards().get(0).getRank() == hand.getCards().get(1).getRank() && bet <= money) {
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
