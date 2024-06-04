package com.arthur.blackjack.models.card.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.card.CardFactory;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.card.Rank;
import com.arthur.blackjack.strategies.Strategy;
import lombok.Setter;

public class DeckImpl implements Deck {

    private List<Card> cards;

    private final GameRules rules;
    @Setter
    private Strategy strategy;

    public DeckImpl(GameRules rules) {
        this.rules = rules;
    }

    @Override
    public void reshuffleDeck() {
        List<Card> oneDeck = new ArrayList<>();
        for (Rank rank : Rank.values())
            for (int i = 0; i < 4; i++)
                oneDeck.add(CardFactory.createCard(rank));

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
