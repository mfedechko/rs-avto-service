package com.rsavto.categories.service.read;

import com.rsavto.categories.docs.Columns;
import com.rsavto.categories.docs.model.CategoryOutputRecord;
import com.rsavto.categories.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author mfedechko
 */
public class CategoriesOutputReader implements OutputReader {

    @Override
    public List<CategoryOutputRecord> readAllRecords(final String filePath) throws IOException {

        final var excelFile = new File(filePath);
        final var fis = new FileInputStream(excelFile);
        final var workbook = new XSSFWorkbook(fis);
        final var firstSheet = workbook.getSheetAt(0);
        final var rowIterator = firstSheet.rowIterator();
        rowIterator.next();
        while (rowIterator.hasNext()) {
            final var row = rowIterator.next();
            final var record = new CategoryOutputRecord();
            final var brandCell = row.getCell(Columns.CATEGORIES_OUTPUT.get(Columns.BRAND));
            final var brand = ExcelUtils.getStringValue(brandCell);
            final var articleCell = row.getCell(Columns.CATEGORIES_OUTPUT.get(Columns.ARTICLE));
            final var article = ExcelUtils.getStringValue(articleCell);
            final var descCell = row.getCell(Columns.CATEGORIES_OUTPUT.get(Columns.DESCRIPTION));
            final var description = ExcelUtils.getStringValue(descCell);
            final var priceCell = row.getCell(Columns.CATEGORIES_OUTPUT.get(Columns.PRICE));
            final var price = ExcelUtils.getDoubleValue(priceCell);
            final var quantityCell = row.getCell(Columns.CATEGORIES_OUTPUT.get(Columns.QUANTITY));
            final var quantity = ExcelUtils.getIntValue(quantityCell);
            final var pictureCell = row.getCell(Columns.CATEGORIES_OUTPUT.get(Columns.PHOTO));
            final var picture = ExcelUtils.getIntValue(pictureCell);

            

        }
    }
}
