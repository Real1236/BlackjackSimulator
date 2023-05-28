package com.arthur.blackjack.unit;

import com.arthur.blackjack.Game;
import com.arthur.blackjack.component.Hand;
import com.arthur.blackjack.config.LoggerConfig;
import com.arthur.blackjack.player.Dealer;
import com.arthur.blackjack.player.Player;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

public class GameTest {
    private static final Logger LOGGER = Logger.getLogger(DealerTest.class.getName());

    @BeforeClass
    public static void setUpLogger() {
        LoggerConfig.configure(LOGGER);
    }

    @Test
    public void testDetermineWinners() {
        Dealer dealer = mock(Dealer.class);
        Hand dealerHand = mock(Hand.class);
        when(dealer.getHand()).thenReturn(dealerHand);
        when(dealerHand.getTotal()).thenReturn(18);

        List<Player> players = new ArrayList<>();
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        players.add(player1);
        players.add(player2);

        Hand player1Hand = mock(Hand.class);
        Hand player2Hand = mock(Hand.class);
        when(player1.getHand()).thenReturn(player1Hand);
        when(player2.getHand()).thenReturn(player2Hand);
        when(player1.getNumOfHands()).thenReturn(1);
        when(player2.getNumOfHands()).thenReturn(1);

        Game game = new Game();
        game.setPlayers(players);
        game.setDealer(dealer);
        game.determineWinners();

        verify(dealer, times(2)).getHand();
        verify(player1).evaluateHand(1, dealer, false);
        verify(player2).evaluateHand(1, dealer, false);
        verify(player1).clearHand(0);
        verify(player2).clearHand(0);
        verify(player1).getMoney();
        verify(player2).getMoney();
        verify(player1, never()).clearHand(1);
        verify(player2, never()).clearHand(1);
        verify(dealer).clearHand();
    }

    @Test
    public void testPayout() {
        List<Player> players = new ArrayList<>();
        Player player = mock(Player.class);
        players.add(player);

        Hand playerHand = mock(Hand.class);
        when(player.getHand()).thenReturn(playerHand);
        when(player.getNumOfHands()).thenReturn(1);

        Game game = new Game();
        game.setPlayers(players);
        Dealer dealer = mock(Dealer.class);
        game.setDealer(dealer);
        game.payout(player, new ArrayList<>());

        verify(player).evaluateHand(1, dealer, false);
        verify(player).clearHand(0);
        verify(player).getMoney();
        verify(player, never()).clearHand(1);
    }
}
