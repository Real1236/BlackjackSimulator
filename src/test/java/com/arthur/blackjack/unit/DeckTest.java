package com.arthur.blackjack.unit;

import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.config.LoggerConfig;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.testng.Assert.*;

public class DeckTest {
    private static final Logger logger = Logger.getLogger(DeckTest.class.getName());

    @BeforeClass
    public static void setUpLogger() {
        LoggerConfig.configure(logger);
    }

    @Test
    public void testDeckSize() {
        Deck deck = new Deck(1);
        logger.log(Level.INFO, "Deck created with 1 deck.");
        int expectedSize = 52;
        assertEquals(expectedSize, deck.getCards().size(), "Deck size is not correct.");
        logger.log(Level.INFO, "testDeckSize passed.");
    }

    @Test
    public void testDealCard() {
        Deck deck = new Deck(1);
        logger.log(Level.INFO, "Deck created with 1 deck.");
        int expectedSize = 51;
        assertNotNull(deck.dealCard(), "No card was dealt.");
        assertEquals(expectedSize, deck.getCards().size(), "Deck size is not correct after dealing a card.");
        logger.log(Level.INFO, "testDealCard passed.");
    }

    @Test
    public void testShuffle() {
        Deck deck = new Deck(1);
        logger.log(Level.INFO, "Deck created with 1 deck.");
        Card firstCard = deck.getCards().get(0);
        for (int i = 0; i < 5; i++) {
            deck.shuffle();
            if (!deck.getCards().get(0).equals(firstCard))
                break;
        }
        assertNotEquals(deck.getCards().get(0), firstCard, "Deck has not been shuffled.");
        logger.info("testShuffle passed.");
    }

}

