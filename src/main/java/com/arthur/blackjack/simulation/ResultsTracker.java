package com.arthur.blackjack.simulation;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.UUID;
import java.util.logging.Logger;

public class ResultsTracker {
    private static final Logger LOGGER = Logger.getLogger(ResultsTracker.class.getName());
    private static final String FILE_PATH = "src/main/resources/result-tracker.xlsx"; // Specify the file path of the Excel file
    private static final String OUTPUT_FILE_PATH = "src/main/resources/results.xlsx";
    private final Workbook workbook;
    private final Sheet sheet;

    public ResultsTracker(int bet) {
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
        sheet = workbook.getSheet("Results");
        sheet.getRow(0).createCell(12).setCellValue(bet);
//        clearExcel();
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

    public void saveExcel() {
//        generateFormulas();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        for (int row = 1; row < 6; row++) {
            Cell c = sheet.getRow(row).getCell(12);
            try {
                evaluator.evaluateFormulaCell(c);
            } catch (Throwable var4) {
                LOGGER.warning("Error while recalculating sheet");
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(OUTPUT_FILE_PATH)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateFormulas() {
        int lastRow = sheet.getLastRowNum();
        for (int rowNumber = 2; rowNumber < lastRow; rowNumber++) {
            Row row = sheet.getRow(rowNumber);
            if (row == null || row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getCellType() == CellType.BLANK)
                break;

            // Amount Bet
            Cell amountBetCell = row.createCell(11);
            amountBetCell.setCellFormula(String.format("$U$4*(SUM(C%d:J%d)+SUM(E%d,G%d,I%d))", rowNumber + 1, rowNumber + 1, rowNumber + 1, rowNumber + 1, rowNumber + 1));

            // Amount Won
            Cell amountWonCell = row.createCell(12);
            amountWonCell.setCellFormula(String.format("$U$4*(D%d+2*E%d+1.5*C%d)", rowNumber + 1, rowNumber + 1, rowNumber + 1));

            // Amount Lost
            Cell amountLostCell = row.createCell(13);
            amountLostCell.setCellFormula(String.format("$U$4*(SUM(J%d,H%d)+2*SUM(I%d,G%d))", rowNumber + 1, rowNumber + 1, rowNumber + 1, rowNumber + 1));

            // Amount Change
            Cell amountChangeCell = row.createCell(15);
            amountChangeCell.setCellFormula(String.format("B%d-B%d", rowNumber + 1, rowNumber));

            // Expected Change
            Cell expectedChangeCell = row.createCell(16);
            expectedChangeCell.setCellFormula(String.format("SUM(C%d*$U$4*1.5,D%d*$U$4,E%d*$U$4*2)-SUM(H%d*$U$4,J%d*$U$4,G%d*$U$4*2,I%d*$U$4*2)",
                    rowNumber + 1, rowNumber + 1, rowNumber + 1, rowNumber + 1, rowNumber + 1, rowNumber + 1, rowNumber + 1));

            // Failed
            Cell failedCell = row.createCell(17);
            failedCell.setCellFormula(String.format("IF(Q%d<>P%d,\"X\",\"\")", rowNumber + 1, rowNumber + 1));
        }
    }

    private void clearExcel() {
        int row = 1;
        while (true) {
            Row excelRow = sheet.getRow(row);
            if (excelRow == null || excelRow.getCell(0) == null)
                break;

            for (int col = 0; col <= 9; col++) {
                Cell cell = excelRow.getCell(col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setBlank();
            }
            row++;
        }
    }
}
