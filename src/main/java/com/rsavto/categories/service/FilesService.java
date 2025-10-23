package com.rsavto.categories.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * @author mfedechko
 */
public class FilesService {

    private final String inputFolder;
    private final String outputFolder;

    public FilesService(final String inputFolder, final String outputFolder) {
        this.inputFolder = inputFolder;
        this.outputFolder = outputFolder;
    }

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
}
