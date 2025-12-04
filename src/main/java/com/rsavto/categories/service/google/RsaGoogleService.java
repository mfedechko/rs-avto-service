package com.rsavto.categories.service.google;

import com.rsavto.categories.data.Category;
import com.rsavto.categories.data.FileNames;
import com.rsavto.categories.docs.CreateGoogleResponse;
import com.rsavto.categories.docs.model.GoogleRecord;
import com.rsavto.categories.docs.model.InputRecord;
import com.rsavto.categories.service.read.AllReader;
import com.rsavto.categories.service.read.RsaReader;
import com.rsavto.categories.service.write.GoogleWriter;
import com.rsavto.categories.site.RsAvtoWebSiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author mfedechko
 */
@Service
public class RsaGoogleService extends GoogleService {

    private static final Logger LOG = LoggerFactory.getLogger(RsaGoogleService.class);

    public RsaGoogleService(final RsaReader rsaReader,
                            final RsAvtoWebSiteService rsAvtoWebSiteService,
                            final GoogleWriter googleWriter) {
        super(rsaReader, rsAvtoWebSiteService, googleWriter);
    }

    @Override
    public CreateGoogleResponse createGoogleDoc() throws IOException {
        LOG.info("Start creating google doc");
        final var eurRate = BigDecimal.valueOf(adminService.getRate());
        final var rsaRecords = recordsReader.readAllRecords().stream()
                .filter(r -> r.getQuantity() > 0 && r.getArticle() != null)
                .toList();
        final var rsaArticles = rsaRecords.stream()
                .map(InputRecord::getArticle)
                .toList();
        final var rsaArticleLinks = rsAvtoWebSiteService.getLinks(rsaArticles);
        final var googleRecords = new ArrayList<GoogleRecord>();
        for (final var record : rsaRecords) {
            final var googleRecord = new GoogleRecord();
            final var article = record.getDescArticle();
            final var brand = record.getDescBrand();
            final var name = record.getDescName();
            googleRecord.setId(article);
            googleRecord.setName(buildRsaGoogleName(article, brand, name));
            googleRecord.setDescription(String.format("%s %s %s", name, brand, article));
            final var partPage = rsaArticleLinks.get(record.getArticle());
            googleRecord.setLink(partPage.getPageLink());
            final var roundedPrice = eurRate.multiply(record.getPrice()).setScale(0, RoundingMode.UP);
            googleRecord.setPrice(roundedPrice + ".00 UAH");
            googleRecord.setPicture(partPage.getImageLink());
            googleRecord.setBrand(brand);
            googleRecords.add(googleRecord);
        }

        googleWriter.createGoogleExcel(googleRecords, FileNames.GOOGLE_RSA);

        LOG.info("Google doc has been successfully created.");
    }

    @Override
    public void updateGoogleDoc() {
        LOG.info("Start updating google doc");
    }

    private static String buildRsaGoogleName(final String article,
                                             final String brand,
                                             final String name) {
        if (name.contains("/")) {
            return name.replaceAll("/", "");
        }
        return String.format("%s %s %s", article, brand, name);
    }
}
