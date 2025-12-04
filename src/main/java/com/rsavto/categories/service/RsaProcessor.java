package com.rsavto.categories.service;

import com.rsavto.categories.service.read.RsaReader;
import com.rsavto.categories.service.write.DocWriter;
import com.rsavto.categories.service.write.WriteResults;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author mfedechko
 */
@Service
public class RsaProcessor {

    private final RsaReader rsaReader;
    private final DocWriter docWriter;

    public RsaProcessor(final RsaReader rsaReader, final DocWriter docWriter) {
        this.rsaReader = rsaReader;
        this.docWriter = docWriter;
    }

    public WriteResults process() throws IOException {
        final var rsaRecords = rsaReader.readAllRecords();
        return docWriter.createFilesForRsaRecords(rsaRecords);
    }
}
