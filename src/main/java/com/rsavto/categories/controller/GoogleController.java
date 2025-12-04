package com.rsavto.categories.controller;

import com.rsavto.categories.service.google.AllGoogleService;
import com.rsavto.categories.service.google.RsaGoogleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author mfedechko
 */
@RestController
public class GoogleController {

    private final AllGoogleService allGoogleService;
    private final RsaGoogleService rsaGoogleService;

    public GoogleController(final AllGoogleService allGoogleService,
                            final RsaGoogleService rsaGoogleService) {
        this.allGoogleService = allGoogleService;
        this.rsaGoogleService = rsaGoogleService;
    }

    @GetMapping("createGoogleDocAll")
    public void googleDocAll() throws IOException {
        allGoogleService.createGoogleDoc();
    }

    @GetMapping("createGoogleDocRsa")
    public void googleDocRsa() throws IOException {
        rsaGoogleService.createGoogleDoc();
    }

}
