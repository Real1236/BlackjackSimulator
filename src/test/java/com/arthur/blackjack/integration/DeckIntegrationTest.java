package com.arthur.blackjack.integration;

import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.component.Rank;
import com.arthur.blackjack.config.LoggerConfig;
import com.arthur.blackjack.core.GameRules;
import com.arthur.blackjack.core.GameSettings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeckIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(DeckIntegrationTest.class.getName());

    @BeforeAll
    public static void setUpLogger() {
        LoggerConfig.configure(LOGGER);
    }

    @MockBean
    private GameRules gameRules;
    @MockBean
    private GameSettings gameSettings;

    @BeforeEach()
    public void setUp() {
        gameRules = mock(GameRules.class);
        gameSettings = mock(GameSettings.class);
    }

    @Test
    public void testDeckCreation() {
        when(gameRules.getNumOfDecks()).thenReturn(8);
        Deck deck = new Deck(gameSettings, gameRules);

        // Count the number of cards for each rank
        Map<Rank, Integer> rankCountMap = new EnumMap<>(Rank.class);
        for (Card card : deck.getCards()) {
            rankCountMap.put(card.getRank(), rankCountMap.getOrDefault(card.getRank(), 0) + 1);
        }

        // Verify that each rank has four copies
        for (Rank rank : Rank.values()) {
            int expectedCount = 4 * gameRules.getNumOfDecks();
            int actualCount = rankCountMap.getOrDefault(rank, 0);
            assertEquals(expectedCount, actualCount, "Incorrect number of cards for rank " + rank);
        }
    }
}
