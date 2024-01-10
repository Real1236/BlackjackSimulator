package com.arthur.blackjack.strategies.impl;

import com.arthur.blackjack.models.card.Card;
import org.springframework.stereotype.Component;

@Component
public class BasicStrategy extends AbstractStrategy {

    public BasicStrategy() {
        super();
    }

    @Override
    public void countCard(Card card) {

    }

    @Override
    public void resetDeckComposition() {

    }

    @Override
    public double getEv() {
        return 0;
    }

    @Override
    public String getFilePath() {
        return "src/main/resources/strategies/BasicStrategy.xlsx";
    }

}
