package com.arthur.blackjack.player;

import com.arthur.blackjack.core.GameSettings;
import com.arthur.blackjack.simulation.StrategyTableReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayerFactory {

    private final GameSettings gameSettings;
    private final StrategyTableReader strategyTableReader;

    @Autowired
    public PlayerFactory(GameSettings gameSettings, StrategyTableReader strategyTableReader) {
        this.gameSettings = gameSettings;
        this.strategyTableReader = strategyTableReader;
    }

    public Player createPlayer() {
        // Perform any additional initialization or customization logic here
        return new Player(gameSettings, strategyTableReader);
    }
}

