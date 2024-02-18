package com.arthur.blackjack.controller.impl;

import com.arthur.blackjack.analytics.AnalyticsFactory;
import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.controller.Game;
import com.arthur.blackjack.controller.GameFactory;
import com.arthur.blackjack.controller.PlayerTurnManager;
import com.arthur.blackjack.models.card.DeckFactory;
import com.arthur.blackjack.models.hand.HandFactory;
import com.arthur.blackjack.models.player.PlayerFactory;
import com.arthur.blackjack.strategies.StrategyFactory;
import lombok.Setter;
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
    private final StrategyFactory strategyFactory;
    private final AnalyticsFactory analyticsFactory;
    @Setter
    private String strategyType;
    @Setter
    private String analyticsType;

    @Autowired
    public GameFactoryImpl(PlayerFactory playerFactory,
                           DeckFactory deckFactory,
                           HandFactory handFactory,
                           PlayerTurnManager playerTurnManager,
                           GameSettings settings,
                           GameRules rules,
                           StrategyFactory strategyFactory,
                           AnalyticsFactory analyticsFactory) {
        this.playerFactory = playerFactory;
        this.deckFactory = deckFactory;
        this.handFactory = handFactory;
        this.playerTurnManager = playerTurnManager;
        this.settings = settings;
        this.rules = rules;
        this.strategyFactory = strategyFactory;
        this.analyticsFactory = analyticsFactory;
    }

    @Override
    public Game createGame(int gameNum) {
        Game game = new Game(
                gameNum,
                playerFactory,
                deckFactory,
                handFactory,
                playerTurnManager,
                settings,
                rules
        );

        switch (strategyType) {
            case "basic" -> game.setStrategy(strategyFactory.getBasicStrategy());
            case "customCounting" -> game.setStrategy(strategyFactory.getCustomCountingStrategy());
            case "hiLo" -> game.setStrategy(strategyFactory.getHiLoStrategy());
            case null, default -> throw new IllegalArgumentException("Invalid strategy type");
        }

        switch (analyticsType) {
            case "csv" -> game.setAnalytics(analyticsFactory.createCsvAnalytics(gameNum));
            case "excel" -> game.setAnalytics(analyticsFactory.createAnalytics(gameNum));
            case null, default -> throw new IllegalArgumentException("Invalid analytics type");
        }

        return game;
    }
}
