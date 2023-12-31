package com.arthur.blackjack.controller;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.arthur.blackjack.models.Player;

import org.apache.logging.log4j.LogManager;

@Component
public class Game {
    private static final Logger logger = LogManager.getLogger(Game.class);

    private Player player;

    public Game(Player player) {
        this.player = player;
    }
    
    public void play() {
        logger.info("Starting a game of Blackjack!");
        logger.info("Player has ${} in their bankroll.", player.getBankroll());
    }

    // boolean isGameOver();
    // String roundStartMessage();
    // Hand createFirstHand();
    // void placeInitialBet();
    // void deal();

}
