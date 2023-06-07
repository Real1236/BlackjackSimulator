package com.arthur.blackjack.integration;

import com.arthur.blackjack.component.Card;
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
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(PlayerIntegrationTest.class.getName());

    @BeforeAll
    public static void setUpLogger() {
        LoggerConfig.configure(LOGGER);
    }

    @MockBean
    private GameRules gameRules;
    @MockBean
    private GameSettings gameSettings;
    @MockBean
    private StrategyTableReader strategyTableReader;

    @BeforeEach()
    public void setUp() {
        gameRules = mock(GameRules.class);
        gameSettings = mock(GameSettings.class);
        strategyTableReader = mock(StrategyTableReader.class);
        when(gameSettings.getStartingBankroll()).thenReturn(100);
        when(gameSettings.getBet()).thenReturn(40);
    }

    @Test
    public void testBlackjackPayout() {
        LOGGER.info("Testing winning Blackjack scenario");

        int bankroll = gameSettings.getStartingBankroll();
        int bet = gameSettings.getBet();

        Player player = new Player(gameSettings, strategyTableReader);
        player.setMoney(bankroll);

        player.addHand();
        player.addCard(new Card(Rank.ACE));
        player.addCard(new Card(Rank.TEN));
        player.getHand().setBet(bet);

        Dealer dealer = new Dealer(gameRules);
        dealer.getHand().addCard(new Card(Rank.QUEEN));
        dealer.getHand().addCard(new Card(Rank.SIX));

        player.evaluateHand(0, dealer);

        assertEquals(bankroll + bet * 2.5, player.getMoney(), "Incorrect payout for player");
    }

    @Test
    public void testPushBlackjackPayout() {
        LOGGER.info("Testing pushing Blackjack scenario");

        int bankroll = gameSettings.getStartingBankroll();
        int bet = gameSettings.getBet();

        Player player = new Player(gameSettings, strategyTableReader);
        player.setMoney(bankroll);

        player.addHand();
        player.addCard(new Card(Rank.ACE));
        player.addCard(new Card(Rank.TEN));
        player.getHand().setBet(bet);

        Dealer dealer = new Dealer(gameRules);
        dealer.getHand().addCard(new Card(Rank.ACE));
        dealer.getHand().addCard(new Card(Rank.TEN));

        player.evaluateHand(0, dealer);

        assertEquals(bankroll + bet, player.getMoney(), "Incorrect payout for player");
    }

    @Test
    public void testGreaterHand() {
        LOGGER.info("Testing winning in greater hand scenario");

        int bankroll = gameSettings.getStartingBankroll();
        int bet = gameSettings.getBet();

        Player player = new Player(gameSettings, strategyTableReader);
        player.setMoney(bankroll);

        player.addHand();
        player.addCard(new Card(Rank.TEN));
        player.addCard(new Card(Rank.TEN));
        player.getHand().setBet(bet);

        Dealer dealer = new Dealer(gameRules);
        dealer.getHand().addCard(new Card(Rank.SEVEN));
        dealer.getHand().addCard(new Card(Rank.TEN));

        player.evaluateHand(0, dealer);

        assertEquals(bankroll + bet * 2, player.getMoney(), "Incorrect payout for player");
    }
}
