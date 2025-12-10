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

    private static final String INPUT_DIR = "input";
    private static final String OUTPUT_DIR = "output";
    private static final String WEBDRIVER = "webdriver";
    private static final String PHOTOS = "photos";
    private static final String CATEGORIES_DIR = "categoryFiles";
    private static final String CHROMEDRIVER = "chromedriver";

    @Value("${workDir}")
    private String workDir;

    @Value("${inPhotosFolder}")
    private String inPhotosFolder;

    public String getLatestFileInDirectory(final String folder) throws FileNotFoundException {
        final var workingFolder = Paths.get(workDir, INPUT_DIR, folder).toFile();
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
        final var categoriesFolder = Paths.get(workDir, OUTPUT_DIR, CATEGORIES_DIR).toFile();
        for (final var file : Objects.requireNonNull(categoriesFolder.listFiles())) {
            file.delete();
        }
    }

    public String getOutputFilePath(final String fileName) {
        return Paths.get(workDir, OUTPUT_DIR, fileName).toString();
    }

    public String getFailedRecordsFilePath() {
        return Paths.get(workDir, OUTPUT_DIR, "errors.xlsx").toString();
    }

    public Path getCategoryFileName(final Category category) {
        return Paths.get(workDir, OUTPUT_DIR, CATEGORIES_DIR, category.getFileName() + "." + FileType.CSV.getExtension());
    }

    public String getPhotosDir() {
        return Paths.get(workDir, INPUT_DIR, PHOTOS).toString();
    }

    public String getWebDriverPath() {
        return Paths.get(workDir, WEBDRIVER, CHROMEDRIVER).toString();
    }

    public String getInputPhotosDir() {
        return inPhotosFolder;
    }


}
