package com.rsavto.categories.service.read;

import com.rsavto.categories.data.AppConstants;
import com.rsavto.categories.data.ColumnNames;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mfedechko
 */
@Service
public class CategoriesCsvReader {

    public Set<String> readAllRecordsFromCsv(final String filePath) throws IOException {
        final var recordLines = Files.readAllLines(Paths.get(filePath));
        final var pictures = new HashSet<String>(recordLines);
        for (final var recordLine : recordLines) {
            final var split = recordLine.split(AppConstants.CSV_DELIMITER);
            if (split.length < 2) {
                continue;
            }
            final var picture = split[AppConstants.CSV_COLUMNS.get(ColumnNames.PICTURE)];
            pictures.add(picture);
        }
        return pictures;
    }

}
