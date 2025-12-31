package com.rsavto.categories.controller;

import com.rsavto.categories.site.AdminService;
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

}
