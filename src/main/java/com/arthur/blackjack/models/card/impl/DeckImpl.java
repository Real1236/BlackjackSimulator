package com.arthur.blackjack.models.card.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.card.CardFactory;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.card.Rank;
import com.arthur.blackjack.strategies.Strategy;

@Component
public class DeckImpl implements Deck {

    List<Card> cards;

    CardFactory cardFactory;

    GameSettings settings;
    GameRules rules;
    Strategy strategy;

    public DeckImpl(CardFactory cardFactory, GameSettings settings, GameRules rules) {
        this.cardFactory = cardFactory;
        this.settings = settings;
        this.rules = rules;
    }

    @Override
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void reshuffleDeck() {
        List<Card> oneDeck = new ArrayList<>();
        for (Rank rank : Rank.values())
            for (int i = 0; i < 4; i++)
                oneDeck.add(cardFactory.createCard(rank));

        cards = new ArrayList<>(oneDeck);
        for (int i = 0; i < rules.getNumOfDecks() - 1; i++)
            cards.addAll(oneDeck);

        Collections.shuffle(cards);
        strategy.resetDeckComposition(); // Reset the deck composition in the Excel
    }

    @Override
    public Card dealCard() {
        Card card = cards.removeLast();
        strategy.countCard(card); // Count the card that was dealt
        return card;
    }

    @Override
    public boolean checkReshuffle() {
        double depth = 1 - ((double) cards.size()) / (rules.getNumOfDecks() * 52);
        return (depth >= rules.getDepthToReshuffle());
    }

}
