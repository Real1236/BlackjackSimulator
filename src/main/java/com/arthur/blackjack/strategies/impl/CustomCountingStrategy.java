package com.arthur.blackjack.strategies.impl;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.utils.GameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class CustomCountingStrategy extends AbstractStrategy {
    private static final Logger logger = LogManager.getLogger(CustomCountingStrategy.class);

    public CustomCountingStrategy(GameRules rules, GameSettings settings) {
        super(rules, settings);
    }

    @Override
    public float getBetSize() {
        // Maintain this cell location
        double playerEdge = workbook.getSheet("ev").getRow(44).getCell(1).getNumericCellValue();
        logger.trace("Player edge: " + playerEdge);

        float trueCount = convertPlayerEdgeToTrueCount(playerEdge);
        float bettingUnits = GameUtils.getBettingUnits(settings.getBetSpread(), trueCount);
        float betSize = bettingUnits * settings.getBetSize();

        return GameUtils.roundDownToMinChipSize(betSize, settings.getBetSize(), settings.getMinChipSize());
    }

    private float convertPlayerEdgeToTrueCount(double playerEdge) {
        // Created a linear function (y = 133.71x + 0.7228) according to the following points:
        // Player Edge  True Count
        // 0.002476     1.019608
        // 0.010137     2.08
        // 0.017127     3.043257
        // 0.024656     4.041451
        // 0.032351     5.076517
        // 0.039783     5.994638
        return (float) (133.71 * playerEdge + 0.7228);
    }

    @Override
    public void countCard(Card card) {
        // Decrement the quantity of the card in the deck
        Sheet deckSheet = workbook.getSheet("Deck");
        Row cardRow = deckSheet.getRow(0);
        Row quantityRow = deckSheet.getRow(1);
        for (int i = 1; i < 11; i++) {
            if (cardRow.getCell(i).getNumericCellValue() == card.getRank().getValue()) {
                int quantity = (int) quantityRow.getCell(i).getNumericCellValue();
                quantityRow.getCell(i).setCellValue(quantity - 1);
                break;
            }
        }
        recalculate(workbook);
    }

    @Override
    public void resetDeckComposition() {
        // Reset the quantity of all cards in the deck
        Sheet deckSheet = workbook.getSheet("Deck");
        Row quantityRow = deckSheet.getRow(1);
        for (int i = 1; i < 11; i++) {
            quantityRow.getCell(i).setCellValue(rules.getNumOfDecks() * 4);
            if (i == 10) // Face cards
                quantityRow.getCell(i).setCellValue(rules.getNumOfDecks() * 16);
        }
        recalculate(workbook);
    }

    @Override
    public String getFilePath() {
        String path = "src/main/resources/strategies/CustomCardCounting/";
        if (rules.isStandsOnSoft17())
            path += "StandsSoft17_CCC.xlsx";
        else
            path += "HitsSoft17_CCC.xlsx";
        return path;
    }
}
