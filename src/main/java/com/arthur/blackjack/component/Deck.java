package com.arthur.blackjack.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;
    private int numOfDecks;

    public Deck(int numOfDecks) {
        cards = new ArrayList<>();
        for (Rank rank : Rank.values()) {
            for (int i = 0; i < 4; i++) {
                cards.add(new Card(rank));
            }
        }
        List<Card> deck = new ArrayList<>(cards);
        for (int i = 0; i < numOfDecks - 1; i++) {
            cards.addAll(deck);
        }
        this.numOfDecks = numOfDecks;
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        if (cards.size() == 0)
            return null;
        return cards.remove(cards.size() - 1);
    }

    public List<Card> getCards() {
        return cards;
    }
}
