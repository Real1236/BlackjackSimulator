package com.arthur.player;

import com.arthur.component.Card;
import com.arthur.component.Hand;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
    private List<Hand> hands;
    private int money;

    public Player(int id, int startingMoney) {
        this.id = id;
        hands = new ArrayList<>();
        money = startingMoney;
    }

    public void placeBet(int hand) {
        if (getHand(hand).getBet() <= money) {
            money -= getHand(hand).getBet();
        } else {
            System.out.println("Error: not enough money to place bet.");
        }
    }

    public int getId() {
        return id;
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

    public List<Hand> getHands() {
        return hands;
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
                && getHand(hand).getBet() <= money;
    }

    public boolean canDouble(int hand) {
        return getHand(hand).getCards().size() == 2 && getHand(hand).getBet() <= money;
    }
}
