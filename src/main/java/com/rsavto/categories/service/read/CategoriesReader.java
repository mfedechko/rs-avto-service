package com.rsavto.categories.service.read;

import com.rsavto.categories.data.ReadErrors;
import com.rsavto.categories.docs.Columns;
import com.rsavto.categories.docs.model.InputRecord;
import com.rsavto.categories.service.FilesService;
import com.rsavto.categories.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mfedechko
 */
public abstract class CategoriesReader {

    protected static final String DESC_SEPARATOR = "/";

    protected final FilesService filesService;
    protected final Map<String, Integer> columnsMap;

    protected CategoriesReader(final FilesService filesService, final Map<String, Integer> columnsMap) {
        this.filesService = filesService;
        this.columnsMap = columnsMap;
    }

    public abstract List<InputRecord> readAllRecords() throws IOException;

    protected static Sheet getFirstXlsxSheet(final String filePath) throws IOException {
        final var excelFile = new File(filePath);
        final var fis = new FileInputStream(excelFile);
        final var workbook = new XSSFWorkbook(fis);
        return workbook.getSheetAt(0);
    }

    protected InputRecord buildInitialRecord(final Row row) {
        final var brand = ExcelUtils.getStringValue(row.getCell(columnsMap.get(Columns.BRAND)));
        final var article = ExcelUtils.getStringValue(row.getCell(columnsMap.get(Columns.ARTICLE)));
        final var description = ExcelUtils.getStringValue(row.getCell(columnsMap.get(Columns.DESCRIPTION)));
        final var errors = new ArrayList<String>();
        final var record = new InputRecord(brand, article, description);

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


}
