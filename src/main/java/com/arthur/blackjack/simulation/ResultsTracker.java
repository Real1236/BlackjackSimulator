package com.arthur.blackjack.simulation;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.UUID;
import java.util.logging.Logger;

@Component
public class ResultsTracker {
    private static final Logger LOGGER = Logger.getLogger(ResultsTracker.class.getName());
    private static final String FILE_PATH = "src/main/resources/result-tracker.xlsx"; // Specify the file path of the Excel file
    private static final String OUTPUT_FILE_PATH = "src/main/resources/results.xlsx";
    private final XSSFWorkbook workbook;
    private Sheet sheet;

    public ResultsTracker() {
        // Creating a copy of the spreadsheet for file exporting purposes
        File originalSpreadsheet = new File(FILE_PATH);
        File copySpreadsheet = new File(UUID.randomUUID() + ".xlsx");
        try {
            FileUtils.copyFile(originalSpreadsheet, copySpreadsheet);
        } catch (IOException e) {
            LOGGER.warning("Error while creating copy of Excel file");
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
        Row row = sheet.getRow(round + 1);
        if (row == null)
            row = sheet.createRow(round + 1);
        row.createCell(0).setCellValue(round);
        row.createCell(1).setCellValue(money);
    }

    public void recordRoundResult(Integer round, RoundResult result) {
        Row row = sheet.getRow(round + 1);
        if (row == null)
            row = sheet.createRow(round + 1);

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
                LOGGER.warning("Error while recalculating sheet");
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
                LOGGER.warning("Error while recalculating sheet");
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
