package com.arthur.blackjack.analytics.impl;

import com.arthur.blackjack.analytics.Analytics;
import com.arthur.blackjack.analytics.RoundResult;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.UUID;

public class AnalyticsImpl implements Analytics {
    private static final Logger logger = LogManager.getLogger(AnalyticsImpl.class);

    private final int gameNum;

    private static final String FILE_PATH = "src/main/resources/analytics/ResultTracker.xlsx"; // Excel file path
    private static final String OUTPUT_FILE_PATH = "src/main/resources/analytics/Results";

    private final XSSFWorkbook workbook;
    private Sheet sheet;

    public AnalyticsImpl(int gameNum) {
        this.gameNum = gameNum;

        // Creating a copy of the spreadsheet for file exporting purposes
        File originalSpreadsheet = new File(FILE_PATH);
        File copySpreadsheet = new File(UUID.randomUUID() + ".xlsx");
        try {
            FileUtils.copyFile(originalSpreadsheet, copySpreadsheet);
        } catch (IOException e) {
            logger.warn("Error while creating copy of Excel file");
            throw new UncheckedIOException(e);
        }

        // Saving workbook and sheet necessary
        try {
            workbook = new XSSFWorkbook(copySpreadsheet);
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createNewResultsSheet(int bet) {
        sheet = workbook.cloneSheet(0, "Results" + gameNum);
        sheet.getRow(0).createCell(16).setCellValue(bet);
    }

    @Override
    public void recordNewRound(Integer round, Integer money) {
        Row row = sheet.getRow(round);
        if (row == null)
            row = sheet.createRow(round);
        row.createCell(0).setCellValue(round);
        row.createCell(1).setCellValue(money);
    }

    @Override
    public void recordInitialBet(Integer round, Integer bet) {
        Row row = sheet.getRow(round);
        if (row == null)
            row = sheet.createRow(round);
        row.createCell(2).setCellValue(bet);
    }

    @Override
    public void recordRoundResult(Integer round, RoundResult result) {
        Row row = sheet.getRow(round);
        if (row == null)
            row = sheet.createRow(round);

        Cell cell = row.getCell(result.getColIndex());
        if (cell == null) {
            cell = row.createCell(result.getColIndex());
            cell.setCellValue(1);
        } else {
            int currentValue = (int) cell.getNumericCellValue();
            cell.setCellValue(currentValue + 1);
        }
    }

    @Override
    public void evaluateFormulas() {
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        for (int row = 1; row < 13; row++) {    // Maintain cell locations
            Cell c = sheet.getRow(row).getCell(16); // Maintain cell locations
            if (c == null || c.getCellType() != CellType.FORMULA)
                continue;

            try {
                evaluator.evaluateFormulaCell(c);
            } catch (Throwable var4) {
                logger.warn("Error while recalculating sheet");
            }
        }

        Row resultDistribution = sheet.getRow(8);   // Maintain cell locations
        for (int col = 15; col < 23; col++) {   // Maintain cell locations
            Cell c = resultDistribution.getCell(col);
            if (c.getCellType() != CellType.FORMULA)
                continue;

            try {
                evaluator.evaluateFormulaCell(c);
            } catch (Throwable var4) {
                logger.warn("Error while recalculating sheet");
            }
        }
    }

    @Override
    public void saveExcel() {
        workbook.removeSheetAt(0);
        try (FileOutputStream outputStream = new FileOutputStream(OUTPUT_FILE_PATH + gameNum + ".xlsx")) {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
