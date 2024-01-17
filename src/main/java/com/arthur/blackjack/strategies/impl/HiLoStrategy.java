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

        // Follows strategy from: https://www.countingedge.com/card-counting/true-count/
        double betMultiple = 0.5 * trueCount + 0.5;
        double betSize = betMultiple * settings.getBetSize();

        // Round down to the nearest multiple of 5
        return Math.max((int) Math.floor(betSize / 5) * 5, settings.getBetSize());
    }

    private float getTrueCount() {
        return count / deck.getNumOfDecksRemaining();
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