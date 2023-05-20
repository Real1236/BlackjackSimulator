package com.arthur.blackjack.unit;

import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.component.Hand;
import com.arthur.blackjack.component.Rank;
import com.arthur.blackjack.config.LoggerConfig;
import com.arthur.blackjack.player.Dealer;
import com.arthur.blackjack.player.Player;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

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

    @Test
    public void testTakeTurn() {
        LOGGER.info("Testing takeTurn method with valid input.");

        Player player = new Player(1, 100);
        player.addHand();
        Player spyPlayer = Mockito.spy(player);
        Dealer dealer = new Dealer();
        Deck deck = new Deck(4);

        String input = "100";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        Mockito.doNothing().when(spyPlayer).playHand(0, dealer, deck);
        spyPlayer.takeTurn(10, dealer, deck);
        assertEquals(Integer.parseInt(input), spyPlayer.getHand().getBet());
    }

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
        Mockito.verify(spyPlayer, Mockito.atLeastOnce()).playHand(0, dealer, deck);
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
        List<Card> mockCards = Arrays.asList(Mockito.mock(Card.class), Mockito.mock(Card.class));
        Hand mockHand = Mockito.mock(Hand.class);
        Mockito.doReturn(mockCards).when(mockHand).getCards();
        Dealer mockDealer = Mockito.mock(Dealer.class);
        Mockito.doReturn(mockHand).when(mockDealer).getHand();
        Deck deck = Mockito.mock(Deck.class);

        // Create a spy player to test the method
        Player spyPlayer = Mockito.spy(player);

        // Stub the necessary methods
        Mockito.doReturn("h").doReturn("s").when(spyPlayer).getPlayerChoice(Mockito.anyInt());
        Mockito.doReturn(false).doReturn(true).when(spyPlayer).performPlayerAction(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Dealer.class), Mockito.any(Deck.class));

        // Call the method
        spyPlayer.playHand(0, mockDealer, deck);

        // Verify that the necessary methods were called
        Mockito.verify(spyPlayer, Mockito.times(2)).getPlayerChoice(0);
        Mockito.verify(spyPlayer, Mockito.times(1)).performPlayerAction("h", 0, mockDealer, deck);
        Mockito.verify(spyPlayer, Mockito.times(1)).performPlayerAction("s", 0, mockDealer, deck);
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
        List<Card> mockCards = Arrays.asList(Mockito.mock(Card.class), Mockito.mock(Card.class));
        Hand mockHand = Mockito.mock(Hand.class);
        Mockito.doReturn(mockCards).when(mockHand).getCards();
        Dealer mockDealer = Mockito.mock(Dealer.class);
        Mockito.doReturn(mockHand).when(mockDealer).getHand();
        Deck deck = Mockito.mock(Deck.class);

        // Create a spy player to test the method
        Player spyPlayer = Mockito.spy(player);

        // Stub the necessary methods
        Mockito.doReturn("h").doReturn("s").when(spyPlayer).getPlayerChoice(Mockito.anyInt());
        Mockito.doReturn(false).doReturn(true).when(spyPlayer).performPlayerAction(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Dealer.class), Mockito.any(Deck.class));

        // Call the method
        spyPlayer.playHand(0, mockDealer, deck);

        // Verify that the necessary methods were called
        Mockito.verify(spyPlayer, Mockito.times(0)).getPlayerChoice(0);
        Mockito.verify(spyPlayer, Mockito.times(0)).performPlayerAction("h", 0, mockDealer, deck);
        Mockito.verify(spyPlayer, Mockito.times(0)).performPlayerAction("s", 0, mockDealer, deck);
    }

}

