package com.arthur.blackjack.component;

import com.arthur.blackjack.core.GameRules;
import com.arthur.blackjack.core.GameSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
    private final int numOfDecks;
    private final int depthToReshuffle;

    public Deck() {
        numOfDecks = GameRules.numOfDecks;
        constructDeck();
        depthToReshuffle = GameSettings.depthToReshuffle;
    }

    public Deck(int numOfDecks) {
        this.numOfDecks = numOfDecks;
        constructDeck();
        depthToReshuffle = GameSettings.depthToReshuffle;
    }

    private void constructDeck() {
        cards = new ArrayList<>();
        for (Rank rank : Rank.values())
            for (int i = 0; i < 4; i++)
                cards.add(new Card(rank));

        List<Card> deck = new ArrayList<>(cards);
        for (int i = 0; i < numOfDecks - 1; i++)
            cards.addAll(deck);

        Collections.shuffle(cards);
    }

    public Card dealCard() {
        if (cards.size() == 0)
            return null;
        return cards.remove(cards.size() - 1);
    }

    public void checkReshuffle() {
        double percentOfShoeRemaining = ((double) cards.size()) / (numOfDecks * 52);
        int depth = (int) ((1 - percentOfShoeRemaining) * 100);
        if (depth >= depthToReshuffle) {
            System.out.println("\nRESHUFFLING DECK\n");
            constructDeck();
        }
    }

    public List<Card> getCards() {
        return cards;
    }
}
