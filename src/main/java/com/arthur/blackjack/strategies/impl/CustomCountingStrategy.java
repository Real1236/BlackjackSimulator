package com.arthur.blackjack.strategies.impl;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.player.Player;

@Component
public class CustomCountingStrategy extends AbstractStrategy {

    private Player player;

    private final GameRules rules;
    private final GameSettings settings;

    public CustomCountingStrategy(Player player, GameRules rules, GameSettings settings) {
        super();
        this.player = player;
        this.rules = rules;
        this.settings = settings;
    }

    @Override
    public int getBet() {
        double ev = getEv();
        if (ev <= 0)
            return settings.getBetSize();

        // TODO: Update betting strategy
        int roundedBet = (int) Math.round(ev * player.getBankroll());
        int multipleOfFive = roundedBet - (roundedBet % 5);
        return Math.max(multipleOfFive, settings.getBetSize());
    }

    private double getEv() {
        String filePath = getFilePath();
        try (FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet evSheet = workbook.getSheet("ev");
            return evSheet.getRow(44).getCell(1).getNumericCellValue();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file", e);
        }
    }

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

            // Evaluate all sheets and update strategy tables
            workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
            updateStrategyTables(workbook);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file", e);
        }
    }

    @Override
    public void resetDeckComposition() {
        String filePath = getFilePath();
        try (FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis)) {
            // Reset the quantity of all cards in the deck
            Sheet deckSheet = workbook.getSheet("Deck");
            Row quantityRow = deckSheet.getRow(1);
            for (int i = 1; i < 11; i++) {
                quantityRow.getCell(i).setCellValue(rules.getNumOfDecks() * 4);
                if (i == 10) // Face cards
                    quantityRow.getCell(i).setCellValue(rules.getNumOfDecks() * 16);
            }

            // Evaluate all sheets and update strategy tables
            workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
            updateStrategyTables(workbook);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file", e);
        }
    }

    @Override
    public String getFilePath() {
        return "src/main/resources/strategies/CustomCardCounting.xlsx";
    }
}
