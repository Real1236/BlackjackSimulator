package com.arthur.blackjack.strategies;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * The Strategy interface represents a strategy for playing blackjack.
 * It defines methods for making decisions during the game.
 */
public abstract class Strategy {

    /**
     * Each hashmap represents a table of decisions for a particular hand.
     * The key is a pair of integers representing the player's hand value and the dealer's upcard value.
     * The value is the action to take for that hand.
     */
    protected Map<Pair<Integer, Integer>, Action> hitTable;
    protected Map<Pair<Integer, Integer>, Action> softTable;
    protected Map<Pair<Integer, Integer>, Action> splitTable;

    public Strategy() {
        String filePath = getFilePath();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(fis)) {

            hitTable = getTable(workbook, "Hit");
            softTable = getTable(workbook, "Soft");
            splitTable = getTable(workbook, "Split");
            System.out.println("Successfully read Excel file");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file", e);
        }
    }

    protected Map<Pair<Integer, Integer>, Action> getTable(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        Map<Pair<Integer, Integer>, Action> table = new HashMap<>();
        int rowIndex = 0;
        for (Row row : sheet) {
            if (rowIndex++ == 0) continue; // Skip the header row

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
    public boolean hit() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Determines whether to double down on the current hand.
     * 
     * @return true if the hand should be doubled down, false otherwise
     */
    public boolean doubleDown() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Determines whether to split the current hand.
     * 
     * @return true if the hand should be split, false otherwise
     */
    public boolean split(int playerOneCardValue, int dealerUpcardValue) {
        throw new UnsupportedOperationException("Not implemented");
    }
    //     String filePath = getFilePath();
    //     try (FileInputStream fis = new FileInputStream(new File(filePath));
    //         Workbook workbook = new XSSFWorkbook(fis)) {

    //         Sheet sheet = workbook.getSheet("Split");
    //         Row row = sheet.getRow(playerValue);
    //         Cell cell = row.getCell(getColumnIndex(dealerCard));

    //         String decision = cell.getStringCellValue();
    //         return decision.equals("Y");  // Assuming "Y" means split
    //     } catch (IOException e) {
    //         throw new RuntimeException("Failed to read Excel file", e);
    //     }
    // }
}
