package com.rsavto.categories.service.read;

import com.rsavto.categories.docs.Columns;
import com.rsavto.categories.docs.model.InputRecord;
import com.rsavto.categories.docs.model.PriceRecord;
import com.rsavto.categories.service.FilesService;
import com.rsavto.categories.util.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mykola.fedechko
 */
public class PriceReader extends CategoriesReader{

    private static final Logger LOG = LoggerFactory.getLogger(PriceReader.class);

    public PriceReader(final FilesService filesService) {
        super(filesService, Columns.PRICE_COLUMNS);
    }

    @Override
    public List<InputRecord> readAllRecords() throws IOException {
        LOG.info("Start reading input doc");
        final var filePath = filesService.getLatestFileInDirectory("price");

        final var sheet = getFirstXlsxSheet(filePath);
        final var rowIterator = sheet.rowIterator();
        rowIterator.next();
        final var priceRecords = new ArrayList<InputRecord>();
        while (rowIterator.hasNext()) {
            final var row = rowIterator.next();
            final var inputRecord = buildInitialRecord(row);
            inputRecord.setInputRow(row.getRowNum());

            final var brand = inputRecord.getBrand();
            final var article = inputRecord.getArticle();
            if(brand == null || article == null) {
                continue;
            }

            final var priceRecord = new PriceRecord(brand, article);

            final var pricePCell = row.getCell(columnsMap.get(Columns.PRICE_P));
            final var priceP = ExcelUtils.getDoubleValue(pricePCell);

            final var priceGCell = row.getCell(columnsMap.get(Columns.PRICE_G));
            final var priceG = ExcelUtils.getDoubleValue(priceGCell);

            final var quantityCell = row.getCell(columnsMap.get(Columns.QUANTITY));
            final var quantity = ExcelUtils.getIntValue(quantityCell);

            priceRecord.setPriceP(priceP);
            priceRecord.setPriceG(priceG);
            priceRecord.setQuantity(quantity);
            priceRecords.add(priceRecord);
        }

        LOG.info("Input doc has been processed. Number of new priceRecords: " + priceRecords.size());
        return priceRecords;
    }

}
