package com.arthur.blackjack.unit;

import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.component.Rank;
import com.arthur.blackjack.config.LoggerConfig;
import com.arthur.blackjack.core.GameRules;
import com.arthur.blackjack.core.GameSettings;
import com.arthur.blackjack.player.Dealer;
import com.arthur.blackjack.player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DealerTest {
    private static final Logger LOGGER = Logger.getLogger(DealerTest.class.getName());

    @MockBean
    private GameRules gameRules;
    @MockBean
    private GameSettings gameSettings;

    @BeforeAll
    public static void setUpLogger() {
        LoggerConfig.configure(LOGGER);
    }

    @BeforeEach()
    public void setUp() {
        gameRules = mock(GameRules.class);
        gameSettings = mock(GameSettings.class);
        when(gameRules.getNumOfDecks()).thenReturn(8);
        when(gameSettings.getDepthToReshuffle()).thenReturn(85);
    }

    @Test
    public void testDealCard() {
        LOGGER.info("Starting testDealCard");
        Player player = mock(Player.class);
        Deck deck = mock(Deck.class);
        when(deck.dealCard()).thenReturn(mock(Card.class));

        Dealer dealer = new Dealer(gameRules);
        dealer.dealCard(player, deck);
        verify(player, times(1)).addCard(any(Card.class));
    }

    @Test
    public void testDealCardToDealer() {
        LOGGER.info("Starting testDealCardToDealer");
        Deck deck = mock(Deck.class);
        when(deck.dealCard()).thenReturn(new Card(Rank.TWO));

        Dealer dealer = new Dealer(gameRules);
        dealer.dealCardToDealer(deck);

        assertEquals(1, dealer.getHand().getCards().size(), "Dealer's hand size should be 1");
    }

    @Test
    public void testPlay() {
        LOGGER.info("Starting testPlay");

        Deck deck = mock(Deck.class);
        when(deck.dealCard()).thenReturn(new Card(Rank.TWO));

        Dealer dealer = new Dealer(gameRules);
        dealer.getHand().addCard(new Card(Rank.TEN));
        dealer.getHand().addCard(new Card(Rank.SIX));
        dealer.play(deck);

        LOGGER.info("Dealer's hand: " + dealer.getHand().toString());
        assertTrue(dealer.getHand().getTotal() >= 17, "Dealer's hand total should be at least 17");
    }

    @Test
    public void testHitSoft17Play() {
        LOGGER.info("Starting testHitSoft17Play");

        when(gameRules.isStandsOnSoft17()).thenReturn(false);
        Deck deck = mock(Deck.class);
        when(deck.dealCard()).thenReturn(new Card(Rank.TEN));

        Dealer dealer = new Dealer(gameRules);
        dealer.getHand().addCard(new Card(Rank.ACE));
        dealer.getHand().addCard(new Card(Rank.SIX));
        dealer.play(deck);

        LOGGER.info("Dealer's hand: " + dealer.getHand().toString());
        assertTrue(dealer.getHand().isHard(), "Dealer's hand should be hard now");
    }

    @Test
    public void testStandSoft17Play() {
        LOGGER.info("Starting testStandSoft17Play");

        when(gameRules.isStandsOnSoft17()).thenReturn(true);
        Deck deck = mock(Deck.class);
        when(deck.dealCard()).thenReturn(new Card(Rank.TEN));

        Dealer dealer = new Dealer(gameRules);
        dealer.getHand().addCard(new Card(Rank.ACE));
        dealer.getHand().addCard(new Card(Rank.SIX));
        dealer.play(deck);

        LOGGER.info("Dealer's hand: " + dealer.getHand().toString());
        assertFalse(dealer.getHand().isHard(), "Dealer's hand should still be soft");
    }

}

