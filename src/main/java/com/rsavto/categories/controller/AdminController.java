package com.rsavto.categories.controller;

import com.rsavto.categories.data.CategoryType;
import com.rsavto.categories.site.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mfedechko
 */
@RestController
public class AdminController {

    private final AdminService adminService;

    public AdminController(final AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("upload")
    public void upload(@RequestParam(name = "type") final CategoryType categoryType) {
        adminService.uploadCategories(categoryType);
    }
}
