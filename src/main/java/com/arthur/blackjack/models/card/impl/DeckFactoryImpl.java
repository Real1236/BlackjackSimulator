package com.arthur.blackjack.models.card.impl;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.models.card.*;
import com.arthur.blackjack.strategies.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeckFactoryImpl implements DeckFactory {

    private final CardFactory cardFactory;
    private final GameRules rules;
    private final Strategy strategy;

    @Autowired
    public DeckFactoryImpl(CardFactory cardFactory, GameRules rules, Strategy strategy) {
        this.cardFactory = cardFactory;
        this.rules = rules;
        this.strategy= strategy;
    }

    @Override
    public Deck createDeck() {
        return new DeckImpl(cardFactory, rules, strategy);
    }
}
