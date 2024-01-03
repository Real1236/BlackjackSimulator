package com.arthur.blackjack.models.card.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.card.CardFactory;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.card.Rank;

public class DeckImpl implements Deck {

    List<Card> cards;

    CardFactory cardFactory;

    GameSettings settings;
    GameRules rules;

    public DeckImpl(CardFactory cardFactory, GameSettings settings, GameRules rules) {
        this.cardFactory = cardFactory;
        this.settings = settings;
        this.rules = rules;
    }

    @Override
    public void reshuffleDeck() {
        List<Card> oneDeck = new ArrayList<>();
        for (Rank rank : Rank.values())
            for (int i = 0; i < 4; i++)
                oneDeck.add(cardFactory.createCard(rank));

        cards = new ArrayList<>(cards);
        for (int i = 0; i < rules.getNumOfDecks() - 1; i++)
            cards.addAll(oneDeck);

        Collections.shuffle(cards);
    }

    @Override
    public Card dealCard() {
        return cards.removeLast();
    }

    @Override
    public boolean checkReshuffle() {
        double percentOfShoeRemaining = ((double) cards.size()) / (rules.getNumOfDecks() * 52);
        int depth = (int) ((1 - percentOfShoeRemaining) * 100);
        return (depth >= rules.getDepthToReshuffle());
    }
    
}
