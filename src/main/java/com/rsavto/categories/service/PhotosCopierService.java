package com.rsavto.categories.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author mfedechko
 */
@Service
public class PhotosCopierService {

    private final FilesService filesService;

    public PhotosCopierService(final FilesService filesService) {
        this.filesService = filesService;
    }

    public void copyPhotos() throws IOException {
        final var inFolder = new File(filesService.getInputPhotosDir());
        for (final var file : inFolder.listFiles()) {
            if (file.isDirectory()) {
                System.out.println("Copy photos for " + file.getName());
                final var files = file.listFiles();
                assert files != null;
                System.out.printf("Copying %s files for %s\n", files.length, file.getName());
                for (final var photoFile : files) {
                    Files.copy(Paths.get(photoFile.getAbsolutePath()), Paths.get(filesService.getPhotosDir(), photoFile.getName()), StandardCopyOption.REPLACE_EXISTING);
                }
                System.out.printf("All files for %s have been copied\n", file.getName());
            }
        }
    }
}
