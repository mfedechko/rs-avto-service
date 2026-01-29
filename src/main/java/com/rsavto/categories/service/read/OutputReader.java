package com.rsavto.categories.service.read;

import com.rsavto.categories.docs.model.OutputRecord;

import java.io.IOException;
import java.util.List;

/**
 * @author mfedechko
 */
public interface OutputReader {

    List<? extends OutputRecord> readAllRecords(String filePath) throws IOException;

}
