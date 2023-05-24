package com.arthur.blackjack.component;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards;
    private int bet;

    public Hand() {
        cards = new ArrayList<>();
    }

    public Hand(int bet) {
        this.bet = bet;
        cards = new ArrayList<>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int getTotal() {
        int total = 0;
        int numAces = 0;
        for (Card card : cards) {
            total += card.getValue();
            if (card.getRank() == Rank.ACE)
                numAces++;
        }
        while (numAces > 0 && total > 21) {
            total -= 10;
            numAces--;
        }
        return total;
    }

    public Card removeCard() {
        return cards.remove(cards.size() - 1);
    }

    public boolean isHard() {
        int total = 0;
        int numAces = 0;
        for (Card card : cards) {
            total += card.getValue();
            if (card.getRank() == Rank.ACE)
                numAces++;
        }
        return numAces <= 0 || total <= 21;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
}
