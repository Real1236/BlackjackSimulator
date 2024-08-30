package com.arthur.blackjack.strategies.impl;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.utils.GameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomCountingStrategy2TC extends CustomCountingStrategy {
    private static final Logger logger = LogManager.getLogger(CustomCountingStrategy2TC.class);

    public CustomCountingStrategy2TC(GameRules rules, GameSettings settings) {
        super(rules, settings);
    }

    @Override
    public float getBetSize() {
        // Maintain this cell location
        double playerEdge = workbook.getSheet("ev").getRow(44).getCell(1).getNumericCellValue();
        logger.trace("Player edge: " + playerEdge);

        float trueCount = GameUtils.convertPlayerEdgeToTrueCount(playerEdge);
        if (trueCount < 2)
            return 0;

        float bettingUnits = GameUtils.getBettingUnits(settings.getBetSpread(), trueCount);
        float betSize = bettingUnits * settings.getBetSize();

        return GameUtils.roundDownToMinChipSize(betSize, settings.getBetSize(), settings.getMinChipSize());
    }
}
