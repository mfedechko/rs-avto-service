package com.rsavto.categories.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mykola Fedechko
 */
public class FtpService {

    private static final Logger LOG = LoggerFactory.getLogger(FtpService.class);

    private final String host;
    private final String user;
    private final String password;
    private final String sourceDir;

    public FtpService(final String host,
                      final String user,
                      final String password,
                      final String sourceDir) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.sourceDir = sourceDir;
    }


    public void uploadPhotos() {
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
            final var photosDir = new File(sourceDir);
            final var files = new ArrayList<File>();
            collectFilesRecursively(photosDir, files);

            final var totalFilesCount = files.size();
            LOG.info("Start uploading {} to FTP", totalFilesCount);
            var uploadedFiles = 0;
            for (final var file : files) {
                client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
                client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
                final var filename = file.getName();
                fis = new FileInputStream(file);
                client.storeFile(filename, fis);
                uploadedFiles++;
                if (uploadedFiles % 10 == 0) {
                    LOG.info("{} out of {} have been uploaded", uploadedFiles, totalFilesCount);
                }
            }

            LOG.info("All files have been successfully uploaded");
            client.logout();
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
