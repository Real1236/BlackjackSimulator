package com.arthur.blackjack.integration;

import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.component.Rank;
import com.arthur.blackjack.config.LoggerConfig;
import com.arthur.blackjack.core.Game;
import com.arthur.blackjack.core.GameRules;
import com.arthur.blackjack.core.GameSettings;
import com.arthur.blackjack.player.Dealer;
import com.arthur.blackjack.player.Player;
import com.arthur.blackjack.player.PlayerFactory;
import com.arthur.blackjack.simulation.StrategyTableReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class GameIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(GameIntegrationTest.class.getName());

    @BeforeAll
    public static void setUpLogger() {
        LoggerConfig.configure(LOGGER);
    }

    private GameRules gameRules;
    private GameSettings gameSettings;
    private StrategyTableReader strategyTableReader;

    @BeforeEach
    public void setUp() {
        gameRules = new GameRules(8, true, false, 2, false, false, false, false, 3.0/2.0);
        gameSettings = new GameSettings(75, 1, 100, 20);
        strategyTableReader = mock(StrategyTableReader.class);
    }

    @Test
    public void testBlackjackWin() {
        LOGGER.info("Testing winning Blackjack scenario");

        int bankroll = gameSettings.getStartingBankroll();
        int bet = gameSettings.getBet();

        Deck deck = new Deck(gameSettings, gameRules);

        // Dealer Cards
        deck.getCards().add(new Card(Rank.QUEEN));
        deck.getCards().add(new Card(Rank.SEVEN));

        // Player cards
        deck.getCards().add(new Card(Rank.ACE));
        deck.getCards().add(new Card(Rank.TEN));

        Player player = new Player(gameSettings, strategyTableReader);
        player.setMoney(bankroll);

        Game game = new Game(gameSettings, deck, new Dealer(gameRules), new PlayerFactory(gameSettings, strategyTableReader));
        game.getPlayers().add(player);
        game.startRound();

        assertEquals(bankroll + bet * gameRules.getBlackjackPayout(), player.getMoney(), "Incorrect payout for player");
    }
}
