package com.rsavto.categories.service.write;

import com.rsavto.categories.data.AppConstants;
import com.rsavto.categories.data.Category;
import com.rsavto.categories.data.FileNames;
import com.rsavto.categories.docs.model.InputRecord;
import com.rsavto.categories.service.DataService;
import com.rsavto.categories.service.FilesService;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Mykola Fedechko
 */
@Service
public class DocWriter {

    private static final int BRAND = 0;
    private static final int ARTICLE = 1;
    private static final int DESC = 2;
    private static final int PRICE = 3;
    private static final int QUANTITY = 4;

    protected static final int AVTOPRO_QUANTITY = 4;

    private final FilesService filesService;
    private final DataService dataService;

    public DocWriter(final FilesService filesService, final DataService dataService) {
        this.filesService = filesService;
        this.dataService = dataService;
    }

    public WriteResults createFilesForAllInputRecords(final List<InputRecord> records) throws IOException {

        final var writeResults = new WriteResults();

        final var notAvailableParts = records.stream()
                .filter(r -> !r.hasErrors())
                .filter(r -> r.getQuantity() == 0)
                .collect(Collectors.toList());
        final var availableParts = records.stream()
                .filter(r -> !r.hasErrors())
                .filter(r -> r.getQuantity() > 0)
                .peek(r -> r.setQuantity(Math.min(AVTOPRO_QUANTITY, r.getQuantity())))
                .collect(Collectors.toList());

        final var availablePartsFilePath = filesService.getOutputFilePath(FileNames.AVTOPRO_ALL);
        final var notAvailablePartsFilePath = filesService.getOutputFilePath(FileNames.ORDER);

        createOutputFile(availableParts, availablePartsFilePath);
        createOutputFile(notAvailableParts, notAvailablePartsFilePath);

        writeResults.setAvailableParts(availableParts.size());
        writeResults.setNotAvailableParts(notAvailableParts.size());
        writeResults.setTotalParts(records.size());

        writeResults.setAvailablePartsFilePath(availablePartsFilePath);
        writeResults.setNotAvailablePartsFilePath(notAvailablePartsFilePath);

        final var errors = records.stream().filter(InputRecord::hasErrors).toList();
        final var errorsFilePath = filesService.getOutputFilePath(FileNames.ORDER);
        createFailedRecordsFile(errors, errorsFilePath);

        writeResults.setErrorsCount(errors.size());
        writeResults.setErrorsFilePath(errorsFilePath);

        final var categoriesWithRecords = records.stream()
                .filter(r -> r.getQuantity() > 0)
                .filter(r -> r.getDescArticle() != null)
                .collect(Collectors.groupingBy(InputRecord::getCategory));

        final var originalArticles = categoriesWithRecords.get(Category.ORIGINAL).stream()
                .map(InputRecord::getDescArticle)
                .collect(Collectors.toSet());

        final var copies = new ArrayList<InputRecord>();
        for (final var entry : categoriesWithRecords.entrySet()) {
            final var category = entry.getKey();
            final var categoryRecords = entry.getValue();
            createCsvFileForCategory(category, categoryRecords, originalArticles, copies);
        }
        final var copiesFilePath = filesService.getOutputFilePath(FileNames.COPIES);
        createOutputFile(copies, copiesFilePath);

        writeResults.setCopiesCount(copies.size());
        writeResults.setCopiesFilePath(copiesFilePath);
        return writeResults;
    }

    public WriteResults createFilesForRsaRecords(final List<InputRecord> records) throws IOException {
        final var writeResults = new WriteResults();
        final var availableRsaParts = records.stream()
                .filter(r -> r.getQuantity() > 0)
                .peek(r -> r.setQuantity(Math.min(AVTOPRO_QUANTITY, r.getQuantity())))
                .collect(Collectors.toList());

        writeResults.setTotalParts(records.size());
        writeResults.setAvailableParts(availableRsaParts.size());
        writeResults.setNotAvailableParts(records.size() -  availableRsaParts.size());

        final var rsaPathFilePath = filesService.getOutputFilePath(FileNames.AVTOPRO_RSA);
        createOutputFile(availableRsaParts, rsaPathFilePath);
        createCsvFileForCategory(Category.RSA, availableRsaParts, Set.of(), List.of());

        writeResults.setAvailablePartsFilePath(rsaPathFilePath);
        return writeResults;
    }


    protected void createOutputFile(final List<InputRecord> parts,
                                    final String filePath) throws IOException {
        final Workbook workbook = new XSSFWorkbook();
        final var sheet = workbook.createSheet();
        createHeaderRow(sheet);
        for (var i = 0; i < parts.size(); i++) {
            final var row = sheet.createRow(i + 1);
            final var part = parts.get(i);
            final var brandCell = row.createCell(BRAND, CellType.STRING);
            brandCell.setCellValue(part.getBrand());
            final var articleCell = row.createCell(ARTICLE, CellType.STRING);
            articleCell.setCellValue(part.getArticle());
            final var descCell = row.createCell(DESC, CellType.STRING);
            descCell.setCellValue(part.getDescription());
            final var priceCell = row.createCell(PRICE, CellType.NUMERIC);
            priceCell.setCellValue(part.getPriceAvtopro().doubleValue());
            final var quantityCell = row.createCell(QUANTITY, CellType.NUMERIC);
            quantityCell.setCellValue(part.getQuantity());
        }

        final var outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }

    public void createCsvFileForCategory(final Category category,
                                         final List<InputRecord> categoryRecords,
                                         final Set<String> originalArticles,
                                         final List<InputRecord> copies) throws IOException {
        final var filePath = filesService.getCategoryFileName(category);
        final var strings = new ArrayList<String>(categoryRecords.size());
        final var BOM = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        strings.add(new String(BOM));
        for (final var record : categoryRecords) {
            final var brand = category == Category.RSA ? record.getBrand() : record.getDescBrand();
            final var article = category == Category.RSA ? record.getArticle() : record.getDescArticle();

            if (category != Category.ORIGINAL && originalArticles.contains(article)) {
                copies.add(record);
                continue;
            }

            final var name = category == Category.RSA ? record.getDescription() : record.getDescName();
            final var price = record.getPrice();
            final var quantity = record.getQuantity();
            final var picture = record.getPicture();
            final var desc = buildDescription(category, brand, article, name);
            final var sb = new StringBuilder();
            sb
                    .append(brand)
                    .append(AppConstants.CSV_DELIMITER)
                    .append(article)
                    .append(AppConstants.CSV_DELIMITER)
                    .append(price)
                    .append(AppConstants.CSV_DELIMITER)
                    .append(name)
                    .append(AppConstants.CSV_DELIMITER)
                    .append(quantity)
                    .append(AppConstants.CSV_DELIMITER)
                    .append(picture)
                    .append(AppConstants.CSV_DELIMITER)
                    .append(quotedString(desc));
            strings.add(sb.toString());

        }
        Files.write(filePath, strings);
    }

    public void createUpdatedCategoryOutputFile(final String filename) {
        final Workbook workbook = new XSSFWorkbook();
        final var sheet = workbook.createSheet();
        createHeaderRow(sheet);

    }

    private static void createHeaderRow(final Sheet sheet) {
        final var row = sheet.createRow(0);
        final var brandCell = row.createCell(BRAND, CellType.STRING);
        brandCell.setCellValue("Бренд");
        final var articleCell = row.createCell(ARTICLE, CellType.STRING);
        articleCell.setCellValue("Артикул");
        final var descCell = row.createCell(DESC, CellType.STRING);
        descCell.setCellValue("Опис");
        final var priceCell = row.createCell(PRICE, CellType.STRING);
        priceCell.setCellValue("Ціна EUR");
        final var quantityCell = row.createCell(QUANTITY, CellType.STRING);
        quantityCell.setCellValue("Кількість");
    }

    private String buildDescription(final Category category,
                                    final String brand,
                                    final String article,
                                    final String name) {
        if (category == Category.RSA) {
            return buildRsaDesc(brand, article, name);
        }

        if (category == Category.ORIGINAL) {
            return buildOriginalDesc(brand, article, name);
        }

        return buildDesc(brand, article, name);
    }

    private String buildRsaDesc(final String brand, final String article, final String desc) {
        return String.format("%s , код запчастини: %s , виробник: %s , " +
                                     "стан: Новий , товар в наявності відправка в день замовлення транспортною компанією Нова пошта \", " +
                                     "Оплата : післяоплата на відділенні служби доставки чи платіж на рахунок . " +
                                     "Наші телефони: %s ( є VIBER ) є можливість перевірки по він коду.",
                             desc, article, brand, dataService.getPhoneNumber());
    }


    private String buildDesc(final String brand, final String article, final String name) {
        return String.format("%s, Оригінальний номер запчастини: %s, " +
                                     "Встановлення на авто: %s, " +
                                     "Країна виробника Польща хороша якість , не оригінал (АНАЛОГ) , стан: Новий, " +
                                     "Товар в наявності відправка в день замовлення транспортною компанією Нова пошта , " +
                                     "Оплата: післяоплата на відділенні служби доставки чи платіж на рахунок . " +
                                     "Наші телефони: %s ( є VIBER ) є можливість перевірки по він коду.",
                             name, article, brand, dataService.getPhoneNumber());
    }


    private String buildOriginalDesc(final String brand, final String article, final String name) {
        return String.format("%s, Оригінальний номер запчастини: %s, " +
                                     "Встановлення на авто: %s, " +
                                     "Виробник: Original, " +
                                     "стан: Новий, Товар в наявності відправка в день замовлення Новою поштою\", " +
                                     "Оплата: накладним платежем або переказ на карту т. %s (VIBER) можливість " +
                                     "перевірки по Vin коду\"",
                             name, article, brand, dataService.getPhoneNumber());
    }


    public void createFailedRecordsFile(final List<InputRecord> inputRecords, final String fileName) throws IOException {

        final Workbook workbook = new XSSFWorkbook();
        final var sheet = workbook.createSheet();
        final var firstRow = sheet.createRow(0);
        firstRow.createCell(0, CellType.STRING).setCellValue("Row");
        firstRow.createCell(1, CellType.STRING).setCellValue("Reason");

        final var failedRecords = inputRecords.stream()
                .filter(InputRecord::hasErrors)
                .toList();

        var rowNum = 1;
        for (final var failedRecord : failedRecords) {
            for (final var error : failedRecord.getErrors()) {
                final var row = sheet.createRow(rowNum);
                row.createCell(0, CellType.STRING).setCellValue(failedRecord.getInputRow());
                row.createCell(1, CellType.STRING).setCellValue(error);
                rowNum++;
            }
        }


        final var outputStream = new FileOutputStream(filesService.getFailedRecordsFilePath());
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();

    }

    private String quotedString(final String value) {
        return String.format("\"%s\"", value);
    }
}
