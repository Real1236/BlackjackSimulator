package com.arthur.blackjack.models.card.impl;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.models.card.CardFactory;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.card.DeckFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeckFactoryImpl implements DeckFactory {

    private final CardFactory cardFactory;
    private final GameRules rules;

    @Autowired
    public DeckFactoryImpl(CardFactory cardFactory, GameRules rules) {
        this.cardFactory = cardFactory;
        this.rules = rules;
    }

    @Override
    public Deck createDeck() {
        return new DeckImpl(cardFactory, rules);
    }
}
