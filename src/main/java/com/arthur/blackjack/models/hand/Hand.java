package com.arthur.blackjack.models.hand;

import com.arthur.blackjack.models.card.Card;

import java.util.List;

public interface Hand {
    List<Card> getCards();
    void addCard(Card card);
    void clearHand();
    Integer getHandValue();
    String toString();
}
