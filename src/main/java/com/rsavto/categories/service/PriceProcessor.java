package com.rsavto.categories.service;

import com.rsavto.categories.docs.Columns;
import com.rsavto.categories.docs.model.InputRecord;
import com.rsavto.categories.docs.model.PriceRecord;
import com.rsavto.categories.service.read.PriceReader;
import com.rsavto.categories.util.ExcelUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author mykola.fedechko
 */
@Service
public class PriceProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(PriceProcessor.class);

    private final FilesService filesService;
    private final PriceReader priceReader;


    public PriceProcessor(final FilesService filesService, final PriceReader priceReader) {
        this.filesService = filesService;
        this.priceReader = priceReader;
    }

    public void updatePrices() throws IOException {

        LOG.info("Updating prices...");
        final var prices = priceReader.readAllRecords();
        final var filePath = filesService.getLatestFileInDirectory("categories");
        final var excelFile = new File(filePath);
        final var fis = new FileInputStream(excelFile);
        final var workbook = new XSSFWorkbook(fis);
        final var sheet = workbook.getSheetAt(0);
        final var rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            final var row = rowIterator.next();

            final var articleCell = row.getCell(Columns.INPUT_FULL.get(Columns.ARTICLE));
            final var article = ExcelUtils.getStringValue(articleCell);
            final var brandCell = row.getCell(Columns.INPUT_FULL.get(Columns.BRAND));
            final var brand = ExcelUtils.getStringValue(brandCell);
            final var priceRecord = (PriceRecord) findPriceRecord(prices, article, brand);
            if (priceRecord != null) {
                final var price1Cell = row.getCell(Columns.INPUT_FULL.get(Columns.PRICE1));
                final var price2Cell = row.getCell(Columns.INPUT_FULL.get(Columns.PRICE2));
                if (price1Cell != null) {
                    price1Cell.setCellValue(priceRecord.getPriceP());
                }
                if (price2Cell != null) {
                    price2Cell.setCellValue(priceRecord.getPriceG());
                }
            }
        }
        fis.close();
        final var outFileName = "with_updated_prices.xlsx";
        final var outFilePath = filesService.getOutputFilePath(outFileName);
        final var outFile = new FileOutputStream(outFilePath);
        workbook.write(outFile);
        outFile.close();
        workbook.close();
        LOG.info("All available prices have been updated");
    }

    private static InputRecord findPriceRecord(final List<InputRecord> prices,
                                               final String article,
                                               final String brand) {
        return prices.stream()
                .filter(p -> p.getArticle().equals(article) && p.getBrand().equals(brand))
                .findFirst()
                .orElse(null);
    }
}
