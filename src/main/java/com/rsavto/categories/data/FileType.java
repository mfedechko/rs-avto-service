package com.rsavto.categories.data;

/**
 * @author Mykola Fedechko
 */
public enum FileType {

    XLSX("xlsx"),
    XLS("xls"),
    CSV("csv"),
    TXT("txt");

    private final String extension;

    FileType(final String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
