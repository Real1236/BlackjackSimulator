package com.arthur.blackjack.integration;

import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.component.Rank;
import com.arthur.blackjack.config.LoggerConfig;
import com.arthur.blackjack.core.GameRules;
import com.arthur.blackjack.core.GameSettings;
import com.arthur.blackjack.player.Dealer;
import com.arthur.blackjack.player.Player;
import com.arthur.blackjack.simulation.StrategyTableReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameRulesIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(GameRulesIntegrationTest.class.getName());

    @BeforeAll
    public static void setUpLogger() {
        LoggerConfig.configure(LOGGER);
    }

    private GameRules gameRules;
    private GameSettings gameSettings;
    private StrategyTableReader strategyTableReader;

    @BeforeEach
    public void setUp() {
        gameRules = mock(GameRules.class);
        gameSettings = new GameSettings(50, 1, 1000, 20, 1);
        strategyTableReader = new StrategyTableReader();
    }

    @Test
    public void testReplitAcesFirstHand(){
        LOGGER.info("Testing can resplit aces on initial hand");

        // Dealer Cards
        Dealer dealer = new Dealer(gameRules);
        dealer.getHand().getCards().add(new Card(Rank.SEVEN));
        dealer.getHand().getCards().add(new Card(Rank.NINE));

        // Player cards
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(gameSettings.getStartingBankroll());
        player.addHand();
        player.getHand().addCard(new Card(Rank.ACE));
        player.getHand().addCard(new Card(Rank.ACE));

        when(gameRules.isResplitAces()).thenReturn(false);
        when(gameRules.getResplitLimit()).thenReturn(2);
        player.takeTurn(dealer, new Deck(gameSettings, gameRules));

        assertTrue(player.getNumOfHands() > 1, "Player did not split");
    }
}
