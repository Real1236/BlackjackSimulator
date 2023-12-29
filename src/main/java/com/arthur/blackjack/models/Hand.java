package com.arthur.blackjack.models;

import java.util.List;

public interface Hand {
    List<Card> getCards();
    void addCard(Card card);
    Integer getHandValue();
    String toString();
}
