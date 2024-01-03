package com.arthur.blackjack.models;

public interface Deck {
    void reshuffleDeck();
    Card dealCard();
    boolean checkReshuffle();
}
