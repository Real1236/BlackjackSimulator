package com.arthur.blackjack.strategies.impl;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.utils.GameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HiLoStrategy2TC extends HiLoStrategy {
    private static final Logger logger = LogManager.getLogger(HiLoStrategy2TC.class);

    private final int count;
    private final int numberOfCardsDealt;

    public HiLoStrategy2TC(GameRules rules, GameSettings settings) {
        super(rules, settings);
        this.count = 0;
        this.numberOfCardsDealt = 0;
    }

    @Override
    public float getBetSize() {
        float trueCount = GameUtils.getTrueCount(rules.getNumOfDecks(), numberOfCardsDealt, count);
        logger.trace("True count: " + trueCount);
        if (trueCount < 2)
            return 0;

        float bettingUnits = GameUtils.getBettingUnits(settings.getBetSpread(), trueCount);
        float betSize = bettingUnits * settings.getBetSize();

        return GameUtils.roundDownToMinChipSize(betSize, settings.getBetSize(), settings.getMinChipSize());
    }
}