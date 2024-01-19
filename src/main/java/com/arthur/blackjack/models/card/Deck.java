package com.arthur.blackjack.models.card;

import com.arthur.blackjack.strategies.Strategy;

public interface Deck {
    void setStrategy(Strategy strategy);

    void reshuffleDeck();

    Card dealCard();

    boolean checkReshuffle();
    float getNumOfDecksRemaining();
}
