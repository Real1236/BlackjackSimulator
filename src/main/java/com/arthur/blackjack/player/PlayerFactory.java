package com.arthur.blackjack.player;

import com.arthur.blackjack.core.Game;
import com.arthur.blackjack.core.GameSettings;
import org.springframework.stereotype.Component;

@Component
public class PlayerFactory {

    private final GameSettings gameSettings;
    private final Game game;

    public PlayerFactory(GameSettings gameSettings, Game game) {
        this.gameSettings = gameSettings;
        this.game = game;
    }

    public Player createPlayer() {
        // Perform any additional initialization or customization logic here
        return new Player(gameSettings, game);
    }
}

