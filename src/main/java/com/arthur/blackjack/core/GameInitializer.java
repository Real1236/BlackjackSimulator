package com.arthur.blackjack.core;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameInitializer {

    private final Game game;

    @Autowired
    public GameInitializer(Game game) {
        this.game = game;
    }

    @PostConstruct
    public void initialize() {
        game.initializeGame();
    }
}

