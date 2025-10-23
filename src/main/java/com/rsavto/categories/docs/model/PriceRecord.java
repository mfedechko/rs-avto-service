package com.rsavto.categories.docs.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mykola.fedechko
 */
@Getter
@Setter
public class PriceRecord extends InputRecord {

    private double priceP;
    private double priceG;

    public PriceRecord(final String brand,
                       final String article) {
        super(brand, article, "");
    }
}
