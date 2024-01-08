package com.arthur.blackjack.strategies;

import org.springframework.stereotype.Component;

@Component
public class BasicStrategy extends Strategy {
    private final String filePath = "src/main/resources/strategies/BasicStrategy.xlsx";

    public BasicStrategy() {
        super();
    }

    @Override
    protected String getFilePath() {
        return filePath;
    }

}
