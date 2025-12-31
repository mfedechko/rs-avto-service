package com.rsavto.categories.controller;

import com.rsavto.categories.data.Category;
import com.rsavto.categories.site.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mfedechko
 */
@RestController
public class UploadCategoriesController {

    private final AdminService adminService;

    public UploadCategoriesController(final AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("uploadAllCategories")
    public void uploadAll() {
        final var noRsaCategories = Arrays.stream(Category.values())
                .filter(c -> c != Category.RSA)
                .collect(Collectors.toList());
        adminService.uploadCategories(noRsaCategories);
    }

    @GetMapping("uploadRSA")
    public void uploadRsa() {
        adminService.uploadCategories(List.of(Category.RSA));
    }

}
