package com.rsavto.categories.service.read;

import com.rsavto.categories.data.Category;
import com.rsavto.categories.docs.Columns;
import com.rsavto.categories.docs.model.InputRecord;
import com.rsavto.categories.service.FilesService;
import com.rsavto.categories.util.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mfedechko
 */
@Service
public class RsaReader extends CategoriesReader {

    private static final Logger LOG = LoggerFactory.getLogger(RsaReader.class);

    public RsaReader(final FilesService filesService) {
        super(filesService, Columns.INPUT_RSA);
    }

    @Override
    public List<InputRecord> readAllRecords() throws IOException {
        LOG.info("Start reading RSA input doc");
        final var filePath = filesService.getLatestFileInDirectory("rsa");
        final var sheet = getFirstXlsxSheet(filePath);
        final var rowIterator = sheet.rowIterator();
        rowIterator.next();
        final var records = new ArrayList<InputRecord>();
        while (rowIterator.hasNext()) {
            final var row = rowIterator.next();

            final var record = buildInitialRecord(row);
            record.setInputRow(row.getRowNum());

            if (record.hasErrors()) {
                records.add(record);
                continue;
            }

            final var descChunks = record.getDescription().split(DESC_SEPARATOR);
            var descArticle = record.getArticle();
            var descBrand = record.getBrand();
            var descName = record.getDescription();
            if (descChunks.length >= 3) {
                descArticle = descChunks[0].strip();
                descBrand = descChunks[1].strip();
                descName = descChunks[2].strip();
            }

            final var priceAvtopro = ExcelUtils.getDoubleValue(row.getCell(columnsMap.get(Columns.AVTOPRO_PRICE)));
            final var price = ExcelUtils.getDoubleValue(row.getCell(columnsMap.get(Columns.PRICE)));
            final var quantity = ExcelUtils.getIntValue(row.getCell(columnsMap.get(Columns.QUANTITY)));
            record.setDescBrand(descBrand);
            record.setDescArticle(descArticle);
            record.setDescName(descName);
            record.setPriceAvtopro(BigDecimal.valueOf(priceAvtopro));
            record.setPrice(BigDecimal.valueOf(price));
            record.setQuantity(quantity);
            record.setCategory(Category.RSA);
            record.setPicture(record.getArticle() + ".jpg");
            records.add(record);
        }
        LOG.info("RSA input doc has been processed. Number of new records: {}", records.size());

        return records;
    }

}
