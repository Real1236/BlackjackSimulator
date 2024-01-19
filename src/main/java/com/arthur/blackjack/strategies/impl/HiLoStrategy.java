package com.arthur.blackjack.strategies.impl;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.card.Rank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class HiLoStrategy extends AbstractStrategy {
    private static final Logger logger = LogManager.getLogger(HiLoStrategy.class);

    private int count;
    private final Deck deck;

    public HiLoStrategy(GameRules rules, GameSettings settings, Deck deck) {
        super(rules, settings);
        this.count = 0;
        this.deck = deck;
    }

    @Override
    public int getBetSize() {
        float trueCount = getTrueCount();
        logger.info("True count: " + trueCount);

        int betMultiple;
        if (trueCount <= 1) {
            betMultiple = 1;
        } else if (trueCount <= 1.5) {
            betMultiple = 2;
        } else if (trueCount <= 2) {
            betMultiple = 3;
        } else if (trueCount <= 2.5) {
            betMultiple = 4;
        } else if (trueCount <= 3) {
            betMultiple = 5;
        } else if (trueCount <= 3.5) {
            betMultiple = 6;
        } else if (trueCount <= 4) {
            betMultiple = 7;
        } else {
            betMultiple = 8;
        }
        float betSize = betMultiple * settings.getBetSize();

        // Round down to the nearest multiple of 5
        return Math.max((int) Math.floor(betSize / 5) * 5, settings.getBetSize());
    }

    private float getTrueCount() {
        float rawCount = (float) count / Math.round(deck.getNumOfDecksRemaining());
        return Math.round(rawCount * 2) / 2.0f;
    }

    @Override
    public void countCard(Card card) {
        int cardValue = card.getRank().getValue();
        if (cardValue >= 2 && cardValue <= 6) {
            count++;
        } else if (cardValue >= 10 || card.getRank().equals(Rank.ACE)) {
            count--;
        }
    }

    @Override
    public void resetDeckComposition() {
        count = 0;
    }

    @Override
    public String getFilePath() {
        return "src/main/resources/strategies/BasicStrategy.xlsx";
    }
}