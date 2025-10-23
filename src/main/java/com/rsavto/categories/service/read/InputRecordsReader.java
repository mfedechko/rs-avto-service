package com.rsavto.categories.service.read;

import com.rsavto.categories.data.Category;
import com.rsavto.categories.data.ReadErrors;
import com.rsavto.categories.docs.Columns;
import com.rsavto.categories.docs.model.InputRecord;
import com.rsavto.categories.service.FilesService;
import com.rsavto.categories.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author mfedechko
 */
public class InputRecordsReader {

    private static final Logger LOG = LoggerFactory.getLogger(InputRecordsReader.class);
    protected static final String DESC_SEPARATOR = "/";

    protected final FilesService filesService;

    public InputRecordsReader(final FilesService filesService) {
        this.filesService = filesService;
    }

    public void readCategories() throws IOException {
        LOG.info("Start reading all categories document");
        final var columnsMap = Columns.INPUT_COLUMNS;
        final var filePath = filesService.getLatestFileInDirectory("categories");

        final var sheet = getXlsxSheet(filePath);
        final var rowIterator = sheet.rowIterator();
        rowIterator.next();
        final var records = new ArrayList<InputRecord>();
        while (rowIterator.hasNext()) {
            final var row = rowIterator.next();
            final var rowNumber = row.getRowNum();
            final var record = buildInitialRecord(row, columnsMap);
            record.setInputRow(rowNumber);
            if (record.hasErrors()) {
                continue;
            }

            final var categoryCell = row.getCell(columnsMap.get(Columns.GROUP));
            final var categoryValue = ExcelUtils.getStringValue(categoryCell);
            if (categoryValue == null) {
                record.getErrors().add(ReadErrors.NO_CATEGORY);
                continue;
            }

            final var category = processCategory(categoryValue);
            final var descChunks = record.getDescription().split(DESC_SEPARATOR);
            if (category != Category.ORIGINAL && descChunks.length < 3) {
                record.getErrors().add(ReadErrors.WRONG_DESC);
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

        LOG.info("Input doc has been processed. Number of new records: {}", records.size());
        final var errorsCount = records.stream().filter(ir -> !ir.getErrors().isEmpty()).count();
        LOG.info("Records with errors: {}", errorsCount);
    }


    public void readAllRecords() throws IOException {
        LOG.info("Start reading RSA input doc");
        final var filePath = filesService.getLatestFileInDirectory("rsa");
        final var columnsMap = Columns.RSA_COLUMNS;
        final var sheet = getXlsxSheet(filePath);
        final var rowIterator = sheet.rowIterator();
        rowIterator.next();
        final var records = new ArrayList<InputRecord>();
        while (rowIterator.hasNext()) {
            final var row = rowIterator.next();
            final var rowNumber = row.getRowNum();

            final var record = buildInitialRecord(row, columnsMap);
            record.setInputRow(rowNumber);
            if (record.hasErrors()) {
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


        LOG.info("RSA input doc has been processed. Number of new records: " + records.size());
        final var errorsCount = records.stream().filter(ir -> !ir.getErrors().isEmpty()).count();
        LOG.info("Records with errors: {}", errorsCount);
    }


    protected static InputRecord buildInitialRecord(final Row row, final Map<String, Integer> columnsMap) {
        final var brand = ExcelUtils.getStringValue(row.getCell(columnsMap.get(Columns.BRAND)));
        final var article = ExcelUtils.getStringValue(row.getCell(columnsMap.get(Columns.ARTICLE)));
        final var description = ExcelUtils.getStringValue(row.getCell(columnsMap.get(Columns.DESCRIPTION)));
        final var errors = new ArrayList<String>();
        final var record =  new InputRecord(brand, article, description);

        if (brand == null) {
            errors.add(ReadErrors.NO_BRAND);
        }

        if (article == null) {
            errors.add(ReadErrors.NO_ARTICLE);
        }

        if (description == null) {
            errors.add(ReadErrors.NO_DESC);
        }

        record.getErrors().addAll(errors);
        return record;
    }

    protected static Sheet getXlsxSheet(final String filePath) throws IOException {
        final var excelFile = new File(filePath);
        final var fis = new FileInputStream(excelFile);
        final var workbook = new XSSFWorkbook(fis);
        return workbook.getSheetAt(0);
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
