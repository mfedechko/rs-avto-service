package com.rsavto.categories.service.write;

import com.rsavto.categories.docs.model.OutputRecord;

import java.io.IOException;
import java.util.List;

/**
 * @author mfedechko
 */
public interface RecordsFileWriter {

    void writeToFile(final String filePath);

    void writeOutputRecords(String filePath, List<? extends OutputRecord> records) throws IOException;

}
