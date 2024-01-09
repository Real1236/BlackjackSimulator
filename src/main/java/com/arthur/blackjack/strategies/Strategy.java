package com.arthur.blackjack.strategies;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.arthur.blackjack.models.hand.Hand;
import com.arthur.blackjack.utils.GameUtils;

/**
 * The Strategy interface represents a strategy for playing blackjack.
 * It defines methods for making decisions during the game.
 */
public abstract class Strategy {

    /**
     * Each hashmap represents a table of decisions for a particular hand.
     * The key is a pair of integers representing the player's hand value and the
     * dealer's upcard value.
     * The value is the action to take for that hand.
     */
    private Map<Pair<Integer, Integer>, Action> hardTable;
    private Map<Pair<Integer, Integer>, Action> softTable;
    private Map<Pair<Integer, Integer>, Action> splitTable;

    public Strategy() {
        String filePath = getFilePath();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
                Workbook workbook = new XSSFWorkbook(fis)) {

            this.hardTable = getTable(workbook, "Hard");
            this.softTable = getTable(workbook, "Soft");
            this.splitTable = getTable(workbook, "Split");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file", e);
        }
    }

    private Map<Pair<Integer, Integer>, Action> getTable(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        Map<Pair<Integer, Integer>, Action> table = new HashMap<>();
        int rowIndex = 0;
        for (Row row : sheet) {
            if (row.getCell(0).getCellType() == CellType.BLANK) // Check if the row is empty
                break;
            if (rowIndex++ == 0) // Skip the first row
                continue;

            int playerValue = (int) row.getCell(0).getNumericCellValue();
            for (int i = 1; i < row.getLastCellNum(); i++) {
                int dealerCard = (int) sheet.getRow(0).getCell(i).getNumericCellValue();
                Action action = Action.fromAbbreviation(row.getCell(i).getStringCellValue());
                table.put(Pair.of(playerValue, dealerCard), action);
            }
        }
        return table;
    }

    protected abstract String getFilePath();

    /**
     * Determines whether to hit (draw another card) on the current hand.
     * 
     * @return true if another card should be drawn, false otherwise
     */
    public boolean hit(Hand playerHand, int dealerUpcardValue) {
        int playerHandValue = playerHand.getHandValue();
        Action action = null;
        if (GameUtils.isHard(playerHand))
            action = hardTable.get(Pair.of(playerHandValue, dealerUpcardValue));
        else
            action = softTable.get(Pair.of(playerHandValue, dealerUpcardValue));
        return action == Action.HIT || action == Action.DOUBLE_DOWN;
    }

    /**
     * Determines whether to double down on the current hand.
     * 
     * @return true if the hand should be doubled down, false otherwise
     */
    public boolean doubleDown(Hand playerHand, int dealerUpcardValue) {
        int playerHandValue = playerHand.getHandValue();
        if (GameUtils.isHard(playerHand))
            return hardTable.get(Pair.of(playerHandValue, dealerUpcardValue)) == Action.DOUBLE_DOWN;
        else
            return softTable.get(Pair.of(playerHandValue, dealerUpcardValue)) == Action.DOUBLE_DOWN;
    }

    /**
     * Determines whether to split the current hand.
     * 
     * @return true if the hand should be split, false otherwise
     */
    public boolean split(int playerOneCardValue, int dealerUpcardValue) {
        return splitTable.get(Pair.of(playerOneCardValue, dealerUpcardValue)) == Action.SPLIT;
    }
}
