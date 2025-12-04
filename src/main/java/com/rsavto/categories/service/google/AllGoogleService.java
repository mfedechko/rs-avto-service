package com.rsavto.categories.service.google;

import com.rsavto.categories.data.Category;
import com.rsavto.categories.data.FileNames;
import com.rsavto.categories.docs.CreateGoogleResponse;
import com.rsavto.categories.docs.model.GoogleRecord;
import com.rsavto.categories.docs.model.InputRecord;
import com.rsavto.categories.service.read.AllReader;
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
public class AllGoogleService extends GoogleService{

    private static final Logger LOG = LoggerFactory.getLogger(AllGoogleService.class);

    public AllGoogleService(final AllReader allReader,
                            final RsAvtoWebSiteService rsAvtoWebSiteService,
                            final GoogleWriter googleWriter) {
        super(allReader, rsAvtoWebSiteService, googleWriter);
    }

    @Override
    public CreateGoogleResponse createGoogleDoc() throws IOException {

        LOG.info("Start creating google doc");
        final var eurRate = BigDecimal.valueOf(adminService.getRate());
        final var records = recordsReader.readAllRecords().stream()
                .filter(r -> r.getQuantity() > 0)
                .filter(r -> r.getDescArticle() != null)
                .toList();

        final var originalArticles = records.stream()
                .filter(r -> r.getCategory() == Category.ORIGINAL)
                .map(InputRecord::getArticle)
                .collect(Collectors.toSet());

        final var articles = records.stream()
                .map(InputRecord::getDescArticle)
                .collect(Collectors.toList());

        final var articleLinks = rsAvtoWebSiteService.getLinks(articles);

        final var googleRecords = new ArrayList<GoogleRecord>();
        for (final var record : records) {

            if (originalArticles.contains(record.getDescArticle()) && record.getCategory() != Category.ORIGINAL) {
                continue;
            }
            final var googleRecord = new GoogleRecord();
            final var article = record.getDescArticle();
            final var brand = record.getDescBrand();
            final var name = record.getDescName();
            googleRecord.setId(article);
            googleRecord.setName(record.getDescription().replaceAll("/", ""));
            googleRecord.setDescription(String.format("%s %s %s", name, brand, article));
            final var partPage = articleLinks.get(article);
            googleRecord.setLink(partPage.getPageLink());
            final var roundedPrice = eurRate.multiply(record.getPrice()).setScale(0, RoundingMode.UP);
            googleRecord.setPrice(roundedPrice + ".00 UAH");
            googleRecord.setPicture(partPage.getImageLink());
            googleRecord.setBrand(brand);
            googleRecords.add(googleRecord);
        }

        googleWriter.createGoogleExcel(googleRecords, FileNames.GOOGLE_ALL);

        LOG.info("Google doc has been successfully created.");
        return null;
    }

    public  void updateGoogleDoc() {
        LOG.info("Start updating google doc");
    }

}
