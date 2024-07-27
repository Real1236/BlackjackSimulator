package com.arthur.blackjack.strategies.impl;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.card.Card;

public class BasicStrategy extends AbstractStrategy {

    public BasicStrategy(GameRules rules, GameSettings settings) {
        super(rules, settings);
    }

    @Override
    public float getBetSize() {
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
