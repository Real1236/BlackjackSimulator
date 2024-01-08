package com.arthur.blackjack.strategies;

import org.springframework.stereotype.Component;

@Component
public class BasicStrategy extends Strategy {
    private final String filePath = "src/main/resources/BasicStrategy.xlsx";

    public BasicStrategy() {
        super();
    }

    @Override
    protected String getFilePath() {
        return filePath;
    }
    
}
