package com.rsavto.categories.service.google;

import com.rsavto.categories.data.Category;
import com.rsavto.categories.data.FileNames;
import com.rsavto.categories.docs.CreateGoogleResponse;
import com.rsavto.categories.docs.model.GoogleRecord;
import com.rsavto.categories.docs.model.InputRecord;
import com.rsavto.categories.service.DataService;
import com.rsavto.categories.service.FilesService;
import com.rsavto.categories.service.read.AllReader;
import com.rsavto.categories.service.read.CategoriesReader;
import com.rsavto.categories.service.read.Googlereader;
import com.rsavto.categories.service.write.GoogleWriter;
import com.rsavto.categories.site.AdminService;
import com.rsavto.categories.site.RsAvtoWebSiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mfedechko
 */
@Service
public class AllGoogleService extends GoogleService {

    private static final Logger LOG = LoggerFactory.getLogger(AllGoogleService.class);

    public AllGoogleService(final AllReader allReader,
                            final RsAvtoWebSiteService rsAvtoWebSiteService,
                            final Googlereader googleReader,
                            final GoogleWriter googleWriter,
                            final AdminService adminService,
                            final FilesService filesService,
                            final DataService dataService) {
        super(allReader, rsAvtoWebSiteService, googleReader, googleWriter, adminService, filesService, dataService);
    }

    @Override
    public CreateGoogleResponse createGoogleDoc() throws IOException {

        LOG.info("Start creating google doc for ALL");
        final var records = getNoErrorRecords();
        final var lists = getRecordsChunks(records);
        processAllChunks(lists);
        return null;
    }

    public void updateGoogleDoc() {
        LOG.info("Start updating google doc");
    }

    @Override
    protected void createDocForChunk(final List<InputRecord> records,
                                     final int chunkIndex) throws IOException {
        final var originalArticles = records.stream()
                .filter(r -> !r.hasErrors())
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
            googleRecord.setDescription(String.format("%s %s %s", name, brand, article));
            final var partPage = articleLinks.get(article);
            googleRecord.setLink(partPage.getPageLink());
            final var roundedPrice = getEurRate().multiply(record.getPrice()).setScale(0, RoundingMode.UP);
            googleRecord.setPrice(roundedPrice + ".00 UAH");
            googleRecord.setPicture(partPage.getImageLink());
            googleRecord.setBrand(brand);
            googleRecord.setName(record.getDescription().replaceAll("/", ""));
            googleRecords.add(googleRecord);
        }

        googleWriter.createGoogleExcel(googleRecords, String.format(FileNames.GOOGLE_ALL_CHUNK, chunkIndex));

        LOG.info("Google doc for chunk {} has been created", chunkIndex);
    }

    @Override
    public String getAllGoogleFileName() {
        return FileNames.GOOGLE_ALL;
    }
}
