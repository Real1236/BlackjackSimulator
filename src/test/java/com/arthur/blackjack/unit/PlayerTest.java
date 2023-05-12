package com.arthur.blackjack.unit;

import com.arthur.blackjack.config.LoggerConfig;
import com.arthur.blackjack.player.Player;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.logging.Logger;

public class PlayerTest {
    private static final Logger LOGGER = Logger.getLogger(PlayerTest.class.getName());

    @BeforeClass
    public static void setUpLogger() {
        LoggerConfig.configure(LOGGER);
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
}

