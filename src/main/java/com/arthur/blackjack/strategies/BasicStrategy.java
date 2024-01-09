package com.arthur.blackjack.strategies;

import org.springframework.stereotype.Component;

@Component
public class BasicStrategy extends Strategy {

    public BasicStrategy() {
        super();
    }

    @Override
    protected String getFilePath() {
        return "src/main/resources/strategies/BasicStrategy.xlsx";
    }

}
