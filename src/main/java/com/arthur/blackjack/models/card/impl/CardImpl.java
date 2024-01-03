package com.arthur.blackjack.models.card.impl;

import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.card.Rank;

public class CardImpl implements Card {

    Rank rank;

    public CardImpl(Rank rank) {
        this.rank = rank;
    }

    @Override
    public Rank getRank() {
        return rank;
    }

    public String toString() {
        return rank.toString();
    }
    
}
