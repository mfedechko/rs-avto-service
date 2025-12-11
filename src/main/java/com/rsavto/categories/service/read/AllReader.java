package com.rsavto.categories.service.read;

import com.rsavto.categories.data.Category;
import com.rsavto.categories.data.FileNames;
import com.rsavto.categories.data.ReadErrors;
import com.rsavto.categories.docs.Columns;
import com.rsavto.categories.docs.model.InputRecord;
import com.rsavto.categories.docs.model.OutputRecord;
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
public class AllReader extends CategoriesReader{

    private static final Logger LOG = LoggerFactory.getLogger(AllReader.class);
    protected static final String DESC_SEPARATOR = "/";

    public AllReader(final FilesService filesService) {
        super(filesService, Columns.INPUT_ALL);
    }

    public List<InputRecord> readAllRecords() throws IOException {
        LOG.info("Start reading RSA input doc");
        final var filePath = filesService.getLatestFileInDirectory("categories");
        final var sheet = getFirstXlsxSheet(filePath);
        final var rowIterator = sheet.rowIterator();
        rowIterator.next();
        final var records = new ArrayList<InputRecord>();
        while (rowIterator.hasNext()) {
            final var row = rowIterator.next();

            final var record = buildInitialRecord(row);
            record.setInputRow(row.getRowNum() + 1);
            if (record.hasErrors()) {
                records.add(record);
                continue;
            }

            final var categoryCell = row.getCell(columnsMap.get(Columns.GROUP));
            final var categoryValue = ExcelUtils.getStringValue(categoryCell);
            if (categoryValue == null) {
                record.getErrors().add(ReadErrors.NO_CATEGORY);
                records.add(record);
                continue;
            }

            final var category = processCategory(categoryValue);
            final var descChunks = record.getDescription().split(DESC_SEPARATOR);
            if (category != Category.ORIGINAL && descChunks.length < 3) {
                record.getErrors().add(ReadErrors.WRONG_DESC);
                records.add(record);
                continue;
            }
            String descArticle = null;
            String descBrand = null;
            String descName = null;
            final var descNameBuilder = new StringBuilder();

            //Description may contain separators ("/")
            if (descChunks.length >= 3) {
                descArticle = descChunks[0].strip();
                descBrand = descChunks[1].strip();
                for (var i = 2; i < descChunks.length; i++) {
                    descNameBuilder.append(descChunks[i].strip()).append("/");
                }
                descName = descNameBuilder.substring(0, descNameBuilder.length() - 1);
            }

            if (category == Category.ORIGINAL) {
                descArticle = record.getArticle();
                descBrand = record.getBrand();
                descName = record.getDescription();
            }

            final var priceAvtopro = ExcelUtils.getDoubleValue(row.getCell(columnsMap.get(Columns.AVTOPRO_PRICE)));
            final var price = ExcelUtils.getDoubleValue(row.getCell(columnsMap.get(Columns.PRICE)));
            final var quantity = ExcelUtils.getIntValue(row.getCell(columnsMap.get(Columns.QUANTITY)));

            record.setCategory(category);
            record.setPriceAvtopro(BigDecimal.valueOf(priceAvtopro));
            record.setPrice(BigDecimal.valueOf(price));
            record.setQuantity(quantity);
            record.setDescBrand(descBrand);
            record.setDescArticle(descArticle);
            record.setDescName(descName);
            final var isOriginal = category == Category.ORIGINAL;
            if (descArticle != null) {
                record.setPicture(isOriginal ? record.getDescArticle() + "OE.jpg" : record.getDescArticle() + ".jpg");
            }

            records.add(record);
        }

        LOG.info("ALL input doc has been processed. Number of new records: {}", records.size());
        return records;
    }

    public List<OutputRecord> readOutputRecords() throws IOException {
        LOG.info("Start reading Avtopro input doc");
        final var records = new ArrayList<OutputRecord>();
        final var filePath = filesService.getOutputFilePath(FileNames.AVTOPRO_ALL);
        final var sheet = getFirstXlsxSheet(filePath);
        final var rowIterator = sheet.rowIterator();
        rowIterator.next();
        while (rowIterator.hasNext()) {
            final var row = rowIterator.next();
            final var record = new OutputRecord();
            final var brand = row.getCell(outputColumns.get(Columns.BRAND)).getStringCellValue();
            final var article = row.getCell(outputColumns.get(Columns.ARTICLE)).getStringCellValue();
            final var desc = row.getCell(outputColumns.get(Columns.DESCRIPTION)).getStringCellValue();
            final var price = row.getCell(outputColumns.get(Columns.PRICE)).getNumericCellValue();
            final var quantity = (int) row.getCell(outputColumns.get(Columns.QUANTITY)).getNumericCellValue();

            record.setBrand(brand);
            record.setArticle(article);
            record.setDesc(desc);
            record.setPrice(price);
            record.setQuantity(quantity);
            records.add(record);
        }

        return  records;
    }

    protected static Category processCategory(final String groupValue) {
        var processedValue = groupValue.strip();
        processedValue = processedValue.replaceAll("c", "с");
        processedValue = processedValue.replaceAll("k", "к");
        processedValue = processedValue.replaceAll("o", "о");
        processedValue = processedValue.replaceAll("e", "е");
        return Category.fromAbbr(processedValue);
    }
}
