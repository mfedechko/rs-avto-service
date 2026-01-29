package com.rsavto.categories.service.write;

import com.rsavto.categories.data.CategoriesXlsxCell;
import com.rsavto.categories.docs.model.OutputRecord;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author mfedechko
 */
public class XlsxFileWriter implements RecordsFileWriter {


    @Override
    public void writeToFile(final String filePath) {

    }

    @Override
    public void writeOutputRecords(final String filePath,
                                   final List<? extends OutputRecord> records) throws IOException {
        final Workbook workbook = new XSSFWorkbook();
        final var sheet = workbook.createSheet();
        for (var i = 0; i < records.size(); i++) {
            final var record = records.get(i);
            final var row = sheet.createRow(i);

            row.createCell(CategoriesXlsxCell.BRAND.getIndex(), CategoriesXlsxCell.BRAND.getCellType()).setCellValue(record.getBrand());
            row.createCell(CategoriesXlsxCell.ARTICLE.getIndex(), CategoriesXlsxCell.ARTICLE.getCellType()).setCellValue(record.getArticle());
            row.createCell(CategoriesXlsxCell.DESCRIPTION.getIndex(), CategoriesXlsxCell.DESCRIPTION.getCellType()).setCellValue(record.getDesc());
            row.createCell(CategoriesXlsxCell.PRICE.getIndex(), CategoriesXlsxCell.PRICE.getCellType()).setCellValue(record.getPrice().doubleValue());
            row.createCell(CategoriesXlsxCell.QUANTITY.getIndex(), CategoriesXlsxCell.QUANTITY.getCellType()).setCellValue(record.getQuantity());
            row.createCell(CategoriesXlsxCell.IMAGE.getIndex(), CategoriesXlsxCell.IMAGE.getCellType()).setCellValue(record.getImageLink());

        }
        final var outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }
}
