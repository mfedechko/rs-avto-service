package com.rsavto.categories.util;

import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;

/**
 * @author mykola.fedechko
 */
@UtilityClass
public class ExcelUtils {

    public static String getStringValue(final Cell cell) {

        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            final var dataFormatter = new DataFormatter();
            return dataFormatter.formatCellValue(cell);
        }

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        }

        return null;
    }

    public static double getDoubleValue(final Cell cell) {

        if (cell == null) {
            return 0;
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }

        if (cell.getCellType() == CellType.STRING) {
            return Double.parseDouble(cell.getStringCellValue());
        }

        return 0;
    }

    public static int getIntValue(final Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            var value = 0;
            try {
                value = Integer.parseInt(cell.getStringCellValue());
            } catch (final NumberFormatException exception) {
                System.out.println("[ERROR] Error parsing quantity. Row: " + cell.getRow().getRowNum());
            }
            return value;
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        }
        return 0;
    }
}
