package com.rsavto.categories.service;

import com.rsavto.categories.data.Category;
import com.rsavto.categories.data.FileType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * @author mfedechko
 */
@Service
public class FilesService {

    @Value("files.inputFolder")
    private String inputFolder;

    @Value("files.outputFolder")
    private String outputFolder;


    public String getLatestFileInDirectory(final String folder) throws FileNotFoundException {
        final var workingFolder = Paths.get(inputFolder, folder).toFile();
        final var files = Arrays.stream(Objects.requireNonNull(workingFolder.listFiles()))
                .filter(f -> !f.getName().equals(".DS_Store"))
                .sorted(Comparator.comparingLong(File::lastModified).reversed())
                .toList();
        if (files.isEmpty()) {
            throw new FileNotFoundException();
        }
        return files.get(0).getAbsolutePath();
    }

    public void cleanCategoriesFolder() {
        final var categoriesFolder = new File(outputFolder + "/categoryFiles");
        for (final var file : Objects.requireNonNull(categoriesFolder.listFiles())) {
            file.delete();
        }
    }

    public String getOutputFilePath(final String fileName) {
        return Paths.get(outputFolder, fileName).toString();
    }

    public String getFailedRecordsFilePath() {
        return Paths.get(outputFolder, "errors.xlsx").toString();
    }

    public Path getCategoryFileName(final Category category) {
        return Paths.get(outputFolder, category.getFileName() + "." + FileType.CSV.getExtension());
    }

    public String getPhotosDir() {
        return Paths.get(inputFolder, "photos").toString();
    }
}
