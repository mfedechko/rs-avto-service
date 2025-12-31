package com.rsavto.categories.controller;

import com.rsavto.categories.service.AllProcessor;
import com.rsavto.categories.service.PriceProcessor;
import com.rsavto.categories.service.RsaProcessor;
import com.rsavto.categories.service.write.WriteResults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author mfedechko
 */
@RestController
public class DocsController {

    private final AllProcessor allProcessor;
    private final RsaProcessor rsaProcessor;
    private final PriceProcessor priceProcessor;

    public DocsController(final AllProcessor allProcessor,
                          final RsaProcessor rsaProcessor,
                          final PriceProcessor priceProcessor) {
        this.allProcessor = allProcessor;
        this.rsaProcessor = rsaProcessor;
        this.priceProcessor = priceProcessor;
    }

    @GetMapping("readAll")
    public ResponseEntity<List<WriteResults>> readInputDocs() throws IOException {
        final var allWriteResults = allProcessor.process();
        final var rsWriteResults = rsaProcessor.process();
        return new ResponseEntity<>(List.of(allWriteResults, rsWriteResults), HttpStatus.OK);
    }

    @GetMapping("price")
    public void priceDocs() throws IOException {
        priceProcessor.updatePrices();
    }

}
