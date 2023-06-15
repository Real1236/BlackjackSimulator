package com.arthur.blackjack.component;

import com.arthur.blackjack.core.GameRules;
import com.arthur.blackjack.core.GameSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Deck {
    private List<Card> cards;
    private final int numOfDecks;
    private final int depthToReshuffle;

    GameSettings gameSettings;
    GameRules gameRules;

    @Autowired
    public Deck(GameSettings gameSettings, GameRules gameRules) {
        this.gameSettings = gameSettings;
        this.gameRules = gameRules;
        numOfDecks = gameRules.getNumOfDecks();
        depthToReshuffle = gameSettings.getDepthToReshuffle();
        constructDeck();
    }

    public void constructDeck() {
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
