package com.rsavto.categories.controller;

import com.rsavto.categories.service.PhotosCopierService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author mfedechko
 */
@RestController
public class PhotosController {

    private final PhotosCopierService photosCopierService;

    public PhotosController(final PhotosCopierService photosCopierService) {
        this.photosCopierService = photosCopierService;
    }

    @GetMapping("downloadPhotos")
    public void downloadPhotos() throws IOException {
        photosCopierService.copyPhotos();
    }
}
