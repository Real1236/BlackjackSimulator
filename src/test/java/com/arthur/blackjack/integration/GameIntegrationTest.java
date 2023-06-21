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
import com.arthur.blackjack.simulation.Action;
import com.arthur.blackjack.simulation.ResultsTracker;
import com.arthur.blackjack.simulation.RoundResult;
import com.arthur.blackjack.simulation.StrategyTableReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GameIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(GameIntegrationTest.class.getName());

    @BeforeAll
    public static void setUpLogger() {
        LoggerConfig.configure(LOGGER);
    }

    private GameRules gameRules;
    private GameSettings gameSettings;
    private StrategyTableReader strategyTableReader;
    private ResultsTracker resultsTracker;

    @BeforeEach
    public void setUp() {
        gameRules = mock(GameRules.class);
        gameSettings = new GameSettings(50, 1, 1000, 20, 1);
        strategyTableReader = new StrategyTableReader();
        resultsTracker = mock(ResultsTracker.class);
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

        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(bankroll);

        Game game = new Game(gameRules, gameSettings, deck, new Dealer(gameRules), new PlayerFactory(gameSettings, gameRules, strategyTableReader), resultsTracker);
        game.getPlayers().add(player);
        game.startRound();

        assertEquals(bankroll + bet * gameRules.getBlackjackPayout(), player.getMoney(), "Incorrect payout for player");
    }

    @Test
    public void testWinningSplitAces() {
        LOGGER.info("Testing winning both hands of split Aces");

        int bankroll = gameSettings.getStartingBankroll();
        int bet = gameSettings.getBet();

        Deck deck = new Deck(gameSettings, gameRules);

        // Dealer's Third Card
        deck.getCards().add(new Card(Rank.TEN));

        // Hand 2 Cards
        deck.getCards().add(new Card(Rank.TEN));

        // Hand 1 Cards
        deck.getCards().add(new Card(Rank.JACK));
        deck.getCards().add(new Card(Rank.SIX));

        // Dealer Cards
        deck.getCards().add(new Card(Rank.SEVEN));
        deck.getCards().add(new Card(Rank.NINE));

        // Player cards
        deck.getCards().add(new Card(Rank.ACE));
        deck.getCards().add(new Card(Rank.ACE));

        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(bankroll);

        when(gameRules.isHitSplitAces()).thenReturn(false);
        when(gameRules.isResplitAces()).thenReturn(false);
        when(gameRules.getResplitLimit()).thenReturn(2);
        Game game = new Game(gameRules, gameSettings, deck, new Dealer(gameRules), new PlayerFactory(gameSettings, gameRules, strategyTableReader), resultsTracker);
        game.getPlayers().add(player);
        game.startRound();

        assertEquals(bankroll + bet * 2, player.getMoney(), "Incorrect payout for player");
    }

    @Test
    public void testLoseDoublingDownOnDealerBJ() {
        LOGGER.info("Testing winning both hands of split Aces");

        GameRules spyGameRules = spy(gameRules);
        doReturn(true).when(spyGameRules).isLoseOnlyOGBetAgainstDealerBJ();

        int bankroll = gameSettings.getStartingBankroll();
        int bet = gameSettings.getBet();

        Deck deck = new Deck(gameSettings, spyGameRules);

        // Hand Card
        deck.getCards().add(new Card(Rank.THREE));

        // Dealer Cards
        deck.getCards().add(new Card(Rank.ACE));
        deck.getCards().add(new Card(Rank.QUEEN));

        // Player cards
        deck.getCards().add(new Card(Rank.EIGHT));
        deck.getCards().add(new Card(Rank.THREE));

        Player player = new Player(gameSettings, spyGameRules, strategyTableReader);
        player.setMoney(bankroll);

        Game game = new Game(gameRules, gameSettings, deck, new Dealer(spyGameRules), new PlayerFactory(gameSettings, spyGameRules, strategyTableReader), resultsTracker);
        game.getPlayers().add(player);
        game.startRound();

        assertEquals(bankroll - bet, player.getMoney(), "Incorrect payout for player");
    }

    @Test
    public void testNotEnoughMoney() {
        LOGGER.info("Test that player is out of money");

        int bankroll = 30;
        GameSettings spyGameSettings = spy(gameSettings);
        doReturn(20).when(spyGameSettings).getBet();
        doReturn(bankroll).when(spyGameSettings).getStartingBankroll();

        Deck deck = new Deck(spyGameSettings, gameRules);

        // Dealer Cards
        deck.getCards().add(new Card(Rank.TEN));
        deck.getCards().add(new Card(Rank.TEN));

        // Player cards
        deck.getCards().add(new Card(Rank.TEN));
        deck.getCards().add(new Card(Rank.EIGHT));

        Player player = new Player(spyGameSettings, gameRules, strategyTableReader);
        player.setMoney(bankroll);

        Game game = new Game(gameRules, spyGameSettings, deck, new Dealer(gameRules), new PlayerFactory(spyGameSettings, gameRules, strategyTableReader), resultsTracker);
        game.getPlayers().add(player);
        game.startRound();

        assertEquals(0, game.getPlayers().size(), "Player was not deleted");
    }

    @Test
    public void testDoubleAgainstDealerAceBJ() {
        LOGGER.info("Test that player loses both double and og bet against dealer's bj with Ace upcard");

        int bankroll = gameSettings.getStartingBankroll();
        int bet = gameSettings.getBet();
        ResultsTracker spyResultsTracker = spy(resultsTracker);
        doNothing().when(spyResultsTracker).recordRoundResult(anyInt(), any(RoundResult.class));

        Deck deck = new Deck(gameSettings, gameRules);

        // Player's last card
        deck.getCards().add(new Card(Rank.TWO));

        // Dealer Cards
        deck.getCards().add(new Card(Rank.TEN));
        deck.getCards().add(new Card(Rank.ACE));

        // Player cards
        deck.getCards().add(new Card(Rank.TEN));
        deck.getCards().add(new Card(Rank.TWO));

        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(bankroll);
        Player spyPlayer = spy(player);
        doReturn(Action.DOUBLE_DOWN).when(spyPlayer).getPlayerChoice(anyInt(), anyInt());

        Game game = new Game(gameRules, gameSettings, deck, new Dealer(gameRules), new PlayerFactory(gameSettings, gameRules, strategyTableReader), resultsTracker);
        game.getPlayers().add(spyPlayer);
        game.startRound();

        assertEquals(bankroll - 2 * bet, spyPlayer.getMoney(), "Payout was incorrect");
    }
}
