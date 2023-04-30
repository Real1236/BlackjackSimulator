package com.arthur.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
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
    }

    public void shuffle() {
        Collections.shuffle(cards);

        // Rigging deck for testing purposes
        // Dealer busts on this card
        cards.add(new Card(Rank.TEN));
        // Split Hands
        cards.add(new Card(Rank.KING));
        cards.add(new Card(Rank.KING));
        cards.add(new Card(Rank.SIX));
        // Dealer
        cards.add(new Card(Rank.SIX));
        cards.add(new Card(Rank.TEN));
        // Initial Hand
        cards.add(new Card(Rank.FIVE));
        cards.add(new Card(Rank.FIVE));
    }

    public Card dealCard() {
        if (cards.size() == 0) {
            return null;
        }
        return cards.remove(cards.size() - 1);
    }
}
