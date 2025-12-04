package com.rsavto.categories.service;

import com.rsavto.categories.service.read.AllReader;
import com.rsavto.categories.service.write.DocWriter;
import com.rsavto.categories.service.write.WriteResults;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author mfedechko
 */
@Service
public class AllProcessor {

    private final FilesService filesService;
    private final AllReader allReader;
    private final DocWriter docWriter;

    public AllProcessor(final FilesService filesService,
                        final AllReader allReader,
                        final DocWriter docWriter) {
        this.filesService = filesService;
        this.allReader = allReader;
        this.docWriter = docWriter;
    }

    public WriteResults process() throws IOException {
        filesService.cleanCategoriesFolder();
        final var allRecords = allReader.readAllRecords();
        return docWriter.createFilesForAllInputRecords(allRecords);

    }
}
