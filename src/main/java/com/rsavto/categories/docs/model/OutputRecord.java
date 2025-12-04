package com.rsavto.categories.docs.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mykola Fedechko
 */
@Getter
@Setter
public class OutputRecord {

    private String brand;
    private String article;
    private String desc;
    private double price;
    private int quantity;
}
