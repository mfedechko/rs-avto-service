package com.rsavto.categories.service;

import com.rsavto.categories.data.Category;
import com.rsavto.categories.data.FileType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
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
    private static final String GOOGLE_TMP = "google_tmp";

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
        if (categoriesFolder.exists()) {
            for (final var file : Objects.requireNonNull(categoriesFolder.listFiles())) {
                file.delete();
            }
        } else {
            categoriesFolder.mkdir();
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
        return Paths.get(workDir, PHOTOS).toString();
    }

    public String getWebDriverPath() {
        return Paths.get(workDir, WEBDRIVER, CHROMEDRIVER).toString();
    }

    public String getInputPhotosDir() {
        return inPhotosFolder;
    }

    public String getGoogleTmpFolder() {
        return Paths.get(workDir, OUTPUT_DIR, GOOGLE_TMP).toString();
    }

    public void createGoogleTmpFolder() throws IOException {
        final var path = Paths.get(workDir, OUTPUT_DIR, GOOGLE_TMP);


        if (Files.exists(path)) {
            final var files = Files.list(path).toList();
            for (final var file : files) {
                Files.delete(file);
            }
            Files.delete(path);
        }
        Files.createDirectory(path);
    }

    public void cleanGoogleTmpFolder() throws IOException {
        final var path = Paths.get(workDir, OUTPUT_DIR, GOOGLE_TMP);
        Files.list(path).forEach(p -> {
            try {
                Files.delete(p);
            } catch (final IOException exc) {
                throw new RuntimeException(exc);
            }
        });

    }

    public String createGoogleTmpFilePath(final String fileName) {
        return Paths.get(workDir, OUTPUT_DIR, GOOGLE_TMP, fileName).toString();
    }

    public String getOutPutCategoriesDirPath() {
        return Paths.get(workDir, OUTPUT_DIR, CATEGORIES_DIR).toString();
    }
}
