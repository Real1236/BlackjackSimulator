package com.arthur.blackjack.player;

import com.arthur.blackjack.core.GameSettings;
import org.springframework.stereotype.Component;

@Component
public class PlayerFactory {

    private final GameSettings gameSettings;

    public PlayerFactory(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public Player createPlayer() {
        // Perform any additional initialization or customization logic here
        return new Player(gameSettings);
    }
}

