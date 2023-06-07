package com.arthur.blackjack.simulation;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class StrategyTableReader {
    private static final String FILE_PATH = "src/main/resources/basic-strategy.xlsx"; // Specify the file path of your strategy Excel file

    private final Map<String, Map<Integer, Map<Integer, Action>>> strategyTable;

    public StrategyTableReader() {
        strategyTable = readStrategyTable();
    }

    public Map<String, Map<Integer, Map<Integer, Action>>> getStrategyTable() {
        return strategyTable;
    }

    public static Map<String, Map<Integer, Map<Integer, Action>>> readStrategyTable() {
        /* {
            Hard:   {   Player Total:   { Dealer Upcard: Action } },
            Soft:   {   Player Total:   { Dealer Upcard: Action } },
            Split:  {   Pair Number:    { Dealer Upcard: Action } }
        } */
        Map<String, Map<Integer, Map<Integer, Action>>> strategyTable = new HashMap<>();

        try (Workbook workbook = WorkbookFactory.create(new File(FILE_PATH))) {
            // Iterate over all three sheets
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Map<Integer, Map<Integer, Action>> table = new HashMap<>();

                // Iterate over the rows and columns to read the strategy data
                Row dealerRow = sheet.getRow(0);
                for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
                    Row row = sheet.getRow(r);
                    int playerTotal = (int) row.getCell(0).getNumericCellValue();
                    if (playerTotal == 0)
                        break;
                    Map<Integer, Action> actionMap = new HashMap<>();
                    for (int c = 1; c < 11; c++) {
                        String action = row.getCell(c).getStringCellValue();
                        if (Objects.equals(action, "Y"))
                            action = "P";

                        int upcard = (int) dealerRow.getCell(c).getNumericCellValue();
                        actionMap.put(upcard, Objects.equals(action, "N") ? null : Action.fromAbbreviation(action.toLowerCase()));
                    }
                    table.put(playerTotal, actionMap);
                }
                strategyTable.put(sheet.getSheetName(), table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strategyTable;
    }
}

