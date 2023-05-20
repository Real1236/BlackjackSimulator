package com.arthur.blackjack.unit;

import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Rank;
import com.arthur.blackjack.config.LoggerConfig;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.logging.Logger;

public class CardTest {

    private static final Logger logger = Logger.getLogger(CardTest.class.getName());

    @BeforeClass
    public static void setUpLogger() {
        LoggerConfig.configure(logger);
    }

    @Test
    public void testAceValue() {
        logger.info("Starting testAceValue");
        Card card = new Card(Rank.ACE);
        for (int i = 1; i <= 21; i++) {
            int cardValue = card.getValue();
            logger.info("Card value for " + i + " is " + cardValue);
            if (i <= 10)
                Assert.assertEquals(cardValue, 11);
            else
                Assert.assertEquals(cardValue, 1);
        }
        logger.info("Finished testAceValue");
    }
}
