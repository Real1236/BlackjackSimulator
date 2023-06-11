package com.arthur.blackjack.simulation;

import com.arthur.blackjack.core.Game;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class ResultsTracker {
    private static final String FILE_PATH = "src/main/resources/results.xlsx"; // Specify the file path of the Excel file
    private final Workbook workbook;
    private final Sheet sheet;

    public ResultsTracker() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Results");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Round");
        headerRow.createCell(1).setCellValue("Money");
        for (RoundResult result : RoundResult.values())
            headerRow.createCell(result.getColIndex()).setCellValue(result.toString());
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
        double amountLost = sheet.getRow(1).getCell(1).getNumericCellValue();

        int totalAmountBetIndex = RoundResult.LOSE.getColIndex() + 2;
        int houseEdgeBet = RoundResult.LOSE.getColIndex() + 3;

        sheet.getRow(0).createCell(totalAmountBetIndex).setCellValue("Total Amount Bet");
        sheet.getRow(0).createCell(houseEdgeBet).setCellValue(Game.totalBet);

        sheet.getRow(1).createCell(totalAmountBetIndex).setCellValue("House Edge");
        sheet.getRow(1).createCell(houseEdgeBet).setCellValue(amountLost/(double) Game.totalBet);

        for (int i = 0; i <= houseEdgeBet; i++)
            sheet.autoSizeColumn(i);

        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("0.00%"));
        sheet.getRow(1).getCell(houseEdgeBet).setCellStyle(style);

        try (FileOutputStream outputStream = new FileOutputStream(FILE_PATH)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
