package com.arthur.blackjack.models.card;

public interface Deck {
    void reshuffleDeck();

    Card dealCard();

    boolean checkReshuffle();
}
