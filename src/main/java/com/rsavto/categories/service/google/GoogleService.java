package com.rsavto.categories.service.google;

import com.rsavto.categories.docs.CreateGoogleResponse;
import com.rsavto.categories.service.read.CategoriesReader;
import com.rsavto.categories.service.write.GoogleWriter;
import com.rsavto.categories.site.AdminService;
import com.rsavto.categories.site.RsAvtoWebSiteService;

import java.io.IOException;

/**
 * @author mfedechko
 */
public abstract class GoogleService {

    protected final CategoriesReader recordsReader;
    protected final RsAvtoWebSiteService rsAvtoWebSiteService;
    protected final GoogleWriter googleWriter;
    protected final AdminService adminService;

    public GoogleService(final CategoriesReader recordsReader,
                         final RsAvtoWebSiteService rsAvtoWebSiteService,
                         final GoogleWriter googleWriter,
                         final AdminService adminService) {
        this.recordsReader = recordsReader;
        this.rsAvtoWebSiteService = rsAvtoWebSiteService;
        this.googleWriter = googleWriter;
        this.adminService = adminService;
    }

    public abstract CreateGoogleResponse createGoogleDoc() throws IOException;

    public abstract void updateGoogleDoc();

}
