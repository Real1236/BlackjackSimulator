package com.arthur.blackjack.models.card;

import com.arthur.blackjack.models.card.Card;

public interface Deck {
    void reshuffleDeck();
    Card dealCard();
    boolean checkReshuffle();
}
