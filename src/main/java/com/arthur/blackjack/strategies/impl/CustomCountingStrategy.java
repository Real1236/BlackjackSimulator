package com.arthur.blackjack.strategies.impl;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.models.card.Card;

@Component
public class CustomCountingStrategy extends AbstractStrategy {

    private final Workbook workbook;

    private final GameRules rules;

    public CustomCountingStrategy(GameRules rules) {
        super();
        try (FileInputStream fis = new FileInputStream(getFilePath())) {
            this.workbook = new XSSFWorkbook(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file", e);
        }
        this.rules = rules;
    }

    @Override
    public int getBetSize() {
        // TODO
        return 0;
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
        return "src/main/resources/strategies/CustomCardCounting.xlsx";
    }
}
