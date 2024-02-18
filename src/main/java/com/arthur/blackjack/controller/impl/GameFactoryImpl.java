package com.arthur.blackjack.controller.impl;

import com.arthur.blackjack.analytics.Analytics;
import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.controller.Game;
import com.arthur.blackjack.controller.GameFactory;
import com.arthur.blackjack.controller.PlayerTurnManager;
import com.arthur.blackjack.models.card.DeckFactory;
import com.arthur.blackjack.models.hand.HandFactory;
import com.arthur.blackjack.models.player.PlayerFactory;
import com.arthur.blackjack.strategies.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameFactoryImpl implements GameFactory {

    private final PlayerFactory playerFactory;
    private final DeckFactory deckFactory;
    private final HandFactory handFactory;
    private final PlayerTurnManager playerTurnManager;
    private final GameSettings settings;
    private final GameRules rules;
    private final Strategy strategy;
    private final Analytics analytics;

    @Autowired
    public GameFactoryImpl(PlayerFactory playerFactory,
                           DeckFactory deckFactory,
                           HandFactory handFactory,
                           PlayerTurnManager playerTurnManager,
                           GameSettings settings,
                           GameRules rules,
                           Strategy strategy,
                           Analytics analytics) {
        this.playerFactory = playerFactory;
        this.deckFactory = deckFactory;
        this.handFactory = handFactory;
        this.playerTurnManager = playerTurnManager;
        this.settings = settings;
        this.rules = rules;
        this.strategy = strategy;
        this.analytics = analytics;
    }

    @Override
    public Game createGame(int gameNum) {
        return new Game(
                gameNum,
                playerFactory,
                deckFactory,
                handFactory,
                playerTurnManager,
                settings,
                rules,
                strategy,
                analytics
        );
    }
}
