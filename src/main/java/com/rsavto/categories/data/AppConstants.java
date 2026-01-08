package com.rsavto.categories.data;

import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * @author mfedechko
 */
@UtilityClass
public class AppConstants {

    public static final String CSV_DELIMITER = ";";

    public static final Map<String, Integer> CSV_COLUMNS = buildCsvColumnsMap();

    private static Map<String, Integer> buildCsvColumnsMap() {
        return Map.of(ColumnNames.BRAND, 0,
                      ColumnNames.ARTICLE, 1,
                      ColumnNames.PRICE, 2,
                      ColumnNames.NAME, 3,
                      ColumnNames.QUANTITY, 4,
                      ColumnNames.PICTURE, 5,
                      ColumnNames.DESC, 6);
    }
}
