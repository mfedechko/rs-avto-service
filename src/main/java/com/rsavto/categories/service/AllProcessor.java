package com.rsavto.categories.service;

import com.rsavto.categories.data.FileNames;
import com.rsavto.categories.docs.model.OutputRecord;
import com.rsavto.categories.service.read.AllReader;
import com.rsavto.categories.service.read.OutputReader;
import com.rsavto.categories.service.write.DocWriter;
import com.rsavto.categories.service.write.WriteResults;
import com.rsavto.categories.service.write.XlsxFileWriter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mfedechko
 */
@Service
public class AllProcessor {

    private final FilesService filesService;
    private final AllReader allReader;
    private final DocWriter docWriter;
    private final OutputReader googleOutputReader;
    private final OutputReader categoriesOutputReader;
    private final XlsxFileWriter categoriesFileWriter;

    public AllProcessor(final FilesService filesService,
                        final AllReader allReader,
                        final DocWriter docWriter,
                        final OutputReader googleOutputReader,
                        final OutputReader categoriesOutputReader,
                        final XlsxFileWriter categoriesFileWriter) {
        this.filesService = filesService;
        this.allReader = allReader;
        this.docWriter = docWriter;
        this.googleOutputReader = googleOutputReader;
        this.categoriesOutputReader = categoriesOutputReader;
        this.categoriesFileWriter = categoriesFileWriter;
    }

    public WriteResults process() throws IOException {
        filesService.cleanCategoriesFolder();
        final var allRecords = allReader.readAllRecords();
        return docWriter.createFilesForAllInputRecords(allRecords);
    }

    public void updateDocWithPictures() throws IOException {
        final var googleOutputFilePath = filesService.getOutputFilePath(FileNames.GOOGLE_ALL);
        final var googleRecords = googleOutputReader.readAllRecords(googleOutputFilePath);

        final var avtoproAllOutputFilePath = filesService.getOutputFilePath(FileNames.AVTOPRO_ALL);
        final var avtoproRsaOutputFilePath = filesService.getOutputFilePath(FileNames.AVTOPRO_RSA);

        final var avtoproAllOutputRecords = categoriesOutputReader.readAllRecords(avtoproAllOutputFilePath);
        final var avtoproRsaOutputRecords = categoriesOutputReader.readAllRecords(avtoproRsaOutputFilePath);

        final var updatedAvtoproAllRecords = new ArrayList<OutputRecord>(avtoproAllOutputRecords.size());
        final var updatedAvtoproRsaRecords = new ArrayList<OutputRecord>(avtoproRsaOutputRecords.size());

        for (final var avtoproRecord : avtoproAllOutputRecords) {
            final var imageLink = findGoogleRecord(avtoproRecord.getArticle(), googleRecords);
            avtoproRecord.setImageLink(imageLink);
            updatedAvtoproAllRecords.add(avtoproRecord);
        }

        for (final var avtoproRecord : avtoproRsaOutputRecords) {
            final var imageLink = findGoogleRecord(avtoproRecord.getArticle(), googleRecords);
            avtoproRecord.setImageLink(imageLink);
            updatedAvtoproRsaRecords.add(avtoproRecord);
        }


        filesService.removeFile(avtoproAllOutputFilePath);
        filesService.removeFile(avtoproRsaOutputFilePath);
        categoriesFileWriter.writeOutputRecords(avtoproAllOutputFilePath, updatedAvtoproAllRecords);
        categoriesFileWriter.writeOutputRecords(avtoproRsaOutputFilePath, updatedAvtoproRsaRecords);

    }

    private String findGoogleRecord(final String article,
                                    final List<? extends OutputRecord> googleRecords) {
        googleRecords.stream()
                .filter(gr -> gr.getArticle().equals(article))
                .findFirst()
                .map(OutputRecord::getArticle)
                .orElse("")
    }
}
