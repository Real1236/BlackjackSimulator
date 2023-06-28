package com.arthur.blackjack.unit;

import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.component.Rank;
import com.arthur.blackjack.config.LoggerConfig;
import com.arthur.blackjack.core.GameRules;
import com.arthur.blackjack.core.GameSettings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeckTest {
    private static final Logger LOGGER = Logger.getLogger(DeckTest.class.getName());

    @BeforeAll
    public static void setUpLogger() {
        LoggerConfig.configure(LOGGER);
    }

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @MockBean
    private GameRules gameRules;
    @MockBean
    private GameSettings gameSettings;

    @BeforeEach()
    public void setUp() {
        gameRules = mock(GameRules.class);
        gameSettings = mock(GameSettings.class);
        when(gameRules.getNumOfDecks()).thenReturn(8);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    public void testDeckSize() {
        LOGGER.info("Testing deck size");
        Deck deck = new Deck(gameSettings, gameRules);
        assertEquals(52 * gameRules.getNumOfDecks(), deck.getCards().size(), "Deck size is not correct.");
    }

    @Test
    public void testDealCard() {
        Deck deck = new Deck(gameSettings, gameRules);
        int expectedSize = gameRules.getNumOfDecks() * 52 - 1;
        assertNotNull(deck.dealCard(), "No card was dealt.");
        assertEquals(expectedSize, deck.getCards().size(), "Deck size is not correct after dealing a card.");
    }

    @Test
    public void testCheckReshuffle() {
        LOGGER.info("Testing reshuffling at certain depth");
        int depthToReshuffle = 37;
        when(gameSettings.getDepthToReshuffle()).thenReturn(depthToReshuffle);
        Deck deck = new Deck(gameSettings, gameRules);

        int cardsNeededToAchieveDepth = (int) Math.ceil(52 * gameRules.getNumOfDecks() * ((double) depthToReshuffle/100));
        for (int i = 0; i < cardsNeededToAchieveDepth; i++)
            deck.dealCard();
        assertEquals(52 * gameRules.getNumOfDecks() - cardsNeededToAchieveDepth, deck.getCards().size(), "Error with dealing cards");
        deck.checkReshuffle();
        assertEquals("RESHUFFLING DECK", outputStreamCaptor.toString().trim());
        assertEquals(52 * gameRules.getNumOfDecks(), deck.getCards().size(), "Depth was achieved but deck was not reshuffled");
    }

    @Test
    public void testCheckReshuffleNoReshuffle() {
        LOGGER.info("Testing no reshuffling if depth isn't achieved");
        int depthToReshuffle = 200;
        when(gameSettings.getDepthToReshuffle()).thenReturn(depthToReshuffle);

        Deck deck = spy(new Deck(gameSettings, gameRules));
        deck.checkReshuffle();

        verify(deck, never()).constructDeck();
    }

    @Test
    public void testShuffle() {
        Deck deck = new Deck(gameSettings, gameRules);
        Rank firstCardRank = deck.getCards().get(0).getRank();
        for (int i = 0; i < 5; i++) {
            deck = new Deck(gameSettings, gameRules);
            if (!deck.getCards().get(0).getRank().equals(firstCardRank))
                break;
        }
        assertNotEquals(deck.getCards().get(0).getRank(), firstCardRank, "Deck has not been shuffled.");
    }

}

