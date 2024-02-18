package com.arthur.blackjack.models.card;

import com.arthur.blackjack.strategies.Strategy;

public interface Deck {
    void reshuffleDeck();

    Card dealCard();

    boolean checkReshuffle();
    void setStrategy(Strategy strategy);
}
