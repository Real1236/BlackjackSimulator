package com.arthur.blackjack.models;

import java.util.List;

public interface Hand {
    List<Card> getCards();
    void addCard(Card card);
    void clearHand();
    Integer getHandValue();
    String toString();
}
