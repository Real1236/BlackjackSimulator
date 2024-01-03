package com.arthur.blackjack.models.impl;

import com.arthur.blackjack.models.Card;
import com.arthur.blackjack.models.CardFactory;
import com.arthur.blackjack.models.Rank;

public class CardFactoryImpl implements CardFactory {

    @Override
    public Card createCard(Rank rank) {
        return new CardImpl(rank);
    }
    
}
