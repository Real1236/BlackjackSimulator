package com.arthur.blackjack.analytics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class Analytics {
    private static final Logger logger = LogManager.getLogger(Analytics.class);

    private static final String FILE_PATH = "src/main/resources/analytics/ResultTracker.xlsx"; // Excel file path
    private static final String OUTPUT_FILE_PATH = "src/main/resources/analytics/Results.xlsx";

    private final XSSFWorkbook workbook;
    private Sheet sheet;

    public Analytics() {
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

    public void createNewResultsSheet(int gameNum, int bet) {
        sheet = workbook.cloneSheet(0, "Results" + gameNum);
        sheet.getRow(0).createCell(12).setCellValue(bet);
    }

    public void writeResults(Integer round, Integer money) {
        Row row = sheet.getRow(round);
        if (row == null)
            row = sheet.createRow(round);
        row.createCell(0).setCellValue(round);
        row.createCell(1).setCellValue(money);
    }

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

    public void evaluateFormulas() {
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        for (int row = 1; row < 13; row++) {
            Cell c = sheet.getRow(row).getCell(12);
            if (c == null || c.getCellType() != CellType.FORMULA)
                continue;

            try {
                evaluator.evaluateFormulaCell(c);
            } catch (Throwable var4) {
                logger.warn("Error while recalculating sheet");
            }
        }

        Row resultDistribution = sheet.getRow(8);
        for (int col = 11; col < 19; col++) {
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

    public void saveExcel() {
        workbook.removeSheetAt(0);
        try (FileOutputStream outputStream = new FileOutputStream(OUTPUT_FILE_PATH)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
