package com.arthur.blackjack.unit;

import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.config.LoggerConfig;
import com.arthur.blackjack.player.Dealer;
import com.arthur.blackjack.player.Player;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.logging.Logger;

public class DealerTest {
    private static final Logger LOGGER = Logger.getLogger(DealerTest.class.getName());

    @BeforeClass
    public static void setUpLogger() {
        LoggerConfig.configure(LOGGER);
    }

    @Test
    public void testDealCard() {
        LOGGER.info("Starting testDealCard");
        Dealer dealer = new Dealer();
        Player player = new Player(1, 100);
        player.addHand();
        Deck deck = new Deck(1);
        int initialDeckSize = deck.getCards().size();

        dealer.dealCard(player, deck);
        assertEquals("Player's hand size should be 1", 1, player.getHand().getCards().size());
        assertEquals("Deck size should decrease by 1", initialDeckSize - 1, deck.getCards().size());
    }

    @Test
    public void testDealCardToDealer() {
        LOGGER.info("Starting testDealCardToDealer");
        Dealer dealer = new Dealer();
        Deck deck = new Deck(1);
        int initialDeckSize = deck.getCards().size();

        dealer.dealCardToDealer(deck);
        assertEquals("Dealer's hand size should be 1", 1, dealer.getHand().getCards().size());
        assertEquals("Deck size should decrease by 1", initialDeckSize - 1, deck.getCards().size());
    }

    @Test
    public void testPlay() {
        LOGGER.info("Starting testPlay");
        Dealer dealer = new Dealer();
        Deck deck = new Deck(1);

        dealer.play(deck);
        LOGGER.info("Dealer's hand: " + dealer.getHand().toString());
        assertTrue("Dealer's hand total should be at least 17", dealer.getHand().getTotal() >= 17);
    }

}

