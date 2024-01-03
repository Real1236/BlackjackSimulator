package com.arthur.blackjack.models.impl;

import com.arthur.blackjack.models.Card;
import com.arthur.blackjack.models.Rank;

public class CardImpl implements Card {

    Rank rank;

    public CardImpl(Rank rank) {
        this.rank = rank;
    }

    @Override
    public Rank getRank() {
        return rank;
    }
    
}
