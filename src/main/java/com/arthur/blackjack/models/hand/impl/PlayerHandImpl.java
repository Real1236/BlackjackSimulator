package com.arthur.blackjack.models.hand.impl;

import java.util.ArrayList;
import java.util.List;

import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.hand.PlayerHand;
import com.arthur.blackjack.models.card.Rank;

public class PlayerHandImpl implements PlayerHand {

    private List<Card> cards;
    private int bet;

    public PlayerHandImpl() {
        this.cards = new ArrayList<Card>();
        this.bet = 0;
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
    public void clearHand() {
        cards.clear();
    }

    @Override
    public Integer getHandValue() {
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

    @Override
    public Integer getBet() {
        return bet;
    }

    @Override
    public void setBet(Integer bet) {
        this.bet = bet;
    }
    
}
