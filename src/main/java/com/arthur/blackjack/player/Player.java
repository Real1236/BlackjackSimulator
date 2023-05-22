package com.arthur.blackjack.player;

import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.component.Hand;

import java.util.*;

public class Player {
    private final int id;
    private final List<Hand> hands;
    private int money;

    public Player(int id, int startingMoney) {
        this.id = id;
        hands = new ArrayList<>();
        money = startingMoney;
    }

    public int getId() {
        return id;
    }

    public int getMoney() {
        return money;
    }

    public void placeBet(int hand) {
        if (getHand(hand).getBet() <= money) {
            money -= getHand(hand).getBet();
        } else {
            System.out.println("Error: not enough money to place bet.");
        }
    }

    public void winBet(int hand) {
        money += 2 * getHand(hand).getBet();
    }

    public void winBlackjack(int hand) {
        money += 2.5 * getHand(hand).getBet();
    }

    public void pushBet(int hand) {
        money += getHand(hand).getBet();
    }

    public void addCard(Card card) {
        this.getHand().addCard(card);
    }

    public void addCard(Card card, int hand) {
        this.getHand(hand).addCard(card);
    }

    public void addHand() {
        hands.add(new Hand());
    }

    public void addHand(int bet) {
        hands.add(new Hand(bet));
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

    public void clearHand(int index) {
        hands.remove(index);
    }

    public boolean canSplit(int hand) {
        return getHand(hand).getCards().size() == 2
                && getHand(hand).getCards().get(0).getRank().getValue() == getHand(hand).getCards().get(1).getRank().getValue()
                && getHand(hand).getBet() <= money;
    }

    public boolean canDouble(int hand) {
        return getHand(hand).getCards().size() == 2 && getHand(hand).getBet() <= money;
    }

    public void takeTurn(int minimumBet, Dealer dealer, Deck deck) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlayer " + this.getId() + "'s turn");
        System.out.println("Money: " + this.getMoney());
        System.out.println("\nPlace your bet:");
        int bet = Integer.parseInt(scanner.nextLine());
        while (bet < minimumBet) {
            System.out.println("Place a bet greater than or equal to the minimum bet:");
            bet = Integer.parseInt(scanner.nextLine());
        }
        this.getHand().setBet(bet);
        playHand(0, dealer, deck);
    }

    public void playHand(int hand, Dealer dealer, Deck deck) {
        this.placeBet(hand);
        boolean hasStood = false;

        while (this.getHand(hand).getTotal() < 21 && !hasStood) {
            System.out.println("\nYour cards: " + this.getHand(hand));
            System.out.println("Your score: " + this.getHand(hand).getTotal());
            System.out.println("Dealer's upcard: " + dealer.getHand().getCards().get(0));

            String choice = getPlayerChoice(hand);
            hasStood = performPlayerAction(choice, hand, dealer, deck);
        }
    }

    public String getPlayerChoice(int hand) {
        Scanner scanner = new Scanner(System.in);
        Map<String, String> choices = getChoices(hand);

        System.out.println("Choose an option: " + choices + "");
        String choice = scanner.nextLine();

        while (!choices.containsKey(choice)) {
            System.out.println("Invalid choice, pick again:");
            choice = scanner.nextLine();
        }

        return choice;
    }

    private Map<String, String> getChoices(int hand) {
        Map<String, String> choices = new HashMap<>();
        choices.put("h", "hit");
        choices.put("s", "stand");
        if (this.canDouble(hand))
            choices.put("d", "double down");
        if (this.canSplit(hand))
            choices.put("p", "split");
        return choices;
    }

    public boolean performPlayerAction(String choice, int hand, Dealer dealer, Deck deck) {
        switch (choice) {
            case "h" -> dealer.dealCard(this, hand, deck);
            case "s" -> { return true; }
            case "d" -> {
                this.placeBet(hand);
                int curBet = this.getHand(hand).getBet();
                this.getHand(hand).setBet(curBet * 2);
                dealer.dealCard(this, hand, deck);
                return true;
            }
            case "p" -> {
                Card temp = this.getHand(hand).removeCard();
                dealer.dealCard(this, hand, deck);
                this.addHand(this.getHand(hand).getBet());
                this.getHand().addCard(temp);
                dealer.dealCard(this, deck);
                playHand(this.getNumOfHands() - 1, dealer, deck);
            }
            default -> System.out.println("Invalid choice. Please choose 'h', 's', or 'p'.");
        }
        return false;
    }

    public void evaluateHand(int handIndex, Dealer dealer) {
        Hand hand = this.getHand(0);
        System.out.println("Player " + this.getId() + " - Hand " + handIndex + ": " + hand);
        System.out.println("Player " + this.getId() + " - Hand " + handIndex + " score: " + hand.getTotal());

        if (hand.getTotal() == 21 && hand.getCards().size() == 2) {
            System.out.println("Player " + this.getId() + " - Hand " + handIndex + " gets Blackjack!");
            this.winBlackjack(0);
        } else if (hand.getTotal() > 21) {
            System.out.println("Player " + this.getId() + " - Hand " + handIndex + " busts!");
        } else if (dealer.getHand().getTotal() > 21 || hand.getTotal() > dealer.getHand().getTotal()) {
            System.out.println("Player " + this.getId() + " - Hand " + handIndex + " wins!");
            this.winBet(0);
        } else if (hand.getTotal() == dealer.getHand().getTotal()) {
            System.out.println("Player " + this.getId() + " - Hand " + handIndex + " pushes.");
            this.pushBet(0);
        } else {
            System.out.println("Player " + this.getId() + " - Hand " + handIndex + " loses.");
        }
    }
}
