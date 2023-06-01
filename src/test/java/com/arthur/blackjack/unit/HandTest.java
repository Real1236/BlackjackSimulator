package com.arthur.blackjack.unit;

import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Hand;
import com.arthur.blackjack.component.Rank;
import com.arthur.blackjack.config.LoggerConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.logging.Logger;
import static org.junit.Assert.*;

public class HandTest {
    private static final Logger LOGGER = Logger.getLogger(HandTest.class.getName());

    @BeforeClass
    public static void setUpLogger() {
        LoggerConfig.configure(LOGGER);
    }

    @Test
    public void testAddCard() {
        LOGGER.info("Running testAddCard");
        Hand hand = new Hand();
        Card card = new Card(Rank.ACE);
        hand.addCard(card);
        assertEquals("The size of the cards list should be 1 after adding a card", 1, hand.getCards().size());
        assertEquals("The first card in the cards list should be the card that was added", card, hand.getCards().get(0));
    }

    @Test
    public void testGetTotal() {
        LOGGER.info("Running testGetTotal");
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE));
        hand.addCard(new Card(Rank.KING));
        assertEquals("The total value of the hand should be 21", 21, hand.getTotal());
    }

    @Test
    public void testGetHardTotal() {
        LOGGER.info("Running testGetHardTotal");
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE));
        hand.addCard(new Card(Rank.FIVE));
        hand.addCard(new Card(Rank.KING));
        assertEquals("The total value of the hand should be 16", 16, hand.getTotal());
    }

    @Test
    public void testGetTotalMulipleAces() {
        LOGGER.info("Running testGetHardTotal");
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.THREE));
        hand.addCard(new Card(Rank.FOUR));
        hand.addCard(new Card(Rank.FIVE));
        hand.addCard(new Card(Rank.ACE));
        hand.addCard(new Card(Rank.NINE));
        hand.addCard(new Card(Rank.TWO));
        hand.addCard(new Card(Rank.FOUR));
        assertEquals("The total value of the hand should be 28", 28, hand.getTotal());
    }

    @Test
    public void testRemoveCard() {
        LOGGER.info("Running testRemoveCard");
        Hand hand = new Hand();
        Card card1 = new Card(Rank.ACE);
        Card card2 = new Card(Rank.KING);
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals("The removed card should be the last card that was added", card2, hand.removeCard());
        assertEquals("The size of the cards list should be 1 after removing a card", 1, hand.getCards().size());
    }

    @Test
    public void testBet() {
        LOGGER.info("Running testBet");
        int bet = 10;
        Hand hand = new Hand(bet);
        assertEquals("The bet of the hand should be equal to the bet passed to the constructor", bet, hand.getBet());
        int newBet = 20;
        hand.setBet(newBet);
        assertEquals("The bet of the hand should be equal to the new bet set using setBet", newBet, hand.getBet());
    }

    @Test
    public void testIsHard() {
        LOGGER.info("Testing if hard hand is hard");
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.TEN));
        hand.addCard(new Card(Rank.SIX));
        assertTrue("Hand should be hard", hand.isHard());
    }

    @Test
    public void testBustedHandIsHard() {
        LOGGER.info("Testing if busted hand is hard");
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.TEN));
        hand.addCard(new Card(Rank.SEVEN));
        hand.addCard(new Card(Rank.SIX));
        assertTrue("Hand should be hard", hand.isHard());
    }

    @Test
    public void testIsSoft() {
        LOGGER.info("Testing if soft hand is hard");
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE));
        hand.addCard(new Card(Rank.SIX));
        assertFalse("Hand should be soft", hand.isHard());
    }
}
