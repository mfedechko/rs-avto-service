package com.rsavto.categories.service.google;

import com.rsavto.categories.data.FileNames;
import com.rsavto.categories.docs.CreateGoogleResponse;
import com.rsavto.categories.docs.model.GoogleRecord;
import com.rsavto.categories.docs.model.InputRecord;
import com.rsavto.categories.service.DataService;
import com.rsavto.categories.service.FilesService;
import com.rsavto.categories.service.read.Googlereader;
import com.rsavto.categories.service.read.RsaReader;
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

/**
 * @author mfedechko
 */
@Service
public class RsaGoogleService extends GoogleService {

    private static final Logger LOG = LoggerFactory.getLogger(RsaGoogleService.class);

    public RsaGoogleService(final RsaReader rsaReader,
                            final RsAvtoWebSiteService rsAvtoWebSiteService,
                            final Googlereader googleReader,
                            final GoogleWriter googleWriter,
                            final AdminService adminService,
                            final FilesService filesService,
                            final DataService dataService) {
        super(rsaReader, rsAvtoWebSiteService, googleReader, googleWriter, adminService, filesService, dataService);
    }

    @Override
    public CreateGoogleResponse createGoogleDoc() throws IOException {
        LOG.info("Start creating google doc");
        final var records = getNoErrorRecords();
        final var lists = getRecordsChunks(records);
        processAllChunks(lists);
        return null;
    }

    @Override
    public void updateGoogleDoc() {
        LOG.info("Start updating google doc");
    }

    @Override
    protected void createDocForChunk(final List<InputRecord> records,
                                   final int chunkIndex) throws IOException {

        final var rsaRecords = getNoErrorRecords();
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
            googleRecord.setDescription(String.format("%s %s %s", name, brand, article));
            final var partPage = rsaArticleLinks.get(record.getArticle());
            googleRecord.setLink(partPage.getPageLink());
            final var roundedPrice = getEurRate().multiply(record.getPrice()).setScale(0, RoundingMode.UP);
            googleRecord.setPrice(roundedPrice + ".00 UAH");
            googleRecord.setPicture(partPage.getImageLink());
            googleRecord.setBrand(brand);
            googleRecord.setName(buildRsaGoogleName(article, brand, name));
            googleRecords.add(googleRecord);
        }

        googleWriter.createGoogleExcel(googleRecords, String.format(FileNames.GOOGLE_RSA_CHUNK, chunkIndex));

        LOG.info("Google doc has been successfully created.");
    }

    private static String buildRsaGoogleName(final String article,
                                             final String brand,
                                             final String name) {
        if (name.contains("/")) {
            return name.replaceAll("/", "");
        }
        return String.format("%s %s %s", article, brand, name);
    }

    @Override
    public String getAllGoogleFileName() {
        return FileNames.GOOGLE_RSA;
    }
}
