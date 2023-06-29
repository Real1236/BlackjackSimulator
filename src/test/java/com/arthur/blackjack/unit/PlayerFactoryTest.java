package com.arthur.blackjack.unit;

import com.arthur.blackjack.config.LoggerConfig;
import com.arthur.blackjack.core.GameRules;
import com.arthur.blackjack.core.GameSettings;
import com.arthur.blackjack.player.Player;
import com.arthur.blackjack.player.PlayerFactory;
import com.arthur.blackjack.simulation.StrategyTableReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class PlayerFactoryTest {
    private static final Logger LOGGER = Logger.getLogger(PlayerFactoryTest.class.getName());

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
    }

    @Test
    public void testCreatePlayer() {
        PlayerFactory playerFactory = new PlayerFactory(gameSettings, gameRules, strategyTableReader);
        assertEquals(Player.class, playerFactory.createPlayer().getClass());
    }
}
