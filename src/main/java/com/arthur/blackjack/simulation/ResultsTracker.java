package com.arthur.blackjack.simulation;

import com.arthur.blackjack.Game;
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
    }

    public void writeResults(Integer round, Integer money) {
        Row row = sheet.createRow(round + 1);
        row.createCell(0).setCellValue(round);
        row.createCell(1).setCellValue(money);
    }

    public void saveExcel() {
        double amountLost = sheet.getRow(1).getCell(1).getNumericCellValue();

        sheet.getRow(0).createCell(3).setCellValue("Total Amount Bet");
        sheet.getRow(0).createCell(4).setCellValue(Game.totalBet);

        sheet.getRow(1).createCell(3).setCellValue("House Edge");
        sheet.getRow(1).createCell(4).setCellValue(amountLost/(double) Game.totalBet);

        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);

        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("0.00%"));
        sheet.getRow(1).getCell(4).setCellStyle(style);

        try (FileOutputStream outputStream = new FileOutputStream(FILE_PATH)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
