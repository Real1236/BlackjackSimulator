package com.arthur.blackjack.models.hand.impl;

import java.util.List;

import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.card.Rank;
import com.arthur.blackjack.models.hand.Hand;

public abstract class AbstractHand implements Hand {

    protected List<Card> cards;

    public AbstractHand(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    @Override
    public void addCard(Card card) {
        cards.add(card);
    }

    @Override
    public int getHandValue() {
        int total = 0;
        int numAces = 0;
        for (Card card : cards) {
            Rank cardRank = card.getRank();
            total += cardRank.getValue();
            if (cardRank == Rank.ACE)
                numAces++;
        }
        while (numAces > 0 && total > 21) {
            total -= 10;
            numAces--;
        }
        return total;
    }
    
}
