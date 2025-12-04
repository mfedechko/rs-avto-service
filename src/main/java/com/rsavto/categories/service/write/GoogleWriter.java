package com.rsavto.categories.service.write;

import com.rsavto.categories.data.FileNames;
import com.rsavto.categories.docs.model.GoogleRecord;
import com.rsavto.categories.service.FilesService;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author mfedechko
 */
@Service
public class GoogleWriter {

    private final FilesService filesService;

    public GoogleWriter(final FilesService filesService) {
        this.filesService = filesService;
    }

    public void createGoogleExcel(final List<GoogleRecord> records,
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


}
