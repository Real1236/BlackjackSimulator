package com.arthur.blackjack.strategies;

import com.arthur.blackjack.strategies.impl.BasicStrategy;
import com.arthur.blackjack.strategies.impl.CustomCountingStrategy;

import com.arthur.blackjack.strategies.impl.HiLoStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class StrategyFactory {

    private final ApplicationContext context;

    public StrategyFactory(ApplicationContext context) {
        this.context = context;
    }

    public Strategy getStrategy(String strategyType) {
        return switch (strategyType) {
            case "basic" -> context.getBean(BasicStrategy.class);
            case "customCounting" -> context.getBean(CustomCountingStrategy.class);
            case "hiLo" -> context.getBean(HiLoStrategy.class);
            case null, default -> throw new IllegalArgumentException("Invalid strategy type: " + strategyType);
        };
    }
}
