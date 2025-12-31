package com.rsavto.categories.controller;

import com.rsavto.categories.service.FtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mfedechko
 */
@RestController
public class FtpController {

    private final FtpService ftpService;

    public FtpController(final FtpService ftpService) {
        this.ftpService = ftpService;
    }

    @GetMapping("ftp")
    public ResponseEntity<String> uploadToFTP() {
        final var photosCount = ftpService.uploadPhotos();
        return ResponseEntity.ok().body(String.format("%s photos uploaded to FTP", photosCount));
    }
}
