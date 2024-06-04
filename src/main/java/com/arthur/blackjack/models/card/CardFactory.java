package com.arthur.blackjack.models.card;

import com.arthur.blackjack.models.card.impl.CardImpl;

public interface CardFactory {
    static Card createCard(Rank rank) {
        return new CardImpl(rank);
    }
}
