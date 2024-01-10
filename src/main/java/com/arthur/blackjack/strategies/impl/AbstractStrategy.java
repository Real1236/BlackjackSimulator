package com.arthur.blackjack.strategies.impl;

import com.arthur.blackjack.models.hand.Hand;
import com.arthur.blackjack.strategies.Action;
import com.arthur.blackjack.strategies.Strategy;
import com.arthur.blackjack.utils.GameUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractStrategy implements Strategy {

    /**
     * Each hashmap represents a table of decisions for a particular hand.
     * The key is a pair of integers representing the player's hand value and the
     * dealer's upcard value.
     * The value is the action to take for that hand.
     */
    protected Map<Pair<Integer, Integer>, Action> hardTable;
    protected Map<Pair<Integer, Integer>, Action> softTable;
    protected Map<Pair<Integer, Integer>, Action> splitTable;

    public AbstractStrategy() {
        String filePath = getFilePath();
        try (FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis)) {
            updateStrategyTables(workbook);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file", e);
        }
    }

    protected void updateStrategyTables(Workbook workbook) {
        this.hardTable = getTable(workbook, "Hard");
        this.softTable = getTable(workbook, "Soft");
        this.splitTable = getTable(workbook, "Split");
    }

    /**
     * Retrieves a table of actions based on player value and dealer card from a
     * given workbook and sheet.
     *
     * @param workbook  the workbook containing the sheet
     * @param sheetName the name of the sheet to retrieve the table from
     * @return a map of pairs of player value and dealer card to corresponding
     *         actions
     */
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

    @Override
    public boolean hit(Hand playerHand, int dealerUpcardValue) {
        int playerHandValue = playerHand.getHandValue();
        Action action;
        if (GameUtils.isHard(playerHand))
            action = hardTable.get(Pair.of(playerHandValue, dealerUpcardValue));
        else
            action = softTable.get(Pair.of(playerHandValue, dealerUpcardValue));
        return action == Action.HIT || action == Action.DOUBLE_DOWN;
    }

    @Override
    public boolean doubleDown(Hand playerHand, int dealerUpcardValue) {
        int playerHandValue = playerHand.getHandValue();
        if (GameUtils.isHard(playerHand))
            return hardTable.get(Pair.of(playerHandValue, dealerUpcardValue)) == Action.DOUBLE_DOWN;
        else
            return softTable.get(Pair.of(playerHandValue, dealerUpcardValue)) == Action.DOUBLE_DOWN;
    }

    @Override
    public boolean split(int playerOneCardValue, int dealerUpcardValue) {
        return splitTable.get(Pair.of(playerOneCardValue, dealerUpcardValue)) == Action.SPLIT;
    }
}
