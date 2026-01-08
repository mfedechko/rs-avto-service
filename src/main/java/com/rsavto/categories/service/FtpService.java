package com.rsavto.categories.service;

import com.rsavto.categories.service.read.CategoriesCsvReader;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mykola Fedechko
 */
@Service
public class FtpService {

    private static final Logger LOG = LoggerFactory.getLogger(FtpService.class);

    private final FilesService filesService;
    private final CategoriesCsvReader categoriesCsvReader;

    private static final String host = "rsavto.com.ua";
    private static final String user = "img_allz994-wLgG";
    private static final String password = "neGZihED1y0JyGAqezj34D";

    public FtpService(final FilesService filesService,
                      final CategoriesCsvReader categoriesCsvReader) {
        this.filesService = filesService;
        this.categoriesCsvReader = categoriesCsvReader;
    }

    //TODO try to implement parallel execution
    public int uploadPhotos() {

        final var client = new FTPClient();
        FileInputStream fis = null;

        LOG.info("Connecting to {} host", host);
        try {
            client.connect(host);
            client.enterLocalPassiveMode();
            client.login(user, password);

            client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
            client.setFileTransferMode(FTP.BINARY_FILE_TYPE);

            LOG.info("Connection established");
            //
            // Create an InputStream of the file to be uploaded
            //
            final var files = collectAvailableFiles();

            final var totalFilesCount = files.size();
            LOG.info("Start uploading {} to FTP", totalFilesCount);
            var uploadedFilesCount = 0;
            for (final var file : files) {
                final var filename = file.getName();
                fis = new FileInputStream(file);
                client.storeFile(filename, fis);
                uploadedFilesCount++;
                if (uploadedFilesCount % 10 == 0) {
                    LOG.info("{} out of {} have been uploaded", uploadedFilesCount, totalFilesCount);
                }
            }

            LOG.info("All files have been successfully uploaded");
            client.logout();
            return uploadedFilesCount;
        } catch (final IOException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                client.disconnect();
            } catch (final IOException exc) {
                exc.printStackTrace();
            }
        }

        return -1;
    }

    private List<File> collectAvailableFiles() {
        final var photosDir = new File(filesService.getPhotosDir());
        final var files = new ArrayList<File>();
        collectFilesRecursively(photosDir, files);
        final var availablePictures = new ArrayList<File>();
        final var picturesFromCategoryFiles = getPicturesFromCategoryFiles();
        for (final var file : files) {
            if (picturesFromCategoryFiles.contains(file.getName())) {
                availablePictures.add(file);
            }
        }
        return availablePictures;
    }

    private Set<String> getPicturesFromCategoryFiles()  {
        final var categoryFilesDir = new File(filesService.getOutPutCategoriesDirPath());
        final var allPictures = new HashSet<String>();
        for (final var file : categoryFilesDir.listFiles()) {
            final Set<String> pictures;
            try {
                pictures = categoriesCsvReader.readAllRecordsFromCsv(file.getAbsolutePath());
                allPictures.addAll(pictures);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return allPictures;
    }

    private static void collectFilesRecursively(final File dir, final List<File> fileList) {
        final var files = dir.listFiles();
        if (null != files) {
            for (final var file : files) {
                if (file.getName().equals(".DS_Store")) {
                    continue;
                }
                if (file.isDirectory()) {
                    collectFilesRecursively(file, fileList);
                } else {
                    fileList.add(file);
                }
            }
        }
    }
}
