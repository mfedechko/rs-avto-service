package com.rsavto.categories.service.write;

import com.rsavto.categories.data.Category;
import com.rsavto.categories.docs.model.GoogleRecord;
import com.rsavto.categories.docs.model.InputRecord;
import com.rsavto.categories.service.DataService;
import com.rsavto.categories.service.FilesService;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Mykola Fedechko
 */
public class DocWriter {

    private static final int BRAND = 0;
    private static final int ARTICLE = 1;
    private static final int DESC = 2;
    private static final int PRICE = 3;
    private static final int QUANTITY = 4;
    private String phoneNumber = "";

    private final FilesService filesService;
    private final DataService dataService;

    public DocWriter(final FilesService filesService, final DataService dataService) {
        this.filesService = filesService;
        this.dataService = dataService;
    }


    protected void writeToFile(final List<InputRecord> parts,
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
            strings.add(String.format("%s;%s;%s;%s;%s;%s;\"%s\"", brand, article, price, name, quantity, picture, desc));
        }
        Files.write(filePath, strings);
    }

    protected void createGoogleExcel(final List<GoogleRecord> records,
                                     final String fileName) throws IOException {
        final Workbook workbook = new XSSFWorkbook();
        final var sheet = workbook.createSheet();
        createHeaderGoogleRow(sheet);
        for (var i = 1; i < records.size(); i++) {
            final var row = sheet.createRow(i);
            final var record = records.get(i);
            final var articleCell = row.createCell(0, CellType.STRING);
            articleCell.setCellValue(record.getId());
            final var nameCell = row.createCell(1, CellType.STRING);
            nameCell.setCellValue(record.getName());
            final var descriptionCell = row.createCell(2, CellType.STRING);
            descriptionCell.setCellValue(record.getDescription());
            final var linkCell = row.createCell(3, CellType.STRING);
            linkCell.setCellValue(record.getLink());
            row.createCell(4, CellType.STRING);
            final var priceCell = row.createCell(5, CellType.NUMERIC);
            priceCell.setCellValue(record.getPrice());
            final var availabilityPage = row.createCell(6, CellType.STRING);
            availabilityPage.setCellValue("у наявності ");
            final var imageCell = row.createCell(7, CellType.STRING);
            imageCell.setCellValue(record.getPicture());
            final var brandCell = row.createCell(10, CellType.STRING);
            brandCell.setCellValue(record.getBrand());
        }
        final var filePath = filesService.getOutputFilePath(fileName);
        final var outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
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

    private static void createHeaderGoogleRow(final Sheet sheet) {
        final var row = sheet.createRow(0);
        row.createCell(0, CellType.STRING).setCellValue("идентификатор");
        row.createCell(1, CellType.STRING).setCellValue("название");
        row.createCell(2, CellType.STRING).setCellValue("описание");
        row.createCell(3, CellType.STRING).setCellValue("ссылка");
        row.createCell(4, CellType.STRING).setCellValue("состояние");
        row.createCell(5, CellType.STRING).setCellValue("цена");
        row.createCell(6, CellType.STRING).setCellValue("наличие");
        row.createCell(7, CellType.STRING).setCellValue("ссылка на изображение");
        row.createCell(8, CellType.STRING).setCellValue("gtin");
        row.createCell(9, CellType.STRING).setCellValue("код производителя");
        row.createCell(10, CellType.STRING).setCellValue("марка");
        row.createCell(11, CellType.STRING).setCellValue("категория продукта google");
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
                                     "Наші телефони: %s ( є VIBER ) є можливість перевірки по він коду.", desc, article, brand, phoneNumber);
    }


    private String buildDesc(final String brand, final String article, final String name) {
        return String.format("%s, Оригінальний номер запчастини: %s, " +
                                     "Встановлення на авто: %s, " +
                                     "Країна виробника Польща хороша якість , не оригінал (АНАЛОГ) , стан: Новий, " +
                                     "Товар в наявності відправка в день замовлення транспортною компанією Нова пошта , " +
                                     "Оплата: післяоплата на відділенні служби доставки чи платіж на рахунок . " +
                                     "Наші телефони: %s ( є VIBER ) є можливість перевірки по він коду.",
                             name, article, brand, phoneNumber);
    }


    private String buildOriginalDesc(final String brand, final String article, final String name) {
        return String.format("%s, Оригінальний номер запчастини: %s, " +
                                     "Встановлення на авто: %s, " +
                                     "Виробник: Original, " +
                                     "стан: Новий, Товар в наявності відправка в день замовлення Новою поштою\", " +
                                     "Оплата: накладним платежем або переказ на карту т. %s (VIBER) можливість " +
                                     "перевірки по Vin коду\"",
                             name, article, brand, phoneNumber);
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
}
