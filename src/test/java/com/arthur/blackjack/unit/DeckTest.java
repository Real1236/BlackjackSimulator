package com.arthur.blackjack.unit;

import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.config.LoggerConfig;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class DeckTest {
    private static final Logger logger = Logger.getLogger(DeckTest.class.getName());

    @BeforeClass
    public static void setUpLogger() {
        LoggerConfig.configure(logger);
    }

    @Test
    public void testDeckSize() {
        Deck deck = new Deck(4);
        logger.log(Level.INFO, "Deck created with 1 deck.");
        int expectedSize = 208;
        assertEquals("Deck size is not correct.", expectedSize, deck.getCards().size());
        logger.log(Level.INFO, "testDeckSize passed.");
    }

    @Test
    public void testDealCard() {
        Deck deck = new Deck(1);
        logger.log(Level.INFO, "Deck created with 1 deck.");
        int expectedSize = 51;
        assertNotNull("No card was dealt.", deck.dealCard());
        assertEquals("Deck size is not correct after dealing a card.", expectedSize, deck.getCards().size());
        logger.log(Level.INFO, "testDealCard passed.");
    }

    @Test
    public void testCheckReshuffle() {
        logger.info("Testing reshuffling at certain depth");
        int numOfDecks = 4;
        int depthToReshuffle = 37;
        Deck deck = new Deck(numOfDecks);
        deck.setDepthToReshuffle(depthToReshuffle);

        int cardsNeededToAchieveDepth = (int) Math.ceil(52 * numOfDecks * ((double) depthToReshuffle/100));
        for (int i = 0; i < cardsNeededToAchieveDepth; i++)
            deck.dealCard();
        assertEquals("Error with dealing cards", 52 * numOfDecks - cardsNeededToAchieveDepth, deck.getCards().size());
        deck.checkReshuffle();
        assertEquals("Depth was achieved but deck was not reshuffled", 52 * numOfDecks, deck.getCards().size());
    }

    @Test
    public void testShuffle() {
        Deck deck = new Deck(1);
        logger.log(Level.INFO, "Deck created with 1 deck.");
        Card firstCard = deck.getCards().get(0);
        for (int i = 0; i < 5; i++) {
            deck = new Deck(1);
            if (!deck.getCards().get(0).equals(firstCard))
                break;
        }
        assertNotEquals("Deck has not been shuffled.", deck.getCards().get(0), firstCard);
        logger.info("testShuffle passed.");
    }

}

