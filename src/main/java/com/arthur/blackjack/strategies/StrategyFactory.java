package com.arthur.blackjack.strategies;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class StrategyFactory {

    private final ApplicationContext context;

    public StrategyFactory(ApplicationContext context) {
        this.context = context;
    }

    public Strategy getStrategy(String strategyType) {
        if ("basic".equals(strategyType)) {
            return context.getBean(BasicStrategy.class);
        // } else if ("counting".equals(strategyType)) {
        //     return context.getBean(CountingStrategy.class);
        } else {
            throw new IllegalArgumentException("Invalid strategy type: " + strategyType);
        }
    }
}