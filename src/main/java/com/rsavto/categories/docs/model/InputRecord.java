package com.rsavto.categories.docs.model;

import com.rsavto.categories.data.Category;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mykola Fedechko
 */
@Getter
@Setter
public class InputRecord {

    private final String id;
    private String brand;
    private String article;
    private String description;
    private Category category;
    private BigDecimal priceAvtopro;
    private BigDecimal price;
    private int quantity;
    private String descBrand;
    private String descArticle;
    private String descName;
    private String picture;
    private List<String> errors = new ArrayList<>();
    private int inputRow;


    public InputRecord(final String brand,
                       final String article,
                       final String description) {
        this.id = buildId(brand, article);
        this.brand = brand;
        this.article = article;
        this.description = description;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    private static String buildId(final String brand, final String article) {
        if (brand != null && article != null) {
            return brand.toLowerCase() + "_" + article.toLowerCase();
        }

        return brand + "_" + article;
    }
}
