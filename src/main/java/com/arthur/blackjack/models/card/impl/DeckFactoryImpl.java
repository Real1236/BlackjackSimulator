package com.arthur.blackjack.models.card.impl;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.card.DeckFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeckFactoryImpl implements DeckFactory {

    private final GameRules rules;

    @Autowired
    public DeckFactoryImpl(GameRules rules) {
        this.rules = rules;
    }

    @Override
    public Deck createDeck() {
        return new DeckImpl(rules);
    }
}
