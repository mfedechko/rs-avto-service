package com.rsavto.categories.data;

import lombok.Getter;
import org.apache.poi.ss.usermodel.CellType;

/**
 * @author mfedechko
 */
@Getter
public enum CategoriesXlsxCell {

    BRAND("Бренд", 0, CellType.STRING),
    ARTICLE("Артикул", 1, CellType.STRING),
    DESCRIPTION("Опис", 2, CellType.STRING),
    PRICE("Ціна", 3, CellType.NUMERIC),
    QUANTITY("Кількість", 4, CellType.NUMERIC),
    IMAGE("Зображення", 5, CellType.STRING);


    private final String name;
    private final int index;
    private final CellType cellType;

    CategoriesXlsxCell(final String name,
                       final int index,
                       final CellType cellType) {
        this.name = name;
        this.index = index;
        this.cellType = cellType;
    }

}
