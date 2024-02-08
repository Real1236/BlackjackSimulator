package com.arthur.blackjack.strategies.impl;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.card.Card;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

@Component
public class CustomCountingStrategy extends AbstractStrategy {
    private static final Logger logger = LogManager.getLogger(CustomCountingStrategy.class);

    public CustomCountingStrategy(GameRules rules, GameSettings settings) {
        super(rules, settings);
    }

    @Override
    public int getBetSize() {
        // Maintain this cell location
        double playerEdge = workbook.getSheet("ev").getRow(44).getCell(1).getNumericCellValue();
        logger.trace("Player edge: " + playerEdge);

        // Follows strategy from: https://www.countingedge.com/card-counting/true-count/
        double betMultiple = 1000 * playerEdge + 1;
        double betSize = betMultiple * settings.getBetSize();

        // Round down to the nearest multiple of 5
        return Math.max((int) Math.floor(betSize / 5) * 5, settings.getBetSize());
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
