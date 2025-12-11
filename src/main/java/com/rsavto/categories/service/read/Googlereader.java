package com.rsavto.categories.service.read;

import com.rsavto.categories.service.FilesService;
import com.rsavto.categories.service.write.GoogleInputRecord;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mfedechko
 */
@Service
public class Googlereader {

    private final FilesService filesService;

    public Googlereader(final FilesService filesService) {
        this.filesService = filesService;
    }

    public List<GoogleInputRecord> readAllGoogleRecords(final String filePath) throws IOException {
        final var googleRecords = new ArrayList<GoogleInputRecord>();
        final var excelFile = new File(filePath);
        final var fis = new FileInputStream(excelFile);
        final var workbook = new XSSFWorkbook(fis);
        final var sheet = workbook.getSheetAt(0);
        final var rowIterator = sheet.rowIterator();
        rowIterator.next();
        while (rowIterator.hasNext()) {
            final var row = rowIterator.next();
            final var id = row.getCell(0).getStringCellValue();
            final var name = row.getCell(1).getStringCellValue();
            final var desc = row.getCell(2).getStringCellValue();
            final var link = row.getCell(3).getStringCellValue();
            final var state = row.getCell(4).getStringCellValue();
            final var price = row.getCell(5).getStringCellValue();
            final var availability = row.getCell(6).getStringCellValue();
            final var pictureLink = row.getCell(7).getStringCellValue();
//            final var gtin = row.getCell(8).getStringCellValue();
//            final var manCode = row.getCell(9).getStringCellValue();
            final var brand = row.getCell(10).getStringCellValue();
//            final var category = row.getCell(11).getStringCellValue();

            final var googleRecord = new GoogleInputRecord();
            googleRecord.setId(id);
            googleRecord.setName(name);
            googleRecord.setDesc(desc);
            googleRecord.setLink(link);
            googleRecord.setState(state);
            googleRecord.setPrice(price);
            googleRecord.setAvailability(availability);
            googleRecord.setPictureLink(pictureLink);
//            googleRecord.setGtin(gtin);
//            googleRecord.setManCode(manCode);
            googleRecord.setBrand(brand);
//            googleRecord.setCategory(category);
            googleRecords.add(googleRecord);
        }

        fis.close();
        workbook.close();

        return googleRecords;
    }

}
