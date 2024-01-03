package com.arthur.blackjack.models.card.impl;

import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.card.CardFactory;
import com.arthur.blackjack.models.card.Rank;

public class CardFactoryImpl implements CardFactory {

    @Override
    public Card createCard(Rank rank) {
        return new CardImpl(rank);
    }
    
}
