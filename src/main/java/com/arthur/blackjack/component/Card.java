package com.arthur.blackjack.component;

public class Card {
    private final Rank rank;

    public Card(Rank rank) {
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public int getValue() {
        return rank.getValue();
    }

    public String toString() {
        return rank.toString();
    }
}
