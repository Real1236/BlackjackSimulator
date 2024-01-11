package com.arthur.blackjack.strategies.impl;

import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.card.Card;
import org.springframework.stereotype.Component;

@Component
public class BasicStrategy extends AbstractStrategy {

    private final GameSettings settings;

    public BasicStrategy(GameSettings settings) {
        super();
        this.settings = settings;
    }

    @Override
    public int getBet() {
        return settings.getBetSize();
    }

    @Override
    public void countCard(Card card) {

    }

    @Override
    public void resetDeckComposition() {

    }

    @Override
    public String getFilePath() {
        return "src/main/resources/strategies/BasicStrategy.xlsx";
    }

}
