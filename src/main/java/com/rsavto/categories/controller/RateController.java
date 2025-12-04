package com.rsavto.categories.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author mfedechko
 */
@RestController
public class RateController {

    @PostMapping("updateRate")
    public void googleDocAll() throws IOException {

    }

    @GetMapping("getRate")
    public void getrate() throws IOException {

    }

}
