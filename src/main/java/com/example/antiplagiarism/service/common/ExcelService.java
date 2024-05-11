package com.example.antiplagiarism.service.common;

import com.example.antiplagiarism.service.database.TextTestService;
import com.example.antiplagiarism.service.model.TextTestSubmitDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.example.antiplagiarism.service.database.TextTestService.TRIADS;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final TextTestService textTestService;

    public byte[] createTextTestReport(TextTestSubmitDto textTestSubmitDto) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        String[] sentencesOne = textTestService.doSentenceSplit(textTestSubmitDto.getTextOne());
        String[] sentencesTwo = textTestService.doSentenceSplit(textTestSubmitDto.getTextTwo());
        if (sentencesOne.length > sentencesTwo.length) {
            sentencesTwo = textTestService.doEqualizeSentences(sentencesTwo, sentencesOne);
        } else if (sentencesOne.length < sentencesTwo.length) {
            sentencesOne = textTestService.doEqualizeSentences(sentencesOne, sentencesTwo);
        }
        final Integer[][] matrixFirst = textTestService.buildTriadsMatrix(sentencesOne);
        final Integer[][] matrixSecond = textTestService.buildTriadsMatrix(sentencesTwo);
        String[] triads = TRIADS.toArray(String[]::new);
        addTriadsMatrix("TextOne", workbook, triads, matrixFirst);
        addTriadsMatrix("TextTwo", workbook, triads, matrixSecond);
        final double[][] correlationMatrixFirst = textTestService.buildCorrelationMatrix(matrixFirst);
        final double[][] correlationMatrixSecond = textTestService.buildCorrelationMatrix(matrixSecond);
        addCorrelationMatrix("TextOne", workbook, triads, correlationMatrixFirst);
        addCorrelationMatrix("TextTwo", workbook, triads, correlationMatrixSecond);
        addTriadsEntriesTable("TextOne", workbook, triads, matrixFirst);
        addTriadsEntriesTable("TextTwo", workbook, triads, matrixSecond);
        return writeToExcelByteArray(workbook);
    }

    private void addTriadsMatrix(String sheetSuffix, Workbook workbook, String[] triads, Integer[][] matrix) {
        Sheet sheet = workbook.createSheet("TriadsMatrix-" + sheetSuffix);
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("№");
        for (int i = 0; i < triads.length; i++) {
            Cell cell = header.createCell(i + 1);
            cell.setCellValue(triads[i]);
        }
        for (int i = 0; i < matrix.length; i++) {
            Row entryRow = sheet.createRow(i + 1);
            for (int j = 0; j < matrix[0].length; j++) {
                if (j == 0) {
                    Cell numberCell = entryRow.createCell(j);
                    numberCell.setCellValue(i + 1);
                }
                Cell valueCell = entryRow.createCell(j + 1);
                valueCell.setCellValue(matrix[i][j]);
            }
        }
    }

    private void addCorrelationMatrix(String sheetSuffix, Workbook workbook, String[] triads, double[][] matrix) {
        Sheet sheet = workbook.createSheet("CorrelMatrix-" + sheetSuffix);
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("");
        for (int i = 0; i < triads.length; i++) {
            Cell cell = header.createCell(i + 1);
            cell.setCellValue(triads[i]);
        }
        for (int i = 0; i < matrix.length; i++) {
            Row entryRow = sheet.createRow(i + 1);
            for (int j = 0; j < matrix[0].length; j++) {
                if (j == 0) {
                    Cell numberCell = entryRow.createCell(j);
                    numberCell.setCellValue(triads[i]);
                }
                Cell valueCell = entryRow.createCell(j + 1);
                valueCell.setCellValue(matrix[i][j]);
            }
        }
    }

    private void addTriadsEntriesTable(String sheetSuffix, Workbook workbook, String[] triads, Integer[][] matrix) {
        Sheet sheet = workbook.createSheet("Triads-" + sheetSuffix);
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Triada");
        header.createCell(1).setCellValue("Entries");
        for (int i = 0; i < triads.length; i++) {
            Row record = sheet.createRow(i + 1);
            record.createCell(0).setCellValue(triads[i]);
            int sum = 0;
            for (int j = 0; j < matrix.length; j++) {
                sum += matrix[j][i];
            }
            record.createCell(1).setCellValue(sum);
        }
    }

    @SneakyThrows
    private byte[] writeToExcelByteArray(Workbook workbook) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
            workbook.close();
        } catch (IOException ignored) {
        }
        return bos.toByteArray();
    }

}
