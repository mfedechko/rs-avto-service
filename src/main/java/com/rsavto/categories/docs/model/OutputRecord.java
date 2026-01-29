package com.rsavto.categories.docs.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Mykola Fedechko
 */
@Getter
@Setter
public class OutputRecord {

    private String brand;
    private String article;
    private String desc;
    private BigDecimal price;
    private int quantity;
    private String imageLink;
}
