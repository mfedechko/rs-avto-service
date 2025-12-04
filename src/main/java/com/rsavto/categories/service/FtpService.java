package com.rsavto.categories.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mykola Fedechko
 */
@Service
public class FtpService {

    private static final Logger LOG = LoggerFactory.getLogger(FtpService.class);

    private final FilesService filesService;

    public FtpService(final FilesService filesService) {
        this.filesService = filesService;
    }

    public int uploadPhotos() {

        final var host = "rsavto.com.ua";
        final var user = "img_allz994-wLgG";
        final var password = "neGZihED1y0JyGAqezj34D";

        final var client = new FTPClient();
        FileInputStream fis = null;

        LOG.info("Connecting to {} host", host);
        try {
            client.connect(host);
            client.enterLocalPassiveMode();
            client.login(user, password);

            LOG.info("Connection established");
            //
            // Create an InputStream of the file to be uploaded
            //
            final var photosDir = new File(filesService.getPhotosDir());
            final var files = new ArrayList<File>();
            collectFilesRecursively(photosDir, files);

            final var totalFilesCount = files.size();
            LOG.info("Start uploading {} to FTP", totalFilesCount);
            var uploadedFilesCount = 0;
            for (final var file : files) {
                client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
                client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
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

    private static void collectFilesRecursively(final File dir, final List<File> fileList) {
        final var files = dir.listFiles();
        if (null != files) {
            for (final var file : files) {
                if (file.isDirectory()) {
                    collectFilesRecursively(file, fileList);
                } else {
                    fileList.add(file);
                }
            }
        }
    }
}
