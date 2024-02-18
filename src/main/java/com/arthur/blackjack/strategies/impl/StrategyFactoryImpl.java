package com.arthur.blackjack.strategies.impl;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.strategies.StrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StrategyFactoryImpl implements StrategyFactory {

    private final GameRules rules;
    private final GameSettings settings;

    @Autowired
    public StrategyFactoryImpl(GameRules rules, GameSettings settings) {
        this.rules = rules;
        this.settings = settings;
    }

    @Override
    public BasicStrategy getBasicStrategy() {
        return new BasicStrategy(rules, settings);
    }

    @Override
    public CustomCountingStrategy getCustomCountingStrategy() {
        return new CustomCountingStrategy(rules, settings);
    }

    @Override
    public HiLoStrategy getHiLoStrategy() {
        return new HiLoStrategy(rules, settings);
    }
}
