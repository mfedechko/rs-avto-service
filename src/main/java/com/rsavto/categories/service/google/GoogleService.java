package com.rsavto.categories.service.google;

import com.rsavto.categories.docs.CreateGoogleResponse;
import com.rsavto.categories.docs.model.InputRecord;
import com.rsavto.categories.service.DataService;
import com.rsavto.categories.service.FilesService;
import com.rsavto.categories.service.read.CategoriesReader;
import com.rsavto.categories.service.read.Googlereader;
import com.rsavto.categories.docs.model.GoogleInputRecord;
import com.rsavto.categories.service.write.GoogleWriter;
import com.rsavto.categories.site.AdminService;
import com.rsavto.categories.site.RsAvtoWebSiteService;
import com.rsavto.categories.util.RsAvtoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author mfedechko
 */
public abstract class GoogleService {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleService.class);

    protected final CategoriesReader recordsReader;
    protected final RsAvtoWebSiteService rsAvtoWebSiteService;
    protected final Googlereader googleReader;
    protected final GoogleWriter googleWriter;
    protected final AdminService adminService;
    protected final FilesService filesService;
    protected final DataService dataService;

    protected int currentProcessedChunk;
    private double eurRate;

    public GoogleService(final CategoriesReader recordsReader,
                         final RsAvtoWebSiteService rsAvtoWebSiteService,
                         final Googlereader googleReader,
                         final GoogleWriter googleWriter,
                         final AdminService adminService,
                         final FilesService filesService,
                         final DataService dataService) {
        this.recordsReader = recordsReader;
        this.rsAvtoWebSiteService = rsAvtoWebSiteService;
        this.googleReader = googleReader;
        this.googleWriter = googleWriter;
        this.adminService = adminService;
        this.filesService = filesService;
        this.dataService = dataService;
    }

    protected BigDecimal getEurRate() {
        if (eurRate == 0) {
            eurRate = adminService.getRate();
        }
        return BigDecimal.valueOf(eurRate);
    }

    protected List<InputRecord> getNoErrorRecords() throws IOException {
        final var allRecords = recordsReader.readAllRecords();
        return allRecords.stream()
                .filter(r -> !r.hasErrors()
                        && r.getQuantity() != 0
                        && r.getDescArticle() != null)
                .toList();
    }

    protected List<List<InputRecord>> getRecordsChunks(final List<InputRecord> allRecords) {
        final var chunkSize = dataService.getChunkSize();
        final var lists = new ArrayList<List<InputRecord>>();
        for (var i = 0; i < allRecords.size(); i = i + chunkSize) {

            var endIndex = i + chunkSize;
            if (endIndex > allRecords.size()) {
                endIndex = allRecords.size();
            }

            lists.add(allRecords.subList(i, endIndex));
        }
        return lists;
    }

    protected void processAllChunks(final List<List<InputRecord>> chunks) throws IOException {
        filesService.createGoogleTmpFolder();
        final var sleepTime = dataService.getSleepTime();
        for (var i = currentProcessedChunk; i < chunks.size(); i++) {
            final var recordsChunk = chunks.get(i);
            createDocForChunk(recordsChunk, i + 1);
            currentProcessedChunk++;
            LOG.info("Chunk {} out of {} has been processed", currentProcessedChunk, chunks.size());
            RsAvtoUtils.countDown(sleepTime);

        }
        currentProcessedChunk = 0;
        combineAllChunks();
        filesService.cleanGoogleTmpFolder();

    }

    protected void combineAllChunks() throws IOException {
        final var allRecords = new ArrayList<GoogleInputRecord>();
        final var googleTmpDir = new File(filesService.getGoogleTmpFolder());
        for (final var file : Objects.requireNonNull(googleTmpDir.listFiles())) {
            final var googleInputRecords = googleReader.readAllGoogleRecords(file.getAbsolutePath());
            allRecords.addAll(googleInputRecords);
        }
        googleWriter.copyGoogleExcel(allRecords, getAllGoogleFileName());
    }

    protected abstract String getAllGoogleFileName();

    public abstract CreateGoogleResponse createGoogleDoc() throws IOException;

    public abstract void updateGoogleDoc();

    protected abstract void createDocForChunk(final List<InputRecord> records,
                                              final int chunkIndex) throws IOException;

}
