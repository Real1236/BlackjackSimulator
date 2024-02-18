package com.arthur.blackjack.models.card.impl;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.strategies.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.card.CardFactory;
import com.arthur.blackjack.models.card.Rank;

@Component
public class CardFactoryImpl implements CardFactory {

    private final GameRules rules;
    private final Strategy strategy;

    @Autowired
    public CardFactoryImpl(GameRules rules, Strategy strategy) {
        this.rules = rules;
        this.strategy= strategy;
    }

    @Override
    public Card createCard(Rank rank) {
        return new CardImpl(rank);
    }

    @Override
    public Deck createDeck() {
        return new DeckImpl(this, rules, strategy);
    }
}
