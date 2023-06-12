package com.arthur.blackjack.unit;

import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.component.Hand;
import com.arthur.blackjack.component.Rank;
import com.arthur.blackjack.config.LoggerConfig;
import com.arthur.blackjack.core.GameRules;
import com.arthur.blackjack.core.GameSettings;
import com.arthur.blackjack.player.Dealer;
import com.arthur.blackjack.player.Player;
import com.arthur.blackjack.simulation.Action;
import com.arthur.blackjack.simulation.StrategyTableReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerTest {
    private static final Logger LOGGER = Logger.getLogger(PlayerTest.class.getName());

    @BeforeAll
    public static void setUpLogger() {
        LoggerConfig.configure(LOGGER);
    }
    
    @MockBean
    private GameSettings gameSettings;
    @MockBean
    private StrategyTableReader strategyTableReader;
    @MockBean
    private GameRules gameRules;

    @BeforeEach()
    public void setUp() {
        strategyTableReader = mock(StrategyTableReader.class);
        gameSettings = mock(GameSettings.class);
        gameRules = mock(GameRules.class);
        when(gameSettings.getDepthToReshuffle()).thenReturn(85);
    }

    @Test
    public void testCanSplit() {
        LOGGER.info("Testing canSplit method with valid split condition.");
        Rank rank = mock(Rank.class);
        when(rank.getValue()).thenReturn(8);

        Card card1 = mock(Card.class);
        when(card1.getRank()).thenReturn(rank);
        Card card2 = mock(Card.class);
        when(card2.getRank()).thenReturn(rank);

        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);

        Hand hand = mock(Hand.class);
        when(hand.getCards()).thenReturn(cards);
        when(hand.getBet()).thenReturn(10);

        Player player = spy(new Player(gameSettings, gameRules, strategyTableReader));
        doReturn(hand).when(player).getHand(0);
        player.setMoney(1000);
        assertTrue(player.canSplit(0));
    }

    @Test
    public void testCannotSplit() {
        LOGGER.info("Testing canSplit method with not matching cards.");
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.THREE));
        player.getHand().setBet(10);
        assertFalse(player.canSplit(0));
    }

    @Test
    public void testCannotSplitNoMoney() {
        LOGGER.info("Testing canSplit method with not matching cards.");
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(50);
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.TWO));
        player.getHand().setBet(51);
        assertFalse(player.canSplit(0));
    }

    @Test
    public void testCanDouble() {
        LOGGER.info("Testing canDouble method with valid double condition.");
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);
        player.setMoney(100);
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.THREE));
        player.getHand().setBet(10);
        assertTrue(player.canDouble(0));
    }

    @Test
    public void testCannotDouble() {
        LOGGER.info("Testing canDouble method with invalid double condition.");
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(50);
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
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(5);
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.THREE));
        player.getHand().setBet(10);
        assertFalse(player.canDouble(0));
    }

    @Test
    public void testCanDoubleAfterSplitting() {
        LOGGER.info("Testing player can double after splitting.");
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);

        // Hand 1
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.THREE));
        player.getHand().setBet(10);

        // Hand 2
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.THREE));
        player.getHand().setBet(10);

        when(gameRules.isDoubleAfterSplit()).thenReturn(true);
        assertTrue(player.canDouble(0));
        assertTrue(player.canDouble(1));
    }

    @Test
    public void testCannotDoubleAfterSplitting() {
        LOGGER.info("Testing player can't double after splitting.");
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);

        // Hand 1
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.THREE));
        player.getHand().setBet(10);

        // Hand 2
        player.addHand();
        player.addCard(new Card(Rank.TWO));
        player.addCard(new Card(Rank.THREE));
        player.getHand().setBet(10);

        when(gameRules.isDoubleAfterSplit()).thenReturn(false);
        assertFalse(player.canDouble(0));
        assertFalse(player.canDouble(1));
    }

    @Test
    public void testPlaceBet() {
        LOGGER.info("Starting testPlaceBet");
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);
        player.addHand(10);

        player.placeBet(0);
        assertEquals(90, player.getMoney(), "Player's money should decrease by bet amount");
        LOGGER.info("Player's money: " + player.getMoney());
    }

    @Test
    public void testWinBet() {
        LOGGER.info("Starting testWinBet");
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);
        player.addHand(10);

        player.winBet(0);
        assertEquals(120, player.getMoney(), "Player's money should increase by 2 times bet amount");
        LOGGER.info("Player's money: " + player.getMoney());
    }

    @Test
    public void testWinBlackjack() {
        LOGGER.info("Starting testWinBlackjack");
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);
        player.addHand(10);

        player.winBlackjack(0);
        assertEquals(125, player.getMoney(), "Player's money should increase by 2.5 times bet amount");
        LOGGER.info("Player's money: " + player.getMoney());
    }

    @Test
    public void testPushBet() {
        LOGGER.info("Starting testPushBet");
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);
        player.addHand(10);

        player.pushBet(0);
        assertEquals(110, player.getMoney(), "Player's money should increase by bet amount");
        LOGGER.info("Player's money: " + player.getMoney());
    }

    @Test
    public void testTakeTurn() {
        LOGGER.info("Testing takeTurn method with valid input.");
        when(gameSettings.getBet()).thenReturn(50);
        Player spyPlayer = Mockito.spy(new Player(gameSettings, gameRules, strategyTableReader));
        Dealer dealer = mock(Dealer.class);
        Deck deck = mock(Deck.class);
        Hand hand = mock(Hand.class);

        when(spyPlayer.getId()).thenReturn(1);
        when(spyPlayer.getMoney()).thenReturn(1);
        doReturn(hand).when(spyPlayer).getHand();
        doNothing().when(spyPlayer).playHand(0, dealer, deck);
        spyPlayer.takeTurn(dealer, deck);
        verify(spyPlayer, times(1)).playHand(0, dealer, deck);
    }

    @Test
    public void testPlayHand() {
        LOGGER.info("Testing playHand method.");

        Card mockCard = mock(Card.class);
        when(mockCard.getValue()).thenReturn(0);

        List<Card> mockCards = Arrays.asList(mockCard, mock(Card.class));
        Hand mockHand = mock(Hand.class);
        when(mockHand.getTotal()).thenReturn(10, 10, 20);
        doReturn(mockCards).when(mockHand).getCards();

        Dealer mockDealer = mock(Dealer.class);
        when(mockDealer.getUpcard()).thenReturn(mockCard);

        Deck deck = mock(Deck.class);

        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.addHand();
        Player spyPlayer = spy(player);
        doNothing().when(spyPlayer).placeBet(anyInt());
        doReturn(mockHand).when(spyPlayer).getHand(anyInt());
        doReturn(Action.HIT, Action.STAND).when(spyPlayer).getPlayerChoice(anyInt(), anyInt());
        doReturn(false, true).when(spyPlayer).performPlayerAction(any(Action.class), anyInt(), any(Dealer.class), any(Deck.class));

        spyPlayer.playHand(0, mockDealer, deck);
        verify(spyPlayer, times(1)).performPlayerAction(Action.HIT, 0, mockDealer, deck);
        verify(spyPlayer, times(1)).performPlayerAction(Action.STAND, 0, mockDealer, deck);
    }

    @Test
    public void testPlayHandOver21() {
        LOGGER.info("Testing playHand method.");

        // Create a mock player, dealer, and deck
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);
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
        Mockito.verify(spyPlayer, Mockito.times(0)).performPlayerAction(Action.HIT, 0, mockDealer, deck);
        Mockito.verify(spyPlayer, Mockito.times(0)).performPlayerAction(Action.STAND, 0, mockDealer, deck);
    }

    @Test
    public void testPerformPlayerActionHit() {
        LOGGER.info("Testing player hitting.");

        // Create a mock instance of the Dealer and Deck
        Dealer dealer = mock(Dealer.class);
        Deck deck = mock(Deck.class);

        // Create a player with a hand and set up necessary objects
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);
        player.addHand();
        player.getHand().setBet(10);

        // Perform player action: Hit
        boolean result = player.performPlayerAction(Action.HIT, 0, dealer, deck);

        // Verify that the dealCard method is called on the dealer with the correct arguments
        Mockito.verify(dealer).dealCard(player, 0, deck);

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
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);
        player.addHand();

        // Perform player action: Stand
        boolean result = player.performPlayerAction(Action.STAND, 0, dealer, deck);

        // Ensure the result is true (player stood)
        assertTrue(result);
    }

    @Test
    public void testPerformPlayerActionDouble() {
        LOGGER.info("Testing player doubling down.");

        int handIndex = 0;
        int originalBet = 10;

        // Create a mock instance of the Dealer, Deck, and Hand
        Deck deck = mock(Deck.class);
        Dealer dealer = mock(Dealer.class);
        Hand mockHand = mock(Hand.class);
        when(mockHand.getBet()).thenReturn(originalBet);

        // Create a player with a hand and set up necessary objects
        Player spyPlayer = spy(new Player(gameSettings, gameRules, strategyTableReader));
        doReturn(true).when(spyPlayer).canDouble(handIndex);
        doNothing().when(spyPlayer).placeBet(handIndex);
        doReturn(mockHand).when(spyPlayer).getHand(handIndex);

        boolean result = spyPlayer.performPlayerAction(Action.DOUBLE_DOWN, handIndex, dealer, deck);

        verify(spyPlayer, times(1)).placeBet(handIndex);
        verify(mockHand, times(1)).setBet(originalBet * 2);
        verify(dealer, times(1)).dealCard(spyPlayer, handIndex, deck);
        assertTrue(result, "Player should automatically stand after doubling");
    }

    @Test
    public void testPerformPlayerActionSplit() {
        LOGGER.info("Testing player splitting.");

        // Create a mock instance of the Dealer and Deck
        Dealer dealer = mock(Dealer.class);
        Deck deck = mock(Deck.class);

        // Create a player with a hand and set up necessary objects
        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);
        player.addHand();
        player.addCard(mock(Card.class));
        player.addCard(mock(Card.class));
        player.getHand().setBet(10);
        int initialBet = player.getHand().getBet();

        // Perform player action: Split
        Player spyPlayer = Mockito.spy(player);
        doNothing().when(spyPlayer).playHand(1, dealer, deck);
        spyPlayer.performPlayerAction(Action.SPLIT, 0, dealer, deck);

        // Verify that the dealCard method is called on the dealer twice (for each new hand)
        Mockito.verify(dealer, Mockito.times(1)).dealCard(spyPlayer, 0, deck);
        Mockito.verify(dealer, Mockito.times(1)).dealCard(spyPlayer, deck);

        // Verify that the player's bet is added to the new hand, number of hands is 2, both hands have 2 cards
        assertEquals(initialBet, player.getHand(player.getNumOfHands() - 1).getBet());
        assertEquals(spyPlayer.getNumOfHands(), 2);
    }

    @Test
    public void testEvaluateHand_Blackjack() {
        LOGGER.info("Testing evaluateHand method with a blackjack hand.");

        Hand mockPlayerHand = mock(Hand.class);
        when(mockPlayerHand.isBlackjack()).thenReturn(true);

        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.addHand(mockPlayerHand);

        Hand mockDealerHand = mock(Hand.class);
        when(mockDealerHand.isBlackjack()).thenReturn(false);

        Dealer dealer = mock(Dealer.class);
        when(dealer.getHand()).thenReturn(mockDealerHand);

        Player spyPlayer = spy(player);
        doNothing().when(spyPlayer).winBlackjack(anyInt());
        spyPlayer.evaluateHand(0, dealer, 1);

        verify(spyPlayer, times(1)).winBlackjack(anyInt());
    }

    @Test
    public void testEvaluateHand_Win() {
        LOGGER.info("Testing evaluateHand method with a winning hand.");

        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);
        player.addHand();
        player.addCard(new Card(Rank.QUEEN));
        player.addCard(new Card(Rank.NINE));
        player.getHand(0).setBet(30);

        Dealer dealer = mock(Dealer.class);
        Hand hand = mock(Hand.class);
        Mockito.when(hand.getTotal()).thenReturn(18);
        Mockito.when(dealer.getHand()).thenReturn(hand);

        player.evaluateHand(0, dealer, 1);

        assertEquals(160, player.getMoney());
    }


    @Test
    public void testEvaluateHand_Bust() {
        LOGGER.info("Testing evaluateHand method with a bust hand.");

        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);
        player.addHand();
        player.addCard(new Card(Rank.KING));
        player.addCard(new Card(Rank.KING));
        player.addCard(new Card(Rank.KING));
        player.getHand(0).setBet(20);

        Dealer dealer = mock(Dealer.class);
        player.evaluateHand(0, dealer, 1);

        assertEquals(100, player.getMoney());
    }

    @Test
    public void testEvaluateHand_Push() {
        LOGGER.info("Testing evaluateHand method with a push hand.");

        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);
        player.addHand();
        player.addCard(new Card(Rank.SEVEN));
        player.addCard(new Card(Rank.EIGHT));
        player.getHand(0).setBet(20);

        Dealer dealer = mock(Dealer.class);
        Hand hand = mock(Hand.class);
        Mockito.when(hand.getTotal()).thenReturn(15);
        Mockito.when(dealer.getHand()).thenReturn(hand);

        player.evaluateHand(0, dealer, 1);

        assertEquals(120, player.getMoney());
    }

    @Test
    public void testEvaluateHand_Loss() {
        LOGGER.info("Testing evaluateHand method with a losing hand.");

        Player player = new Player(gameSettings, gameRules, strategyTableReader);
        player.setMoney(100);
        player.addHand();
        player.addCard(new Card(Rank.JACK));
        player.addCard(new Card(Rank.FIVE));
        player.getHand(0).setBet(20);

        Dealer dealer = mock(Dealer.class);
        Hand hand = mock(Hand.class);
        Mockito.when(hand.getTotal()).thenReturn(17);
        Mockito.when(dealer.getHand()).thenReturn(hand);

        player.evaluateHand(0, dealer, 1);

        assertEquals(100, player.getMoney());
    }

}

