package com.arthur.blackjack.strategies.impl;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.card.Rank;
import com.arthur.blackjack.utils.GameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HiLoStrategy extends AbstractStrategy {
    private static final Logger logger = LogManager.getLogger(HiLoStrategy.class);

    private int count;
    private int numberOfCardsDealt;

    public HiLoStrategy(GameRules rules, GameSettings settings) {
        super(rules, settings);
        this.count = 0;
        this.numberOfCardsDealt = 0;
    }

    @Override
    public float getBetSize() {
        float trueCount = getTrueCount();
        logger.trace("True count: " + trueCount);

        float bettingUnits = GameUtils.getBettingUnits(settings.getBetSpread(), trueCount);
        float betSize = bettingUnits * settings.getBetSize();

        return GameUtils.roundDownToMinChipSize(betSize, settings.getBetSize(), settings.getMinChipSize());
    }

    private float getTrueCount() {
        int cardsRemainingInDeck = rules.getNumOfDecks() * 52 - numberOfCardsDealt;
        float numOfDecksRemaining = (float) cardsRemainingInDeck / 52;

        float rawCount = (float) count / Math.round(numOfDecksRemaining);
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
        numberOfCardsDealt++;
    }

    @Override
    public void resetDeckComposition() {
        count = 0;
        numberOfCardsDealt = 0;
    }

    @Override
    public String getFilePath() {
        return "src/main/resources/strategies/BasicStrategy.xlsx";
    }
}