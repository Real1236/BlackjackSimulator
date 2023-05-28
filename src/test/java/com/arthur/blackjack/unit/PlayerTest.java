package com.arthur.blackjack.unit;

import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.component.Hand;
import com.arthur.blackjack.component.Rank;
import com.arthur.blackjack.config.LoggerConfig;
import com.arthur.blackjack.player.Dealer;
import com.arthur.blackjack.player.Player;
import com.arthur.blackjack.simulation.Action;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerTest {
    private static final Logger LOGGER = Logger.getLogger(PlayerTest.class.getName());

    @BeforeClass
    public static void setUpLogger() {
        LoggerConfig.configure(LOGGER);
    }

    @Test
    public void testCanSplit() {
        LOGGER.info("Testing canSplit method with valid split condition.");
        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.TWO));
        player.getHand().setBet(10);
        assertTrue(player.canSplit(0));
    }

    @Test
    public void testCannotSplit() {
        LOGGER.info("Testing canSplit method with not matching cards.");
        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.THREE));
        player.getHand().setBet(10);
        assertFalse(player.canSplit(0));
    }

    @Test
    public void testCannotSplitTwice() {
        LOGGER.info("Testing canSplit method with multiple hands.");
        Player player = new Player(1, 100);

        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.TWO));
        player.getHand().setBet(10);

        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.TWO));
        player.getHand().setBet(10);

        assertFalse(player.canSplit(0));
        assertFalse(player.canSplit(1));
    }

    @Test
    public void testCannotSplitNoMoney() {
        LOGGER.info("Testing canSplit method with not matching cards.");
        Player player = new Player(1, 50);
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.TWO));
        player.getHand().setBet(51);
        assertFalse(player.canSplit(0));
    }

    @Test
    public void testCanDouble() {
        LOGGER.info("Testing canDouble method with valid double condition.");
        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.THREE));
        player.getHand().setBet(10);
        assertTrue(player.canDouble(0));
    }

    @Test
    public void testCannotDouble() {
        LOGGER.info("Testing canDouble method with invalid double condition.");
        Player player = new Player(1, 50);
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.THREE));
        player.addCard(new Card(Rank.FOUR));
        player.getHand().setBet(10);
        assertFalse(player.canDouble(0));
    }

    @Test
    public void testCannotDoubleNoMoney() {
        LOGGER.info("Testing canDouble method because not enough money.");
        Player player = new Player(1, 5);
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.THREE));
        player.getHand().setBet(10);
        assertFalse(player.canDouble(0));
    }

    @Test
    public void testPlaceBet() {
        LOGGER.info("Starting testPlaceBet");
        Player player = new Player(1, 100);
        player.addHand(10);

        player.placeBet(0);
        assertEquals("Player's money should decrease by bet amount", 90, player.getMoney());
        LOGGER.info("Player's money: " + player.getMoney());
    }

    @Test
    public void testWinBet() {
        LOGGER.info("Starting testWinBet");
        Player player = new Player(1, 100);
        player.addHand(10);

        player.winBet(0);
        assertEquals("Player's money should increase by 2 times bet amount", 120, player.getMoney());
        LOGGER.info("Player's money: " + player.getMoney());
    }

    @Test
    public void testWinBlackjack() {
        LOGGER.info("Starting testWinBlackjack");
        Player player = new Player(1, 100);
        player.addHand(10);

        player.winBlackjack(0);
        assertEquals("Player's money should increase by 2.5 times bet amount", 125, player.getMoney());
        LOGGER.info("Player's money: " + player.getMoney());
    }

    @Test
    public void testPushBet() {
        LOGGER.info("Starting testPushBet");
        Player player = new Player(1, 100);
        player.addHand(10);

        player.pushBet(0);
        assertEquals("Player's money should increase by bet amount", 110, player.getMoney());
        LOGGER.info("Player's money: " + player.getMoney());
    }


//    TODO Refactor @Test
//    public void testTakeTurn() {
//        LOGGER.info("Testing takeTurn method with valid input.");
//
//        Player player = new Player(1, 100);
//        player.addHand();
//        Player spyPlayer = Mockito.spy(player);
//        Dealer dealer = new Dealer();
//        Deck deck = new Deck(4);
//
//        String input = "100";
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
//        System.setIn(inputStream);
//
//        Mockito.doNothing().when(spyPlayer).playHand(0, dealer, deck);
//        spyPlayer.takeTurn(10, dealer, deck);
//        assertEquals(Integer.parseInt(input), spyPlayer.getHand().getBet());
//    }

    @Test
    public void testTakeTurnWithInvalidAndValidBet() {
        LOGGER.info("Testing takeTurn method with invalid and valid input.");

        Player player = new Player(1, 100);
        player.addHand();
        Player spyPlayer = Mockito.spy(player);
        Dealer dealer = new Dealer();
        Deck deck = new Deck(4);

        // Set the input stream to provide an invalid bet value followed by a valid bet value
        String invalidInput = "5\n";
        String validInput = "20";
        String input = invalidInput + validInput;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        Mockito.doNothing().when(spyPlayer).playHand(0, dealer, deck);
        spyPlayer.takeTurn(10, dealer, deck);

        // Verify that the while loop was triggered
        verify(spyPlayer, Mockito.atLeastOnce()).playHand(0, dealer, deck);
        assertEquals(Integer.parseInt(validInput), spyPlayer.getHand().getBet());
    }

    @Test
    public void testPlayHand() {
        LOGGER.info("Testing playHand method.");

        // Create a mock player, dealer, and deck
        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.SEVEN));

        // Mock dealer and deck
        List<Card> mockCards = Arrays.asList(mock(Card.class), mock(Card.class));
        Hand mockHand = mock(Hand.class);
        Mockito.doReturn(mockCards).when(mockHand).getCards();
        Dealer mockDealer = mock(Dealer.class);
        Mockito.doReturn(mockHand).when(mockDealer).getHand();
        Mockito.doReturn(mock(Card.class)).when(mockDealer).getUpcard();
        Deck deck = mock(Deck.class);

        // Create a spy player to test the method
        Player spyPlayer = Mockito.spy(player);

        // Stub the necessary methods
        // TODO refactor for simulation
//        Mockito.doReturn("h").doReturn("s").when(spyPlayer).getPlayerChoice(Mockito.anyInt());
        Mockito.doReturn(false).doReturn(true).when(spyPlayer).performPlayerAction(Mockito.any(Action.class), Mockito.anyInt(), Mockito.any(Dealer.class), Mockito.any(Deck.class));
        Mockito.doReturn(Action.HIT).doReturn(Action.STAND).when(spyPlayer).getPlayerChoice(Mockito.anyInt(), Mockito.anyInt());

        // Call the method
        spyPlayer.playHand(0, mockDealer, deck);

        // Verify that the necessary methods were called
        // TODO refactor for simulation
//        Mockito.verify(spyPlayer, Mockito.times(2)).getPlayerChoice(0);
        verify(spyPlayer, Mockito.times(1)).performPlayerAction(Action.HIT, 0, mockDealer, deck);
        verify(spyPlayer, Mockito.times(1)).performPlayerAction(Action.STAND, 0, mockDealer, deck);
    }

    @Test
    public void testPlayHandOver21() {
        LOGGER.info("Testing playHand method.");

        // Create a mock player, dealer, and deck
        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(new Card(Rank.TEN));
        player.addCard(new Card(Rank.SEVEN));
        player.addCard(new Card(Rank.SEVEN));

        // Mock dealer and deck
        List<Card> mockCards = Arrays.asList(mock(Card.class), mock(Card.class));
        Hand mockHand = mock(Hand.class);
        Mockito.doReturn(mockCards).when(mockHand).getCards();
        Dealer mockDealer = mock(Dealer.class);
        Mockito.doReturn(mockHand).when(mockDealer).getHand();
        Deck deck = mock(Deck.class);

        // Create a spy player to test the method
        Player spyPlayer = Mockito.spy(player);

        // Stub the necessary methods
        // TODO refactor for simulation
//        Mockito.doReturn("h").doReturn("s").when(spyPlayer).getPlayerChoice(Mockito.anyInt());
        Mockito.doReturn(false).doReturn(true).when(spyPlayer).performPlayerAction(Mockito.any(Action.class), Mockito.anyInt(), Mockito.any(Dealer.class), Mockito.any(Deck.class));

        // Call the method
        spyPlayer.playHand(0, mockDealer, deck);

        // Verify that the necessary methods were called
        // TODO refactor for simulation
//        Mockito.verify(spyPlayer, Mockito.times(0)).getPlayerChoice(0);
        verify(spyPlayer, Mockito.times(0)).performPlayerAction(Action.HIT, 0, mockDealer, deck);
        verify(spyPlayer, Mockito.times(0)).performPlayerAction(Action.STAND, 0, mockDealer, deck);
    }

    @Test
    public void testPerformPlayerActionHit() {
        LOGGER.info("Testing player hitting.");

        // Create a mock instance of the Dealer and Deck
        Dealer dealer = mock(Dealer.class);
        Deck deck = mock(Deck.class);

        // Create a player with a hand and set up necessary objects
        Player player = new Player(1, 100);
        player.addHand();
        player.getHand().setBet(10);

        // Perform player action: Hit
        boolean result = player.performPlayerAction(Action.HIT, 0, dealer, deck);

        // Verify that the dealCard method is called on the dealer with the correct arguments
        verify(dealer).dealCard(player, 0, deck);

        // Ensure the result is false (player didn't stand)
        assertFalse(result);
    }

    @Test
    public void testPerformPlayerActionStand() {
        LOGGER.info("Testing player standing.");

        // Create a mock instance of the Dealer and Deck
        Dealer dealer = mock(Dealer.class);
        Deck deck = mock(Deck.class);

        // Create a player with a hand and set up necessary objects
        Player player = new Player(1, 100);
        player.addHand();

        // Perform player action: Stand
        boolean result = player.performPlayerAction(Action.STAND, 0, dealer, deck);

        // Ensure the result is true (player stood)
        assertTrue(result);
    }

    @Test
    public void testPerformPlayerActionDouble() {
        LOGGER.info("Testing player doubling down.");

        // Create a mock instance of the Dealer and Deck
        Dealer dealer = mock(Dealer.class);
        Deck deck = mock(Deck.class);

        // Create a player with a hand and set up necessary objects
        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(mock(Card.class));
        player.addCard(mock(Card.class));
        player.getHand().setBet(10);
        int initialBet = player.getHand().getBet();

        // Perform player action: Double
        boolean result = player.performPlayerAction(Action.DOUBLE_DOWN, 0, dealer, deck);

        // Verify that the dealCard method is called on the dealer with the correct arguments
        verify(dealer).dealCard(player, 0, deck);

        // Ensure the player's bet is doubled
        assertEquals("Initial bet was not doubled", initialBet * 2, player.getHand().getBet());
        assertEquals("Bet was not deducted from player's money", 100 - initialBet, player.getMoney());

        // Ensure the result is true (player doubled and stood)
        assertTrue(result);
    }

    @Test
    public void testPerformPlayerActionSplit() {
        LOGGER.info("Testing player splitting.");

        // Create a mock instance of the Dealer and Deck
        Dealer dealer = mock(Dealer.class);
        Deck deck = mock(Deck.class);

        // Create a player with a hand and set up necessary objects
        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(mock(Card.class));
        player.addCard(mock(Card.class));
        player.getHand().setBet(10);
        int initialBet = player.getHand().getBet();

        // Perform player action: Split
        Player spyPlayer = spy(player);
        doNothing().when(spyPlayer).playHand(1, dealer, deck);
        spyPlayer.performPlayerAction(Action.SPLIT, 0, dealer, deck);

        // Verify that the dealCard method is called on the dealer twice (for each new hand)
        verify(dealer, times(1)).dealCard(spyPlayer, 0, deck);
        verify(dealer, times(1)).dealCard(spyPlayer, deck);

        // Verify that player gets to play the split hand
        verify(spyPlayer, times(1)).playHand(1, dealer, deck);

        // Verify that the player's bet is added to the new hand, number of hands is 2, both hands have 2 cards
        assertEquals(initialBet, player.getHand(player.getNumOfHands() - 1).getBet());
        assertEquals(spyPlayer.getNumOfHands(), 2);
    }

    @Test
    public void testPerformPlayerActionSplitAces() {
        LOGGER.info("Testing player splitting Aces.");

        // Create a mock instance of the Dealer and Deck
        Dealer dealer = mock(Dealer.class);
        Deck deck = mock(Deck.class);

        // Create a player with a hand and set up necessary objects
        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(new Card(Rank.ACE));
        player.addCard(new Card(Rank.ACE));
        player.getHand().setBet(10);
        int initialBet = player.getHand().getBet();

        // Perform player action: Split
        Player spyPlayer = spy(player);
        spyPlayer.performPlayerAction(Action.SPLIT, 0, dealer, deck);

        // Verify that the dealCard method is called on the dealer twice (for each new hand)
        verify(dealer, times(1)).dealCard(spyPlayer, 0, deck);
        verify(dealer, times(1)).dealCard(spyPlayer, deck);

        // Verify that player does not get to play the split hand
        verify(spyPlayer, times(0)).playHand(1, dealer, deck);

        // Verify that the player's bet is added to the new hand, number of hands is 2, both hands have 2 cards
        assertEquals(initialBet, player.getHand(player.getNumOfHands() - 1).getBet());
        assertEquals(spyPlayer.getNumOfHands(), 2);
    }

    @Test
    public void testEvaluateHand_Blackjack() {
        LOGGER.info("Testing evaluateHand method with a blackjack hand.");

        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(new Card(Rank.ACE));
        player.addCard(new Card(Rank.KING));
        player.getHand(0).setBet(20);

        Dealer dealer = mock(Dealer.class);
        Hand dealerHand = mock(Hand.class);
        when(dealer.getHand()).thenReturn(dealerHand);
        when(dealerHand.getTotal()).thenReturn(20);
        player.evaluateHand(0, dealer, false);

        assertEquals(150, player.getMoney());
    }

    @Test
    public void testEvaluateHand_BlackjackOnSplitHands() {
        LOGGER.info("Testing evaluateHand method with a split blackjack hand.");

        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(new Card(Rank.ACE));
        player.addCard(new Card(Rank.KING));
        player.getHand(0).setBet(20);

        Dealer dealer = mock(Dealer.class);
        Hand dealerHand = mock(Hand.class);
        when(dealer.getHand()).thenReturn(dealerHand);
        when(dealerHand.getTotal()).thenReturn(20);
        player.evaluateHand(0, dealer, true);

        assertEquals(140, player.getMoney());
    }

    @Test
    public void testEvaluateHand_Win() {
        LOGGER.info("Testing evaluateHand method with a winning hand.");

        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(new Card(Rank.QUEEN));
        player.addCard(new Card(Rank.NINE));
        player.getHand(0).setBet(30);

        Dealer dealer = mock(Dealer.class);
        Hand hand = mock(Hand.class);
        when(hand.getTotal()).thenReturn(18);
        when(dealer.getHand()).thenReturn(hand);

        player.evaluateHand(0, dealer, false);

        assertEquals(160, player.getMoney());
    }


    @Test
    public void testEvaluateHand_Bust() {
        LOGGER.info("Testing evaluateHand method with a bust hand.");

        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(new Card(Rank.KING));
        player.addCard(new Card(Rank.KING));
        player.addCard(new Card(Rank.KING));
        player.getHand(0).setBet(20);

        Dealer dealer = mock(Dealer.class);
        player.evaluateHand(0, dealer, false);

        assertEquals(100, player.getMoney());
    }

    @Test
    public void testEvaluateHand_Push() {
        LOGGER.info("Testing evaluateHand method with a push hand.");

        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(new Card(Rank.SEVEN));
        player.addCard(new Card(Rank.EIGHT));
        player.getHand(0).setBet(20);

        Dealer dealer = mock(Dealer.class);
        Hand hand = mock(Hand.class);
        when(hand.getTotal()).thenReturn(15);
        when(dealer.getHand()).thenReturn(hand);

        player.evaluateHand(0, dealer, false);

        assertEquals(120, player.getMoney());
    }

    @Test
    public void testEvaluateHand_DealerBlackjackTenUpcard() {
        LOGGER.info("Testing evaluateHand method with a doubled hand on a dealer Blackjack with a 10 upcard.");

        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(new Card(Rank.JACK));
        player.addCard(new Card(Rank.FIVE));
        player.getHand(0).setBet(20);
        player.getHand().doubleDown();

        Dealer dealer = mock(Dealer.class);
        Hand hand = mock(Hand.class);
        when(hand.getTotal()).thenReturn(21);
        when(dealer.getHand()).thenReturn(hand);
        when(dealer.blackjackTenUpcard()).thenReturn(true);

        player.evaluateHand(0, dealer, false);

        assertEquals(120, player.getMoney());
    }

    @Test
    public void testEvaluateHand_Loss() {
        LOGGER.info("Testing evaluateHand method with a losing hand.");

        Player player = new Player(1, 100);
        player.addHand();
        player.addCard(new Card(Rank.JACK));
        player.addCard(new Card(Rank.FIVE));
        player.getHand(0).setBet(20);

        Dealer dealer = mock(Dealer.class);
        Hand hand = mock(Hand.class);
        when(hand.getTotal()).thenReturn(17);
        when(dealer.getHand()).thenReturn(hand);

        player.evaluateHand(0, dealer, false);

        assertEquals(100, player.getMoney());
    }

}

