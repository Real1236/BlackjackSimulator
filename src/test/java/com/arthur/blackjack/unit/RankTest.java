package com.arthur.blackjack.unit;

import com.arthur.blackjack.component.Rank;
import com.arthur.blackjack.config.LoggerConfig;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

public class RankTest {

    private static final Logger logger = Logger.getLogger(RankTest.class.getName());

    @BeforeClass
    public static void setUpLogger() {
        LoggerConfig.configure(logger);
    }

    @Test
    public void testGetValue() {
        logger.info("Starting testGetValue");
        assertEquals("The value of ACE should be 1", 11, Rank.ACE.getValue());
        assertEquals("The value of TWO should be 2", 2, Rank.TWO.getValue());
        assertEquals("The value of THREE should be 3", 3, Rank.THREE.getValue());
        assertEquals("The value of FOUR should be 4", 4, Rank.FOUR.getValue());
        assertEquals("The value of FIVE should be 5", 5, Rank.FIVE.getValue());
        assertEquals("The value of SIX should be 6", 6, Rank.SIX.getValue());
        assertEquals("The value of SEVEN should be 7", 7, Rank.SEVEN.getValue());
        assertEquals("The value of EIGHT should be 8", 8, Rank.EIGHT.getValue());
        assertEquals("The value of NINE should be 9", 9, Rank.NINE.getValue());
        assertEquals("The value of TEN should be 10", 10, Rank.TEN.getValue());
        assertEquals("The value of JACK should be 10", 10, Rank.JACK.getValue());
        assertEquals("The value of QUEEN should be 10", 10, Rank.QUEEN.getValue());
        assertEquals("The value of KING should be 10", 10, Rank.KING.getValue());
    }
}
