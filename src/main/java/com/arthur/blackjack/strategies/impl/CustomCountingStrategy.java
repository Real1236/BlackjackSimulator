package com.arthur.blackjack.strategies.impl;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.arthur.blackjack.models.card.Card;

public class CustomCountingStrategy extends AbstractStrategy {

    @Override
    public void countCard(Card card) {
        String filePath = getFilePath();
        try (FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis)) {
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
            workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file", e);
        }
    }

    @Override
    public String getFilePath() {
        return "src/main/resources/strategies/CustomCardCounting.xlsx";
    }
}